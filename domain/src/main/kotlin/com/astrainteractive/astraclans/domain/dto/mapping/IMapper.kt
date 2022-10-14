package com.astrainteractive.astraclans.domain.dto.mapping

const val NOT_EXISTS_ID = -1

interface IMapper<I, O> {
    fun toDTO(it: I): O
    fun fromDTO(it: O): I
}

interface ExposedMapper<I, O> {
    fun toExposed(it: O): I.() -> Unit
}