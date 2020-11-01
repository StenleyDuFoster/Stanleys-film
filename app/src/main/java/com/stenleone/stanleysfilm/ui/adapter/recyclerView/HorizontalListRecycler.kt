package com.stenleone.stanleysfilm.ui.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import com.stenleone.stanleysfilm.databinding.ItemCardMovieSmallBinding
import com.stenleone.stanleysfilm.interfaces.ItemClick
import com.stenleone.stanleysfilm.interfaces.RecyclerViewInterface
import com.stenleone.stanleysfilm.network.entity.movie.Movie
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.base.BaseRecyclerView
import io.reactivex.rxkotlin.addTo
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class HorizontalListRecycler : BaseRecyclerView() {

    companion object {
        const val TYPE_SMALL = 0
        const val TYPE_LARGE = 1
        const val TYPE_MORE_INFO = 2
    }

    var typeHolder = TYPE_SMALL
    val itemList: ArrayList<Movie> = arrayListOf()
    lateinit var listener: ItemClick

    override fun getItemViewType(position: Int): Int {
        return if (position < itemList.size) {
            typeHolder
        } else {
            TYPE_MORE_INFO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SMALL -> SmallMovieHolder(ItemCardMovieSmallBinding.inflate(LayoutInflater.from(parent.context)))
            TYPE_LARGE -> LargeMovieHolder(ItemCardMovieSmallBinding.inflate(LayoutInflater.from(parent.context)))
            else -> WatchMoreMovieHolder(ItemCardMovieSmallBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    override fun getItemCount(): Int {
        return itemList.size + 1
    }

    inner class SmallMovieHolder(val binding: ItemCardMovieSmallBinding) :
        RecyclerView.ViewHolder(binding.root), RecyclerViewInterface {

        override fun bind() {
            binding.movie = itemList[adapterPosition]
            setupClicks()
        }

        private fun setupClicks() {
            binding.card.clicks()
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe {
                    if (this@HorizontalListRecycler::listener.isInitialized) {
                        listener.click(itemList[adapterPosition])
                    }
                }
                .addTo(compositeDisposable)
        }

        override fun unBind() {

        }
    }

    inner class LargeMovieHolder(val binding: ItemCardMovieSmallBinding) :
        RecyclerView.ViewHolder(binding.root), RecyclerViewInterface {

        override fun bind() {
            binding.movie = itemList[adapterPosition]
            setupClicks()
        }

        private fun setupClicks() {
            binding.card.clicks()
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe {
                    if (this@HorizontalListRecycler::listener.isInitialized) {
                        listener.click(itemList[adapterPosition])
                    }
                }
                .addTo(compositeDisposable)
        }

        override fun unBind() {

        }
    }

    inner class WatchMoreMovieHolder(val binding: ItemCardMovieSmallBinding) :
        RecyclerView.ViewHolder(binding.root), RecyclerViewInterface {

        override fun bind() {
            setupClicks()
        }

        private fun setupClicks() {
            binding.card.clicks()
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe {
                    if (this@HorizontalListRecycler::listener.isInitialized) {
                        listener.click(itemList[adapterPosition])
                    }
                }
                .addTo(compositeDisposable)
        }

        override fun unBind() {

        }
    }
}