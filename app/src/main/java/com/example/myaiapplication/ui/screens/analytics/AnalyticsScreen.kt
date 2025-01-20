package com.example.myaiapplication.ui.screens.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.himanshoe.charty.common.ChartData
import com.himanshoe.charty.common.ChartDataCollection
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.config.PieChartConfig
import com.himanshoe.charty.pie.config.PieChartDefaults
import com.himanshoe.charty.pie.model.PieData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("数据分析") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // 在这里添加数据分析的UI组件，例如图表、统计信息等
            Text("数据分析内容展示")
            val data = ChartDataCollection(listOf<ChartData>(PieData(20F, 20, Color.Blue),PieData(40F, 20, Color.Red), PieData(55F, 20, Color.Gray)))
            PieChart(dataCollection = data, modifier = Modifier.fillMaxSize(), pieChartConfig = PieChartConfig(donut = true, showLabel = false))

        }
    }
}
