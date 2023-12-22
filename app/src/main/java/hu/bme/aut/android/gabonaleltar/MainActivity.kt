package hu.bme.aut.android.gabonaleltar

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceControl
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import hu.bme.aut.android.gabonaleltar.busket.BusketFragment
import hu.bme.aut.android.gabonaleltar.busket.BusketViewModel
import hu.bme.aut.android.gabonaleltar.data.BusketItem
import hu.bme.aut.android.gabonaleltar.data.GrainItem
import hu.bme.aut.android.gabonaleltar.databinding.ActivityMainBinding
import hu.bme.aut.android.gabonaleltar.grain.GrainViewModel
import hu.bme.aut.android.gabonaleltar.grain.ItemPurchaseDialogFragment
import hu.bme.aut.android.gabonaleltar.transaction.TransactionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(),
    ItemPurchaseDialogFragment.PurchaseItemDialogListener,
BusketFragment.OrderConfirmationListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedViewModel: GrainViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var busketViewModel: BusketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusBarColor)
        }

        val busketFragment = BusketFragment()
        busketFragment.orderConfirmationListener = this

        sharedViewModel = ViewModelProvider(this).get(GrainViewModel::class.java)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        busketViewModel = ViewModelProvider(this).get(BusketViewModel::class.java)
    }

    override fun onGrainItemPurchased(grainItem: GrainItem, purchasedAmount: Int) {

        Toast.makeText(this, "${grainItem.name} kosárba helyezve: ${purchasedAmount} Kg", Toast.LENGTH_LONG).show()
        var calculatedPrice = purchasedAmount * grainItem.price.toString().toInt()
        var busketItem = BusketItem(grainItem.id, grainItem.name, purchasedAmount, calculatedPrice, grainItem.imageResourceId)
        busketViewModel.addToBusket(busketItem, purchasedAmount, calculatedPrice)
    }

    override fun onOrderConfirmed(selectedItems: List<BusketItem>) {
        processOrder(selectedItems)
    }

    private fun processOrder(selectedItems: List<BusketItem>) {
        Log.d("MainActivity", "Processing order with ${selectedItems.size} items")
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                for (item in selectedItems) {
                    Log.d("Busket Content","${item.name}, ${item.amount} megvásárolva")
                    val originalGrainItem = sharedViewModel.getGrainItemByName(item.name)
                    if(originalGrainItem != null) {
                        sharedViewModel.onGrainItemPurchased(originalGrainItem, item.amount)
                        transactionViewModel.insertTransaction(originalGrainItem, item.amount)
                    }
                }
            }
            busketViewModel.clearBusket()
        }
    }
}