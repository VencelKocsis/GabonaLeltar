package hu.bme.aut.android.gabonaleltar.transaction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.gabonaleltar.data.GrainDatabase
import hu.bme.aut.android.gabonaleltar.data.GrainItem
import hu.bme.aut.android.gabonaleltar.data.TransactionItem
import hu.bme.aut.android.gabonaleltar.data.TransactionItemDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val transactionItemDao: TransactionItemDAO
    private val allTransactionItems: LiveData<List<TransactionItem>>
    private val selectedTransactionItems = MutableLiveData<List<TransactionItem>?>()
    private val transactionItemsByMonth = MediatorLiveData<List<Pair<Int, List<TransactionItem>>>>()

    init {
        val database = GrainDatabase.getInstance(application)
        transactionItemDao = database.transactionDao()
        allTransactionItems = transactionItemDao.getAll()

        transactionItemsByMonth.addSource(allTransactionItems) { transactions ->
            val groupedTransactions = transactions.groupBy { calculateMonth(it.date) }
                .toList()
                .sortedByDescending { it.first }
            transactionItemsByMonth.value = groupedTransactions
        }
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

    fun getSelectedTransactionItems(): MutableLiveData<List<TransactionItem>?> {
        return selectedTransactionItems
    }

    fun updateSelectedTransactionItems(selectedGrain: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val allTransactions = allTransactionItems.value

                // Filter the transactions based on the selectedGrain
                val filteredTransactions = allTransactions?.filter { transaction ->
                    transaction.grainId == selectedGrain
                }

                // Update the MutableLiveData with the filtered list
                selectedTransactionItems.postValue(filteredTransactions)
            }
        }
    }

    fun getTransactionItemsByMonth(): LiveData<List<Pair<Int, List<TransactionItem>>>> {
        return transactionItemsByMonth
    }

    private fun calculateMonth(date: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return calendar.get(Calendar.MONTH)
    }
}