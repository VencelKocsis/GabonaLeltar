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
            val fragmentManager = (holder.binding.root.context as AppCompatActivity).supportFragmentManager
            val dialogFragment = ItemPurchaseDialogFragment.newInstance(grainItem)
            dialogFragment.show(fragmentManager, "Purchase Item")
        }
    }

    override fun getItemCount(): Int = items.size

    inner class GrainViewHolder(val binding: ItemGrainListBinding) : RecyclerView.ViewHolder(binding.root)
}
