package hu.bme.aut.android.gabonaleltar.grain

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.gabonaleltar.adapter.GrainAdapter
import hu.bme.aut.android.gabonaleltar.data.GrainItem
import hu.bme.aut.android.gabonaleltar.databinding.FragmentPurchaseBinding

class PurchaseFragment : Fragment(), ItemPurchaseDialogFragment.PurchaseItemDialogListener {
    private lateinit var binding: FragmentPurchaseBinding
    private lateinit var adapter: GrainAdapter
    private lateinit var sharedViewModel: GrainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedViewModel = ViewModelProvider(requireActivity()).get(GrainViewModel::class.java)
    }

    override fun onCreateView(infalter: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPurchaseBinding.inflate(infalter, container, false)
        binding.toolbar.title = "Gabona Vásárlás"
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        /** Akkor kell csak ha módosítjuk a magok listáját, és wipe data után betöltjük az adatbázisba*/
        sharedViewModel.insertGrainItems()

        initRecyclerView()

        return binding.root
    }
    private fun initRecyclerView() {
        adapter = GrainAdapter(sharedViewModel.getGrainItems())
        binding.rvMain.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMain.adapter = adapter
    }
    override fun onGrainItemPurchased(grainItem: GrainItem, purchasedAmount: Int) {
        sharedViewModel.onGrainItemPurchased(grainItem, purchasedAmount)
    }
}