package com.stenleone.stanleysfilm.ui.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.databinding.ItemCardMovieSmallBinding
import com.stenleone.stanleysfilm.interfaces.RecyclerViewInterface
import com.stenleone.stanleysfilm.network.entity.movie.Movie
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.base.BaseRecyclerView
import java.util.ArrayList

class HorizontalListRecycler : BaseRecyclerView() {

    val itemList: ArrayList<Movie> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StandardHolder(ItemCardMovieSmallBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class StandardHolder(val binding: ItemCardMovieSmallBinding) : RecyclerView.ViewHolder(binding.root), RecyclerViewInterface {

        override fun bind() {
            binding.movie = itemList[adapterPosition]
        }

        override fun unBind() {

        }

    }
}