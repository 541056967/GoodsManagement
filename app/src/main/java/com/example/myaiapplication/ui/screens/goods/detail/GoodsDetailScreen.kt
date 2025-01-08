package com.example.myaiapplication.ui.screens.goods.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myaiapplication.domain.model.Goods
import com.example.myaiapplication.ui.components.DetailItem
import com.example.myaiapplication.ui.components.DetailSection
import com.example.myaiapplication.ui.components.ImagePreview
import com.example.myaiapplication.ui.components.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodsDetailScreen(
    onEditClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    viewModel: GoodsDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedPhotoUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) {
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.goods?.name ?: "物品详情") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    state.goods?.let { goods ->
                        IconButton(onClick = { onEditClick(goods.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "编辑")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "删除")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.error != null) {
                Text(
                    text = state.error!!,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                state.goods?.let { goods ->
                    GoodsDetailContent(
                        goods = goods,
                        onPhotoClick = { selectedPhotoUrl = it }
                    )
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("确认删除") },
                text = { Text("确定要删除这个物品吗？") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.onEvent(GoodsDetailEvent.DeleteGoods)
                            showDeleteDialog = false
                        }
                    ) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("取消")
                    }
                }
            )
        }

        // 照片预览对话框
        selectedPhotoUrl?.let { url ->
            AlertDialog(
                onDismissRequest = { selectedPhotoUrl = null },
                confirmButton = {
                    TextButton(onClick = { selectedPhotoUrl = null }) {
                        Text("关闭")
                    }
                },
                text = {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(url)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoodsDetailContent(
    goods: Goods,
    onPhotoClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 照片部分放在最上面
        if (goods.photoUrls.isNotEmpty()) {
            item {
                DetailSection(title = "照片") {
                    ImagePreview(
                        images = goods.photoUrls,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        item {
            DetailSection(title = "基本信息") {
                DetailItem("名称", goods.name)
                goods.description?.let { DetailItem("描述", it) }
                goods.brand?.let { DetailItem("品牌", it) }
                DetailItem("类别", goods.category)
                DetailItem("状态", goods.status.name)
            }
        }

        // 价格信息部分
        goods.purchaseInfo?.let { info ->
            item {
                DetailSection(title = "价格信息") {
                    DetailItem("购买日期", info.date.toString())
                    DetailItem("购买价格", "¥${info.purchasePrice}")
                    info.currentMarketPrice?.let { 
                        DetailItem("当前市场价", "¥$it")
                    }
                    DetailItem("购买渠道", info.channel)
                }
            }
        }

        item {
            DetailSection(title = "位置信息") {
                DetailItem("区域", goods.location.areaId)
                DetailItem("位置", goods.location.containerPath)
                goods.location.detail?.let { DetailItem("详细位置", it) }
            }
        }

        if (goods.tags.isNotEmpty()) {
            item {
                DetailSection(title = "标签") {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        goods.tags.forEach { tag ->
                            SuggestionChip(
                                onClick = { },
                                label = { Text(tag) }
                            )
                        }
                    }
                }
            }
        }

        if (goods.attributes.isNotEmpty()) {
            item {
                DetailSection(title = "属性") {
                    goods.attributes.forEach { (key, value) ->
                        DetailItem(key, value)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun DetailItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun FlowRow(
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