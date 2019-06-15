package com.racjonalnytraktor.findme3.domain.base

import io.reactivex.Scheduler
import io.reactivex.Single

abstract class SingleUseCase<in Parameter, Result>(subscribeScheduler: Scheduler,
                             observeScheduler: Scheduler)
    : BaseUseCase(subscribeScheduler,observeScheduler) {

    abstract fun buildUseCaseSingle(request: Parameter? = null): Single<Result>

    fun excecute(request: Parameter? = null,
                 onSuccess: ((r: Result) -> Unit),
                 onError: ((t: Throwable) -> Unit)){
        val single = buildUseCaseSingleWithSchedulers(request)
        val disposable = single.subscribe(onSuccess,onError)
        addDisposable(disposable)
    }

    private fun buildUseCaseSingleWithSchedulers(request: Parameter?): Single<Result>{
        return buildUseCaseSingle(request)
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
    }
}