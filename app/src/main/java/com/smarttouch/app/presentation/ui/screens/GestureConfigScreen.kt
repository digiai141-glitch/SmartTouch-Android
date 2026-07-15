package com.smarttouch.app.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smarttouch.app.R
import com.smarttouch.app.data.model.GestureMapping
import com.smarttouch.app.data.model.GestureZone
import com.smarttouch.app.presentation.ui.components.ActionPickerDialog
import com.smarttouch.app.presentation.ui.components.GestureZoneCard
import com.smarttouch.app.presentation.viewmodel.GestureConfigViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestureConfigScreen(
    onNavigateBack: () -> Unit,
    viewModel: GestureConfigViewModel = hiltViewModel(),
) {
    val mappingsByZone by viewModel.mappingsByZone.collectAsStateWithLifecycle()
    var editingMapping by remember { mutableStateOf<GestureMapping?>(null) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_gestures)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val zones = GestureZone.entries
            items(zones, key = { it.key }) { zone ->
                val mappings = mappingsByZone[zone] ?: emptyList()
                GestureZoneCard(
                    zone = zone,
                    mappings = mappings,
                    onMappingClick = { mapping -> editingMapping = mapping },
                )
            }
        }
    }

    editingMapping?.let { mapping ->
        ActionPickerDialog(
            currentMapping = mapping,
            onDismiss = { editingMapping = null },
            onConfirm = { updated ->
                viewModel.updateMapping(updated)
                editingMapping = null
            },
        )
    }
}
