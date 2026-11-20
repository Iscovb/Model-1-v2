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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model1gold.data.Model1Repository
import com.example.model1gold.model.UnknownItem
import com.example.model1gold.ui.theme.CrimsonBear
import com.example.model1gold.ui.theme.EmeraldBull
import com.example.model1gold.ui.theme.GoldPrimary
import com.example.model1gold.ui.theme.SkyAccent
import com.example.model1gold.ui.theme.SlateBorder
import com.example.model1gold.ui.theme.SlateSurface
import com.example.model1gold.ui.theme.SlateSurfaceVariant
import com.example.model1gold.ui.theme.TextSecondary
import java.util.Locale
import kotlin.math.abs

private enum class ToolsSubTab(val title: String) {
    RISK_CALC("Risk Calc"),
    DAILY_RECAP("Daily Recap"),
    KNOWLEDGE("Knowledge")
}

@Composable
fun ToolsScreen(unknowns: List<UnknownItem>) {
    var activeSubTab by remember { mutableStateOf(ToolsSubTab.RISK_CALC) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ToolsSubTab.entries.forEach { tab ->
                FilterChip(
                    selected = activeSubTab == tab,
                    onClick = { activeSubTab = tab },
                    label = { Text(tab.title, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.weight(1f).testTag("tools_tab_${tab.name.lowercase()}"),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldPrimary,
                        selectedLabelColor = Color.Black
                    )
                )
            }
        }

        when (activeSubTab) {
            ToolsSubTab.RISK_CALC -> RiskCalculatorContent()
            ToolsSubTab.DAILY_RECAP -> DailyRecapContent()
            ToolsSubTab.KNOWLEDGE -> KnowledgeBaseContent(unknowns)
        }
    }
}

@Composable
private fun RiskCalculatorContent() {
    var balanceStr by remember { mutableStateOf("10000") }
    var riskPctStr by remember { mutableStateOf("1.0") }
    var entryStr by remember { mutableStateOf("4027.80") }
    var slStr by remember { mutableStateOf("4035.50") }
    var tpStr by remember { mutableStateOf("4004.70") }

    val balance = balanceStr.toDoubleOrNull() ?: 0.0
    val riskPct = riskPctStr.toDoubleOrNull() ?: 1.0
    val entry = entryStr.toDoubleOrNull() ?: 0.0
    val sl = slStr.toDoubleOrNull() ?: 0.0
    val tp = tpStr.toDoubleOrNull() ?: 0.0

    val riskAmount = balance * (riskPct / 100.0)
    val slDistance = abs(entry - sl)
    val tpDistance = abs(tp - entry)
    val rMultiple = if (slDistance > 0.0) tpDistance / slDistance else 0.0
    // XAUUSD standard lot = 100 oz -> $1 move = $100 per 1.00 lot
    val lotSize = if (slDistance > 0.0) riskAmount / (slDistance * 100.0) else 0.0
    val targetProfit = riskAmount * rMultiple

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = BorderStroke(1.dp, SlateBorder),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("XAUUSD Risk Calculator (1% Rule)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = GoldPrimary)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = balanceStr,
                            onValueChange = { balanceStr = it },
                            label = { Text("Account ($)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("calc_balance_input")
                        )
                        OutlinedTextField(
                            value = riskPctStr,
                            onValueChange = { riskPctStr = it },
                            label = { Text("Risk (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("calc_risk_input")
                        )
                    }
                    OutlinedTextField(
                        value = entryStr,
                        onValueChange = { entryStr = it },
                        label = { Text("Entry #1 Price") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = slStr,
                            onValueChange = { slStr = it },
                            label = { Text("Stop Loss") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = tpStr,
                            onValueChange = { tpStr = it },
                            label = { Text("Take Profit") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Natiijada Xisaabta (Position Sizing)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ResultPill("Lot Size", String.format(Locale.US, "%.2f lots", lotSize), GoldPrimary, Modifier.weight(1f))
                        ResultPill("Risk Amount", String.format(Locale.US, "$%.2f", riskAmount), CrimsonBear, Modifier.weight(1f))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ResultPill("SL Distance", String.format(Locale.US, "$%.2f", slDistance), SkyAccent, Modifier.weight(1f))
                        ResultPill("R-Multiple", String.format(Locale.US, "%.2fR", rMultiple), EmeraldBull, Modifier.weight(1f))
                    }
                    ResultPill("Target Profit", String.format(Locale.US, "+$%.2f", targetProfit), EmeraldBull, Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun ResultPill(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(color = SlateSurfaceVariant, shape = RoundedCornerShape(10.dp), modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, fontSize = 11.sp, color = TextSecondary)
            Spacer(Modifier.height(2.dp))
            Text(value, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace, color = color)
        }
    }
}

@Composable
private fun DailyRecapContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(Model1Repository.dailyRecaps, key = { it.id }) { recap ->
            val statusColor = when (recap.status) {
                "WIN" -> EmeraldBull
                "LOSS" -> CrimsonBear
                else -> GoldPrimary
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${recap.date} • ${recap.dayOfWeek}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(
                            text = "${recap.direction} • ${recap.status}",
                            color = statusColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }
                    Text(recap.dolNarrative, fontSize = 12.sp, color = GoldPrimary, fontWeight = FontWeight.SemiBold)
                    Text(recap.summarySomali, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 18.sp)
                    Surface(color = SlateSurfaceVariant, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Casharka Maalinta: ${recap.keyLessonSomali}",
                            fontSize = 11.sp,
                            color = SkyAccent,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KnowledgeBaseContent(unknowns: List<UnknownItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Sharciyada Muhiimka ah ee Model#1", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = GoldPrimary)
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = BorderStroke(1.dp, SlateBorder),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("• Asset: XAUUSD (Gold) kaliya", fontSize = 12.sp)
                    Text("• 7H Sessions: Asia 17:00 NY | London 00:00 NY | NY 07:00 NY", fontSize = 12.sp)
                    Text("• Prime Purge Hours: 8-10am, 2-3am, 8-10pm NY", fontSize = 12.sp)
                    Text("• Valid SMT Pairs: XAUEUR, XAUGBP, Silver KALIYA", fontSize = 12.sp, color = EmeraldBull)
                    Text("• Invalid SMT Pairs: XAUCHF, XAUNZD, AUD, JPY", fontSize = 12.sp, color = CrimsonBear)
                    Text("• Risk & Target: 1% Risk, SL ~$9-$10, 3R default (min 2R)", fontSize = 12.sp)
                }
            }
        }
        item {
            Spacer(Modifier.height(4.dp))
            Text("Mawduucyada Aan La Hubin (UNKNOWNs)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = GoldPrimary)
        }
        items(unknowns, key = { it.id }) { item ->
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = BorderStroke(1.dp, SlateBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(item.topic, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GoldPrimary)
                    Text(item.currentKnowledge, fontSize = 12.sp, color = TextSecondary, lineHeight = 17.sp)
                }
            }
        }
    }
}
