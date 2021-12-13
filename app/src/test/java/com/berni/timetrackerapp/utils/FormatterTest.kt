package com.berni.timetrackerapp.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FormatterTest {

    @Test
    fun testTimeStringFromDouble() {
        val result = Formatter.getTimeStringFromDouble(1.0)
        val expectedResult = "00:00:01"
        assertThat(result).isEqualTo(expectedResult)
    }

}