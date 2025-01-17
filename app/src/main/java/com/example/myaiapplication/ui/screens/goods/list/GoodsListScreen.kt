package com.example.myaiapplication.ui.screens.goods.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.example.myaiapplication.domain.model.Goods

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodsListScreen(
    onGoodsClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    viewModel: GoodsListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var isFromGoodsEdit by rememberSaveable { mutableStateOf(false) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current




    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (isFromGoodsEdit) {
                    selectedTabIndex = 0
                    viewModel.onEvent(GoodsListEvent.Refresh).also { isFromGoodsEdit = false }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("物品管理") },
                actions = {
                    IconButton(onClick = {
                        isFromGoodsEdit = true
                        onAddClick()
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "添加")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 搜索栏
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onEvent(GoodsListEvent.SearchQueryChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("搜索物品") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                edgePadding = 0.dp,  // 设置边缘留白
                indicator = { tabPositions ->
                    val tabPosition = tabPositions[selectedTabIndex]
                    val textWidth = tabPosition.width
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .widthIn(textWidth + 1.dp)
                            .height(1.dp)
                    )
                }
            )  {
                state.areaIds.forEachIndexed { index, title ->
                    Tab(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(32.dp),
                        text = { Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(vertical = 8.dp),
                            text = title,
                            fontSize = 12.sp) },
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            viewModel.onEvent(GoodsListEvent.AreaSelected(title))
                        }
                    )
                }
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.error!!)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement =Arrangement.spacedBy(8.dp)
                ) {
                   items(state.goods.size) { index ->
                       GoodsItem(
                           goods = state.goods[index],
                           onClick = { onGoodsClick(state.goods[index].id) }
                       )
                   }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoodsItem(
    goods: Goods,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // 图片部分
        Surface(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(100.dp),
            shape = RoundedCornerShape(4.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                if (goods.photoUrls.isNotEmpty()) {
                    AsyncImage(
                        model = goods.photoUrls.first(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start // 水平居中
        ) {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = goods.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        if (goods.purchaseInfo.purchasePrice.isNotEmpty()) {

        }
        //价格信息
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start // 水平居中
        ) {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "￥" + goods.purchaseInfo.purchasePrice,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 标签展示
        if (goods.tags.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                goods.tags.take(3).forEach { tag ->
                    SuggestionChip(
                        onClick = { },
                        label = {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
                if (goods.tags.size > 3) {
                    Text(
                        text = "+${goods.tags.size - 3}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
} 