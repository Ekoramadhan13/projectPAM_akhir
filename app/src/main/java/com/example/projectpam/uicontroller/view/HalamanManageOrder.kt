package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.projectpam.uicontroller.viewmodel.AdminOrderViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanManageOrder(
    viewModel: AdminOrderViewModel,
    onBack: () -> Unit
) {
    val orders by viewModel.orders.collectAsState()
    val message by viewModel.message.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    // 🔥 LISTENER MESSAGE (WAJIB)
    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.clearMessage()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Orders (Admin)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(orders) { order ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        /* ===== HEADER ===== */
                        Text(
                            "Order ID : ${order.order_id}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("User ID  : ${order.user_id}")
                        Text("Alamat   : ${order.address}")
                        Text("Total    : Rp ${order.total_price}")
                        Text("Status   : ${order.status}")

                        Spacer(Modifier.height(12.dp))
                        Divider()
                        Spacer(Modifier.height(12.dp))

                        /* ===== ITEM PESANAN ===== */
                        Text("Item Pesanan", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(8.dp))

                        order.items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                AsyncImage(
                                    model = item.product?.image_url,
                                    contentDescription = item.product?.name,
                                    modifier = Modifier.size(60.dp)
                                )

                                Spacer(Modifier.width(12.dp))

                                Column {
                                    Text(item.product?.name ?: "Produk")
                                    Text("Qty   : ${item.quantity}")
                                    Text("Harga : Rp ${item.price}")
                                }
                            }
                        }

                        Spacer(Modifier.height(12.dp))
                        Divider()
                        Spacer(Modifier.height(12.dp))

                        /* ===== DROPDOWN STATUS ===== */
                        val statusList = listOf("Dikemas", "Dikirim", "Selesai")
                        var expanded by remember { mutableStateOf(false) }
                        var selectedStatus by remember(order.order_id) {
                            mutableStateOf(order.status)
                        }

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = selectedStatus,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Ubah Status Pesanan") },
                                trailingIcon = {
                                    Icon(Icons.Default.ArrowDropDown, null)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                statusList.forEach { status ->
                                    DropdownMenuItem(
                                        text = { Text(status) },
                                        onClick = {
                                            selectedStatus = status
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.updateStatus(
                                    orderId = order.order_id,
                                    status = selectedStatus
                                )
                            }
                        ) {
                            Text("Update Status")
                        }
                    }
                }
            }
        }
    }
}
