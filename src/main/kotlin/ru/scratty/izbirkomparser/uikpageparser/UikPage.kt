package ru.scratty.izbirkomparser.uikpageparser

import java.time.ZonedDateTime

data class UikPage(
    val id: Long,
    val href: String,
    val name: String,
    val stats: List<Int?>,
    val parsingDate: ZonedDateTime = ZonedDateTime.now()
)
