package com.racjonalnytraktor.findme3.domain.base

import io.reactivex.Completable
import io.reactivex.Scheduler

abstract class CompletableUseCase<in Parameter>(subscribeScheduler: Scheduler,
                                                observeScheduler: Scheduler
)
    : BaseUseCase(subscribeScheduler,observeScheduler) {

    abstract fun buildUseCaseCompletable(request: Parameter? = null): Completable

    fun excecute(request: Parameter? = null,
                 onComplete: (() -> Unit),
                 onError: ((t: Throwable) -> Unit)){
        val completable = buildUseCaseCompletableWithSchedulers(request)
        val disposable = completable.subscribe(onComplete, onError)
        addDisposable(disposable)
    }

    fun raw(request: Parameter? = null): Completable{
        return buildUseCaseCompletableWithSchedulers(request)
    }

    private fun buildUseCaseCompletableWithSchedulers(request: Parameter?): Completable {
        return buildUseCaseCompletable(request)
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
    }
}