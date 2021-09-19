package ru.scratty.izbirkomparser.uikparser

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Uik(
    val id: Long,
    val text: String,
    val href: String,
    val isUik: Boolean
) {
    @JsonIgnore
    val children = arrayListOf<Uik>()

    fun setChildren(col: Collection<Uik>) {
        children.clear()
        children.addAll(col)
    }

    fun toListUiks(): List<Uik> = children + children.flatMap { it.toListUiks() }
}
