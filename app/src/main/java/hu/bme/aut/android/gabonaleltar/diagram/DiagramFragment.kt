package hu.bme.aut.android.gabonaleltar.diagram

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import hu.bme.aut.android.gabonaleltar.databinding.FragmentDiagramBinding
import hu.bme.aut.android.gabonaleltar.transaction.TransactionViewModel

object ChartUtils {
    fun setBarChartColors(barDataSet: BarDataSet) {
        // Choose your custom colors for the bars
        val customColors = intArrayOf(
            Color.rgb(67, 206, 162),
            Color.rgb(255, 204, 0),
            Color.rgb(255, 102, 0),
            // Add more colors as needed
        )

        barDataSet.setColors(*customColors)
    }
}

class DiagramFragment : Fragment() {
    private lateinit var binding: FragmentDiagramBinding
    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiagramBinding.inflate(inflater, container, false)
        binding.toolbar.title = "Forgalom Diagram"
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val barChart: BarChart = binding.chartGrains

        transactionViewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)

        val dataSet = BarDataSet(emptyList(), "Months")
        ChartUtils.setBarChartColors(dataSet)

        val barData = BarData(dataSet)
        barChart.data = barData

        transactionViewModel.getTransactionItemsByMonth()
            .observe(viewLifecycleOwner, Observer { monthTransactions ->
                val entries = mutableListOf<BarEntry>()

                for ((month, transactions) in monthTransactions) {
                    val totalAmount = transactions.sumByDouble { it.amount.toDouble() }
                    entries.add(BarEntry(month.toFloat(), totalAmount.toFloat()))
                }

                dataSet.values = entries
                barChart.invalidate()
            })

        return binding.root
    }
}
