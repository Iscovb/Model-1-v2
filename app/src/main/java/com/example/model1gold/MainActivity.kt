package com.example.model1gold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model1gold.ui.screens.ChecklistScreen
import com.example.model1gold.ui.screens.CoachScreen
import com.example.model1gold.ui.screens.JournalScreen
import com.example.model1gold.ui.screens.ScannerScreen
import com.example.model1gold.ui.screens.ToolsScreen
import com.example.model1gold.ui.theme.GoldPrimary
import com.example.model1gold.ui.theme.Model1GoldTheme
import com.example.model1gold.ui.theme.ObsidianBg
import com.example.model1gold.ui.theme.SlateSurface
import com.example.model1gold.ui.theme.TextSecondary
import com.example.model1gold.viewmodel.Model1ViewModel

private enum class AppTab(val label: String, val icon: ImageVector) {
    SCANNER("Scanner", Icons.Default.Radar),
    CHECKLIST("11-Point", Icons.Default.Checklist),
    JOURNAL("Journal", Icons.Default.Book),
    TOOLS("Tools", Icons.Default.Build),
    COACH("Coach", Icons.Default.Forum)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Model1GoldTheme {
                Model1GoldApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Model1GoldApp(viewModel: Model1ViewModel = viewModel()) {
    var activeTab by remember { mutableStateOf(AppTab.SCANNER) }

    val nyTime by viewModel.nyTime.collectAsStateWithLifecycle()
    val scenario by viewModel.scenario.collectAsStateWithLifecycle()
    val scannerState by viewModel.scannerState.collectAsStateWithLifecycle()
    val checklistSide by viewModel.checklistSide.collectAsStateWithLifecycle()
    val checklistScores by viewModel.checklistScores.collectAsStateWithLifecycle()
    val logs by viewModel.logs.collectAsStateWithLifecycle()
    val unknowns by viewModel.unknowns.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = ObsidianBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SlateSurface,
                    titleContentColor = Color.White
                ),
                title = {
                    Column {
                        Text(
                            text = "Model#1 Gold Study Agent",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            color = GoldPrimary
                        )
                        Text(
                            text = "XAUUSD ICT TET Mentorship 2026",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = SlateSurface) {
                AppTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = activeTab == tab,
                        onClick = { activeTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.testTag("nav_${tab.name.lowercase()}"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                AppTab.SCANNER -> ScannerScreen(
                    nyTime = nyTime,
                    scannerState = scannerState,
                    currentScenario = scenario,
                    onScenarioChange = viewModel::setScenario
                )
                AppTab.CHECKLIST -> ChecklistScreen(
                    side = checklistSide,
                    scores = checklistScores,
                    onSideChange = viewModel::setChecklistSide,
                    onScoreToggle = viewModel::setChecklistItemStatus,
                    onReset = viewModel::resetChecklist,
                    onSendToJournal = { activeTab = AppTab.JOURNAL }
                )
                AppTab.JOURNAL -> JournalScreen(
                    logs = logs,
                    onAddLog = viewModel::addTradeLog,
                    onDeleteLog = viewModel::deleteTradeLog
                )
                AppTab.TOOLS -> ToolsScreen(unknowns = unknowns)
                AppTab.COACH -> CoachScreen(
                    messages = chatMessages,
                    onSendMessage = viewModel::sendChatMessage
                )
            }
        }
    }
}
