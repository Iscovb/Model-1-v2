package com.example.model1gold.model

import kotlinx.serialization.Serializable

enum class ChecklistStatus { PASS, FAIL, CANT_SEE }

enum class ChecklistCategory(val label: String) {
    HTF("HTF & Structure"),
    TIMING("Timing & Purge"),
    LIQUIDITY("Liquidity & SMT"),
    ENTRY("Entry, SL & TP")
}

data class ChecklistItemDef(
    val id: String,
    val name: String,
    val somaliName: String,
    val category: ChecklistCategory,
    val description: String,
    val tooltip: String,
    val buyRequirement: String,
    val sellRequirement: String
)

@Serializable
enum class TradeSide { BUY, SELL }

@Serializable
enum class TradeResult { WIN, LOSS, MISSED, INVALIDATION, OPEN }

@Serializable
data class TradeLogEntry(
    val id: String,
    val date: String,
    val timeNY: String,
    val side: TradeSide,
    val entryPrice: Double,
    val slPrice: Double,
    val tpPrice: Double,
    val rMultiple: Double,
    val result: TradeResult,
    val itemsPassed: List<String>,
    val notes: String,
    val confidenceScore: Int = 3,
    val createdAt: Long
)

data class UnknownItem(
    val id: String,
    val topic: String,
    val isConfirmed: Boolean,
    val currentKnowledge: String,
    val userNotes: String = ""
)

data class NYTimeStatus(
    val formattedTime: String,
    val formattedDate: String,
    val eatFormattedTime: String,
    val isMarketOpen: Boolean,
    val isWeekendMarketClosed: Boolean,
    val activeSessionName: String,
    val activeSessionOpen: String,
    val activeSessionDesc: String,
    val isPurgeWindow: Boolean,
    val activePurgeName: String?,
    val nextPurgeDescription: String
)

enum class ScannerScenario { CURRENT_DEVELOPING, MORNING_SELL_COMPLETED }

data class ActiveSetup(
    val direction: String, // "BUY", "SELL", "NO_SETUP"
    val confidenceScore: Int,
    val totalChecklist: Int,
    val passedItems: List<String>,
    val missingItems: List<String>,
    val currentStage: String,
    val entryPrice: Double,
    val slPrice: Double,
    val tpPrice: Double,
    val slDistance: Double,
    val rMultiple: Double,
    val summarySomali: String,
    val warnings: List<String>
)

data class LiveScannerState(
    val currentPrice: Double,
    val dayHigh: Double,
    val dayLow: Double,
    val pdl: Double,
    val pdh: Double,
    val pwh: Double,
    val pwl: Double,
    val dailyBias: String,
    val sevenHourSession: String,
    val isPurgeActive: Boolean,
    val purgeDetails: String,
    val lastUpdated: String,
    val activeSetup: ActiveSetup
)

data class ChatMessage(
    val isUser: Boolean,
    val content: String
)

data class DailyRecapItem(
    val id: String,
    val date: String,
    val dayOfWeek: String,
    val dailyBias: String,
    val dolNarrative: String,
    val dayHigh: Double,
    val dayLow: Double,
    val pdh: Double,
    val pdl: Double,
    val direction: String,
    val status: String,
    val rMultiple: Double,
    val summarySomali: String,
    val keyLessonSomali: String
)
