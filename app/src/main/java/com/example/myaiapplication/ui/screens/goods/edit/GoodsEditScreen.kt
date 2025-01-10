package com.example.myaiapplication.ui.screens.goods.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myaiapplication.ui.components.DetailSection
import com.example.myaiapplication.ui.components.ImagePicker
import com.example.myaiapplication.util.ImageManager
import com.example.myaiapplication.util.PermissionManager
import com.example.myaiapplication.domain.model.GoodsStatus
import com.example.myaiapplication.domain.model.Location
import com.example.myaiapplication.domain.model.PurchaseInfo
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodsEditScreen(
    onSaveSuccess: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: GoodsEditViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val imageManager = remember { ImageManager(context) }
    val permissionManager = remember { PermissionManager(context) }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.name.isNotEmpty()) state.name else "新建物品") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(GoodsEditEvent.Save) }) {
                        Icon(Icons.Default.Save, contentDescription = "保存")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 基本信息
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { viewModel.onEvent(GoodsEditEvent.NameChanged(it)) },
                    label = { Text("名称") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.error?.contains("名称") == true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = state.description,
                    onValueChange = { viewModel.onEvent(GoodsEditEvent.DescriptionChanged(it)) },
                    label = { Text("描述") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = state.brand,
                    onValueChange = { viewModel.onEvent(GoodsEditEvent.BrandChanged(it)) },
                    label = { Text("品牌") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = state.category,
                    onValueChange = { viewModel.onEvent(GoodsEditEvent.CategoryChanged(it)) },
                    label = { Text("类别") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.error?.contains("类别") == true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                // 状态选择
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = {}
                ) {
                    OutlinedTextField(
                        value = state.status.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("状态") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = false,
                        onDismissRequest = {}
                    ) {
                        GoodsStatus.values().forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.name) },
                                onClick = { viewModel.onEvent(GoodsEditEvent.StatusChanged(status)) }
                            )
                        }
                    }
                }

                // 价格信息
                OutlinedTextField(
                    value = state.purchaseInfo.purchasePrice,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("^\\d*\\.?\\d{0,2}"))) {
                            viewModel.onEvent(GoodsEditEvent.PurchaseInfoChanged(
                                state.purchaseInfo.copy(purchasePrice = newValue)
                            ))
                        }
                    },
                    label = { Text("购入价格") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                // 位置信息
                OutlinedTextField(
                    value = state.location.areaId,
                    onValueChange = { 
                        viewModel.onEvent(GoodsEditEvent.LocationChanged(
                            state.location.copy(areaId = it)
                        ))
                    },
                    label = { Text("区域") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = state.location.containerPath,
                    onValueChange = { 
                        viewModel.onEvent(GoodsEditEvent.LocationChanged(
                            state.location.copy(containerPath = it)
                        ))
                    },
                    label = { Text("位置路径") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                DetailSection(title = "照片") {
                    ImagePicker(
                        images = state.photoUrls,
                        onImagesChanged = { urls ->
                            viewModel.onEvent(GoodsEditEvent.PhotosChanged(urls))
                        },
                        imageManager = imageManager,
                        permissionManager = permissionManager,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 错误提示
                state.error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
} 