package hu.bme.aut.android.gabonaleltar.storage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import hu.bme.aut.android.gabonaleltar.data.GrainDatabase
import hu.bme.aut.android.gabonaleltar.data.GrainItem
import hu.bme.aut.android.gabonaleltar.databinding.FragmentStorageBinding
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
    override fun onAttach(context: Context) {
        super.onAttach(context)
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

        return binding.root
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
                        Toast.makeText(context, "${amount} tonna ${grainItem.name} ${modifier}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Nem lehet negatív szám a raktárban", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}