package com.stenleone.stanleysfilm.ui.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.databinding.ItemStudioBinding
import com.stenleone.stanleysfilm.interfaces.ItemClick
import com.stenleone.stanleysfilm.interfaces.RecyclerViewInterface
import com.stenleone.stanleysfilm.network.entity.movie.ProductionCompany
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.base.BaseRecyclerView
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import java.lang.IndexOutOfBoundsException
import java.util.ArrayList

class StudiosListRecycler : BaseRecyclerView() {

    val itemList: ArrayList<ProductionCompany> = arrayListOf()
    lateinit var listener: ItemClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StudioHolder(ItemStudioBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return if(itemList.size == 0) {
            HorizontalListMovie.DEFAULT_LIST_SIZE
        } else {
            itemList.size
        }
    }

    inner class StudioHolder(val binding: ItemStudioBinding) :
        RecyclerView.ViewHolder(binding.root), RecyclerViewInterface {

        override fun bind() {
            binding.apply {
                try {
                    studio = itemList[adapterPosition]
                } catch (e: IndexOutOfBoundsException) {

                }

                shimmerLay.hideShimmer()
            }

            setupClicks()
        }

        private fun setupClicks() {
            binding.cardLay.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    if (this@StudiosListRecycler::listener.isInitialized) {
                        listener.click(itemList[adapterPosition])
                    }
                    try {
                        Toast.makeText(binding.root.context, itemList[adapterPosition].name, Toast.LENGTH_SHORT).show()
                    } catch (e: IndexOutOfBoundsException) {

                    }
                }
                .launchIn(recyclerScope)
        }

        override fun unBind() {

        }
    }
}