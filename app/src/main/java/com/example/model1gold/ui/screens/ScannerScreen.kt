package com.example.model1gold.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model1gold.model.LiveScannerState
import com.example.model1gold.model.NYTimeStatus
import com.example.model1gold.model.ScannerScenario
import com.example.model1gold.ui.theme.CrimsonBear
import com.example.model1gold.ui.theme.EmeraldBull
import com.example.model1gold.ui.theme.GoldPrimary
import com.example.model1gold.ui.theme.SkyAccent
import com.example.model1gold.ui.theme.SlateBorder
import com.example.model1gold.ui.theme.SlateSurface
import com.example.model1gold.ui.theme.SlateSurfaceVariant
import com.example.model1gold.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun ScannerScreen(
    nyTime: NYTimeStatus,
    scannerState: LiveScannerState,
    currentScenario: ScannerScenario,
    onScenarioChange: (ScannerScenario) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Live NY & EAT Clock + Session Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = BorderStroke(1.dp, if (nyTime.isPurgeWindow) GoldPrimary else SlateBorder),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "NY Time",
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "NY TIME",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Surface(
                            color = if (nyTime.isMarketOpen) EmeraldBull.copy(alpha = 0.15f) else CrimsonBear.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = if (nyTime.isMarketOpen) "MARKET OPEN" else "WEEKEND CLOSED",
                                color = if (nyTime.isMarketOpen) EmeraldBull else CrimsonBear,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = nyTime.formattedTime,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = nyTime.formattedDate,
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "EAT (Muqdisho)",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = nyTime.eatFormattedTime,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = SkyAccent
                            )
                        }
                    }

                    Surface(
                        color = if (nyTime.isPurgeWindow) GoldPrimary.copy(alpha = 0.15f) else SlateSurfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "${nyTime.activeSessionName} • ${nyTime.activeSessionDesc}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = scannerState.purgeDetails,
                                fontSize = 11.sp,
                                color = if (nyTime.isPurgeWindow) GoldPrimary else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Scenario Switcher
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = currentScenario == ScannerScenario.CURRENT_DEVELOPING,
                    onClick = { onScenarioChange(ScannerScenario.CURRENT_DEVELOPING) },
                    label = { Text("Current Developing", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f).testTag("scenario_developing_chip"),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldPrimary,
                        selectedLabelColor = Color.Black
                    )
                )
                FilterChip(
                    selected = currentScenario == ScannerScenario.MORNING_SELL_COMPLETED,
                    onClick = { onScenarioChange(ScannerScenario.MORNING_SELL_COMPLETED) },
                    label = { Text("Morning Sell (3R)", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f).testTag("scenario_morning_sell_chip"),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldPrimary,
                        selectedLabelColor = Color.Black
                    )
                )
            }
        }

        // XAUUSD Price & Key Levels Grid
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = BorderStroke(1.dp, SlateBorder),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("XAUUSD (GOLD)", fontSize = 12.sp, color = GoldPrimary, fontWeight = FontWeight.Bold)
                            Text(
                                text = "$${String.format(Locale.US, "%.2f", scannerState.currentPrice)}",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Surface(
                            color = CrimsonBear.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "DAILY BIAS: ${scannerState.dailyBias}",
                                color = CrimsonBear,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        LevelPill("Day High", scannerState.dayHigh, Modifier.weight(1f))
                        LevelPill("Day Low", scannerState.dayLow, Modifier.weight(1f))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        LevelPill("PDH", scannerState.pdh, Modifier.weight(1f))
                        LevelPill("PDL", scannerState.pdl, Modifier.weight(1f))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        LevelPill("PWH", scannerState.pwh, Modifier.weight(1f))
                        LevelPill("PWL", scannerState.pwl, Modifier.weight(1f))
                    }
                }
            }
        }

        // Active Setup Card
        item {
            val setup = scannerState.activeSetup
            val progress = setup.confidenceScore.toFloat() / setup.totalChecklist.toFloat()
            val dirColor = when (setup.direction) {
                "BUY" -> EmeraldBull
                "SELL" -> CrimsonBear
                else -> TextSecondary
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = BorderStroke(1.dp, dirColor.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.RadioButtonChecked,
                                contentDescription = "Active Setup",
                                tint = dirColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Setup: ${setup.direction} (${setup.confidenceScore}/${setup.totalChecklist})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = dirColor
                            )
                        }
                        if (setup.rMultiple > 0) {
                            Text(
                                text = "${setup.rMultiple}R",
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace,
                                color = GoldPrimary
                            )
                        }
                    }

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                        color = if (setup.confidenceScore >= 8) EmeraldBull else GoldPrimary,
                        trackColor = SlateSurfaceVariant
                    )

                    Text(
                        text = setup.summarySomali,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 19.sp
                    )

                    if (setup.entryPrice > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LevelPill("Entry #1", setup.entryPrice, Modifier.weight(1f))
                            LevelPill("Stop Loss", setup.slPrice, Modifier.weight(1f))
                            LevelPill("Target TP", setup.tpPrice, Modifier.weight(1f))
                        }
                    }

                    if (setup.passedItems.isNotEmpty()) {
                        Text("Qodobada Gudbay:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldBull)
                        setup.passedItems.forEach { passed ->
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Passed",
                                    tint = EmeraldBull,
                                    modifier = Modifier.size(14.dp).padding(top = 2.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(passed, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }

                    if (setup.missingItems.isNotEmpty()) {
                        Text("Waxa Dhiman / Fiiro Gaar ah:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                        setup.missingItems.forEach { missing ->
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Missing",
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(14.dp).padding(top = 2.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(missing, fontSize = 12.sp, color = TextSecondary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LevelPill(label: String, price: Double, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SlateSurfaceVariant)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column {
            Text(label, fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
            Text(
                text = String.format(Locale.US, "%.2f", price),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
