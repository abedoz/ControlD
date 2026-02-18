package com.controld.app.ui.devices

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.controld.app.data.model.Device
import com.controld.app.data.model.Profile

@Composable
fun DeviceCreateDialog(
    profiles: List<Profile>,
    onDismiss: () -> Unit,
    onCreate: (name: String, profileId: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedProfileId by remember { mutableStateOf<String?>(null) }
    var profileDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Device") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Device Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = profileDropdownExpanded,
                    onExpandedChange = { profileDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = profiles.find { it.pk == selectedProfileId }?.name ?: "No profile",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Profile") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = profileDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = profileDropdownExpanded,
                        onDismissRequest = { profileDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("No profile") },
                            onClick = {
                                selectedProfileId = null
                                profileDropdownExpanded = false
                            }
                        )
                        profiles.forEach { profile ->
                            DropdownMenuItem(
                                text = { Text(profile.name) },
                                onClick = {
                                    selectedProfileId = profile.pk
                                    profileDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onCreate(name, selectedProfileId) },
                enabled = name.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DeviceEditDialog(
    device: Device,
    profiles: List<Profile>,
    onDismiss: () -> Unit,
    onSave: (name: String?, profileId: String?, status: Int?) -> Unit
) {
    var name by remember { mutableStateOf(device.name) }
    var selectedProfileId by remember { mutableStateOf(device.profile?.pk) }
    var profileDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Device") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Device Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = profileDropdownExpanded,
                    onExpandedChange = { profileDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = profiles.find { it.pk == selectedProfileId }?.name ?: "No profile",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Profile") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = profileDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = profileDropdownExpanded,
                        onDismissRequest = { profileDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("No profile") },
                            onClick = {
                                selectedProfileId = null
                                profileDropdownExpanded = false
                            }
                        )
                        profiles.forEach { profile ->
                            DropdownMenuItem(
                                text = { Text(profile.name) },
                                onClick = {
                                    selectedProfileId = profile.pk
                                    profileDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Resolver info (read-only)
                device.resolvers?.let { resolvers ->
                    HorizontalDivider()
                    Text(
                        text = "DNS Resolvers",
                        style = MaterialTheme.typography.titleSmall
                    )
                    resolvers.dot?.let {
                        Text(
                            text = "DoT: $it",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    resolvers.doh?.let {
                        Text(
                            text = "DoH: $it",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name, selectedProfileId, null) },
                enabled = name.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
