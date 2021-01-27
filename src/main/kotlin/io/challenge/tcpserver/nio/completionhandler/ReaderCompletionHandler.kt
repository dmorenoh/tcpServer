package io.challenge.tcpserver.nio.completionhandler

import io.challenge.tcpserver.nio.attachments.ReadAttachment
import io.challenge.tcpserver.nio.logMessageActorNIO
import io.challenge.tcpserver.scope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import mu.KotlinLogging
import java.nio.ByteBuffer
import java.nio.channels.CompletionHandler
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

object ReadCompletionHandler : CompletionHandler<Int, ReadAttachment> {
    @ObsoleteCoroutinesApi
    override fun completed(result: Int, att: ReadAttachment) {
        if (att.channelGroup.isShutdown || !att.server.isOpen) {
            att.client.close()
        } else {
            att.process()
        }
    }

    override fun failed(t: Throwable, att: ReadAttachment) {
        if (att.channelGroup.isShutdown || !att.server.isOpen)
            Thread.currentThread().interrupt()
        logger.error { "Error reading from ${att.client.remoteAddress}" }
    }
}


fun String.isValidNumber(): Boolean = this.matches(Regex("\\d{9}"))

@ObsoleteCoroutinesApi
fun ReadAttachment.process() {

    val msg = this.buffer.getLine()
    this.buffer.clear()
    when {
        msg.isValidNumber() -> {
            scope.launch { processValidItem(msg) }
            this.client.asyncRead(ReadAttachment(this.channelGroup, this.server, this.client))
        }
        msg == "terminate" -> {
            this.client.close()
            this.server.close()
            this.channelGroup.shutdown()
            this.channelGroup.awaitTermination(10, TimeUnit.SECONDS)
        }
        else -> {
            logger.warn { "Invalid input $msg" }
            this.client.close()
        }
    }
}

@ObsoleteCoroutinesApi
suspend fun processValidItem(newItem: String) {
    logger.info { "New Item: $newItem" }
    logMessageActorNIO.send(newItem)
}


fun ByteBuffer.getLine(): String {
    val buffer = this
    this.flip()
    val bytes = ByteArray(buffer.limit())
    buffer[bytes]
    return String(buffer.array()).trim { it <= ' ' }
}