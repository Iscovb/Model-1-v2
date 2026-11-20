package com.example.model1gold.data

import com.example.model1gold.model.ActiveSetup
import com.example.model1gold.model.ChecklistCategory
import com.example.model1gold.model.ChecklistItemDef
import com.example.model1gold.model.DailyRecapItem
import com.example.model1gold.model.LiveScannerState
import com.example.model1gold.model.NYTimeStatus
import com.example.model1gold.model.ScannerScenario
import com.example.model1gold.model.TradeLogEntry
import com.example.model1gold.model.TradeResult
import com.example.model1gold.model.TradeSide
import com.example.model1gold.model.UnknownItem
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Model1Repository {

    val checklistItems: List<ChecklistItemDef> = listOf(
        ChecklistItemDef(
            id = "bias",
            name = "HTF Bias Alignment",
            somaliName = "Iswaafajinta Bias-ka (Daily -> 7H -> 1H)",
            category = ChecklistCategory.HTF,
            description = "Daily, 7H (session candles), iyo 1H waa inay hal dhinac u jeedaan.",
            tooltip = "7H waxay furantaa Asia 17:00, London 00:00, NY 07:00 NY time.",
            buyRequirement = "Bullish orderflow Daily -> 7H -> 1H",
            sellRequirement = "Bearish orderflow Daily -> 7H -> 1H"
        ),
        ChecklistItemDef(
            id = "liquidity_flow",
            name = "IRL -> ERL / ERL -> IRL",
            somaliName = "Socodka Liquidity-ga (IRL/ERL)",
            category = ChecklistCategory.HTF,
            description = "Buys waxay ka socdaan IRL una socdaan ERL; Sells waxay ka socdaan ERL una socdaan IRL.",
            tooltip = "IRL (Internal Range Liquidity) -> ERL (External Range Liquidity).",
            buyRequirement = "Qiimuhu wuxuu ka yimid IRL wuxuuna ku socdaa ERL",
            sellRequirement = "Qiimuhu wuxuu ka yimid ERL wuxuuna ku socdaa IRL"
        ),
        ChecklistItemDef(
            id = "kl",
            name = "KL (Key Level)",
            somaliName = "Heerka Furaha ah (KL)",
            category = ChecklistCategory.HTF,
            description = "Buys: OB, FVG ama IFVG ee 4H ama 1H. Sells: Major LQ (old high ama old low).",
            tooltip = "Key Level waa meesha setup-ku ka bilaabmo.",
            buyRequirement = "KL waa OB, FVG ama IFVG ee 4H ama 1H",
            sellRequirement = "KL waa Major LQ (old high ama old low)"
        ),
        ChecklistItemDef(
            id = "crt",
            name = "CRT (Candle Range Theory)",
            somaliName = "Qaabka CRT ee 4H ama 1H",
            category = ChecklistCategory.HTF,
            description = "Candle 1 wuxuu dhigaa range-ka, Candle 2 wuxuu sweep ku sameeyaa hal dhinac, Candle 3 wuxuu aadaa dhinaca kale.",
            tooltip = "Noocyada: 2-candle, 3-candle, inside bar, Type 6, Daily.",
            buyRequirement = "CRT oo sweep ku sameeyay hoos (range low) oo kor u socda",
            sellRequirement = "CRT oo sweep ku sameeyay kor (range high) oo hoos u socda"
        ),
        ChecklistItemDef(
            id = "purge_hour",
            name = "Purge Hour (NY Time)",
            somaliName = "Saacadda Purge-ka (NY time)",
            category = ChecklistCategory.TIMING,
            description = "Waqtiyada ugu fiican: 8-10pm, 2-3am, iyo 8-10am NY (sidoo kale 6-8am pre-NY).",
            tooltip = "Dhammaan saacadaha waxaa loo xisaabiyaa waqtiga New York.",
            buyRequirement = "Purge-ku wuxuu dhacay 8-10pm, 2-3am ama 8-10am NY",
            sellRequirement = "Purge-ku wuxuu dhacay 8-10pm, 2-3am ama 8-10am NY"
        ),
        ChecklistItemDef(
            id = "ts_sweep",
            name = "TS (Turtle Soup) Sweep",
            somaliName = "Sweep-ka Turtle Soup (TS)",
            category = ChecklistCategory.LIQUIDITY,
            description = "Sweep lagu sameeyo swing high/low, kadibna qiimuhu dib ugu xirmo range-ka gudihiisa (failed breakout).",
            tooltip = "Jebin been ah (failed breakout) oo range-ka dib loogu xirmay.",
            buyRequirement = "Sweep swing low oo dib ugu xirmay range-ka gudihiisa",
            sellRequirement = "Sweep swing high oo dib ugu xirmay range-ka gudihiisa"
        ),
        ChecklistItemDef(
            id = "smt",
            name = "SMT Divergence (2SSMT)",
            somaliName = "SMT Divergence (Lammaanaha Saxda ah)",
            category = ChecklistCategory.LIQUIDITY,
            description = "SMT waxaa kaliya lagu sameyn karaa: XAUEUR, XAUGBP, ama Silver. LAMA OGOLA kuwa kale.",
            tooltip = "Lama ogola: XAUCHF, XAUNZD, AUD, JPY. Waa laba-marxaladood (2SSMT).",
            buyRequirement = "Dahabku wuxuu sameeyay Lower Low laakiin XAUEUR/Silver wuxuu sameeyay Higher Low",
            sellRequirement = "Dahabku wuxuu sameeyay Higher High laakiin XAUEUR/Silver wuxuu sameeyay Lower High"
        ),
        ChecklistItemDef(
            id = "psp",
            name = "PSP Confirmation",
            somaliName = "Xaqiijinta PSP (Isla Saacadda)",
            category = ChecklistCategory.LIQUIDITY,
            description = "Candles-ka labada asset (Dahabka iyo XAUEUR/Silver) waxay ku xirmaan midabbo iska soo horjeeda isla saacaddaas.",
            tooltip = "Tusaale: Dahabku cagaar, XAUEUR guduud isla 1H.",
            buyRequirement = "Midabbada candle-yada Dahabka iyo SMT pair-ka oo iska soo horjeeda isla 1H",
            sellRequirement = "Midabbada candle-yada Dahabka iyo SMT pair-ka oo iska soo horjeeda isla 1H"
        ),
        ChecklistItemDef(
            id = "entry_line",
            name = "Entry #1 Line (5m OB)",
            somaliName = "Xariiqda Gelitaanka #1 (Dhamaadka 5m OB)",
            category = ChecklistCategory.ENTRY,
            description = "Xariiqda ku taal DHAMAADKA Order Block-ka ee 5m. Waa Limit Order. Haddii aan dib loogu noqon, waa missed trade.",
            tooltip = "Line at the END of the OB. Haddii uusan dib ugu laaban #1 line, ma jiro trade.",
            buyRequirement = "Buy limit order oo sax ah xariiqda dhamaadka 5m OB",
            sellRequirement = "Sell limit order oo sax ah xariiqda dhamaadka 5m OB"
        ),
        ChecklistItemDef(
            id = "sl_beyond_sweep",
            name = "SL Beyond TS Sweep",
            somaliName = "SL oo ka dambeeya TS Sweep-ka",
            category = ChecklistCategory.ENTRY,
            description = "Stop Loss wuxuu wax yar ka shisheeyaa sweep-ka (caadi ahaan ~$9-$10 dahabka). Haddii SL aad u weyn yahay, ka bood.",
            tooltip = "SL hoos uga dhacsan sweep low (buys) ama kor uga baxsan sweep high (sells).",
            buyRequirement = "SL wax yar ka hooseeya TS sweep low (~$9-$10)",
            sellRequirement = "SL wax yar ka sarreeya TS sweep high (~$9-$10)"
        ),
        ChecklistItemDef(
            id = "tp_distance",
            name = "TP Distance (3R / 2R Targets)",
            somaliName = "Bartilmaameedka TP (3R default ama 2R)",
            category = ChecklistCategory.ENTRY,
            description = "3R by default. 2R haddii ERL dhowdahay ama SL weyn yahay. Targets: PDL/PDH, PWL/PWH, PML/PMH, TG.",
            tooltip = "Weligaa ha qaadan wax ka yar 2R.",
            buyRequirement = "3R (ama 2R) bartilmaameedsanaya PDH, PWH, PMH ama TG",
            sellRequirement = "3R (ama 2R) bartilmaameedsanaya PDL, PWL, PML ama TG"
        )
    )

    val initialUnknowns: List<UnknownItem> = listOf(
        UnknownItem(
            id = "kod",
            topic = "Micnaha KOD (KOD Meaning)",
            isConfirmed = false,
            currentKnowledge = "Lama hubo waxa KOD u taagan tahay ama sida saxda ah ee mentor-ku ugu adeegsado TSQ sequence-ka: (OTE -> KOD) -> TG.",
            userNotes = "Haddii aad ka hesho video ama Discord qeexidda KOD, halkan ku dar."
        ),
        UnknownItem(
            id = "ote_usage",
            topic = "Sida mentor-ku u isticmaalo OTE",
            isConfirmed = false,
            currentKnowledge = "OTE waa 62%-79% Fibonacci retracement, laakiin lama xaqiijin sida saxda ah ee mentor-ka TET ugu dhex daro Model#1 entry-ga."
        ),
        UnknownItem(
            id = "of_and_5m_ob",
            topic = "Qeexidda Orderflow (OF) & 5m Valid OB",
            isConfirmed = false,
            currentKnowledge = "Qeexitaanka saxda ah ee Orderflow (OF) ee mentor-ka iyo waxa ka dhiga Order Block (OB) mid valid ah waqtiga 5m."
        ),
        UnknownItem(
            id = "daily_bias_candle",
            topic = "Sida loo doorto Daily Bias Candle-ka",
            isConfirmed = false,
            currentKnowledge = "Qaabka saxda ah ee mentor-ku u xusho candle-ka Daily-ga ee bias-ka laga qaato maalin kasta."
        ),
        UnknownItem(
            id = "open_trade_invalidation",
            topic = "Invalidation marka trade furan yahay",
            isConfirmed = false,
            currentKnowledge = "Lama hubo in trade-ka furan isla markiiba gacanta lagu xiro ama loo daayo inuu SL ku dhaco marka 0.5 EQ HTF candle la dhaafo."
        )
    )

    val initialLogs: List<TradeLogEntry> = listOf(
        TradeLogEntry(
            id = "log-1",
            date = "2026-03-12",
            timeNY = "08:15",
            side = TradeSide.BUY,
            entryPrice = 2154.20,
            slPrice = 2144.50,
            tpPrice = 2183.30,
            rMultiple = 3.0,
            result = TradeResult.WIN,
            itemsPassed = listOf("Orderflow", "IRL to ERL", "KL", "CRT", "Purge", "PSP", "TS", "M#1", "SMT", "5m"),
            notes = "Purge 8am NY. TS sweep 1H OB. SMT clear with XAUEUR. Limit order hit at line #1. Target reached at PDH.",
            confidenceScore = 5,
            createdAt = 1773300000000L
        ),
        TradeLogEntry(
            id = "log-2",
            date = "2026-03-18",
            timeNY = "02:30",
            side = TradeSide.SELL,
            entryPrice = 2188.50,
            slPrice = 2198.00,
            tpPrice = 2160.00,
            rMultiple = 3.0,
            result = TradeResult.WIN,
            itemsPassed = listOf("Major LQ", "ERL to IRL", "KL", "CRT", "Purge", "PSP", "TS", "M#1", "SMT", "5m"),
            notes = "London session 2:30am purge. Sweep of Asian high. SMT with Silver. Clean 5m OB return.",
            confidenceScore = 5,
            createdAt = 1773800000000L
        ),
        TradeLogEntry(
            id = "log-3",
            date = "2026-03-24",
            timeNY = "09:10",
            side = TradeSide.BUY,
            entryPrice = 2165.00,
            slPrice = 2155.50,
            tpPrice = 2184.00,
            rMultiple = 2.0,
            result = TradeResult.MISSED,
            itemsPassed = listOf("IRL to ERL", "KL", "CRT", "Purge", "TS", "SMT"),
            notes = "Price missed Entry #1 line by 30 cents, then rallied 400 pips to target. Limit order not filled.",
            confidenceScore = 4,
            createdAt = 1774300000000L
        ),
        TradeLogEntry(
            id = "log-4",
            date = "2026-03-29",
            timeNY = "08:45",
            side = TradeSide.BUY,
            entryPrice = 2172.40,
            slPrice = 2163.00,
            tpPrice = 2200.60,
            rMultiple = 3.0,
            result = TradeResult.LOSS,
            itemsPassed = listOf("IRL to ERL", "KL", "CRT", "Purge", "5m"),
            notes = "Purge occurred but SMT failed to form with XAUEUR. Sweep wick hit SL before reversal.",
            confidenceScore = 2,
            createdAt = 1774700000000L
        ),
        TradeLogEntry(
            id = "log-5",
            date = "2026-04-03",
            timeNY = "08:20",
            side = TradeSide.SELL,
            entryPrice = 2195.00,
            slPrice = 2204.50,
            tpPrice = 2166.50,
            rMultiple = 3.0,
            result = TradeResult.WIN,
            itemsPassed = listOf("Major LQ", "ERL to IRL", "KL", "CRT", "Purge", "PSP", "TS", "M#1", "SMT", "5m"),
            notes = "NY session 8:20am purge. TS sweep 4H Key Level. SMT with Silver. Complete 3R target hit.",
            confidenceScore = 5,
            createdAt = 1775200000000L
        )
    )

    val dailyRecaps: List<DailyRecapItem> = listOf(
        DailyRecapItem(
            id = "recap-1",
            date = "2026-10-29",
            dayOfWeek = "Khamiis (Thursday)",
            dailyBias = "BEARISH",
            dolNarrative = "Sweep of Asian/London High ($4,034.09) -> Target PDL ($4,004.70)",
            dayHigh = 4034.09,
            dayLow = 3975.65,
            pdh = 4120.50,
            pdl = 4015.00,
            direction = "SELL",
            status = "WIN",
            rMultiple = 3.0,
            summarySomali = "NY Prime Purge (08:00-10:00 NY) wuxuu bixiyay 10/11 Model#1 SELL setup oo nadiif ah. Silver wuxuu sameeyay SMT divergence, 5m Entry #1 limit waa la taabtay.",
            keyLessonSomali = "Marka Daily Bias iyo 7H candle ay isku raacaan oo SMT + PSP ay isla saacadda dhacaan, 3R waa la gaaraa."
        ),
        DailyRecapItem(
            id = "recap-2",
            date = "2026-10-28",
            dayOfWeek = "Arbaco (Wednesday)",
            dailyBias = "BULLISH",
            dolNarrative = "London 02:30 NY Purge -> 1H OB Sweep -> PDH Delivery",
            dayHigh = 4118.40,
            dayLow = 4042.10,
            pdh = 4115.00,
            pdl = 4030.00,
            direction = "BUY",
            status = "WIN",
            rMultiple = 3.0,
            summarySomali = "London session 02:30 NY purge ayaa sweep ku sameeyay 1H FVG/OB. XAUEUR wuxuu xaqiijiyay 2SSMT iyo PSP.",
            keyLessonSomali = "Saburka ilaa 5m Entry #1 Line wuxuu yareeyaa SL distance ($8.50) wuxuuna kordhiyaa R-multiple."
        ),
        DailyRecapItem(
            id = "recap-3",
            date = "2026-10-27",
            dayOfWeek = "Talaado (Tuesday)",
            dailyBias = "NEUTRAL",
            dolNarrative = "Consolidation inside Asian Range — No clean TS Sweep",
            dayHigh = 4065.00,
            dayLow = 4028.00,
            pdh = 4090.00,
            pdl = 4010.00,
            direction = "NO_TRADE",
            status = "NO_SETUP",
            rMultiple = 0.0,
            summarySomali = "Ma jirin SMT divergence oo sax ah mana dhicin TS sweep xilliga Purge hours-ka. Go'aanka saxda ah wuxuu ahaa inaan la gelin.",
            keyLessonSomali = "Inaad ka fogaato maalinta aan lahayn 11-ka qodob waa guul weyn oo Model#1 ah."
        )
    )

    fun getNYTimeStatus(): NYTimeStatus {
        val nyNow = ZonedDateTime.now(ZoneId.of("America/New_York"))
        val eatNow = ZonedDateTime.now(ZoneId.of("Africa/Mogadishu"))

        val timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US)
        val dateFmt = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy", Locale.US)

        val hour = nyNow.hour
        val minute = nyNow.minute
        val dow = nyNow.dayOfWeek

        val isWeekendMarketClosed =
            (dow == DayOfWeek.FRIDAY && hour >= 16) ||
                dow == DayOfWeek.SATURDAY ||
                (dow == DayOfWeek.SUNDAY && hour < 17)

        val (sessionName, sessionOpen, sessionDesc) = when {
            isWeekendMarketClosed -> Triple(
                "Suuqu Waa Xiran Yahay (Weekend)",
                "Axad 17:00 NY",
                "Suuqu wuxuu xiran yahay Jimco 16:00 NY ilaa Axad 17:00 NY"
            )
            hour in 0..6 -> Triple(
                "London Session",
                "00:00 NY",
                "7H Candle furmay 00:00 NY"
            )
            hour in 7..16 -> Triple(
                "New York Session",
                "07:00 NY",
                "7H Candle furmay 07:00 NY"
            )
            else -> Triple(
                "Asia Session",
                "17:00 NY",
                "7H Candle furmay 17:00 NY"
            )
        }

        val timeInMinutes = hour * 60 + minute
        var isPurgeWindow = false
        var activePurgeName: String? = null
        val nextPurgeDescription: String

        when {
            isWeekendMarketClosed -> {
                nextPurgeDescription = "Suuqu wuxuu furmayaa Axadda 17:00 NY (Asia Open)"
            }
            timeInMinutes in (8 * 60) until (10 * 60) -> {
                isPurgeWindow = true
                activePurgeName = "NY Prime Purge (8:00am - 10:00am NY)"
                nextPurgeDescription = "Dhamaadka Purge-ka: 10:00am NY"
            }
            timeInMinutes in (6 * 60) until (8 * 60) -> {
                isPurgeWindow = true
                activePurgeName = "Pre-NY Purge (6:00am - 8:00am NY)"
                nextPurgeDescription = "U gudubka NY Purge: 8:00am NY"
            }
            timeInMinutes in (2 * 60) until (3 * 60) -> {
                isPurgeWindow = true
                activePurgeName = "London Purge (2:00am - 3:00am NY)"
                nextPurgeDescription = "Dhamaadka Purge-ka: 3:00am NY"
            }
            timeInMinutes in (20 * 60) until (22 * 60) -> {
                isPurgeWindow = true
                activePurgeName = "Asia/Night Purge (8:00pm - 10:00pm NY)"
                nextPurgeDescription = "Dhamaadka Purge-ka: 10:00pm NY"
            }
            else -> {
                nextPurgeDescription = when {
                    timeInMinutes < 2 * 60 -> "Purge xiga: London Purge (2:00am NY)"
                    timeInMinutes < 6 * 60 -> "Purge xiga: Pre-NY Purge (6:00am NY)"
                    timeInMinutes < 8 * 60 -> "Purge xiga: NY Prime Purge (8:00am NY)"
                    timeInMinutes < 20 * 60 -> "Purge xiga: Night Purge (8:00pm NY)"
                    else -> "Purge xiga: London Purge (2:00am NY)"
                }
            }
        }

        return NYTimeStatus(
            formattedTime = nyNow.format(timeFmt),
            formattedDate = nyNow.format(dateFmt),
            eatFormattedTime = eatNow.format(timeFmt),
            isMarketOpen = !isWeekendMarketClosed,
            isWeekendMarketClosed = isWeekendMarketClosed,
            activeSessionName = sessionName,
            activeSessionOpen = sessionOpen,
            activeSessionDesc = sessionDesc,
            isPurgeWindow = isPurgeWindow,
            activePurgeName = activePurgeName,
            nextPurgeDescription = nextPurgeDescription
        )
    }

    fun evaluateMarketState(scenario: ScannerScenario, timeStatus: NYTimeStatus): LiveScannerState {
        val isPurgeActive = scenario == ScannerScenario.MORNING_SELL_COMPLETED || timeStatus.isPurgeWindow
        val purgeDetails = when {
            scenario == ScannerScenario.MORNING_SELL_COMPLETED ->
                "NY Prime Purge Window (08:00 AM - 10:00 AM NY) — Setup-kii Saaka!"
            timeStatus.isWeekendMarketClosed ->
                "SUUQU WAA XIRAN YAHAY (Weekend): Jimco 16:00 NY ilaa Axad 17:00 NY."
            timeStatus.isPurgeWindow ->
                timeStatus.activePurgeName ?: "Purge Window Active"
            else ->
                "${timeStatus.nextPurgeDescription}. Waqtigan lama joogo Purge!"
        }

        val activeSetup = when {
            scenario == ScannerScenario.MORNING_SELL_COMPLETED -> ActiveSetup(
                direction = "SELL",
                confidenceScore = 10,
                totalChecklist = 11,
                passedItems = listOf(
                    "HTF Bias Alignment (Daily Bearish)",
                    "ERL to IRL Liquidity Flow",
                    "Key Level (4H/1H Major LQ 4034)",
                    "CRT (Asia/London high sweep)",
                    "Purge Hour (8:00 - 10:00 AM NY)",
                    "TS (Turtle Soup) Sweep (4034.09)",
                    "SMT Divergence (Silver failed higher high)",
                    "PSP Confirmed (opposite color closes)",
                    "5m Entry #1 Line (4027.80 limit hit)",
                    "SL Beyond Sweep (4035.50 - $7.70 distance)"
                ),
                missingItems = listOf(
                    "Waxba kama dhiman — Setup-ku wuxuu si buuxda u qaatay 3R Take Profit ($4,004.70)!"
                ),
                currentStage = "TARGET_HIT",
                entryPrice = 4027.80,
                slPrice = 4035.50,
                tpPrice = 4004.70,
                slDistance = 7.70,
                rMultiple = 3.0,
                summarySomali = "Trade-kii SELL ee saaka wuxuu si buuxda u buuxiyay 10/11 qodob ee Model#1. Waxaa la qaatay 3R buuxda ($4,004.70).",
                warnings = listOf("Trade-kan waa la dhammaystiray (Completed). Ha eryan qiimaha hadda!")
            )
            timeStatus.isWeekendMarketClosed -> ActiveSetup(
                direction = "NO_SETUP",
                confidenceScore = 0,
                totalChecklist = 11,
                passedItems = emptyList(),
                missingItems = listOf(
                    "SUUQU WAA XIRAN YAHAY (Jimco 16:00 NY ilaa Axad 17:00 NY)",
                    "Ma jiro wax trade ah oo socon kara inta suuqu xiran yahay",
                    "Sug furitaanka Asia Session Axadda 17:00 NY"
                ),
                currentStage = "MARKET_CLOSED_WEEKEND",
                entryPrice = 0.0,
                slPrice = 0.0,
                tpPrice = 0.0,
                slDistance = 0.0,
                rMultiple = 0.0,
                summarySomali = "SUUQU WAA XIRAN YAHAY (WEEKEND). Isticmaal waqtigan barashada recaps-ka iyo 11-ka qodob ilaa Axadda 17:00 NY.",
                warnings = listOf("Weligaa ha galin orders xilliga weekend-ka.")
            )
            else -> ActiveSetup(
                direction = "BUY",
                confidenceScore = 3,
                totalChecklist = 11,
                passedItems = listOf(
                    "ERL / PDL Liquidity Swept ($3,975.65)",
                    "4H Key Level Reaction Zone ($3,980 - $3,995)",
                    "Potential CRT Candle 2 Forming"
                ),
                missingItems = listOf(
                    "Daily Bias weli waa BEARISH (Counter-trend caution)",
                    "Lama joogo Prime Purge Window",
                    "SMT Divergence (XAUEUR/Silver) weli lama xaqiijin",
                    "5m Entry #1 Line weli lama samayn"
                ),
                currentStage = "DEVELOPING_WATCHLIST",
                entryPrice = 3992.00,
                slPrice = 3982.50,
                tpPrice = 4020.50,
                slDistance = 9.50,
                rMultiple = 3.0,
                summarySomali = "Setup-ku weli wuu dhismayaa (3/11). Sug saacadda Purge-ka iyo xaqiijinta SMT + PSP ka hor inta aadan go'aan qaadan.",
                warnings = listOf("Ha degdegin — 3/11 kuma filna Model#1 entry!")
            )
        }

        return LiveScannerState(
            currentPrice = 3996.01,
            dayHigh = 4034.09,
            dayLow = 3975.65,
            pdl = 4015.00,
            pdh = 4120.50,
            pwh = 4235.00,
            pwl = 3950.00,
            dailyBias = "BEARISH",
            sevenHourSession = timeStatus.activeSessionName,
            isPurgeActive = isPurgeActive,
            purgeDetails = purgeDetails,
            lastUpdated = timeStatus.formattedTime,
            activeSetup = activeSetup
        )
    }

    fun answerModel1Question(question: String): String {
        val q = question.lowercase(Locale.US)
        return when {
            q.contains("kod") ->
                "Ma hubo micnaha rasmiga ah ee KOD. Aqoonta Model#1 waxaa KOD loogu calaamadeeyay UNKNOWN. Haddii aad ka hesho sharaxaadda mentor-ka TET, fadlan ku dar qaybta Knowledge Base -> Unknowns."
            q.contains("smt") || q.contains("lammaan") || q.contains("xaueur") || q.contains("silver") ->
                "Sharciga SMT ee Model#1 (XAUUSD):\n• Lammaanaha kaliya ee la ogol yahay: XAUEUR, XAUGBP, ama Silver.\n• LAMA OGOLA: XAUCHF, XAUNZD, AUD, JPY.\n• Waa inuu ahaadaa 2-Stage SMT (2SSMT) oo ay la socoto PSP confirmation (midabbo candle oo iska soo horjeeda isla saacadda 1H)."
            q.contains("purge") || q.contains("saacad") || q.contains("waqti") ->
                "Saacadaha Purge-ka ee Model#1 (New York Time):\n• NY Prime Purge: 08:00 AM - 10:00 AM NY\n• Pre-NY Purge: 06:00 AM - 08:00 AM NY\n• London Purge: 02:00 AM - 03:00 AM NY\n• Asia / Night Purge: 08:00 PM - 10:00 PM NY\n7H Session Candles waxay furmaan: Asia 17:00 NY, London 00:00 NY, NY 07:00 NY."
            q.contains("entry") || q.contains("#1") || q.contains("limit") ->
                "Entry #1 Line (5m OB):\n• Xariiqda #1 waxaa la dhigaa DHAMAADKA Order Block-ka ee 5m.\n• Waxaa la isticmaalaa Limit Order.\n• Haddii qiimuhu uusan dib ugu soo laaban Entry #1 Line, waa 'Missed Trade' — weligaa ha eryan qiimaha."
            q.contains("risk") || q.contains("3r") || q.contains("2r") || q.contains("sl") || q.contains("tp") ->
                "Sharciga Risk & Targets ee Model#1:\n• Risk: 1% account-ka trade kasta.\n• Stop Loss (SL): Wax yar ka dambeeya TS Sweep-ka (~$9-$10 dahabka). Haddii SL aad u weyn yahay, ka bood setup-ka.\n• Take Profit (TP): 3R by default, ama 2R haddii ERL dhowdahay. Bartilmaameedyo: PDH/PDL, PWH/PWL, PMH/PML, TG."
            q.contains("invalidation") || q.contains("jabo") ->
                "Invalidation-ka Model#1:\n• Setup-ku wuu jabaa haddii qiimuhu dhaafo 0.5 EQ ee HTF candle ama jabo TS sweep level-ka.\n• Fiiro gaar ah: Waxa laga yeelo trade hore u furan marka 0.5 EQ la dhaafo (manual close vs SL) weli waa UNKNOWN — ma hubo go'aanka rasmiga ah ee mentor-ka."
            q.contains("buy") || q.contains("sell") || q.contains("signal") || q.contains("iibso") || q.contains("iibi") ->
                "Xusuusin: Ma bixiyo baaqyo iibsi/iibin ah (no buy/sell signals). Fadlan isticmaal Mode B (11-Point Checklist) si aad adigu u hubiso in setup-ku buuxiyay sharciyada Model#1."
            else ->
                "Waad ku mahadsan tahay su'aashaada! Waxaan ku caawin karaa sharciyada rasmiga ah ee Model#1: HTF Bias (Daily->7H->1H), IRL/ERL, Key Level (KL), CRT, Purge Hours, TS Sweep, SMT (XAUEUR/XAUGBP/Silver), PSP, Entry #1 Line, SL/TP & 1% Risk. Haddii mawduucu ka baxsan yahay aqoontan, sharcigeygu waa 'ma hubo' (DO NOT INVENT)."
        }
    }
}
