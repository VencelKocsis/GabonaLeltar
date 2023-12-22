package hu.bme.aut.android.gabonaleltar.busket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.gabonaleltar.data.BusketItem

class BusketViewModel : ViewModel() {
    private val _selectedItems = MutableLiveData<List<BusketItem>>()
    val selectedItems: LiveData<List<BusketItem>> = _selectedItems

    fun addToBusket(item: BusketItem, purchasedAmount: Int, calculatedPrice: Int) {

        BusketItem(
            id = item.id,
            name = item.name,
            amount = purchasedAmount,
            price = calculatedPrice,
            imageResourceId = item.imageResourceId
        )
        val currentSelectedItems = _selectedItems.value.orEmpty().toMutableList()
        currentSelectedItems.add(item)
        _selectedItems.value = currentSelectedItems
    }

    fun SumSelectedItemsPrice(): Int {
        var Sum = 0
        _selectedItems.value.let { selectedItems ->
            if (selectedItems != null) {
                for (item in selectedItems) {
                    Sum += item.price
                }
            }
        }
        return Sum
    }

    fun removeFromBusket(item: BusketItem) {
        val currentSelectedItems = _selectedItems.value.orEmpty().toMutableList()
        currentSelectedItems.remove(item)
        _selectedItems.value = currentSelectedItems
    }

    fun clearBusket() {
        _selectedItems.value = emptyList()
    }
}
