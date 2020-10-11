package com.stenleone.stanleysfilm.util.extencial

import io.reactivex.disposables.CompositeDisposable

fun CompositeDisposable.disposeWithCheck() {
    if (!this.isDisposed) {
        this.dispose()
    }
}