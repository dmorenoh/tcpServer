package io.challenge.tcpserver.nio.completionhandler

import io.challenge.tcpserver.nio.attachments.ReadAttachment
import io.challenge.tcpserver.nio.validInputs
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousChannelGroup
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel


class ReadCompletionHandlerTest {

    val group = mockk<AsynchronousChannelGroup>(relaxed = true)
    val server = mockk<AsynchronousServerSocketChannel>(relaxed = true)
    val client = mockk<AsynchronousSocketChannel>(relaxed = true)

    @ObsoleteCoroutinesApi
    @Test
    fun `should close client when server closed`() {
        val att = ReadAttachment(
            channelGroup = group,
            server = server,
            client = client
        )
        every { group.isShutdown } returns true
        ReadCompletionHandler.completed(1, att)
        verify { client.close() }
    }


    @ObsoleteCoroutinesApi
    @Test
    fun `should close process input when server connected`() {

        val att = ReadAttachment(
            channelGroup = group,
            server = server,
            client = client,
            buffer = ByteBuffer.wrap("000222333".toByteArray())
        )
        validInputs = mutableSetOf()
        every { group.isShutdown } returns false
        every { server.isOpen } returns true
        runBlocking {
            ReadCompletionHandler.completed(1, att)
        }

        assert(validInputs.contains("000222333"))
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `should close client when input is invalid`() {

        val att = ReadAttachment(
            channelGroup = group,
            server = server,
            client = client,
            buffer = ByteBuffer.wrap("invalid".toByteArray())
        )
        validInputs = mutableSetOf()
        every { group.isShutdown } returns false
        every { server.isOpen } returns true
        runBlocking {
            ReadCompletionHandler.completed(1, att)
        }

        verify { client.close() }
    }


    @ObsoleteCoroutinesApi
    @Test
    fun `should close client and server when input is terminate`() {

        val att = ReadAttachment(
            channelGroup = group,
            server = server,
            client = client,
            buffer = ByteBuffer.wrap("terminate".toByteArray())
        )
        validInputs = mutableSetOf()
        every { group.isShutdown } returns false
        every { server.isOpen } returns true
        runBlocking {
            ReadCompletionHandler.completed(1, att)
        }

        verify {
            client.close()
            server.close()
            group.shutdown()

        }
    }
}

