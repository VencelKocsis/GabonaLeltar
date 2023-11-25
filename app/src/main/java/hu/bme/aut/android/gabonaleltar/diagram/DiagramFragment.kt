package hu.bme.aut.android.gabonaleltar.diagram

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.github.mikephil.charting.formatter.ValueFormatter
import hu.bme.aut.android.gabonaleltar.databinding.FragmentDiagramBinding
import hu.bme.aut.android.gabonaleltar.transaction.TransactionViewModel

object ChartUtils {
    fun generateDistinctColors(size: Int): IntArray {
        val colors = IntArray(size)

        val saturation = 0.8f
        val brightness = 0.8f

        for (i in 0 until size) {
            val hue = (i * (360 / size)).toFloat()
            colors[i] = Color.HSVToColor(floatArrayOf(hue, saturation, brightness))
        }

        return colors
    }

    fun setBarChartColors(barDataSet: BarDataSet) {
        val customColors = generateDistinctColors(barDataSet.entryCount)
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

        val barChart: BarChart = binding.chartGrains

        transactionViewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)

        val dataSet = BarDataSet(emptyList(), "Napok")
        ChartUtils.setBarChartColors(dataSet)

        val barData = BarData(dataSet)
        barChart.data = barData

        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.description.isEnabled = false
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.setDrawAxisLine(true)
        barChart.xAxis.setDrawLabels(true)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisLeft.setDrawAxisLine(true)
        barChart.axisLeft.setDrawLabels(true)
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = true
        barChart.animateY(1000)

        transactionViewModel.transactionItemsByDay
            .observe(viewLifecycleOwner, Observer { dayTransactions ->
                Log.d("DiagramFragment", "Received data: $dayTransactions")
                val entries = mutableListOf<BarEntry>()

                dayTransactions.forEachIndexed { index, (day, transactions) ->
                    val totalAmount = transactions.sumByDouble { it.amount.toDouble() }
                    entries.add(BarEntry(index.toFloat(), totalAmount.toFloat()))
                }

                val dataSet = BarDataSet(entries, "Days")
                ChartUtils.setBarChartColors(dataSet)

                val barData = BarData(dataSet)

                barData.setValueTextSize(14f)

                barChart.data = barData
                barChart.invalidate()
            })

        return binding.root
    }
}
