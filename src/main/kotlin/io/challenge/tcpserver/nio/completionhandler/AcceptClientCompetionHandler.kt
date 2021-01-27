package io.challenge.tcpserver.nio.completionhandler

import io.challenge.tcpserver.nio.attachments.ClientAttachment
import io.challenge.tcpserver.nio.attachments.ReadAttachment
import kotlinx.coroutines.ObsoleteCoroutinesApi
import mu.KotlinLogging
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler

private val logger = KotlinLogging.logger {}

object AcceptClientCompletionHandler : CompletionHandler<AsynchronousSocketChannel, ClientAttachment> {

    @ObsoleteCoroutinesApi
    override fun completed(client: AsynchronousSocketChannel, clientAttachment: ClientAttachment) {
        logger.info { "Receive a new connection:$client.remoteAddress" }
        client.asyncRead(ReadAttachment(clientAttachment.channelGroup, clientAttachment.server, client)).also {
            clientAttachment.server.accept(clientAttachment, this)
        }
    }

    override fun failed(exc: Throwable, clientAttachment: ClientAttachment) {
        Thread.currentThread().interrupt()
        logger.error { "Error accepting client from: ${clientAttachment.server.localAddress}" }
    }

}

@ObsoleteCoroutinesApi
fun AsynchronousSocketChannel.asyncRead(attachment: ReadAttachment) {
    logger.info { "About to read:${attachment.client}.remoteAddress" }
    this.read(attachment.buffer, attachment, ReadCompletionHandler)
}