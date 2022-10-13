package com.astrainteractive.astraclans.domain.exception

sealed class DomainException : Exception() {

    object PlayerNotFoundException : DomainException()
    object PlayerLockedException : DomainException()
    object PlayerDataNotExists : DomainException()
}