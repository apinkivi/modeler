package fi.apinkivi.modeler.json

/** Map of counters that preserve insert order. */
typealias Counter<T> = LinkedHashMap<T, Int>

fun <T> Counter<T>.inc(element: T) {
    this[element] = (this[element] ?: 0) + 1
}
