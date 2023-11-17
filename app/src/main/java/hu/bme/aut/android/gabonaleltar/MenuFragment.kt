package hu.bme.aut.android.gabonaleltar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.gabonaleltar.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPurchase.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_purchaseFragment)
        }
        binding.btnTransactions.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_transactionFragment)
        }
        binding.btnStorage.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_storageFragment)
        }
        binding.btnDiagram.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_diagramFragment)
        }
    }
}