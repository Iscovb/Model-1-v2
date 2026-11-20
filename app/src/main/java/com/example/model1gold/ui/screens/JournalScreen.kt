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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model1gold.model.TradeLogEntry
import com.example.model1gold.model.TradeResult
import com.example.model1gold.model.TradeSide
import com.example.model1gold.ui.theme.CrimsonBear
import com.example.model1gold.ui.theme.EmeraldBull
import com.example.model1gold.ui.theme.GoldPrimary
import com.example.model1gold.ui.theme.SlateBorder
import com.example.model1gold.ui.theme.SlateSurface
import com.example.model1gold.ui.theme.TextSecondary
import java.time.LocalDate
import java.util.Locale

@Composable
fun JournalScreen(
    logs: List<TradeLogEntry>,
    onAddLog: (String, String, TradeSide, Double, Double, Double, Double, TradeResult, List<String>, String, Int) -> Unit,
    onDeleteLog: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    val wins = logs.count { it.result == TradeResult.WIN }
    val losses = logs.count { it.result == TradeResult.LOSS }
    val winRate = if (wins + losses > 0) (wins * 100.0 / (wins + losses)) else 0.0
    val totalR = logs.sumOf {
        when (it.result) {
            TradeResult.WIN -> it.rMultiple
            TradeResult.LOSS -> -1.0
            else -> 0.0
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Stats Summary Card
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
                        Text("Mode C: Trading Journal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Button(
                            onClick = { showAddDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("add_trade_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Trade")
                            Spacer(Modifier.width(4.dp))
                            Text("Ku Dar", fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatBox("Win Rate", String.format(Locale.US, "%.0f%%", winRate), EmeraldBull, Modifier.weight(1f))
                        StatBox("Total R", String.format(Locale.US, "%+.1fR", totalR), GoldPrimary, Modifier.weight(1f))
                        StatBox("W / L", "$wins / $losses", MaterialTheme.colorScheme.onSurface, Modifier.weight(1f))
                    }
                }
            }
        }

        // Trade Entries
        items(logs, key = { it.id }) { log ->
            val resultColor = when (log.result) {
                TradeResult.WIN -> EmeraldBull
                TradeResult.LOSS -> CrimsonBear
                else -> GoldPrimary
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = BorderStroke(1.dp, resultColor.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                color = if (log.side == TradeSide.BUY) EmeraldBull.copy(alpha = 0.2f) else CrimsonBear.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = log.side.name,
                                    color = if (log.side == TradeSide.BUY) EmeraldBull else CrimsonBear,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Text("${log.date} • ${log.timeNY} NY", fontSize = 12.sp, color = TextSecondary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${log.result.name} (${log.rMultiple}R)",
                                color = resultColor,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            IconButton(
                                onClick = { onDeleteLog(log.id) },
                                modifier = Modifier.testTag("delete_log_${log.id}")
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete trade", tint = TextSecondary)
                            }
                        }
                    }

                    Text(
                        text = "Entry: ${log.entryPrice}  |  SL: ${log.slPrice}  |  TP: ${log.tpPrice}",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (log.notes.isNotBlank()) {
                        Text(log.notes, fontSize = 12.sp, color = TextSecondary, lineHeight = 17.sp)
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddTradeDialog(
            onDismiss = { showAddDialog = false },
            onSave = { side, entry, sl, tp, rMult, result, notes ->
                onAddLog(
                    LocalDate.now().toString(),
                    "08:30",
                    side,
                    entry,
                    sl,
                    tp,
                    rMult,
                    result,
                    listOf("Model#1"),
                    notes,
                    4
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun StatBox(label: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Surface(
        color = Color(0xFF1E293B),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 10.sp, color = TextSecondary)
            Spacer(Modifier.height(2.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = valueColor, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
private fun AddTradeDialog(
    onDismiss: () -> Unit,
    onSave: (TradeSide, Double, Double, Double, Double, TradeResult, String) -> Unit
) {
    var side by remember { mutableStateOf(TradeSide.BUY) }
    var result by remember { mutableStateOf(TradeResult.WIN) }
    var entryText by remember { mutableStateOf("4000.00") }
    var slText by remember { mutableStateOf("3990.50") }
    var tpText by remember { mutableStateOf("4028.50") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Diiwaangeli Trade Cusub", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = side == TradeSide.BUY,
                        onClick = { side = TradeSide.BUY },
                        label = { Text("BUY") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = side == TradeSide.SELL,
                        onClick = { side = TradeSide.SELL },
                        label = { Text("SELL") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(TradeResult.WIN, TradeResult.LOSS, TradeResult.MISSED).forEach { r ->
                        FilterChip(
                            selected = result == r,
                            onClick = { result = r },
                            label = { Text(r.name, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                OutlinedTextField(
                    value = entryText,
                    onValueChange = { entryText = it },
                    label = { Text("Entry Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = slText,
                        onValueChange = { slText = it },
                        label = { Text("SL Price") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = tpText,
                        onValueChange = { tpText = it },
                        label = { Text("TP Price") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Faahfaahin (Notes)") },
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val entry = entryText.toDoubleOrNull() ?: 0.0
                    val sl = slText.toDoubleOrNull() ?: 0.0
                    val tp = tpText.toDoubleOrNull() ?: 0.0
                    val slDist = kotlin.math.abs(entry - sl)
                    val tpDist = kotlin.math.abs(tp - entry)
                    val rMult = if (slDist > 0) ((tpDist / slDist) * 10).toInt() / 10.0 else 3.0
                    onSave(side, entry, sl, tp, rMult, result, notes)
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                modifier = Modifier.testTag("confirm_save_trade_button")
            ) {
                Text("Kaydi", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Jooji")
            }
        }
    )
}
