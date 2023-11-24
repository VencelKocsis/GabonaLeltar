package hu.bme.aut.android.gabonaleltar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.gabonaleltar.grain.ItemPurchaseDialogFragment
import hu.bme.aut.android.gabonaleltar.data.GrainItem
import hu.bme.aut.android.gabonaleltar.databinding.ItemGrainListBinding

class GrainAdapter(private val grainList: LiveData<List<GrainItem>>) :
    RecyclerView.Adapter<GrainAdapter.GrainViewHolder>() {

    private val items = mutableListOf<GrainItem>()

    init {
        grainList.observeForever(Observer {
            items.clear()
            items.addAll(it)
            notifyDataSetChanged()
        })
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GrainViewHolder(
        ItemGrainListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: GrainViewHolder, position: Int) {
        val grainItem = items[position]

        holder.binding.ibGrain.setImageResource(grainItem.imageResourceId)
        holder.binding.tvName.text = grainItem.name

        holder.itemView.setOnClickListener {

            holder.binding.layout.requestFocus()

            // Play a scale animation on ImageButton
            holder.binding.ibGrain.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction {
                // Reset the scale to its original size
                holder.binding.ibGrain.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
            }.start()

            // Play a scale animation on TextView
            holder.binding.tvName.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction {
                // Reset the scale to its original size
                holder.binding.tvName.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
            }.start()

            val fragmentManager = (holder.binding.root.context as AppCompatActivity).supportFragmentManager
            val dialogFragment = ItemPurchaseDialogFragment.newInstance(grainItem)
            dialogFragment.show(fragmentManager, "Purchase Item")
        }
    }

    override fun getItemCount(): Int = items.size

    inner class GrainViewHolder(val binding: ItemGrainListBinding) : RecyclerView.ViewHolder(binding.root)
}
