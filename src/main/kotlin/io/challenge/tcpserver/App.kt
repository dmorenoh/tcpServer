package io.challenge.tcpserver

import io.challenge.tcpserver.nio.runServerAsync
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.ticker
import java.io.File
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.nio.channels.AsynchronousChannelGroup
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.ServerSocketChannel
import java.time.Duration
import java.util.*
import java.util.Collections.unmodifiableList
import java.util.Collections.unmodifiableSet
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger


@ObsoleteCoroutinesApi
fun main() {
    File("numbers.log").delete()
    startReportScheduler()
    runServerAsync(4000)
}

val scope = CoroutineScope(Dispatchers.IO)
val reports = mutableListOf<Report>()






