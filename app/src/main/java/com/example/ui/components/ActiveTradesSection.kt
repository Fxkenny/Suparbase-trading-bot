package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Trade
import com.example.data.model.TradeType
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun ActiveTradesSection(
    activeTrades: List<Trade>,
    onCloseTrade: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SuparbaseDarkCard)
            .border(1.dp, SuparbaseCardBorderSubtle, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 14.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(SuparbaseCyan)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ACTIVE TRADES (${activeTrades.size})",
                    color = TextWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
            Text(
                text = "LIVE FLOATING P/L",
                color = TextMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (activeTrades.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No open positions. SMC Engine waiting for high-probability OB / FVG mitigation.",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                activeTrades.forEach { trade ->
                    ActiveTradeCard(trade = trade, onClose = { onCloseTrade(trade.id) })
                }
            }
        }
    }
}

@Composable
fun ActiveTradeCard(
    trade: Trade,
    onClose: () -> Unit
) {
    val isBuy = trade.type == TradeType.BUY
    val typeColor = if (isBuy) SuparbaseNeonGreen else SuparbaseRed
    val pnlColor = if (trade.pnl >= 0) SuparbaseNeonGreen else SuparbaseRed

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF070B10))
            .border(1.dp, Color(0x22384759), RoundedCornerShape(8.dp))
            .padding(10.dp)
            .testTag("active_trade_${trade.id}")
    ) {
        Column {
            // Header Row: Pair, Type, Lot, and Live PnL + Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(typeColor.copy(alpha = 0.15f))
                            .border(0.5.dp, typeColor, RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = trade.type.name,
                            color = typeColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = trade.pair,
                        color = TextWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${trade.lotSize} Lots",
                        color = TextMuted,
                        fontSize = 10.5.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${if (trade.pnl >= 0) "+$" else "-$"}${String.format(Locale.US, "%,.2f", kotlin.math.abs(trade.pnl))}",
                            color = pnlColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "${if (trade.pnlPercent >= 0) "+" else ""}${trade.pnlPercent}%",
                            color = pnlColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x22FF3B56))
                            .testTag("close_trade_${trade.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Position",
                            tint = SuparbaseRed,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Details Grid: Entry, Current, SL, TP1, TP2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TradeParamColumn(label = "ENTRY", value = String.format(Locale.US, "%.5f", trade.entryPrice))
                TradeParamColumn(label = "CURRENT", value = String.format(Locale.US, "%.5f", trade.currentPrice))
                TradeParamColumn(label = "STOP LOSS", value = String.format(Locale.US, "%.5f", trade.stopLoss), valueColor = SuparbaseRed)
                TradeParamColumn(label = "TP1 (1:2)", value = String.format(Locale.US, "%.5f", trade.takeProfit1), valueColor = SuparbaseNeonGreen)
                TradeParamColumn(label = "TP2 (1:4)", value = String.format(Locale.US, "%.5f", trade.takeProfit2), valueColor = SuparbaseGold)
            }
        }
    }
}

@Composable
fun TradeParamColumn(
    label: String,
    value: String,
    valueColor: Color = TextWhite
) {
    Column {
        Text(text = label, color = TextMuted, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = valueColor, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold)
    }
}
