package com.example.myaiapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val spacing = 8.dp.toPx()
        
        val rows = mutableListOf<List<androidx.compose.ui.layout.Placeable>>()
        var rowWidth = 0
        var rowPlaceables = mutableListOf<androidx.compose.ui.layout.Placeable>()
        
        measurables.forEach { measurable ->
            val placeable = measurable.measure(constraints.copy(minWidth = 0))
            
            if (rowWidth + placeable.width > constraints.maxWidth && rowPlaceables.isNotEmpty()) {
                rows.add(rowPlaceables)
                rowPlaceables = mutableListOf()
                rowWidth = 0
            }
            
            rowPlaceables.add(placeable)
            rowWidth += placeable.width + spacing.toInt()
        }
        
        if (rowPlaceables.isNotEmpty()) {
            rows.add(rowPlaceables)
        }
        
        val height = rows.size * (rows.firstOrNull()?.firstOrNull()?.height ?: 0) +
                (rows.size - 1) * spacing.toInt()
        
        layout(constraints.maxWidth, height.toInt()) {
            var y = 0
            rows.forEach { row ->
                var x = 0
                row.forEach { placeable ->
                    placeable.place(x, y)
                    x += placeable.width + spacing.toInt()
                }
                y += (row.firstOrNull()?.height ?: 0) + spacing.toInt()
            }
        }
    }
} 