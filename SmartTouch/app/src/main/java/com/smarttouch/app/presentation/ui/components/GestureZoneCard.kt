package com.smarttouch.app.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smarttouch.app.data.model.GestureAction
import com.smarttouch.app.data.model.GestureMapping
import com.smarttouch.app.data.model.GestureZone

@Composable
fun GestureZoneCard(
    zone: GestureZone,
    mappings: List<GestureMapping>,
    onMappingClick: (GestureMapping) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val activeMappings = mappings.filter { it.action != GestureAction.NONE }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (activeMappings.isNotEmpty())
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            else
                MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(zone.displayNameRes),
                        style = MaterialTheme.typography.titleSmall,
                    )
                    if (activeMappings.isNotEmpty()) {
                        Text(
                            text = "${activeMappings.size} active mapping${if (activeMappings.size > 1) "s" else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    } else {
                        Text(
                            text = "No actions mapped",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                    )
                }
            }

            // Gesture rows
            AnimatedVisibility(visible = expanded) {
                Column {
                    HorizontalDivider()
                    mappings.forEachIndexed { index, mapping ->
                        MappingRow(
                            mapping = mapping,
                            onClick = { onMappingClick(mapping) },
                        )
                        if (index < mappings.lastIndex) {
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MappingRow(
    mapping: GestureMapping,
    onClick: () -> Unit,
) {
    val hasAction = mapping.action != GestureAction.NONE

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(mapping.gestureType.displayNameRes),
                style = MaterialTheme.typography.bodyMedium,
            )
            TextButton(onClick = onClick) {
                Text(
                    text = if (hasAction) stringResource(mapping.action.displayNameRes) else "Tap to assign",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (hasAction) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
