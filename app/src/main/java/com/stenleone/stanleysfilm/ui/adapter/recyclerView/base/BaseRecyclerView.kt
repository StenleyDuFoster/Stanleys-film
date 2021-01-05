package com.stenleone.stanleysfilm.ui.adapter.recyclerView.base

import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.interfaces.RecyclerViewInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseRecyclerView : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val recyclerJob = Job()
    protected val recyclerScope = CoroutineScope(Dispatchers.Main + recyclerJob)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecyclerViewInterface).bind()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as RecyclerViewInterface).unBind()
        super.onViewDetachedFromWindow(holder)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        recyclerJob.cancel()
        super.onDetachedFromRecyclerView(recyclerView)
    }
}