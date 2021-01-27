package io.challenge.tcpserver.nio

import io.challenge.tcpserver.nio.attachments.ClientAttachment
import io.challenge.tcpserver.nio.completionhandler.AcceptClientCompletionHandler
import java.lang.Thread.currentThread
import java.net.InetSocketAddress
import java.net.StandardSocketOptions
import java.nio.channels.AsynchronousChannelGroup
import java.nio.channels.AsynchronousServerSocketChannel
import java.util.concurrent.Executors


fun runServerAsync(port: Int) {
    val threads = 3
    val channelGroup = AsynchronousChannelGroup.withFixedThreadPool(threads, Executors.defaultThreadFactory())
    println("Starting")
    AsynchronousServerSocketChannel.open(channelGroup)
        .setOption(StandardSocketOptions.SO_REUSEPORT, true)
        .bind(InetSocketAddress(port))
        .acceptClientConnection(channelGroup)

    try {
        currentThread().join()
    } catch (e: InterruptedException) {
    }

}

fun AsynchronousServerSocketChannel.acceptClientConnection(channelGroup: AsynchronousChannelGroup) {
    println("New client")
    this.accept(ClientAttachment(channelGroup, this), AcceptClientCompletionHandler)
}



