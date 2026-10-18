package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AccountStats
import com.example.data.model.BotSettings
import com.example.ui.theme.*

@Composable
fun SuparbaseTopBar(
    settings: BotSettings,
    stats: AccountStats,
    onToggleBot: () -> Unit,
    onToggleSimulation: (Boolean) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenBacktest: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alphaPulse"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SuparbaseBlack)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Top Branding Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(
                                if (settings.isRunning) SuparbaseNeonGreen.copy(alpha = pulseAlpha)
                                else SuparbaseRed
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SUPARBASE",
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "TRADING BOT",
                        color = SuparbaseNeonGreen,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SuparbaseGold.copy(alpha = 0.15f))
                            .border(0.5.dp, SuparbaseGold.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "PREMIUM 24/7 AI-POWERED",
                            color = SuparbaseGold,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (settings.isSimulationMode) "SIMULATION (DEMO)" else "LIVE BROKER",
                        color = if (settings.isSimulationMode) SuparbaseCyan else SuparbaseNeonGreen,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Action Buttons (Backtest, Cloud/Settings, Bot Toggle)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onOpenBacktest,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SuparbaseDarkCard)
                        .border(1.dp, SuparbaseCardBorderSubtle, RoundedCornerShape(8.dp))
                        .testTag("open_backtest_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Timeline,
                        contentDescription = "Strategy Backtesting",
                        tint = SuparbaseGold,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onOpenSettings,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SuparbaseDarkCard)
                        .border(1.dp, SuparbaseCardBorderSubtle, RoundedCornerShape(8.dp))
                        .testTag("open_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Settings & Cloud Run",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Bot On/Off Chip Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (settings.isRunning) SuparbaseNeonGreen.copy(alpha = 0.18f)
                            else SuparbaseRed.copy(alpha = 0.18f)
                        )
                        .border(
                            1.dp,
                            if (settings.isRunning) SuparbaseNeonGreen else SuparbaseRed,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { onToggleBot() }
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .testTag("bot_toggle_chip"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(if (settings.isRunning) SuparbaseNeonGreen else SuparbaseRed)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (settings.isRunning) "RUNNING" else "STOPPED",
                            color = if (settings.isRunning) SuparbaseNeonGreen else SuparbaseRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Account Stats HUD Cards (Prop-Firm Dashboard Layout)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AccountStatCard(
                modifier = Modifier.weight(1f),
                title = "BALANCE",
                value = "$${String.format(java.util.Locale.US, "%,.2f", stats.balance)}",
                subtitle = "Equity: $${String.format(java.util.Locale.US, "%,.2f", stats.equity)}",
                valueColor = TextWhite
            )

            AccountStatCard(
                modifier = Modifier.weight(1f),
                title = "DAILY P/L",
                value = "${if (stats.dailyPnl >= 0) "+$" else "-$"}${String.format(java.util.Locale.US, "%,.2f", kotlin.math.abs(stats.dailyPnl))}",
                subtitle = "${if (stats.dailyPnlPercent >= 0) "+" else ""}${stats.dailyPnlPercent}%",
                valueColor = if (stats.dailyPnl >= 0) SuparbaseNeonGreen else SuparbaseRed
            )

            AccountStatCard(
                modifier = Modifier.weight(1f),
                title = "WIN RATE",
                value = "${stats.winRate}%",
                subtitle = "${stats.wins}W / ${stats.losses}L (${stats.totalTrades} total)",
                valueColor = SuparbaseGold
            )
        }
    }
}

@Composable
fun AccountStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    valueColor: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SuparbaseDarkCard)
            .border(1.dp, SuparbaseCardBorderSubtle, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = title,
                color = TextMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = value,
                color = valueColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 8.5.sp,
                maxLines = 1
            )
        }
    }
}
