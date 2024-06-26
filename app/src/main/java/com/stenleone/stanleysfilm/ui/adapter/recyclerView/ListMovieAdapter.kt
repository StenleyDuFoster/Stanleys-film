package com.stenleone.stanleysfilm.ui.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.databinding.ItemCardMovieBigBinding
import com.stenleone.stanleysfilm.databinding.ItemCardMovieSmallBinding
import com.stenleone.stanleysfilm.databinding.ItemCardMovieVerticalBinding
import com.stenleone.stanleysfilm.interfaces.ItemClickParcelable
import com.stenleone.stanleysfilm.interfaces.RecyclerViewInterface
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.base.BaseRecyclerView
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import java.util.ArrayList
import javax.inject.Inject

class ListMovieAdapter @Inject constructor() : BaseRecyclerView() {

    companion object {
        const val TYPE_SMALL = 0
        const val TYPE_LARGE = 1
        const val TYPE_MORE_INFO = 2
        const val TYPE_VERTICAL = 2

        const val DEFAULT_LIST_SIZE = 10
    }

    var typeHolder = TYPE_SMALL
    val itemList: ArrayList<MovieUI> = arrayListOf()
    lateinit var listener: ItemClickParcelable

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
            TYPE_LARGE -> LargeMovieHolder(ItemCardMovieBigBinding.inflate(LayoutInflater.from(parent.context)))
            TYPE_VERTICAL -> VerticalMovieHolder(ItemCardMovieVerticalBinding.inflate(LayoutInflater.from(parent.context)))
            else -> WatchMoreMovieHolder(ItemCardMovieSmallBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    override fun getItemCount(): Int {
        return if (itemList.size > 0) {
            itemList.size
        } else {
            DEFAULT_LIST_SIZE
        }
    }

    inner class SmallMovieHolder(val binding: ItemCardMovieSmallBinding) :
        RecyclerView.ViewHolder(binding.root), RecyclerViewInterface {

        override fun bind() {
            binding.apply {
                executePendingBindings()
                if (itemList.size > 0) {
                    movie = itemList[adapterPosition]
                    shimmerViewContainer.hideShimmer()
                }
            }

            setupClicks()
        }

        private fun setupClicks() {
            binding.card.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    if (this@ListMovieAdapter::listener.isInitialized) {
                        listener.click(itemList[adapterPosition])
                    }
                }
                .launchIn(recyclerScope)
        }

        override fun unBind() {

        }
    }

    inner class LargeMovieHolder(val binding: ItemCardMovieBigBinding) :
        RecyclerView.ViewHolder(binding.root), RecyclerViewInterface {

        override fun bind() {
            binding.apply {
                if (itemList.size > 0) {
                    movie = itemList[adapterPosition]
                    shimmerViewContainer.hideShimmer()
                }
            }

            setupClicks()
        }

        private fun setupClicks() {
            binding.card.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    if (this@ListMovieAdapter::listener.isInitialized) {
                        listener.click(itemList[adapterPosition])
                    }
                }
                .launchIn(recyclerScope)
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
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    if (this@ListMovieAdapter::listener.isInitialized) {
                        try {
                            listener.click(itemList[adapterPosition])
                        } catch (e: IndexOutOfBoundsException) {

                        }
                    }
                }
                .launchIn(recyclerScope)
        }

        override fun unBind() {

        }
    }

    inner class VerticalMovieHolder(val binding: ItemCardMovieVerticalBinding) :
        RecyclerView.ViewHolder(binding.root), RecyclerViewInterface {

        override fun bind() {
            binding.apply {
                if (itemList.size > 0) {
                    movie = itemList[adapterPosition]
                    shimmerViewContainer.hideShimmer()
                }
            }

            setupClicks()
        }

        private fun setupClicks() {
            binding.card.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    if (this@ListMovieAdapter::listener.isInitialized) {
                        try {
                            listener.click(itemList[adapterPosition])
                        } catch (e: IndexOutOfBoundsException) {

                        }
                    }
                }
                .launchIn(recyclerScope)
        }

        override fun unBind() {

        }
    }
}