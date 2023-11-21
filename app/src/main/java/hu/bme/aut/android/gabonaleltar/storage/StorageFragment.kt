package hu.bme.aut.android.gabonaleltar.storage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.gabonaleltar.R
import hu.bme.aut.android.gabonaleltar.adapter.TransactionAdapter
import hu.bme.aut.android.gabonaleltar.data.GrainDatabase
import hu.bme.aut.android.gabonaleltar.data.GrainItem
import hu.bme.aut.android.gabonaleltar.databinding.FragmentStorageBinding
import hu.bme.aut.android.gabonaleltar.transaction.TransactionFragment
import hu.bme.aut.android.gabonaleltar.transaction.TransactionViewModel
import kotlin.concurrent.thread

class StorageFragment : Fragment() {

    private lateinit var binding: FragmentStorageBinding
    private lateinit var database: GrainDatabase
    private val spinnerGrainList = mutableListOf<String>(
        "búza",
        "árpa",
        "kukorica",
        "napraforgó",
        "csíkos napraforgó",
        "fehér napraforgó",
        "zab",
        "hántolt zab",
        "repce",
        "vörös cirok",
        "fehér cirok",
        "sárga borsó",
        "zöld borsó",
        "velő borsó",
        "barna borsó",
        "vörös köles",
        "fehér köles",
        "sárga köles",
        "kendermag",
        "hajdina",
        "mungóbab",
        "szeklice",
        "bükköny",
        "fénymag",
        "sárga len",
        "barna len",
        "muhar",
        "máriatövis",
        "tigrismogyoró",
        "gazdaságos magkeverék",
        "rc magkeverék",
        "speciál magkeverék",
        "prémium magkeverék",
        "tyúk magkeverék",
        "csibedara"
    )

    private lateinit var adapter: TransactionAdapter
    private lateinit var sharedViewModel: TransactionViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedViewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStorageBinding.inflate(inflater, container, false)
        binding.toolbar.title = "Raktár"
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        database = GrainDatabase.getInstance(requireContext())

        val spinner = binding.spinnerGrain
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerGrainList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        binding.btnAdd.setOnClickListener {
            val selectedGrain = spinner.selectedItem as String
            val amount = binding.edtAmount.text.toString().toIntOrNull() ?: 0
            modifyGrain(selectedGrain, amount, "+")
        }

        binding.btnRemove.setOnClickListener {
            val selectedGrain = spinner.selectedItem as String
            val amount = binding.edtAmount.text.toString().toIntOrNull() ?: 0
            modifyGrain(selectedGrain, amount, "-")
        }

        binding.btnCnagePrice.setOnClickListener {
            if (binding.cbPriceChange.isChecked && binding.edNewPrice.text.isNotEmpty() && !binding.edNewPrice.text.equals(0)) {
                val selectedGrain = spinner.selectedItem as String
                val newPrice = binding.edNewPrice.text.toString().toInt()
                modifyGrain(selectedGrain, newPrice)
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedGrain = spinnerGrainList[position]
                initRecyclerView(selectedGrain = getGrainIdByName(spinner.selectedItem.toString()))
                Toast.makeText(requireContext(), "Kiválasztva: $selectedGrain", Toast.LENGTH_LONG).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("Not yet implemented")
            }
        }

        return binding.root
    }

    private fun initRecyclerView(selectedGrain: Long) {
        sharedViewModel.getSelectedTransactionItems().observe(viewLifecycleOwner, Observer { transactions ->
            transactions.let {
                adapter = TransactionAdapter(sharedViewModel.getTransactionItems(), sharedViewModel, R.layout.storage_selected_grain)
                binding.rvSelectedGrain.layoutManager = LinearLayoutManager(requireContext())
                binding.rvSelectedGrain.adapter = adapter
                if (it != null) {
                    adapter.updateData(it)
                }
            }
        })

        sharedViewModel.updateSelectedTransactionItems(selectedGrain)
    }

    private fun modifyGrain(selectedGrain: String, amount: Int, operator: String) {
        thread {
            val grainItem: GrainItem? = database.grainDao().getGrainByName(selectedGrain)
            if(grainItem != null) {
                var currentAmount = grainItem.amount
                var modifier = ""
                var newAmount = -1
                if(operator == "+") {
                    newAmount = currentAmount + amount
                    modifier = "hozzá lett adva a raktárhoz"
                } else if(operator == "-") {
                    newAmount = currentAmount - amount
                    modifier = "el lett vonva a raktárból"
                }

                if(newAmount >= 0) {
                    grainItem.amount = newAmount
                    database.grainDao().update(grainItem)
                    activity?.runOnUiThread {
                        Toast.makeText(context, "${amount} Kg ${grainItem.name} ${modifier}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Nem lehet negatív szám a raktárban", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun modifyGrain(selectedGrain: String, newPrice: Int) {
        thread {
            val grainItem: GrainItem? = database.grainDao().getGrainByName(selectedGrain)
            if(grainItem != null) {
                val oldPrice = grainItem.price
                grainItem.price = newPrice
                database.grainDao().update(grainItem)
                activity?.runOnUiThread {
                    Toast.makeText(context, "$selectedGrain ára módosítva lett $oldPrice (Kg/Ft)-ról $newPrice (Kg/Ft)-ra", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun getGrainIdByName(selectedGrain: String): Long {
        return when (selectedGrain) {
            "búza" -> 0L
            "árpa" -> 1L
            "kukorica" -> 2L
            "napraforgó" -> 3L
            "csíkos napra" -> 4L
            "fehér napra" -> 5L
            "zab" -> 6L
            "hántolt zab" -> 7L
            "repce" -> 8L
            "vörös cirok" -> 9L
            "fehér cirok" -> 10L
            "sárga borsó" -> 11L
            "zöld borsó" -> 12L
            "velő borsó" -> 13L
            "barna borsó" -> 14L
            "vörös köles" -> 15L
            "fehér köles" -> 16L
            "sárga köles" -> 17L
            "kendermag" -> 18L
            "hajdina" -> 19L
            "mungóbab" -> 20L
            "szeklice" -> 21L
            "bükköny" -> 22L
            "fénymag" -> 23L
            "sárga len" -> 24L
            "barna len" -> 25L
            "muhar" -> 26L
            "máriatövis" -> 27L
            "tigrismogyoró" -> 28L
            "gazdaságos magkev" -> 29L
            "rc magkev" -> 30L
            "speciál magkev" -> 31L
            "prémium magkev" ->32L
            "tyúk magkev" -> 33L
            "csibedara" -> 34L
            else -> -1
        }
    }
}