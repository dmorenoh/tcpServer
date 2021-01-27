package io.challenge.tcpserver.nio.attachments

import java.nio.ByteBuffer
import java.nio.channels.AsynchronousChannelGroup
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel

data class ClientAttachment(
    val channelGroup: AsynchronousChannelGroup,
    val server: AsynchronousServerSocketChannel
)

data class ReadAttachment(
    val channelGroup: AsynchronousChannelGroup,
    val server: AsynchronousServerSocketChannel,
    val client: AsynchronousSocketChannel,
    val isReadMode: Boolean = true,
    val buffer: ByteBuffer = ByteBuffer.allocate(2048)
)
