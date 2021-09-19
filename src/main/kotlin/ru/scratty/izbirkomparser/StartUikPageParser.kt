package ru.scratty.izbirkomparser

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.readValue
import ru.scratty.izbirkomparser.uikpageparser.UikPageParser
import ru.scratty.izbirkomparser.uikparser.Uik
import java.io.FileInputStream
import java.io.FileOutputStream

fun main() {
    val mapper = jacksonMapperBuilder().addModule(JavaTimeModule()).build()

    FileOutputStream("src/main/resources/uikpage/all.json").bufferedWriter().use { writer ->
        UikPageParser().use { uikPageParser ->

            FileInputStream("src/main/resources/uik/process.json").bufferedReader().use { reader ->
                val lines = reader.lineSequence()

                lines.forEach {
                    val uik = mapper.readValue<Uik>(it.removeSuffix(","))
                    if (uik.isUik) {
                        try {
                            val uikPage = uikPageParser.parseUikPage(uik)

                            writer.appendLine(mapper.writeValueAsString(uikPage))
                            writer.flush()
                        } catch (ex: Exception) {
                            println("Ошибка парсинга результатов ${uik.id} УИКа:")
                            ex.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}