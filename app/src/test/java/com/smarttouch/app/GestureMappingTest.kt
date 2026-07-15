package com.smarttouch.app

import com.google.common.truth.Truth.assertThat
import com.smarttouch.app.data.model.GestureAction
import com.smarttouch.app.data.model.GestureMapping
import com.smarttouch.app.data.model.GestureType
import com.smarttouch.app.data.model.GestureZone
import org.junit.Test

class GestureMappingTest {

    @Test
    fun `mapping id is stable and deterministic`() {
        val m1 = GestureMapping(GestureZone.TOP_EDGE, GestureType.SINGLE_TAP)
        val m2 = GestureMapping(GestureZone.TOP_EDGE, GestureType.SINGLE_TAP, GestureAction.FLASHLIGHT)
        assertThat(m1.id).isEqualTo(m2.id)
        assertThat(m1.id).isEqualTo("top_edge__single_tap")
    }

    @Test
    fun `all gesture zones have unique keys`() {
        val keys = GestureZone.entries.map { it.key }
        assertThat(keys).containsNoDuplicates()
    }

    @Test
    fun `all gesture types have unique keys`() {
        val keys = GestureType.entries.map { it.key }
        assertThat(keys).containsNoDuplicates()
    }

    @Test
    fun `all gesture actions have unique keys`() {
        val keys = GestureAction.entries.map { it.key }
        assertThat(keys).containsNoDuplicates()
    }

    @Test
    fun `GestureZone fromKey returns correct zone`() {
        assertThat(GestureZone.fromKey("top_edge")).isEqualTo(GestureZone.TOP_EDGE)
        assertThat(GestureZone.fromKey("floating")).isEqualTo(GestureZone.FLOATING)
    }

    @Test
    fun `GestureZone fromKey falls back to TOP_EDGE for unknown key`() {
        assertThat(GestureZone.fromKey("unknown_zone")).isEqualTo(GestureZone.TOP_EDGE)
    }

    @Test
    fun `GestureAction fromKey returns NONE for unknown key`() {
        assertThat(GestureAction.fromKey("xyz")).isEqualTo(GestureAction.NONE)
    }

    @Test
    fun `mapping copy preserves id`() {
        val original = GestureMapping(GestureZone.LEFT_EDGE, GestureType.SWIPE_DOWN)
        val updated = original.copy(action = GestureAction.VOLUME_DOWN)
        assertThat(updated.id).isEqualTo(original.id)
        assertThat(updated.action).isEqualTo(GestureAction.VOLUME_DOWN)
    }
}
