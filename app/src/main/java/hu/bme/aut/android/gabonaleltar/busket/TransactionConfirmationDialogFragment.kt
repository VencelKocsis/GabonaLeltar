package hu.bme.aut.android.gabonaleltar.busket

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class TransactionConfirmationDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Vásárlás megtörtént")
            .setMessage("Sikeres Vásárlás!")
            .setPositiveButton("Ok") {_, _ ->
                dismiss()
            }
            .create()
    }
}