package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
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
    assertEquals("عسل ساوالان", appName)
  }

  @Test
  fun `test persian digits formatting`() {
    val persian = com.example.util.PersianUtils.toPersianDigits("123450")
    assertEquals("۱۲۳۴۵۰", persian)
  }

  @Test
  fun `test price formatting`() {
    val formatted = com.example.util.PersianUtils.formatPrice(450000)
    assertEquals("۴۵۰,۰۰۰ تومان", formatted)
  }
}
