package com.example.model1gold.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model1gold.data.Model1Repository
import com.example.model1gold.model.ChatMessage
import com.example.model1gold.model.ChecklistStatus
import com.example.model1gold.model.LiveScannerState
import com.example.model1gold.model.NYTimeStatus
import com.example.model1gold.model.ScannerScenario
import com.example.model1gold.model.TradeLogEntry
import com.example.model1gold.model.TradeResult
import com.example.model1gold.model.TradeSide
import com.example.model1gold.model.UnknownItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Model1ViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("model1_gold_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    private val _nyTime = MutableStateFlow(Model1Repository.getNYTimeStatus())
    val nyTime: StateFlow<NYTimeStatus> = _nyTime.asStateFlow()

    private val _scenario = MutableStateFlow(ScannerScenario.CURRENT_DEVELOPING)
    val scenario: StateFlow<ScannerScenario> = _scenario.asStateFlow()

    private val _scannerState = MutableStateFlow(
        Model1Repository.evaluateMarketState(_scenario.value, _nyTime.value)
    )
    val scannerState: StateFlow<LiveScannerState> = _scannerState.asStateFlow()

    // Mode B: 11-Point Checklist state
    private val _checklistSide = MutableStateFlow(TradeSide.BUY)
    val checklistSide: StateFlow<TradeSide> = _checklistSide.asStateFlow()

    private val _checklistScores = MutableStateFlow<Map<String, ChecklistStatus>>(emptyMap())
    val checklistScores: StateFlow<Map<String, ChecklistStatus>> = _checklistScores.asStateFlow()

    // Mode C: Trade Journal logs
    private val _logs = MutableStateFlow<List<TradeLogEntry>>(loadLogs())
    val logs: StateFlow<List<TradeLogEntry>> = _logs.asStateFlow()

    // Unknowns tracker
    private val _unknowns = MutableStateFlow(Model1Repository.initialUnknowns)
    val unknowns: StateFlow<List<UnknownItem>> = _unknowns.asStateFlow()

    // AI Study Coach Chat
    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage(
                isUser = false,
                content = "Asc! Waxaan ahay kaaliyahaaga barashada Model#1 (XAUUSD kaliya) ee ICT TET mentorship (2026).\n\nSharcigeyga adag: Waxba ma abuuro (DO NOT INVENT). Haddii wax aan la hubin la i weydiiyo, waxaan ku leeyahay \"ma hubo\". Weligayna ma bixiyo baaqyo iibsi/iibin ah.\n\nMaxaan kaala shaqeeyaa maanta?"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                val status = Model1Repository.getNYTimeStatus()
                _nyTime.value = status
                _scannerState.value = Model1Repository.evaluateMarketState(_scenario.value, status)
                delay(1000L)
            }
        }
    }

    fun setScenario(newScenario: ScannerScenario) {
        _scenario.value = newScenario
        _scannerState.value = Model1Repository.evaluateMarketState(newScenario, _nyTime.value)
    }

    fun setChecklistSide(side: TradeSide) {
        _checklistSide.value = side
    }

    fun setChecklistItemStatus(itemId: String, status: ChecklistStatus) {
        val updated = _checklistScores.value.toMutableMap()
        if (updated[itemId] == status) {
            updated.remove(itemId)
        } else {
            updated[itemId] = status
        }
        _checklistScores.value = updated
    }

    fun resetChecklist() {
        _checklistScores.value = emptyMap()
    }

    fun addTradeLog(
        date: String,
        timeNY: String,
        side: TradeSide,
        entryPrice: Double,
        slPrice: Double,
        tpPrice: Double,
        rMultiple: Double,
        result: TradeResult,
        itemsPassed: List<String>,
        notes: String,
        confidenceScore: Int
    ) {
        val entry = TradeLogEntry(
            id = "log-${System.currentTimeMillis()}",
            date = date,
            timeNY = timeNY,
            side = side,
            entryPrice = entryPrice,
            slPrice = slPrice,
            tpPrice = tpPrice,
            rMultiple = rMultiple,
            result = result,
            itemsPassed = itemsPassed,
            notes = notes,
            confidenceScore = confidenceScore,
            createdAt = System.currentTimeMillis()
        )
        val updated = listOf(entry) + _logs.value
        _logs.value = updated
        saveLogs(updated)
    }

    fun deleteTradeLog(id: String) {
        val updated = _logs.value.filterNot { it.id == id }
        _logs.value = updated
        saveLogs(updated)
    }

    fun updateUnknownNote(id: String, note: String, confirmed: Boolean) {
        _unknowns.value = _unknowns.value.map {
            if (it.id == id) it.copy(userNotes = note, isConfirmed = confirmed) else it
        }
    }

    fun sendChatMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return
        val reply = Model1Repository.answerModel1Question(trimmed)
        _chatMessages.value = _chatMessages.value + listOf(
            ChatMessage(isUser = true, content = trimmed),
            ChatMessage(isUser = false, content = reply)
        )
    }

    private fun loadLogs(): List<TradeLogEntry> {
        val raw = prefs.getString("trade_logs_json", null) ?: return Model1Repository.initialLogs
        return try {
            json.decodeFromString<List<TradeLogEntry>>(raw)
        } catch (_: Exception) {
            Model1Repository.initialLogs
        }
    }

    private fun saveLogs(entries: List<TradeLogEntry>) {
        prefs.edit().putString("trade_logs_json", json.encodeToString(entries)).apply()
    }
}
