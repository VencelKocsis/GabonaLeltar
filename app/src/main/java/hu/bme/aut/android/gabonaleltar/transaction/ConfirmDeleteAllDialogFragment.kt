package hu.bme.aut.android.gabonaleltar.transaction

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.gabonaleltar.R
import hu.bme.aut.android.gabonaleltar.adapter.TransactionAdapter
import hu.bme.aut.android.gabonaleltar.data.GrainDatabase
import kotlin.concurrent.thread

class ConfirmDeleteAllDialogFragment(private val adapter: TransactionAdapter) : DialogFragment() {
    private lateinit var database: GrainDatabase

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage("Biztosan kitörlöd az összes tranzakciót?")
            .setPositiveButton(R.string.yes) { _, _ -> onDeleteAll() }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun onDeleteAll() {
        thread {
            database = GrainDatabase.getInstance(requireContext())
            database.transactionDao().deleteAll()
            Handler(Looper.getMainLooper()).post {
                adapter.deleteAll()
            }
        }
    }
}