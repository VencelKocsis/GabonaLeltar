package hu.bme.aut.android.gabonaleltar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.android.gabonaleltar.data.GrainItem
import hu.bme.aut.android.gabonaleltar.databinding.ActivityMainBinding
import hu.bme.aut.android.gabonaleltar.grain.GrainViewModel
import hu.bme.aut.android.gabonaleltar.grain.ItemPurchaseDialogFragment
import hu.bme.aut.android.gabonaleltar.transaction.TransactionViewModel

class MainActivity : AppCompatActivity(), ItemPurchaseDialogFragment.PurchaseItemDialogListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedViewModel: GrainViewModel
    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setTitle("Gabona Leltár")
        setSupportActionBar(binding.toolbar)

        sharedViewModel = ViewModelProvider(this).get(GrainViewModel::class.java)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        binding
    }

    override fun onGrainItemPurchased(grainItem: GrainItem, purchasedAmount: Int) {
        if(sharedViewModel.onGrainItemPurchased(grainItem, purchasedAmount)) {
            Toast.makeText(this, "${grainItem.name} vásárlás megtörtént: ${purchasedAmount} tonna", Toast.LENGTH_LONG).show()
            transactionViewModel.insertTransaction(grainItem, purchasedAmount)
        } else {
            Toast.makeText(this, "vásárlás nem sikerült", Toast.LENGTH_LONG).show()
        }
    }
}