package hu.bme.aut.android.gabonaleltar.busket

import androidx.fragment.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.gabonaleltar.adapter.BusketAdapter
import hu.bme.aut.android.gabonaleltar.data.BusketItem
import hu.bme.aut.android.gabonaleltar.databinding.FragmentBusketBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class BusketFragment : Fragment() {

    private lateinit var binding: FragmentBusketBinding
    private lateinit var busketViewModel: BusketViewModel
    private lateinit var adapter: BusketAdapter

    var orderConfirmationListener: OrderConfirmationListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OrderConfirmationListener) {
            orderConfirmationListener = context
        } else {
            throw RuntimeException("$context must implement OrderConfirmationListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBusketBinding.inflate(inflater, container, false)

        busketViewModel = ViewModelProvider(requireActivity()).get(BusketViewModel::class.java)
        busketViewModel.selectedItems.observe(viewLifecycleOwner, Observer { selectedItems ->
            updateRecyclerView(selectedItems)
            updateSum()
        })

        binding.btnConfirmPurchase.setOnClickListener {
            if(busketViewModel.selectedItems.value.orEmpty().isNotEmpty()) {
                orderConfirmationListener?.onOrderConfirmed(busketViewModel.selectedItems.value.orEmpty())
                showTransactionConfirmationDialog()
            } else {
                Toast.makeText(requireContext(), "A kosár üres", Toast.LENGTH_LONG).show()
            }
        }

        initRecyclerView()

        return binding.root
    }

    private fun showTransactionConfirmationDialog() {
        val dialog = TransactionConfirmationDialogFragment()
        dialog.show(requireActivity().supportFragmentManager, "TransactionConfirmationDialogFragment")
    }

    private fun updateSum() {
        val decimalFormatSymbols = DecimalFormatSymbols()
        decimalFormatSymbols.groupingSeparator = ','
        val decimalFormat = DecimalFormat("###,###,###", decimalFormatSymbols)

        val formattedSum = decimalFormat.format(busketViewModel.SumSelectedItemsPrice())

        binding.tvSum.text = "Fizetendő: " + formattedSum + " Ft"
    }

    private fun initRecyclerView() {
        adapter = BusketAdapter(busketViewModel.selectedItems.value.orEmpty(), onItemRemoveClick = {
            busketViewModel.removeFromBusket(it)
        })
        binding.rvBusket.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBusket.adapter = adapter
    }

    private fun updateRecyclerView(updatedItems: List<BusketItem>) {
        adapter = BusketAdapter(updatedItems, onItemRemoveClick = {
            busketViewModel.removeFromBusket(it)
        })
        binding.rvBusket.adapter = adapter
    }

    interface OrderConfirmationListener {
        fun onOrderConfirmed(selectedItems: List<BusketItem>)
    }
}
