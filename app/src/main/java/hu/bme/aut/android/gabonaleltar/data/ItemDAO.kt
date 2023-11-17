package hu.bme.aut.android.gabonaleltar.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GrainItemDAO {
    @Query("SELECT * FROM grainitem")
    fun getAll(): LiveData<List<GrainItem>>

    @Insert
    fun insert(grainItems: GrainItem): Long

    @Update
    fun update(grainItem: GrainItem)

    @Delete
    fun delete(grainItem: GrainItem)

    @Query("DELETE FROM grainitem")
    fun deleteAll()

    @Query("SELECT * FROM grainitem WHERE name = :name LIMIT 1")
    fun getGrainByName(name: String): GrainItem?
}

@Dao
interface TransactionItemDAO {
    @Query("SELECT * FROM transactionitem")
    fun getAll(): LiveData<List<TransactionItem>>

    @Insert
    fun insert(transactionItem: TransactionItem): Long

    @Update
    fun update(transactionItem: TransactionItem)

    @Delete
    fun delete(transactionItem: TransactionItem)

    @Query("DELETE FROM transactionitem")
    fun deleteAll()
}