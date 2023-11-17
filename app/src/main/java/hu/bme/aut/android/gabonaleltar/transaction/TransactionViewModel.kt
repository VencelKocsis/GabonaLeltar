package hu.bme.aut.android.gabonaleltar.transaction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.gabonaleltar.data.GrainDatabase
import hu.bme.aut.android.gabonaleltar.data.GrainItem
import hu.bme.aut.android.gabonaleltar.data.GrainItemDAO
import hu.bme.aut.android.gabonaleltar.data.TransactionItem
import hu.bme.aut.android.gabonaleltar.data.TransactionItemDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val transactionItemDao: TransactionItemDAO
    private val allTransactionItems: LiveData<List<TransactionItem>>

    init {
        val database = GrainDatabase.getInstance(application)
        transactionItemDao = database.transactionDao()
        allTransactionItems = transactionItemDao.getAll()
    }

    fun insertTransaction(grainItem: GrainItem, purchasedAmount: Int) {
        val transactionItem = grainItem.id?.let {
            TransactionItem(
                date = System.currentTimeMillis(),
                grainId = it,
                amount = purchasedAmount.toDouble(),
                totalCost = grainItem.price * purchasedAmount
            )
        }
        if (transactionItem != null) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    transactionItemDao.insert(transactionItem)
                }
            }
        }
    }

    fun deleteTransactionItem(transactionItem: TransactionItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                transactionItemDao.delete(transactionItem)
            }
        }
    }

    fun getTransactionItems(): LiveData<List<TransactionItem>> {
        return allTransactionItems
    }
}