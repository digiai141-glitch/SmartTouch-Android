package com.smarttouch.app

import com.google.common.truth.Truth.assertThat
import com.smarttouch.app.data.model.AppSettings
import org.junit.Test

class AppSettingsTest {

    @Test
    fun `default settings have expected values`() {
        val defaults = AppSettings.Default
        assertThat(defaults.isServiceEnabled).isFalse()
        assertThat(defaults.hapticFeedback).isTrue()
        assertThat(defaults.startOnBoot).isTrue()
        assertThat(defaults.sensitivity).isEqualTo(5)
        assertThat(defaults.gestureZoneSize).isEqualTo(24)
    }

    @Test
    fun `sensitivity is stored as provided`() {
        val s = AppSettings(sensitivity = 7)
        assertThat(s.sensitivity).isEqualTo(7)
    }
}
