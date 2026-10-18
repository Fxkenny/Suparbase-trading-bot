package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SuparbaseBlack
import com.example.ui.viewmodel.TradingBotViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TradingBotViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SuparbaseTradingBotApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SuparbaseTradingBotApp(viewModel: TradingBotViewModel) {
    val context = LocalContext.current

    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val stats by viewModel.accountStats.collectAsStateWithLifecycle()
    val selectedPair by viewModel.selectedPair.collectAsStateWithLifecycle()
    val selectedTf by viewModel.selectedTimeframe.collectAsStateWithLifecycle()
    val isTradingView by viewModel.isLiveTradingView.collectAsStateWithLifecycle()
    val chartData by viewModel.chartData.collectAsStateWithLifecycle()
    val activeTrades by viewModel.activeTrades.collectAsStateWithLifecycle()
    val closedTrades by viewModel.closedTrades.collectAsStateWithLifecycle()
    val signals by viewModel.signals.collectAsStateWithLifecycle()
    val backtestResult by viewModel.backtestResult.collectAsStateWithLifecycle()
    val isBacktestingLoading by viewModel.isBacktestingRunning.collectAsStateWithLifecycle()
    val bannerNotice by viewModel.bannerNotice.collectAsStateWithLifecycle()

    var showBacktestDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(bannerNotice) {
        bannerNotice?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearBanner()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(SuparbaseBlack),
        topBar = {
            SuparbaseTopBar(
                settings = settings,
                stats = stats,
                onToggleBot = { viewModel.toggleBot() },
                onToggleSimulation = { viewModel.toggleSimulationMode(it) },
                onOpenSettings = { showSettingsDialog = true },
                onOpenBacktest = { showBacktestDialog = true }
            )
        },
        containerColor = SuparbaseBlack,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. Chart Section (SMC Smart Candlestick Canvas or Live TradingView)
            item {
                InteractiveSmcChart(
                    chartData = chartData,
                    selectedPair = selectedPair,
                    selectedTimeframe = selectedTf,
                    isTradingViewMode = isTradingView,
                    onSelectPair = { viewModel.selectPair(it) },
                    onSelectTimeframe = { viewModel.selectTimeframe(it) },
                    onToggleChartMode = { viewModel.toggleLiveTradingView(it) }
                )
            }

            // 2. Bot Controls (Risk slider, Pairs, Sessions, News filter, Start/Stop, Emergency Close)
            item {
                BotControlsSection(
                    settings = settings,
                    balance = stats.balance,
                    onToggleBot = { viewModel.toggleBot() },
                    onRiskChanged = { viewModel.setRiskPercent(it) },
                    onTogglePair = { viewModel.togglePair(it) },
                    onToggleSession = { l, ny, a -> viewModel.toggleSession(l, ny, a) },
                    onToggleNewsFilter = { viewModel.toggleNewsFilter(it) },
                    onEmergencyCloseAll = { viewModel.emergencyCloseAll() }
                )
            }

            // 3. Active Trades Table / Cards
            item {
                ActiveTradesSection(
                    activeTrades = activeTrades,
                    onCloseTrade = { viewModel.closeTrade(it) }
                )
            }

            // 4. Closed Trade History Table & CSV Export
            item {
                TradeHistorySection(
                    closedTrades = closedTrades,
                    onExportCsv = { viewModel.exportTradesToCsv(context) }
                )
            }

            // 5. Real-Time SMC Signal Feed
            item {
                SignalFeedSection(signals = signals)
            }
        }
    }

    // Backtest Modal
    if (showBacktestDialog) {
        BacktestingDialog(
            initialPair = selectedPair,
            backtestResult = backtestResult,
            isLoading = isBacktestingLoading,
            onRunBacktest = { pair, period ->
                viewModel.runBacktest(pair, period)
            },
            onDismiss = { showBacktestDialog = false }
        )
    }

    // Settings, Broker API, Telegram & Cloud Run 24/7 Dialog
    if (showSettingsDialog) {
        SettingsAndCloudRunDialog(
            settings = settings,
            onSaveBrokerSettings = { type, mTok, mAcc, dTok, dApp ->
                viewModel.updateBrokerSettings(type, mTok, mAcc, dTok, dApp)
            },
            onSaveTelegramSettings = { token, chatId, enabled ->
                viewModel.updateTelegramSettings(token, chatId, enabled)
            },
            onSendTestTelegram = { viewModel.sendTestTelegramAlert(context) },
            onToggleSimulation = { viewModel.toggleSimulationMode(it) },
            onDismiss = { showSettingsDialog = false }
        )
    }
}
