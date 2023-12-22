package hu.bme.aut.android.gabonaleltar.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.gabonaleltar.data.BusketItem
import hu.bme.aut.android.gabonaleltar.data.GrainItem
import hu.bme.aut.android.gabonaleltar.databinding.ItemBusketBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class BusketAdapter(
    private val items: List<BusketItem>,
    private val onItemRemoveClick: (BusketItem) -> Unit) :
        RecyclerView.Adapter<BusketAdapter.BusketViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BusketViewHolder {
        val binding = ItemBusketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BusketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BusketAdapter.BusketViewHolder, position: Int) {
        val item = items[position]

        val decimalFormatSymbols = DecimalFormatSymbols()
        decimalFormatSymbols.groupingSeparator = ','
        val decimalFormat = DecimalFormat("###,###,###", decimalFormatSymbols)

        val formattedAmount = decimalFormat.format(item.amount)
        val formattedPrice = decimalFormat.format(item.price)

        holder.binding.ibBusketGrain.setImageResource(item.imageResourceId)
        holder.binding.tvBusketName.text = "${item.name}"
        holder.binding.tvBusketPrice.text = "Fizetendő: ${formattedPrice} Ft"
        holder.binding.tvBusketQuantity.text = "Tömeg: ${formattedAmount} Kg"

        holder.binding.btnibRemove.setOnClickListener{
            onItemRemoveClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class BusketViewHolder(val binding: ItemBusketBinding) : RecyclerView.ViewHolder(binding.root)
}