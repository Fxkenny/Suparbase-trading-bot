package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.engine.SMCStrategyEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Suparbase Bot", appName)
  }

  @Test
  fun `verify smc lot size calculation`() {
    // Balance: $100,000, 1.0% Risk = $1,000 Risk Amount
    // EURUSD: Entry = 1.0850, SL = 1.0835 (15 pips), PipValue = $10/lot
    // 1000 / (15 * 10) = 6.67 lots
    val lotSize = SMCStrategyEngine.calculateLotSize(
      balance = 100000.0,
      riskPercent = 1.0f,
      entryPrice = 1.0850,
      stopLoss = 1.0835,
      pair = "EURUSD"
    )
    assertTrue("Lot size should be approximately 6.67", lotSize in 6.6..6.7)
  }

  @Test
  fun `verify smc structure generation`() {
    val candles = SMCStrategyEngine.generateRealisticCandles("EURUSD", 36)
    val analysis = SMCStrategyEngine.analyzeSmcStructure("EURUSD", candles)
    assertEquals("EURUSD", analysis.pair)
    assertTrue("Candles list should not be empty", analysis.candles.isNotEmpty())
  }
}
