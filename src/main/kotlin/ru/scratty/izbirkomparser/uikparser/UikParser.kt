package ru.scratty.izbirkomparser.uikparser

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.io.BufferedWriter
import java.io.Closeable
import java.io.FileOutputStream
import java.nio.charset.Charset

class UikParser : Closeable {

    companion object {
        private const val ROOT_ID = 100100067795854
        private const val ROOT_NAME = "ЦИК России"
        private const val ROOT_HREF = "region/izbirkom?action=show&root=0&tvd=100100067795854&vrn=100100067795849&prver=0&pronetvd=null&region=0&sub_region=0"

        private const val API_URL = "http://www.primorsk.vybory.izbirkom.ru/region/izbirkom?action=tvdTree&tvdchildren=true&vrn=100100067795849&tvd=%d"

        private val CHARSET = Charset.forName("Windows-1251")
    }

    private val rootUik = Uik(ROOT_ID, ROOT_NAME, ROOT_HREF, false)

    private val httpClient = HttpClients.createDefault()
    private val mapper = jacksonObjectMapper()

    fun parseAllUik(): Uik = FileOutputStream("src/main/resources/uik/process.json").bufferedWriter().use {
        parseChildren(it, rootUik)
    }

    private fun parseChildren(writer: BufferedWriter, uik: Uik): Uik {
        val children = try {
            sendRequest(uik.id)
        } catch (ex: Exception) {
            println("Ошибка во время парсинга дочерних элементов:")
            ex.printStackTrace()

            emptyList()
        }

        children.forEach {
            writer.appendLine(mapper.writeValueAsString(it))
            if (!it.isUik) {
                parseChildren(writer, it)
            }
        }
        writer.flush()
        println("Сохранено ${children.size} дочерних элементов для ${uik.text}")

        uik.setChildren(children)
        return uik
    }

    private fun sendRequest(id: Long): List<Uik> {
        val req = HttpGet(API_URL.format(id))

        val resp = httpClient.execute(req)
        val body = resp.entity.content.bufferedReader(CHARSET)
        return mapper.readValue(body)
    }

    override fun close() {
        httpClient.close()
    }
}