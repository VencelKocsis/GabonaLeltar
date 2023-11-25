package hu.bme.aut.android.gabonaleltar.transaction

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
    val transactionItemsByDay = MutableLiveData<List<Pair<Long, List<TransactionItem>>>>()
    init {
        val database = GrainDatabase.getInstance(application)
        transactionItemDao = database.transactionDao()
        allTransactionItems = transactionItemDao.getAll()

        TestData()

        allTransactionItems.observeForever { transactions ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val groupedTransactions = transactions.groupBy { calculateDay(it.date) }
                        .toList()
                        .sortedBy { it.first }
                    transactionItemsByDay.postValue(groupedTransactions)
                }
            }
        }
    }

    private fun TestData() {
        val transactions = mutableListOf<TransactionItem>()

        val currentDate = System.currentTimeMillis()
        val grainIds = listOf(1, 2, 3) // Assuming you have at least 3 grain items

        for (day in 0 until 100) {
            for (grainId in grainIds) {
                transactions.add(
                    TransactionItem(
                        date = currentDate - day * 24 * 60 * 60 * 1000,
                        grainId = grainId.toLong(),
                        amount = (1..5).random().toDouble(), // Random amount between 1 and 5
                        totalCost = (10..50).random() // Random total cost between 10 and 50
                    )
                )
            }
        }

        for (transaction in transactions) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    transactionItemDao.insert(transaction)
                }
            }
        }

    }

    private fun calculateDay(date: Long): Long {
        // Truncate the timestamp to represent the start of the day
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
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
}