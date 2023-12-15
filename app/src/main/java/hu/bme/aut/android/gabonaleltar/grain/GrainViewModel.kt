package hu.bme.aut.android.gabonaleltar.grain

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.gabonaleltar.R
import hu.bme.aut.android.gabonaleltar.data.GrainDatabase
import hu.bme.aut.android.gabonaleltar.data.GrainItem
import hu.bme.aut.android.gabonaleltar.data.GrainItemDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GrainViewModel(application: Application) : AndroidViewModel(application) {
    private val grainItemDao: GrainItemDAO
    private val allGrainItems: LiveData<List<GrainItem>>

    init {
        val database = GrainDatabase.getInstance(application)
        grainItemDao = database.grainDao()
        allGrainItems = grainItemDao.getAll()
    }

    fun insertGrainItem(grainItem: GrainItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                grainItemDao.insert(grainItem)
            }
        }
    }

    fun insertGrainItems() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                for(grainItem in grainItems) {
                    grainItemDao.insert(grainItem)
                }
            }
        }
    }

    private fun updateGrainItem(grainItem: GrainItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                grainItemDao.update(grainItem)
            }
        }
    }

    fun deleteGrainItem(grainItem: GrainItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                grainItemDao.delete(grainItem)
            }
        }
    }

    fun getGrainItems(): LiveData<List<GrainItem>> {
        return allGrainItems
    }

    fun onGrainItemPurchased(grainItem: GrainItem, purchasedAmount: Int): Boolean {
        val updatedAmount = grainItem.amount - purchasedAmount
        if(updatedAmount >= 0 && purchasedAmount > 0) {
            grainItem.amount = updatedAmount
            updateGrainItem(grainItem)
            Log.d("GrainItem", "${grainItem.name} purchased: $purchasedAmount tonna")
            return true
        } else {
            Log.d("GrainItem", "Not enough grain in stock for ${grainItem.name}")
            return false
        }
    }

    private val grainItems: List<GrainItem> = listOf(
        GrainItem(0, "búza", 1000, 100, R.drawable.ic_grain_default_foreground),
        GrainItem(1, "árpa", 900, 80, R.mipmap.ic_barley_round),
        GrainItem(2, "kukorica", 1200, 120, R.drawable.ic_grain_default_foreground),
        GrainItem(3, "napraforgó", 1500, 150, R.drawable.ic_grain_default_foreground),
        GrainItem(4, "csíkos napraforgó", 1600, 160, R.drawable.ic_grain_default_foreground),
        GrainItem(5, "fehér napraforgó", 1600, 160, R.drawable.ic_grain_default_foreground),
        GrainItem(6, "zab", 800, 80, R.drawable.ic_grain_default_foreground),
        GrainItem(7, "hántolt zab", 900, 90, R.drawable.ic_grain_default_foreground),
        GrainItem(8, "repce", 1100, 110, R.drawable.ic_grain_default_foreground),
        GrainItem(9, "vörös cirok", 1000, 100, R.drawable.ic_grain_default_foreground),
        GrainItem(10, "fehér cirok", 1000, 100, R.drawable.ic_grain_default_foreground),
        GrainItem(11, "sárga borsó", 1300, 130, R.drawable.ic_grain_default_foreground),
        GrainItem(12, "zöld borsó", 1400, 140, R.drawable.ic_grain_default_foreground),
        GrainItem(13, "velő borsó", 1500, 150, R.drawable.ic_grain_default_foreground),
        GrainItem(14, "barna borsó", 1500, 150, R.drawable.ic_grain_default_foreground),
        GrainItem(15, "vörös köles", 1100, 110, R.drawable.ic_grain_default_foreground),
        GrainItem(16, "fehér köles", 1100, 110, R.drawable.ic_grain_default_foreground),
        GrainItem(17, "sárga köles", 1100, 110, R.drawable.ic_grain_default_foreground),
        GrainItem(18, "kendermag", 1600, 160, R.drawable.ic_grain_default_foreground),
        GrainItem(19, "hajdina", 1200, 120, R.drawable.ic_grain_default_foreground),
        GrainItem(20, "mungóbab", 1400, 140, R.drawable.ic_grain_default_foreground),
        GrainItem(21, "szeklice", 1100, 110, R.drawable.ic_grain_default_foreground),
        GrainItem(22, "bükköny", 900, 90, R.drawable.ic_grain_default_foreground),
        GrainItem(23, "fénymag", 900, 90, R.drawable.ic_grain_default_foreground),
        GrainItem(24, "sárga len", 900, 90, R.drawable.ic_grain_default_foreground),
        GrainItem(25, "barna len", 900, 90, R.drawable.ic_grain_default_foreground),
        GrainItem(26, "muhar", 900, 90, R.drawable.ic_grain_default_foreground),
        GrainItem(27, "máriatövis", 1200, 120, R.drawable.ic_grain_default_foreground),
        GrainItem(28, "tigrismogyoró", 1400, 140, R.drawable.ic_grain_default_foreground),
        GrainItem(29, "gazdaságos magkeverék", 1600, 160, R.drawable.ic_grain_default_foreground),
        GrainItem(30, "rc magkeverék", 1700, 170, R.drawable.ic_grain_default_foreground),
        GrainItem(31, "speciál magkeverék", 1800, 180, R.drawable.ic_grain_default_foreground),
        GrainItem(32, "prémium magkeverék", 1900, 190, R.drawable.ic_grain_default_foreground),
        GrainItem(33, "tyúk magkeverék", 2000, 200, R.drawable.ic_grain_default_foreground),
        GrainItem(34, "csibedara", 1600, 160, R.drawable.ic_grain_default_foreground)
    )
}