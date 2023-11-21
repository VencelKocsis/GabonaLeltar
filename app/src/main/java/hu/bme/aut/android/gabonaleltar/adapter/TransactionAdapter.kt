package hu.bme.aut.android.gabonaleltar.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.gabonaleltar.R
import hu.bme.aut.android.gabonaleltar.data.TransactionItem
import hu.bme.aut.android.gabonaleltar.databinding.ItemTransactionListBinding
import hu.bme.aut.android.gabonaleltar.transaction.TransactionViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TransactionAdapter(private val transactionList: LiveData<List<TransactionItem>>,
                         private var transactionViewModel: TransactionViewModel,
                         private val layoutResourceId: Int) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private val items = mutableListOf<TransactionItem>()

    @RequiresApi(Build.VERSION_CODES.O)
    private val currentDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @RequiresApi(Build.VERSION_CODES.O)
    private val formattedDate = currentDate.format(dateFormat)

    init {
        transactionList.observeForever(Observer {
            items.clear()
            items.addAll(it)
            notifyDataSetChanged()
        })
    }
    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TransactionViewHolder(
        ItemTransactionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTransactionListBinding.inflate(inflater, parent, false)

        if(layoutResourceId == R.layout.storage_selected_grain) {
            binding.ibDelete.visibility = View.GONE
        }

        return TransactionViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transactionItem = items[position]
        val decimalFormatSymbols = DecimalFormatSymbols()
        decimalFormatSymbols.groupingSeparator = ','
        val decimalFormat = DecimalFormat("###,###,###", decimalFormatSymbols)
        val formattedTotalCost = decimalFormat.format(transactionItem.totalCost)
        val formattedAmount = decimalFormat.format(transactionItem.amount)

        holder.binding.tvTRDate.text = formattedDate
        holder.binding.tvTRName.text = getGrainNameById(transactionItem.grainId)
        holder.binding.tvTRAmount.text = "${formattedAmount} Kg"
        holder.binding.tvTRIncome.text = "${formattedTotalCost} Ft"

        holder.binding.ibDelete.setOnClickListener {
            onDeleteTransactionItem(transactionItem)
        }
    }

    private fun onDeleteTransactionItem(transactionItem: TransactionItem) {
        transactionViewModel.deleteTransactionItem(transactionItem)
    }

    override fun getItemCount(): Int = items.size


    companion object {
        fun getGrainNameById(id: Long): String {
            return when (id) {
                0L -> "búza"
                1L -> "árpa"
                2L -> "kukorica"
                3L -> "napraforgó"
                4L -> "csíkos napra"
                5L -> "fehér napra"
                6L -> "zab"
                7L -> "hántolt zab"
                8L -> "repce"
                9L -> "vörös cirok"
                10L -> "fehér cirok"
                11L -> "sárga borsó"
                12L -> "zöld borsó"
                13L -> "velő borsó"
                14L -> "barna borsó"
                15L -> "vörös köles"
                16L -> "fehér köles"
                17L -> "sárga köles"
                18L -> "kendermag"
                19L -> "hajdina"
                20L -> "mungóbab"
                21L -> "szeklice"
                22L -> "bükköny"
                23L -> "fénymag"
                24L -> "sárga len"
                25L -> "barna len"
                26L -> "muhar"
                27L -> "máriatövis"
                28L -> "tigrismogyoró"
                29L -> "gazdaságos magkev"
                30L -> "rc magkev"
                31L -> "speciál magkev"
                32L -> "prémium magkev"
                33L -> "tyúk magkev"
                34L -> "csibedara"
                else -> "Unknown Grain"
            }
        }
    }

    fun deleteAll() {
        items.clear()
        notifyDataSetChanged()
    }

    fun updateData(newData: List<TransactionItem>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(val binding: ItemTransactionListBinding) : RecyclerView.ViewHolder(binding.root)
}
