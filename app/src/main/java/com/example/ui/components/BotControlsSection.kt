package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BotSettings
import com.example.ui.theme.*
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun BotControlsSection(
    settings: BotSettings,
    balance: Double,
    onToggleBot: () -> Unit,
    onRiskChanged: (Float) -> Unit,
    onTogglePair: (String) -> Unit,
    onToggleSession: (Boolean, Boolean, Boolean) -> Unit,
    onToggleNewsFilter: (Boolean) -> Unit,
    onEmergencyCloseAll: () -> Unit
) {
    var showEmergencyDialog by remember { mutableStateOf(false) }

    val allPairs = listOf("EURUSD", "GBPUSD", "XAUUSD", "GBPJPY")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SuparbaseDarkCard)
            .border(1.dp, SuparbaseCardBorderSubtle, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        // Section Header
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
                    text = "SMC BOT EXECUTION CONTROLS",
                    color = TextWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }

            // Quick Status
            Text(
                text = if (settings.isRunning) "STATUS: ARMED 24/7" else "STATUS: STANDBY",
                color = if (settings.isRunning) SuparbaseNeonGreen else SuparbaseRed,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Risk Slider
        val riskAmount = balance * (settings.riskPercent / 100.0)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Account Risk Per Trade",
                color = TextWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${String.format(Locale.US, "%.1f", settings.riskPercent)}%",
                    color = SuparbaseNeonGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "($${String.format(Locale.US, "%,.0f", riskAmount)})",
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }

        Slider(
            value = settings.riskPercent,
            onValueChange = { onRiskChanged((it * 10).roundToInt() / 10f) },
            valueRange = 0.5f..5.0f,
            steps = 8,
            colors = SliderDefaults.colors(
                thumbColor = SuparbaseNeonGreen,
                activeTrackColor = SuparbaseNeonGreen,
                inactiveTrackColor = Color(0x3348576A)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("risk_slider")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Target Pairs Selection (Max 3 pairs concurrent)
        Text(
            text = "Tradable Pairs (${settings.enabledPairs.size}/${settings.maxSimultaneousPairs} Active)",
            color = TextWhite,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            allPairs.forEach { pair ->
                val isEnabled = settings.enabledPairs.contains(pair)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isEnabled) SuparbaseNeonGreen.copy(alpha = 0.15f) else Color(0x11FFFFFF))
                        .border(
                            1.dp,
                            if (isEnabled) SuparbaseNeonGreen else Color(0x22FFFFFF),
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { onTogglePair(pair) }
                        .padding(vertical = 7.dp)
                        .testTag("pair_chip_$pair"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = pair,
                        color = if (isEnabled) SuparbaseNeonGreen else TextMuted,
                        fontSize = 11.sp,
                        fontWeight = if (isEnabled) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Session Time Filter (London & New York mandatory for SMC liquidity)
        Text(
            text = "Active Trading Sessions (SMC Killzones)",
            color = TextWhite,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SessionChip(
                name = "London (08-16 GMT)",
                isActive = settings.sessionLondon,
                modifier = Modifier.weight(1f),
                onClick = { onToggleSession(!settings.sessionLondon, settings.sessionNewYork, settings.sessionAsia) }
            )
            SessionChip(
                name = "New York (13-21 GMT)",
                isActive = settings.sessionNewYork,
                modifier = Modifier.weight(1f),
                onClick = { onToggleSession(settings.sessionLondon, !settings.sessionNewYork, settings.sessionAsia) }
            )
            SessionChip(
                name = "Asian (00-08 GMT)",
                isActive = settings.sessionAsia,
                modifier = Modifier.weight(1f),
                onClick = { onToggleSession(settings.sessionLondon, settings.sessionNewYork, !settings.sessionAsia) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ForexFactory High Impact News Filter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF070B10))
                .border(0.5.dp, Color(0x22FFFFFF), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "High-Impact News Filter (ForexFactory)",
                    color = TextWhite,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Auto-pause execution 30 min before & after red folder releases",
                    color = TextMuted,
                    fontSize = 8.5.sp
                )
            }
            Switch(
                checked = settings.pauseOnHighImpactNews,
                onCheckedChange = onToggleNewsFilter,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = SuparbaseGold,
                    checkedTrackColor = SuparbaseGold.copy(alpha = 0.4f),
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = Color(0x22FFFFFF)
                ),
                modifier = Modifier.testTag("news_filter_switch")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Primary Bot Action Buttons: Start/Stop & Emergency Close All
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onToggleBot,
                modifier = Modifier
                    .weight(1.5f)
                    .height(44.dp)
                    .testTag("main_toggle_bot_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (settings.isRunning) SuparbaseRedDark else SuparbaseNeonGreen,
                    contentColor = if (settings.isRunning) TextWhite else SuparbaseBlack
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (settings.isRunning) "PAUSE BOT" else "START BOT (24/7)",
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    letterSpacing = 0.5.sp
                )
            }

            OutlinedButton(
                onClick = { showEmergencyDialog = true },
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("emergency_close_button"),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = SuparbaseRed
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, SuparbaseRed),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "CLOSE ALL",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }
    }

    if (showEmergencyDialog) {
        AlertDialog(
            onDismissRequest = { showEmergencyDialog = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = "Alert", tint = SuparbaseRed) },
            title = {
                Text(
                    text = "EMERGENCY CLOSE ALL",
                    color = TextWhite,
                    fontWeight = FontWeight.Black
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to immediately close all active forex & gold positions at market price?",
                    color = TextMuted,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showEmergencyDialog = false
                        onEmergencyCloseAll()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SuparbaseRed),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("YES, CLOSE ALL", color = TextWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEmergencyDialog = false }) {
                    Text("CANCEL", color = TextMuted)
                }
            },
            containerColor = SuparbaseDarkCard
        )
    }
}

@Composable
fun SessionChip(
    name: String,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isActive) SuparbaseGold.copy(alpha = 0.15f) else Color(0x0DFFFFFF))
            .border(
                0.8.dp,
                if (isActive) SuparbaseGold else Color(0x1AFFFFFF),
                RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(if (isActive) SuparbaseGold else TextMuted)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = name,
                color = if (isActive) SuparbaseGold else TextMuted,
                fontSize = 9.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1
            )
        }
    }
}
