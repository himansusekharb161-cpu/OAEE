package com.example

import com.example.ui.components.ExamPaceStatus
import com.example.ui.components.TimerDisplayMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExamTimerTest {

    @Test
    fun `verify timer display modes exist`() {
        assertEquals(3, TimerDisplayMode.values().size)
        assertNotNull(TimerDisplayMode.EXPANDED_PRESSURE_HUD)
        assertNotNull(TimerDisplayMode.COMPACT_PILL)
        assertNotNull(TimerDisplayMode.HIDDEN_ZEN)
    }

    @Test
    fun `verify exam pace status badges`() {
        val ahead = ExamPaceStatus.AHEAD_OF_PACE
        assertEquals("Ahead of Pace ⚡", ahead.label)

        val onTrack = ExamPaceStatus.ON_TRACK
        assertEquals("On Track ⏱️", onTrack.label)

        val fallingBehind = ExamPaceStatus.FALLING_BEHIND
        assertEquals("Time Crunch ⚠️", fallingBehind.label)
    }
}
