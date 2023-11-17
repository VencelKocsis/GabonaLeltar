package hu.bme.aut.android.gabonaleltar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grainitem")
data class GrainItem (
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "amount") var amount: Int,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "imageResourceId") var imageResourceId: Int
)

@Entity(tableName = "transactionitem")
data class TransactionItem (
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "date") var date: Long,
    @ColumnInfo(name = "grainId") var grainId: Long,
    @ColumnInfo(name = "amount") var amount: Double,
    @ColumnInfo(name = "totalCost") var totalCost: Int
)