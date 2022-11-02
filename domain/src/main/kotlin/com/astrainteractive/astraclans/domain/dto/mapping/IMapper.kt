package com.astrainteractive.astraclans.domain.dto.mapping

interface ExposedMapper<I, O> {
    fun toExposed(it: O): I.() -> Unit
}