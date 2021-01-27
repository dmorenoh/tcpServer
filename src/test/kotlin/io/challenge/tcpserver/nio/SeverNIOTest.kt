package io.challenge.tcpserver.nio

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.io.File
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future


class SeverNIOTest {

    @Test
    fun `should accept clients`() {

        File("numbers.log").delete()
        Executors.newSingleThreadExecutor().submit {
            runBlocking {
                runServerAsync(4000)
            }

        }
        Thread.sleep(1000)

        val executor: ExecutorService = Executors.newCachedThreadPool()

        val messages = (0..1000)
            .map { it.toString().padStart(9, '0') }.toTypedArray()

        val messages1 = (1000..2000)
            .map { it.toString().padStart(9, '0') }.toTypedArray()

        listOf(
            Runnable { triggerClient("localhost", 4000, messages) },
            Runnable { triggerClient("localhost", 4000, messages1) }
        ).forEach { executor.submit(it) }

        Thread.sleep(10000)
        assert(validInputs.size > 0)
        assert(File("numbers.log").exists())
    }

    fun triggerClient(hostName: String, port: Int, messages: Array<String>) {
        val client = AsynchronousSocketChannel.open()

        val future: Future<*> = client.connect(InetSocketAddress(hostName, port))
        future.get()
        Thread.sleep(30)
        println("Client is started: " + client.isOpen)
        println("Sending messages to server: ")

        for (i in messages.indices) {
            val message: ByteArray = messages[i].toByteArray()
            val buffer = ByteBuffer.wrap(message)
            client.write(buffer, Any(), object : CompletionHandler<Int, Any> {
                override fun completed(result: Int, att: Any) {
                    buffer.clear()
                }

                override fun failed(t: Throwable, att: Any) {
                    buffer.clear()
                }
            })
            Thread.sleep(10)
        }
        client.close()
    }
}
