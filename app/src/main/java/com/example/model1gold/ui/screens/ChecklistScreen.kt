package com.example.model1gold.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model1gold.data.Model1Repository
import com.example.model1gold.model.ChecklistStatus
import com.example.model1gold.model.TradeSide
import com.example.model1gold.ui.theme.CrimsonBear
import com.example.model1gold.ui.theme.EmeraldBull
import com.example.model1gold.ui.theme.GoldPrimary
import com.example.model1gold.ui.theme.SlateBorder
import com.example.model1gold.ui.theme.SlateSurface
import com.example.model1gold.ui.theme.TextSecondary

@Composable
fun ChecklistScreen(
    side: TradeSide,
    scores: Map<String, ChecklistStatus>,
    onSideChange: (TradeSide) -> Unit,
    onScoreToggle: (String, ChecklistStatus) -> Unit,
    onReset: () -> Unit,
    onSendToJournal: () -> Unit
) {
    val passedCount = scores.values.count { it == ChecklistStatus.PASS }
    val failedCount = scores.values.count { it == ChecklistStatus.FAIL }

    val (verdict, verdictColor) = when {
        passedCount >= 9 && failedCount == 0 -> "A+ MODEL#1 SETUP ($passedCount/11)" to EmeraldBull
        passedCount >= 6 -> "DEVELOPING SETUP ($passedCount/11)" to GoldPrimary
        else -> "NO TRADE YET ($passedCount/11)" to CrimsonBear
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header & Verdict Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = BorderStroke(1.dp, verdictColor.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Mode B: 11-Point Chart Review", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(verdict, color = verdictColor, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        }
                        IconButton(
                            onClick = onReset,
                            modifier = Modifier.testTag("checklist_reset_button")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset checklist", tint = TextSecondary)
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = side == TradeSide.BUY,
                            onClick = { onSideChange(TradeSide.BUY) },
                            label = { Text("BUY (Bullish)", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.weight(1f).testTag("checklist_buy_chip"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldBull,
                                selectedLabelColor = Color.Black
                            )
                        )
                        FilterChip(
                            selected = side == TradeSide.SELL,
                            onClick = { onSideChange(TradeSide.SELL) },
                            label = { Text("SELL (Bearish)", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.weight(1f).testTag("checklist_sell_chip"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CrimsonBear,
                                selectedLabelColor = Color.White
                            )
                        )
                    }

                    Button(
                        onClick = onSendToJournal,
                        modifier = Modifier.fillMaxWidth().testTag("send_to_journal_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Send to Journal")
                        Spacer(Modifier.width(8.dp))
                        Text("U Dir Trade Log (Journal)", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 11 Checklist Items
        items(Model1Repository.checklistItems, key = { it.id }) { item ->
            val currentStatus = scores[item.id]
            val borderColor = when (currentStatus) {
                ChecklistStatus.PASS -> EmeraldBull
                ChecklistStatus.FAIL -> CrimsonBear
                ChecklistStatus.CANT_SEE -> GoldPrimary
                null -> SlateBorder
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = BorderStroke(1.dp, borderColor.copy(alpha = 0.7f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.category.label.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Text(
                            text = item.name,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Text(item.somaliName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        text = if (side == TradeSide.BUY) item.buyRequirement else item.sellRequirement,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = currentStatus == ChecklistStatus.PASS,
                            onClick = { onScoreToggle(item.id, ChecklistStatus.PASS) },
                            label = { Text("PASS", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.weight(1f).testTag("pass_${item.id}"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldBull,
                                selectedLabelColor = Color.Black
                            )
                        )
                        FilterChip(
                            selected = currentStatus == ChecklistStatus.FAIL,
                            onClick = { onScoreToggle(item.id, ChecklistStatus.FAIL) },
                            label = { Text("FAIL", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.weight(1f).testTag("fail_${item.id}"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CrimsonBear,
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            selected = currentStatus == ChecklistStatus.CANT_SEE,
                            onClick = { onScoreToggle(item.id, ChecklistStatus.CANT_SEE) },
                            label = { Text("N/A", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.weight(1f).testTag("na_${item.id}"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldPrimary,
                                selectedLabelColor = Color.Black
                            )
                        )
                    }
                }
            }
        }
    }
}
