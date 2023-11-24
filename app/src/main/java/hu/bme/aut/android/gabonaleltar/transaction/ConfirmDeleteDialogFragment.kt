package hu.bme.aut.android.gabonaleltar.transaction

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmDeleteDialogFragment(private val onDeleteListener: () -> Unit) : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Tranzakció törlése")
            .setMessage("Biztosan kitörlöd a tranzakciót?")
            .setPositiveButton("Igen") {_, _ ->
                onDeleteListener.invoke()
            }
            .setNegativeButton("Mégse") {dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}