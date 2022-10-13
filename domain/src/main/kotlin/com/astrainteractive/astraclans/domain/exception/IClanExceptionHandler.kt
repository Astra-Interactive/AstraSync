package com.astrainteractive.astraclans.domain.exception

interface IClanExceptionHandler {
    fun handle(e: DomainException) {
        when (e) {

            else -> {}
        }
    }


    fun handle(e: Exception)
}