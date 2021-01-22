package com.stenleone.stanleysfilm.ui.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.databinding.ItemGenreBinding
import com.stenleone.stanleysfilm.interfaces.ItemClickParcelable
import com.stenleone.stanleysfilm.interfaces.RecyclerViewInterface
import com.stenleone.stanleysfilm.network.entity.movie.Genre
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.base.BaseRecyclerView
import java.util.ArrayList
import javax.inject.Inject

class GenreListRecycler @Inject constructor() : BaseRecyclerView() {

    val itemList: ArrayList<Genre> = arrayListOf()
    lateinit var listener: ItemClickParcelable

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