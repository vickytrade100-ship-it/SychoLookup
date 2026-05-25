package com.sycho.lookup.utils

import java.text.SimpleDateFormat
import java.util.*

fun String.isValidQuery(): Boolean {
    val clean = this.trim()
    return when {
        clean.startsWith("92") && clean.length == 12 && clean.all { it.isDigit() } -> true
        clean.replace("-", "").length == 13 && clean.replace("-", "").all { it.isDigit() } -> true
        else -> false
    }
}

fun Long.toRelativeTime(): String {
    val diff = System.currentTimeMillis() - this
    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000}m ago"
        diff < 86_400_000 -> "${diff / 3_600_000}h ago"
        else -> SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(this))
    }
}

fun String.sanitizeQuery(): String = this.trim().replace("-", "")

fun String.toTitleCase(): String =
    split(" ").joinToString(" ") { it.lowercase().replaceFirstChar(Char::uppercaseChar) }

fun String.formatAsCnic(): String {
    val clean = this.replace("-", "")
    return if (clean.length == 13)
        "${clean.substring(0, 5)}-${clean.substring(5, 12)}-${clean.last()}"
    else this
}
