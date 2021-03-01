package ru.developer.amukovozov.testapp.util

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class BaseFragment(@LayoutRes layoutResId: Int) : Fragment(layoutResId) {

    private var compositeDisposable: CompositeDisposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable?.dispose()
        compositeDisposable = CompositeDisposable()
    }

    override fun onDestroyView() {
        compositeDisposable?.clear()
        super.onDestroyView()
    }

    protected fun Disposable.disposeOnViewDestroy(): Disposable {
        compositeDisposable?.add(this)
        return this
    }
}