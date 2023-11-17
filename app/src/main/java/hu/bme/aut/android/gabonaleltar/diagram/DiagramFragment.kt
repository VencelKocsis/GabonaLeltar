package hu.bme.aut.android.gabonaleltar.diagram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.trackPipAnimationHintView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import hu.bme.aut.android.gabonaleltar.adapter.TransactionAdapter
import hu.bme.aut.android.gabonaleltar.databinding.FragmentDiagramBinding
import hu.bme.aut.android.gabonaleltar.transaction.TransactionViewModel

class DiagramFragment : Fragment() {
    private lateinit var binding: FragmentDiagramBinding
    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDiagramBinding.inflate(inflater, container, false)
        binding.toolbar.title = "Forgalom Diagram"
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val pieChart: PieChart = binding.chartGrains

        transactionViewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)

        transactionViewModel.getTransactionItems().observe(viewLifecycleOwner, Observer { transactionItems ->
            val entries = mutableListOf<PieEntry>()

            for (transactionItem in transactionItems) {
                val grainName = TransactionAdapter.getGrainNameById(transactionItem.grainId)
                entries.add(PieEntry(transactionItem.amount.toFloat(), grainName))
            }
            val dataSet = PieDataSet(entries, "Forgalom")
            dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

            val pieData = PieData(dataSet)
            pieChart.data = pieData

            pieChart.description.isEnabled = false
            pieChart.isDrawHoleEnabled = true
            pieChart.holeRadius = 50f
            pieChart.setHoleColor(android.R.color.transparent)

            pieChart.invalidate()
        })

        return binding.root
    }
}