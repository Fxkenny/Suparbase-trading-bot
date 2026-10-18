package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.SignalBadgeType
import com.example.data.model.SignalLog
import com.example.ui.theme.*

@Composable
fun SignalFeedSection(signals: List<SignalLog>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SuparbaseDarkCard)
            .border(1.dp, SuparbaseCardBorderSubtle, RoundedCornerShape(12.dp))
            .padding(14.dp)
            .testTag("signal_feed_section")
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
                        .background(SuparbaseNeonGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "REAL-TIME SMC SIGNAL FEED",
                    color = TextWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
            Text(
                text = "ALGORITHM ACTIVE",
                color = SuparbaseNeonGreen,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (signals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Awaiting liquidity grab & MSS displacement...", color = TextMuted, fontSize = 11.sp)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                signals.take(8).forEach { sig ->
                    SignalFeedRow(sig = sig)
                }
            }
        }
    }
}

@Composable
fun SignalFeedRow(sig: SignalLog) {
    val (badgeBg, badgeBorder, badgeText, badgeLabel) = when (sig.badgeType) {
        SignalBadgeType.SWEEP -> Quad(SuparbaseGold.copy(alpha = 0.15f), SuparbaseGold, SuparbaseGold, "STEP 1: SWEEP")
        SignalBadgeType.MSS -> Quad(SuparbaseCyan.copy(alpha = 0.15f), SuparbaseCyan, SuparbaseCyan, "STEP 2: MSS")
        SignalBadgeType.ORDER_BLOCK -> Quad(SuparbaseNeonGreen.copy(alpha = 0.15f), SuparbaseNeonGreen, SuparbaseNeonGreen, "STEP 3: OB/FVG")
        SignalBadgeType.ENTRY_BUY -> Quad(SuparbaseNeonGreen.copy(alpha = 0.2f), SuparbaseNeonGreen, SuparbaseNeonGreen, "STEP 4: BUY")
        SignalBadgeType.ENTRY_SELL -> Quad(SuparbaseRed.copy(alpha = 0.2f), SuparbaseRed, SuparbaseRed, "STEP 4: SELL")
        SignalBadgeType.TAKE_PROFIT -> Quad(SuparbaseNeonGreen.copy(alpha = 0.25f), SuparbaseNeonGreen, SuparbaseNeonGreen, "TP SECURED")
        SignalBadgeType.STOP_LOSS -> Quad(SuparbaseRed.copy(alpha = 0.25f), SuparbaseRed, SuparbaseRed, "SL EXIT")
        SignalBadgeType.NEWS_ALERT -> Quad(Color(0x33FF9100), Color(0xFFFF9100), Color(0xFFFF9100), "NEWS PAUSE")
        SignalBadgeType.INFO -> Quad(Color(0x1AFFFFFF), Color(0x33FFFFFF), TextWhite, "SYSTEM")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF070B10))
            .border(0.5.dp, Color(0x1AFFFFFF), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 7.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(badgeBg)
                            .border(0.5.dp, badgeBorder, RoundedCornerShape(3.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = badgeLabel,
                            color = badgeText,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${sig.pair} • ${sig.timeframe}",
                        color = TextWhite,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = sig.timestamp,
                    color = TextMuted,
                    fontSize = 8.5.sp
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = sig.message,
                color = TextWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 13.sp
            )
        }
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
