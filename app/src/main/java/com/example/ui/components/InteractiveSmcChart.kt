package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*
import kotlin.math.max
import kotlin.math.min

@Composable
fun InteractiveSmcChart(
    chartData: SmcChartData?,
    selectedPair: String,
    selectedTimeframe: String,
    isTradingViewMode: Boolean,
    onSelectPair: (String) -> Unit,
    onSelectTimeframe: (String) -> Unit,
    onToggleChartMode: (Boolean) -> Unit
) {
    val pairs = listOf("EURUSD", "GBPUSD", "XAUUSD", "GBPJPY")
    val timeframes = listOf("M15", "H1", "H4")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SuparbaseDarkCard)
            .border(1.dp, SuparbaseCardBorderSubtle, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        // Pair & Timeframe selector controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pair tabs
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                pairs.forEach { pair ->
                    val isSelected = pair == selectedPair
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) SuparbaseNeonGreen.copy(alpha = 0.2f) else Color.Transparent)
                            .border(
                                1.dp,
                                if (isSelected) SuparbaseNeonGreen else Color(0x3348576A),
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { onSelectPair(pair) }
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                            .testTag("pair_tab_$pair"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = pair,
                            color = if (isSelected) SuparbaseNeonGreen else TextMuted,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            // Chart Mode switch (SMC vs TradingView)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0x3300E5FF))
                    .border(1.dp, SuparbaseCyan, RoundedCornerShape(6.dp))
                    .clickable { onToggleChartMode(!isTradingViewMode) }
                    .padding(horizontal = 8.dp, vertical = 5.dp)
                    .testTag("chart_mode_toggle")
            ) {
                Text(
                    text = if (isTradingViewMode) "SMC SMART CHART" else "TRADINGVIEW LIVE",
                    color = SuparbaseCyan,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Timeframe tabs & trend confluence info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                timeframes.forEach { tf ->
                    val isSelected = tf == selectedTimeframe
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSelected) SuparbaseGold.copy(alpha = 0.2f) else Color(0x11FFFFFF))
                            .border(
                                0.5.dp,
                                if (isSelected) SuparbaseGold else Color.Transparent,
                                RoundedCornerShape(4.dp)
                            )
                            .clickable { onSelectTimeframe(tf) }
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tf,
                            color = if (isSelected) SuparbaseGold else TextMuted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Higher Timeframe confluence indicators
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "H1: ", color = TextMuted, fontSize = 9.sp)
                Text(
                    text = chartData?.trendH1 ?: "BULLISH",
                    color = SuparbaseNeonGreen,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "H4: ", color = TextMuted, fontSize = 9.sp)
                Text(
                    text = chartData?.trendH4 ?: "DISCOUNT",
                    color = SuparbaseGold,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Active SMC Setup Banner
        chartData?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF071217))
                    .border(0.5.dp, Color(0x3300FF88), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(SuparbaseNeonGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = it.activeSetupText,
                        color = TextWhite,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (isTradingViewMode) {
            TradingViewWebWidget(pair = selectedPair)
        } else {
            // Native High-Precision SMC Candlestick Canvas
            SmcCanvasCandles(chartData = chartData)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // SMC Overlay Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(color = SuparbaseNeonGreen, label = "Bullish OB (Demand)")
            LegendItem(color = SuparbaseCyan, label = "FVG (50% CE)")
            LegendItem(color = SuparbaseGold, label = "Liquidity Sweep ($)")
            LegendItem(color = TextWhite, label = "MSS / BOS")
        }
    }
}

