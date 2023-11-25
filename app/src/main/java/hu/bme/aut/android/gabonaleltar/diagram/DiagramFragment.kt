package hu.bme.aut.android.gabonaleltar.diagram

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ChartUtils {
    fun setBarChartColor(barDataSet: BarDataSet, color: Int) {
        val customColors = IntArray(barDataSet.entryCount) { color }
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

        val barData = BarData(dataSet)
        barChart.data = barData

        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(false)
        barChart.description.isEnabled = false
        barChart.xAxis.setDrawGridLines(true)
        barChart.xAxis.setDrawAxisLine(true)
        barChart.xAxis.setDrawLabels(true)
        barChart.axisLeft.setDrawGridLines(true)
        barChart.axisLeft.setDrawAxisLine(true)
        barChart.axisLeft.setDrawLabels(true)
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = true
        barChart.animateY(1000)

        transactionViewModel.transactionItemsByDay
            .observe(viewLifecycleOwner, Observer { dayTransactions ->
                val entries = mutableListOf<BarEntry>()

                dayTransactions.forEachIndexed { index, (day, transactions) ->
                    val totalAmount = transactions.sumByDouble { it.amount.toDouble() }
                    entries.add(BarEntry(index.toFloat(), totalAmount.toFloat()))
                }

                val dataSet = BarDataSet(entries, "Nap")
                ChartUtils.setBarChartColor(dataSet, Color.rgb(255, 165, 0))

                val barData = BarData(dataSet)

                barChart.xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val index = value.toInt()
                        return if (index >= 0 && index < dayTransactions.size) {
                            val day = dayTransactions[index].first
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(day))
                        } else {
                            ""
                        }
                    }
                }

                barChart.axisLeft.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return String.format(Locale.getDefault(), "%.2f Kg", value)
                    }
                }

                barData.setValueTextSize(14f)

                barChart.data = barData
                barChart.invalidate()
            })

        return binding.root
    }
}
