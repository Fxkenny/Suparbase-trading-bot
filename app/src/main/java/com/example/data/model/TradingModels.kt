package com.example.data.model

enum class TradeType {
    BUY, SELL
}

enum class TradeStatus {
    OPEN, CLOSED_TP1, CLOSED_TP2, CLOSED_SL, MANUAL_CLOSED
}

data class Trade(
    val id: String,
    val pair: String,
    val type: TradeType,
    val entryPrice: Double,
    val currentPrice: Double,
    val stopLoss: Double,
    val takeProfit1: Double,
    val takeProfit2: Double,
    val lotSize: Double,
    val pnl: Double,
    val pnlPercent: Double,
    val status: TradeStatus,
    val openTime: String,
    val closeTime: String? = null,
    val exitPrice: Double? = null,
    val riskReward: String = "1:2 / 1:4",
    val closeReason: String? = null
)

data class Candle(
    val timestamp: Long,
    val timeLabel: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long = 1000L
) {
    val isBullish: Boolean get() = close >= open
}

enum class SmcDirection {
    BULLISH, BEARISH
}

data class OrderBlock(
    val id: String,
    val direction: SmcDirection,
    val top: Double,
    val bottom: Double,
    val startIndex: Int,
    val endIndex: Int,
    val isMitigated: Boolean = false
)

data class FairValueGap(
    val id: String,
    val direction: SmcDirection,
    val top: Double,
    val bottom: Double,
    val equilibrium50: Double,
    val startIndex: Int,
    val endIndex: Int,
    val isFilled: Boolean = false
)

data class LiquiditySweep(
    val id: String,
    val type: String, // "BSL" (Buy-side) or "SSL" (Sell-side)
    val price: Double,
    val index: Int,
    val description: String
)

data class StructureBreak(
    val id: String,
    val type: String, // "BOS" or "MSS/CHoCH"
    val direction: SmcDirection,
    val price: Double,
    val index: Int
)

data class SmcChartData(
    val pair: String,
    val timeframe: String,
    val currentPrice: Double,
    val candles: List<Candle>,
    val orderBlocks: List<OrderBlock>,
    val fairValueGaps: List<FairValueGap>,
    val liquiditySweeps: List<LiquiditySweep>,
    val structureBreaks: List<StructureBreak>,
    val trendH1: String,
    val trendH4: String,
    val activeSetupText: String
)

enum class SignalBadgeType {
    SWEEP, MSS, ORDER_BLOCK, ENTRY_BUY, ENTRY_SELL, TAKE_PROFIT, STOP_LOSS, NEWS_ALERT, INFO
}

data class SignalLog(
    val id: String,
    val timestamp: String,
    val pair: String,
    val timeframe: String,
    val message: String,
    val step: Int, // 1 to 5 SMC Steps
    val badgeType: SignalBadgeType
)

enum class BrokerType {
    META_API, DERIV
}

data class BotSettings(
    val isRunning: Boolean = true,
    val isSimulationMode: Boolean = true,
    val riskPercent: Float = 1.0f,
    val enabledPairs: Set<String> = setOf("EURUSD", "GBPUSD", "XAUUSD", "GBPJPY"),
    val sessionLondon: Boolean = true,
    val sessionNewYork: Boolean = true,
    val sessionAsia: Boolean = false,
    val pauseOnHighImpactNews: Boolean = true,
    val brokerType: BrokerType = BrokerType.META_API,
    val metaApiToken: String = "",
    val metaApiAccountId: String = "",
    val derivToken: String = "",
    val derivAppId: String = "1089",
    val telegramBotToken: String = "",
    val telegramChatId: String = "",
    val telegramAlertsEnabled: Boolean = false,
    val maxTradesPerPairPerDay: Int = 2,
    val maxSimultaneousPairs: Int = 3
)

data class AccountStats(
    val balance: Double = 100000.00,
    val equity: Double = 101850.40,
    val dailyPnl: Double = 1850.40,
    val dailyPnlPercent: Double = 1.85,
    val winRate: Double = 73.5,
    val totalTrades: Int = 38,
    val wins: Int = 28,
    val losses: Int = 10,
    val profitFactor: Double = 2.45
)

data class BacktestResult(
    val pair: String,
    val period: String,
    val totalTrades: Int,
    val winRate: Double,
    val profitFactor: Double,
    val maxDrawdown: Double,
    val netProfit: Double,
    val equityCurve: List<Double>,
    val executedTrades: List<Trade>
)
