package hu.bme.aut.android.gabonaleltar.transaction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.gabonaleltar.R
import hu.bme.aut.android.gabonaleltar.adapter.TransactionAdapter
import hu.bme.aut.android.gabonaleltar.databinding.FragmentTransactionBinding

class TransactionFragment: Fragment() {
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var adapter: TransactionAdapter
    private lateinit var sharedViewModel: TransactionViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedViewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        binding.toolbar.title = "Vásárlás Tranzakciók"
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        initRecyclerView()

        return binding.root
    }
    fun initRecyclerView() {
        adapter = TransactionAdapter(sharedViewModel.getTransactionItems(), sharedViewModel, R.layout.item_transaction_list)
        binding.rvTransaction.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransaction.adapter = adapter
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_transaction, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete_all -> {
                val dialog = ConfirmDeleteAllDialogFragment(adapter)
                dialog.show(requireActivity().supportFragmentManager, "ConfirmDeleteAllDialog")
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }
}