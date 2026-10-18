package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
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
import com.example.data.model.TradeStatus
import com.example.data.model.TradeType
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun TradeHistorySection(
    closedTrades: List<Trade>,
    onExportCsv: () -> Unit
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
                        .background(SuparbaseGold)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "CLOSED TRADE HISTORY (${closedTrades.size})",
                    color = TextWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }

            // Export CSV button
            Button(
                onClick = onExportCsv,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SuparbaseGold.copy(alpha = 0.15f),
                    contentColor = SuparbaseGold
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, SuparbaseGold),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier
                    .height(30.dp)
                    .testTag("export_csv_button")
            ) {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = "Export CSV",
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "EXPORT CSV",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (closedTrades.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No closed trades recorded yet.", color = TextMuted, fontSize = 11.sp)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                closedTrades.take(8).forEach { trade ->
                    ClosedTradeRow(trade = trade)
                }
            }
        }
    }
}

@Composable
fun ClosedTradeRow(trade: Trade) {
    val isWin = trade.pnl >= 0
    val pnlColor = if (isWin) SuparbaseNeonGreen else SuparbaseRed
    val typeColor = if (trade.type == TradeType.BUY) SuparbaseNeonGreen else SuparbaseRed

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF070B10))
            .border(0.5.dp, Color(0x1AFFFFFF), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 7.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = trade.pair,
                        color = TextWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = trade.type.name,
                        color = typeColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "@ ${String.format(Locale.US, "%.5f", trade.entryPrice)}",
                        color = TextMuted,
                        fontSize = 9.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${trade.closeReason ?: "Target Hit"} • ${trade.closeTime ?: trade.openTime}",
                    color = TextMuted,
                    fontSize = 8.sp,
                    maxLines = 1
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (isWin) "+$" else "-$"}${String.format(Locale.US, "%,.2f", kotlin.math.abs(trade.pnl))}",
                    color = pnlColor,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "${trade.lotSize} Lots (${if (isWin) "WIN" else "LOSS"})",
                    color = TextMuted,
                    fontSize = 8.sp
                )
            }
        }
    }
}
