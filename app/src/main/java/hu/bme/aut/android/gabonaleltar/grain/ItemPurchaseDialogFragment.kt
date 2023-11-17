package hu.bme.aut.android.gabonaleltar.grain

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.android.gabonaleltar.R
import hu.bme.aut.android.gabonaleltar.data.GrainItem
import hu.bme.aut.android.gabonaleltar.databinding.DialogPurchaseItemBinding

class ItemPurchaseDialogFragment : DialogFragment() {

    interface PurchaseItemDialogListener {
        fun onGrainItemPurchased(grainItem: GrainItem, purchasedAmount: Int)
    }

    private lateinit var grainItem: GrainItem
    private lateinit var binding: DialogPurchaseItemBinding
    private lateinit var listener: PurchaseItemDialogListener
    private lateinit var sharedViewModel: GrainViewModel
    companion object {
        const val TAG = "ItemPurchaseDialogFragment"

        fun newInstance(grainItem: GrainItem): ItemPurchaseDialogFragment {
            val fragment = ItemPurchaseDialogFragment()
            fragment.grainItem = grainItem
            return fragment

        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedViewModel = ViewModelProvider(requireActivity()).get(GrainViewModel::class.java)
        listener = context as? PurchaseItemDialogListener ?: throw RuntimeException("Fragment must implement the PurchaseItemDialogListener interface")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPurchaseItemBinding.inflate(LayoutInflater.from(context))

        binding.ivPurchaseGrain.setImageResource(grainItem.imageResourceId)
        binding.tvPurchaseName.text = "${grainItem.name} vásárlás"
        binding.tvPurchasePrice.text = "Ár: ${grainItem.price} Ft/kg"
        binding.tvRemainingAmount.text = "Raktáron: ${grainItem.amount} tonna"

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton(R.string.v_s_rl_s) { dialogInterface, i ->
                if(isValid()) {
                    val purchaseAmount = binding.etPurchaseAmount.text.toString().toInt()
                    listener.onGrainItemPurchased(grainItem, purchaseAmount)
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
    }
    private fun isValid() = binding.etPurchaseAmount.text.isNotEmpty()
}