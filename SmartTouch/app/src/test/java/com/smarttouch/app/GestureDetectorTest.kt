package com.smarttouch.app

import android.content.Context
import android.os.SystemClock
import android.view.MotionEvent
import android.view.ViewConfiguration
import com.google.common.truth.Truth.assertThat
import com.smarttouch.app.data.model.GestureType
import com.smarttouch.app.service.GestureDetector
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test

class GestureDetectorTest {

    private lateinit var context: Context
    private val detectedGestures = mutableListOf<GestureType>()
    private lateinit var detector: GestureDetector

    @After
    fun tearDown() {
        unmockkAll()
        detector.destroy()
    }

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        val viewConfig = mockk<ViewConfiguration>(relaxed = true)
        mockkStatic(ViewConfiguration::class)
        every { ViewConfiguration.get(any()) } returns viewConfig
        every { viewConfig.scaledTouchSlop } returns 8
        every { ViewConfiguration.getLongPressTimeout() } returns 500
        every { ViewConfiguration.getDoubleTapTimeout() } returns 300

        mockkStatic(SystemClock::class)
        every { SystemClock.uptimeMillis() } returns 0L

        detector = GestureDetector(context, sensitivity = 5) { gesture ->
            detectedGestures.add(gesture)
        }
    }

    @Test
    fun `swipe right detected when horizontal distance exceeds threshold`() {
        val now = 100L
        val down = motionEvent(MotionEvent.ACTION_DOWN, 100f, 400f, now)
        val up = motionEvent(MotionEvent.ACTION_UP, 250f, 400f, now + 200)

        detector.onTouchEvent(down)
        detector.onTouchEvent(up)

        assertThat(detectedGestures).contains(GestureType.SWIPE_RIGHT)
    }

    @Test
    fun `swipe left detected when horizontal distance exceeds threshold leftward`() {
        val now = 100L
        val down = motionEvent(MotionEvent.ACTION_DOWN, 250f, 400f, now)
        val up = motionEvent(MotionEvent.ACTION_UP, 100f, 400f, now + 200)

        detector.onTouchEvent(down)
        detector.onTouchEvent(up)

        assertThat(detectedGestures).contains(GestureType.SWIPE_LEFT)
    }

    @Test
    fun `swipe up detected when vertical distance exceeds threshold upward`() {
        val now = 100L
        val down = motionEvent(MotionEvent.ACTION_DOWN, 400f, 400f, now)
        val up = motionEvent(MotionEvent.ACTION_UP, 400f, 250f, now + 200)

        detector.onTouchEvent(down)
        detector.onTouchEvent(up)

        assertThat(detectedGestures).contains(GestureType.SWIPE_UP)
    }

    @Test
    fun `swipe down detected when vertical distance exceeds threshold downward`() {
        val now = 100L
        val down = motionEvent(MotionEvent.ACTION_DOWN, 400f, 250f, now)
        val up = motionEvent(MotionEvent.ACTION_UP, 400f, 400f, now + 200)

        detector.onTouchEvent(down)
        detector.onTouchEvent(up)

        assertThat(detectedGestures).contains(GestureType.SWIPE_DOWN)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun motionEvent(action: Int, x: Float, y: Float, time: Long): MotionEvent {
        return MotionEvent.obtain(time, time, action, x, y, 0).also {
            // rawX/rawY setters aren't public; detector uses rawX/rawY so we mock via obtain
        }
    }
}
