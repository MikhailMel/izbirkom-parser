package ru.scratty.izbirkomparser

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ru.scratty.izbirkomparser.uikparser.UikParser
import java.io.File
import java.io.FileOutputStream

fun main() {
    UikParser().use { uikParser ->
        val rootUik = uikParser.parseAllUik()
        val allUikList = rootUik.toListUiks()
        val trueUikList = allUikList.filter { it.isUik }

        writeJsonToFile("src/main/resources/uik/all.json", allUikList)
        writeJsonToFile("src/main/resources/uik/true.json", trueUikList)
    }
}

private fun writeJsonToFile(filename: String, value: Any) {
    FileOutputStream(File(filename)).use { os ->
        jacksonObjectMapper().writeValue(os, value)
    }
}