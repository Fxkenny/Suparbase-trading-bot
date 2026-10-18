package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BacktestResult
import com.example.ui.theme.*
import java.util.Locale
import kotlin.math.max

@Composable
fun BacktestingDialog(
    initialPair: String,
    backtestResult: BacktestResult?,
    isLoading: Boolean,
    onRunBacktest: (pair: String, period: String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedPair by remember { mutableStateOf(initialPair) }
    var selectedPeriod by remember { mutableStateOf("3 Months") }

    val pairs = listOf("EURUSD", "GBPUSD", "XAUUSD", "GBPJPY")
    val periods = listOf("1 Month", "3 Months", "6 Months", "1 Year")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(16.dp))
                .background(SuparbaseDarkCard)
                .border(1.dp, SuparbaseCardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Dialog Title Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp, 16.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(SuparbaseGold)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SMC STRATEGY BACKTESTING",
                                color = TextWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(
                            text = "Historical Order Block & FVG Liquidity Simulation",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Select Pair
                Text(text = "SELECT ASSET", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    pairs.forEach { pair ->
                        val isSelected = pair == selectedPair
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) SuparbaseNeonGreen.copy(alpha = 0.2f) else Color(0x11FFFFFF))
                                .border(1.dp, if (isSelected) SuparbaseNeonGreen else Color(0x22FFFFFF), RoundedCornerShape(6.dp))
                                .clickable { selectedPair = pair }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = pair,
                                color = if (isSelected) SuparbaseNeonGreen else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Select Period
                Text(text = "TESTING TIMEFRAME HORIZON", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    periods.forEach { period ->
                        val isSelected = period == selectedPeriod
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) SuparbaseGold.copy(alpha = 0.2f) else Color(0x11FFFFFF))
                                .border(1.dp, if (isSelected) SuparbaseGold else Color(0x22FFFFFF), RoundedCornerShape(6.dp))
                                .clickable { selectedPeriod = period }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = period,
                                color = if (isSelected) SuparbaseGold else TextMuted,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { onRunBacktest(selectedPair, selectedPeriod) },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("run_backtest_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SuparbaseNeonGreen,
                        contentColor = SuparbaseBlack
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = SuparbaseBlack, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "SIMULATING SMC TRADES...", fontWeight = FontWeight.Black, fontSize = 12.sp)
                    } else {
                        Text(text = "EXECUTE BACKTEST", fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 0.5.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Results view
                if (backtestResult != null) {
                    Text(
                        text = "PERFORMANCE METRICS (${backtestResult.pair} • ${backtestResult.period})",
                        color = TextWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        MetricCard(modifier = Modifier.weight(1f), label = "WIN RATE", value = "${backtestResult.winRate}%", color = SuparbaseGold)
                        MetricCard(modifier = Modifier.weight(1f), label = "PROFIT FACTOR", value = "${backtestResult.profitFactor}", color = SuparbaseNeonGreen)
                        MetricCard(modifier = Modifier.weight(1f), label = "MAX DRAWDOWN", value = "${backtestResult.maxDrawdown}%", color = SuparbaseRed)
                        MetricCard(modifier = Modifier.weight(1f), label = "NET PROFIT", value = "+$${String.format(Locale.US, "%,.0f", backtestResult.netProfit)}", color = SuparbaseNeonGreen)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "EQUITY GROWTH CURVE (ACCOUNT BAL)", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    // Equity Curve Canvas
                    EquityCurveChart(points = backtestResult.equityCurve)
                }
            }
        }
    }
}

@Composable
fun MetricCard(modifier: Modifier = Modifier, label: String, value: String, color: Color) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF070B10))
            .border(0.5.dp, Color(0x22384759), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column {
            Text(text = label, color = TextMuted, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = color, fontSize = 11.sp, fontWeight = FontWeight.Black, maxLines = 1)
        }
    }
}

@Composable
fun EquityCurveChart(points: List<Double>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF05080C))
            .border(1.dp, Color(0x22384759), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        if (points.size < 2) return@Box

        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val minVal = points.minOrNull() ?: 100000.0
            val maxVal = points.maxOrNull() ?: 120000.0
            val span = max(1.0, maxVal - minVal)

            fun pointToOffset(index: Int, value: Double): Offset {
                val x = (index.toFloat() / (points.size - 1)) * width
                val y = height - ((value - minVal) / span * height).toFloat()
                return Offset(x, y)
            }

            val path = Path()
            val fillPath = Path()

            val firstOffset = pointToOffset(0, points[0])
            path.moveTo(firstOffset.x, firstOffset.y)
            fillPath.moveTo(firstOffset.x, height)
            fillPath.lineTo(firstOffset.x, firstOffset.y)

            for (i in 1 until points.size) {
                val off = pointToOffset(i, points[i])
                path.lineTo(off.x, off.y)
                fillPath.lineTo(off.x, off.y)
            }

            val lastOffset = pointToOffset(points.size - 1, points.last())
            fillPath.lineTo(lastOffset.x, height)
            fillPath.close()

            // Fill gradient
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(SuparbaseNeonGreen.copy(alpha = 0.35f), Color.Transparent)
                )
            )

            // Line
            drawPath(
                path = path,
                color = SuparbaseNeonGreen,
                style = Stroke(width = 2.5f)
            )
        }
    }
}
