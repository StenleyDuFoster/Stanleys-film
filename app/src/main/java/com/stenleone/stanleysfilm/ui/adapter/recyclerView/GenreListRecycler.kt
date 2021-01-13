package com.stenleone.stanleysfilm.ui.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.databinding.ItemCardMovieBigBinding
import com.stenleone.stanleysfilm.databinding.ItemCardMovieSmallBinding
import com.stenleone.stanleysfilm.databinding.ItemGenreBinding
import com.stenleone.stanleysfilm.interfaces.ItemClick
import com.stenleone.stanleysfilm.interfaces.RecyclerViewInterface
import com.stenleone.stanleysfilm.network.entity.movie.Genre
import com.stenleone.stanleysfilm.network.entity.movie.Movie
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.base.BaseRecyclerView
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import dagger.Component
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import java.util.ArrayList
import javax.inject.Inject

class GenreListRecycler @Inject constructor() : BaseRecyclerView() {

    val itemList: ArrayList<Genre> = arrayListOf()
    lateinit var listener: ItemClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GenreHolder(ItemGenreBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class GenreHolder(val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root), RecyclerViewInterface {

        override fun bind() {
            binding.apply {
                genre = itemList[adapterPosition]
            }

            setupClicks()
        }

        private fun setupClicks() {
//            binding.card.clicks()
//                .throttleFirst(BindingConstant.SMALL_THROTTLE)
//                .onEach {
//                    if (this@GenreListRecycler::listener.isInitialized) {
//                        listener.click(itemList[adapterPosition])
//                    }
//                }
//                .launchIn(recyclerScope)
        }

        override fun unBind() {

        }
    }
}