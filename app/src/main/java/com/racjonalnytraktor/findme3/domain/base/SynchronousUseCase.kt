package com.racjonalnytraktor.findme3.domain.base

interface SynchronousUseCase<out Result, in Parameter> {

    fun execute(request: Parameter? = null): Result
}