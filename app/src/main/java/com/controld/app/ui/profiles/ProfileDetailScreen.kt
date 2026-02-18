package com.controld.app.ui.profiles

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.controld.app.ui.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    profileId: String,
    profileName: String,
    onNavigateBack: () -> Unit,
    viewModel: ProfileDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(profileId) {
        viewModel.initialize(profileId, profileName)
    }

    LaunchedEffect(uiState.actionMessage) {
        uiState.actionMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearActionMessage()
        }
    }

    val tabs = listOf("Services", "Rules", "Filters")

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(profileName) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadAll() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            LoadingIndicator(modifier = Modifier.padding(padding))
        } else {
            Column(modifier = Modifier.padding(padding)) {
                TabRow(selectedTabIndex = uiState.selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = uiState.selectedTab == index,
                            onClick = { viewModel.selectTab(index) },
                            text = { Text(title) }
                        )
                    }
                }

                when (uiState.selectedTab) {
                    0 -> ServicesTab(
                        services = uiState.services,
                        onModifyService = { serviceId, status, doValue ->
                            viewModel.modifyService(serviceId, status, doValue)
                        }
                    )
                    1 -> RulesTab(
                        rules = uiState.rules,
                        groups = uiState.ruleGroups,
                        onCreateRule = { host, status, group ->
                            viewModel.createRule(host, status, group)
                        },
                        onDeleteRule = { viewModel.deleteRule(it) }
                    )
                    2 -> FiltersTab(
                        filters = uiState.filters,
                        externalFilters = uiState.externalFilters,
                        onModifyFilter = { filterPk, status ->
                            viewModel.modifyFilter(filterPk, status)
                        }
                    )
                }
            }
        }
    }
}