@Composable
fun SmcCanvasCandles(chartData: SmcChartData?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF05080C))
            .border(1.dp, Color(0x22384759), RoundedCornerShape(8.dp))
    ) {
        if (chartData == null || chartData.candles.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Scanning SMC Liquidity Pools...", color = TextMuted, fontSize = 12.sp)
            }
            return@Box
        }

        Canvas(modifier = Modifier.fillMaxSize().padding(horizontal = 6.dp, vertical = 10.dp)) {
            val width = size.width
            val height = size.height

            val candles = chartData.candles
            val count = candles.size
            if (count < 2) return@Canvas

            var minPrice = candles.minOf { it.low }
            var maxPrice = candles.maxOf { it.high }
            val priceSpan = max(0.00001, maxPrice - minPrice)
            // 8% margin top and bottom
            val margin = priceSpan * 0.08
            minPrice -= margin
            maxPrice += margin
            val totalSpan = maxPrice - minPrice

            fun priceToY(price: Double): Float {
                val ratio = (maxPrice - price) / totalSpan
                return (ratio * height).toFloat()
            }

            val stepX = width / count.toFloat()
            val candleWidth = max(2f, stepX * 0.65f)

            // Draw horizontal price gridlines
            val gridCount = 4
            val gridDottedEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 6f), 0f)
            for (g in 0..gridCount) {
                val p = minPrice + (totalSpan * (g.toDouble() / gridCount))
                val y = priceToY(p)
                drawLine(
                    color = Color(0x18FFFFFF),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f,
                    pathEffect = gridDottedEffect
                )
            }

            // 1. Draw Fair Value Gaps (FVG)
            for (fvg in chartData.fairValueGaps) {
                val startX = (fvg.startIndex * stepX)
                val endX = width
                val topY = priceToY(fvg.top)
                val botY = priceToY(fvg.bottom)
                val ceY = priceToY(fvg.equilibrium50)

                val fvgHeight = max(2f, botY - topY)
                drawRect(
                    color = Color(0x3300E5FF),
                    topLeft = Offset(startX, topY),
                    size = Size(endX - startX, fvgHeight)
                )
                // 50% Consequent Encroachment dotted line
                drawLine(
                    color = SuparbaseCyan,
                    start = Offset(startX, ceY),
                    end = Offset(endX, ceY),
                    strokeWidth = 1.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f), 0f)
                )
            }

            // 2. Draw Order Blocks (OB)
            for (ob in chartData.orderBlocks) {
                val startX = (ob.startIndex * stepX)
                val endX = width
                val topY = priceToY(ob.top)
                val botY = priceToY(ob.bottom)
                val obHeight = max(3f, botY - topY)

                val obColor = if (ob.direction == SmcDirection.BULLISH) OrderBlockBullish else OrderBlockBearish
                val borderColor = if (ob.direction == SmcDirection.BULLISH) SuparbaseNeonGreen else SuparbaseRed

                drawRect(
                    color = obColor,
                    topLeft = Offset(startX, topY),
                    size = Size(endX - startX, obHeight)
                )
                // Outline
                drawLine(
                    color = borderColor.copy(alpha = 0.8f),
                    start = Offset(startX, topY),
                    end = Offset(endX, topY),
                    strokeWidth = 1.5f
                )
                drawLine(
                    color = borderColor.copy(alpha = 0.8f),
                    start = Offset(startX, botY),
                    end = Offset(endX, botY),
                    strokeWidth = 1.5f
                )
            }

            // 3. Draw Candlesticks
            for (i in 0 until count) {
                val c = candles[i]
                val centerX = (i * stepX) + (stepX / 2f)
                val openY = priceToY(c.open)
                val closeY = priceToY(c.close)
                val highY = priceToY(c.high)
                val lowY = priceToY(c.low)

                val isBullish = c.isBullish
                val color = if (isBullish) CandleBullish else CandleBearish

                // Wick
                drawLine(
                    color = color,
                    start = Offset(centerX, highY),
                    end = Offset(centerX, lowY),
                    strokeWidth = 1.5f
                )

                // Body
                val bodyTop = min(openY, closeY)
                val bodyBottom = max(openY, closeY)
                val bodyHeight = max(2f, bodyBottom - bodyTop)

                drawRect(
                    color = color,
                    topLeft = Offset(centerX - candleWidth / 2f, bodyTop),
                    size = Size(candleWidth, bodyHeight)
                )
            }

            // 4. Draw Structure Breaks (BOS / MSS)
            for (sb in chartData.structureBreaks) {
                if (sb.index < count) {
                    val x = (sb.index * stepX) + (stepX / 2f)
                    val y = priceToY(sb.price)
                    val lineLen = stepX * 4f
                    drawLine(
                        color = if (sb.type.contains("MSS")) SuparbaseGold else TextWhite,
                        start = Offset(x - lineLen, y),
                        end = Offset(min(width, x + lineLen), y),
                        strokeWidth = 2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 4f), 0f)
                    )
                }
            }

            // 5. Draw Liquidity Sweeps
            for (sw in chartData.liquiditySweeps) {
                if (sw.index < count) {
                    val x = (sw.index * stepX) + (stepX / 2f)
                    val y = priceToY(sw.price)
                    drawCircle(
                        color = SuparbaseGold,
                        radius = 4f,
                        center = Offset(x, y + 10f)
                    )
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, color = TextMuted, fontSize = 8.5.sp)
    }
}
