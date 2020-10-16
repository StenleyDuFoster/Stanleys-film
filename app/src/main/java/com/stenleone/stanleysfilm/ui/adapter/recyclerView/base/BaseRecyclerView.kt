package com.stenleone.stanleysfilm.ui.adapter.recyclerView.base

import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.interfaces.RecyclerViewInterface
import com.stenleone.stanleysfilm.util.extencial.disposeWithCheck
import io.reactivex.disposables.CompositeDisposable

abstract class BaseRecyclerView : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecyclerViewInterface).bind()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as RecyclerViewInterface).unBind()
        super.onViewDetachedFromWindow(holder)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        compositeDisposable.disposeWithCheck()
        super.onDetachedFromRecyclerView(recyclerView)
    }
}