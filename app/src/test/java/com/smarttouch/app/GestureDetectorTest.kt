package com.smarttouch.app

import android.os.SystemClock
import android.view.MotionEvent
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.smarttouch.app.data.model.GestureType
import com.smarttouch.app.service.GestureDetector
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for [GestureDetector].
 *
 * Uses Robolectric so that Android framework classes (Handler, Looper,
 * ViewConfiguration, MotionEvent) work on the JVM without a device.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class GestureDetectorTest {

    private val detectedGestures = mutableListOf<GestureType>()
    private lateinit var detector: GestureDetector

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        detector = GestureDetector(context, sensitivity = 5) { gesture ->
            detectedGestures.add(gesture)
        }
    }

    @After
    fun tearDown() {
        detector.destroy()
        detectedGestures.clear()
    }

    // ── Swipe gestures ────────────────────────────────────────────────────────

    @Test
    fun `swipe right detected when horizontal delta exceeds threshold`() {
        sendSwipe(startX = 50f, startY = 300f, endX = 250f, endY = 300f)
        assertThat(detectedGestures).contains(GestureType.SWIPE_RIGHT)
    }

    @Test
    fun `swipe left detected when moving from right to left`() {
        sendSwipe(startX = 250f, startY = 300f, endX = 50f, endY = 300f)
        assertThat(detectedGestures).contains(GestureType.SWIPE_LEFT)
    }

    @Test
    fun `swipe up detected when moving upward`() {
        sendSwipe(startX = 300f, startY = 400f, endX = 300f, endY = 200f)
        assertThat(detectedGestures).contains(GestureType.SWIPE_UP)
    }

    @Test
    fun `swipe down detected when moving downward`() {
        sendSwipe(startX = 300f, startY = 200f, endX = 300f, endY = 400f)
        assertThat(detectedGestures).contains(GestureType.SWIPE_DOWN)
    }

    @Test
    fun `tiny movement does not trigger a swipe`() {
        sendSwipe(startX = 100f, startY = 300f, endX = 105f, endY = 300f)
        assertThat(detectedGestures).doesNotContain(GestureType.SWIPE_RIGHT)
        assertThat(detectedGestures).doesNotContain(GestureType.SWIPE_LEFT)
    }

    @Test
    fun `slow swipe beyond timeout does not register as swipe`() {
        val t = SystemClock.uptimeMillis()
        val down = MotionEvent.obtain(t, t, MotionEvent.ACTION_DOWN, 50f, 300f, 0)
        // Simulate a gesture that takes longer than swipeMaxDuration (500 ms)
        val up = MotionEvent.obtain(t, t + 700, MotionEvent.ACTION_UP, 250f, 300f, 0)
        detector.onTouchEvent(down)
        detector.onTouchEvent(up)
        down.recycle()
        up.recycle()
        assertThat(detectedGestures).doesNotContain(GestureType.SWIPE_RIGHT)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun sendSwipe(
        startX: Float, startY: Float,
        endX: Float, endY: Float,
        durationMs: Long = 150,
    ) {
        val t = SystemClock.uptimeMillis()
        val down = MotionEvent.obtain(t, t, MotionEvent.ACTION_DOWN, startX, startY, 0)
        val up   = MotionEvent.obtain(t, t + durationMs, MotionEvent.ACTION_UP, endX, endY, 0)
        detector.onTouchEvent(down)
        detector.onTouchEvent(up)
        down.recycle()
        up.recycle()
    }
}
