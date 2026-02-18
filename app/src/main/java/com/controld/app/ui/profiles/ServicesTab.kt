package com.controld.app.ui.profiles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.controld.app.data.model.Service
import com.controld.app.data.model.ServiceCategory

@Composable
fun ServicesTab(
    services: List<ServiceCategory>,
    onModifyService: (serviceId: String, status: Int, doValue: String?) -> Unit
) {
    if (services.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No services configured",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            services.forEach { category ->
                item {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                items(category.services, key = { it.pk }) { service ->
                    ServiceItem(
                        service = service,
                        onModify = { status, doValue ->
                            onModifyService(service.pk, status, doValue)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceItem(
    service: Service,
    onModify: (status: Int, doValue: String?) -> Unit
) {
    var showActionDialog by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                service.action?.let { action ->
                    val statusText = when (action.status) {
                        0 -> "Blocked"
                        1 -> "Bypassed"
                        2 -> "Spoofed"
                        3 -> "Redirected"
                        else -> "Default"
                    }
                    val statusColor = when (action.status) {
                        0 -> MaterialTheme.colorScheme.error
                        1 -> MaterialTheme.colorScheme.tertiary
                        2 -> MaterialTheme.colorScheme.secondary
                        3 -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodySmall,
                        color = statusColor
                    )
                }
            }

            // Quick action buttons
            IconButton(onClick = { onModify(0, null) }) {
                Icon(
                    Icons.Default.Block,
                    contentDescription = "Block",
                    tint = if (service.action?.status == 0) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
            IconButton(onClick = { onModify(1, null) }) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Bypass",
                    tint = if (service.action?.status == 1) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
            IconButton(onClick = { showActionDialog = true }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More options"
                )
            }
        }
    }

    if (showActionDialog) {
        ServiceActionDialog(
            serviceName = service.name,
            currentStatus = service.action?.status,
            onDismiss = { showActionDialog = false },
            onAction = { status, doValue ->
                showActionDialog = false
                onModify(status, doValue)
            }
        )
    }
}

@Composable
fun ServiceActionDialog(
    serviceName: String,
    currentStatus: Int?,
    onDismiss: () -> Unit,
    onAction: (status: Int, doValue: String?) -> Unit
) {
    var selectedAction by remember { mutableIntStateOf(currentStatus ?: -1) }
    var doValue by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(serviceName) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Select action:", style = MaterialTheme.typography.bodyMedium)

                listOf(
                    -1 to "Default (no action)",
                    0 to "Block",
                    1 to "Bypass",
                    2 to "Spoof (redirect to IP)",
                    3 to "Redirect via Proxy"
                ).forEach { (action, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = selectedAction == action,
                            onClick = { selectedAction = action }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = label, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                if (selectedAction == 2 || selectedAction == 3) {
                    OutlinedTextField(
                        value = doValue,
                        onValueChange = { doValue = it },
                        label = {
                            Text(
                                if (selectedAction == 2) "Target IP" else "IATA Code (e.g. LAX)"
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val dv = if (selectedAction == 2 || selectedAction == 3) doValue.takeIf { it.isNotBlank() } else null
                    onAction(selectedAction, dv)
                }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
