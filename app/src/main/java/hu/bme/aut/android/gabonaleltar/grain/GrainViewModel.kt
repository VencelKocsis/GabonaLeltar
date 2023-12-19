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
        GrainItem(0, "búza", 1000, 100, R.mipmap.ic_wheat),
        GrainItem(1, "árpa", 900, 80, R.mipmap.ic_barley),
        GrainItem(2, "kukorica", 1200, 120, R.mipmap.ic_corn),
        GrainItem(3, "napraforgó", 1500, 150, R.mipmap.ic_sunflower),
        GrainItem(4, "csíkos napraforgó", 1600, 160, R.mipmap.ic_sunflower_striped),
        GrainItem(5, "fehér napraforgó", 1600, 160, R.mipmap.ic_sunflower_white),
        GrainItem(6, "zab", 800, 80, R.mipmap.ic_oat),
        GrainItem(7, "hántolt zab", 900, 90, R.mipmap.ic_oat_groats),
        GrainItem(8, "repce", 1100, 110, R.mipmap.ic_colza),
        GrainItem(9, "vörös cirok", 1000, 100, R.mipmap.ic_sorghum_red),
        GrainItem(10, "fehér cirok", 1000, 100, R.mipmap.ic_sorghum),
        GrainItem(11, "sárga borsó", 1300, 130, R.mipmap.ic_pea_yellow),
        GrainItem(12, "zöld borsó", 1400, 140, R.mipmap.ic_pea_green),
        GrainItem(13, "velő borsó", 1500, 150, R.mipmap.ic_pea_marrow),
        GrainItem(14, "barna borsó", 1500, 150, R.mipmap.ic_pea_brown),
        GrainItem(15, "vörös köles", 1100, 110, R.mipmap.ic_millet_red),
        GrainItem(16, "fehér köles", 1100, 110, R.mipmap.ic_millet_white),
        GrainItem(17, "sárga köles", 1100, 110, R.mipmap.ic_millet_yellow),
        GrainItem(18, "kendermag", 1600, 160, R.mipmap.ic_hempseed),
        GrainItem(19, "hajdina", 1200, 120, R.mipmap.ic_buckwheat),
        GrainItem(20, "mungóbab", 1400, 140, R.mipmap.ic_mung_beans),
        GrainItem(21, "szeklice", 1100, 110, R.mipmap.ic_safflower),
        GrainItem(22, "bükköny", 900, 90, R.mipmap.ic_vetch),
        GrainItem(23, "fénymag", 900, 90, R.mipmap.ic_canary_grass),
        GrainItem(24, "sárga len", 900, 90, R.mipmap.ic_flaxseed_yellow),
        GrainItem(25, "barna len", 900, 90, R.mipmap.ic_flaxseed_brown),
        GrainItem(26, "muhar", 900, 90, R.mipmap.ic_setaria),
        GrainItem(27, "máriatövis", 1200, 120, R.mipmap.ic_milk_thistle),
        GrainItem(28, "tigrismogyoró", 1400, 140, R.mipmap.ic_chufa),
        GrainItem(29, "gazdaságos magkeverék", 1600, 160, R.mipmap.ic_sparrow_mix),
        GrainItem(30, "rc magkeverék", 1700, 170, R.mipmap.ic_chickadee_mix),
        GrainItem(31, "speciál magkeverék", 1800, 180, R.mipmap.ic_pigeon_mix),
        GrainItem(32, "prémium magkeverék", 1900, 190, R.mipmap.ic_golden_pigeon),
        GrainItem(33, "tyúk magkeverék", 2000, 200, R.mipmap.ic_hen_mix),
        GrainItem(34, "csibedara", 1600, 160, R.mipmap.ic_chicks_mix)
    )
}