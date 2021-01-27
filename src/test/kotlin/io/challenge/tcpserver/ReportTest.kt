package io.challenge.tcpserver

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ReportTest {
    @Test
    fun `should return first report when no previous report`() {
        val inputs = setOf("value1", "value2", "value3", "value4")
        val result = createLastReport(oldReports = emptyList(), inputs = inputs, duplicated = 4)
        val expected = Report(4, 4, 4)
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `should return last report when previous report`() {
        val oldReports = listOf(
            Report(3, 5, 3),
            Report(5, 4, 8)
        )
        val inputs = setOf(
            "value1",
            "value2",
            "value3",
            "value4",
            "value5",
            "value6",
            "value7",
            "value8",
            "value9",
            "value10",
            "value11",
            "value12",
            "value13",
        )
        val result = createLastReport(oldReports = oldReports, inputs = inputs, duplicated = 15)
        val expected = Report(5, 6, 13)
        Assertions.assertEquals(expected, result)
    }
}