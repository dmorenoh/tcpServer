package io.challenge.tcpserver

import io.challenge.tcpserver.nio.duplicated
import io.challenge.tcpserver.nio.validInputs
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.*


data class Report(val newItems: Int, val duplicatedItems: Int, val total: Int) {
    fun plus(report: Report) = copy(
        newItems = newItems + report.newItems,
        duplicatedItems = duplicatedItems + report.duplicatedItems
    )

}

fun createLastReport(oldReports: List<Report>, inputs: Set<String>, duplicated: Int): Report {
    val acc = oldReports.fold(Report(0, 0, 0)) { sum, item -> sum.plus(item) }
    return Report(inputs.size - acc.newItems, duplicated - acc.duplicatedItems, inputs.size)
}

@ObsoleteCoroutinesApi
fun startReportScheduler() {
    GlobalScope.launch {
        val tickerChannel = ticker(Duration.ofSeconds(10).toMillis())
        for (event in tickerChannel) {
            val lastReport = createLastReport(
                Collections.unmodifiableList(reports),
                Collections.unmodifiableSet(validInputs), duplicated.get()
            )
            reports.add(lastReport)
            println(lastReport)
        }
    }
}