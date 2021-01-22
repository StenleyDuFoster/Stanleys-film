package com.stenleone.stanleysfilm.ui.adapter.viewPager

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.stenleone.stanleysfilm.databinding.ImageViewerOverlayBinding
import com.stenleone.stanleysfilm.databinding.ItemImageBinding
import com.stenleone.stanleysfilm.interfaces.ItemClick
import com.stenleone.stanleysfilm.interfaces.RecyclerViewInterface
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import com.stenleone.stanleysfilm.util.glide.GlideApp
import com.stenleone.stanleysfilm.util.glide.GlideModule
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import java.util.ArrayList
import javax.inject.Inject

class ImageViewPager @Inject constructor() : RecyclerView.Adapter<ImageViewPager.ImageHolder>() {

    val listItems = ArrayList<String>()
    var title = ""

    lateinit var moveListener: ItemClick

    private val pagerJob = Job()
    private val pagerScope = CoroutineScope(Main + pagerJob)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind()

    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    inner class ImageHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root), RecyclerViewInterface {

        override fun bind() {
            binding.apply {
                url = listItems[adapterPosition]
                content.throttleClicks(
                    {
                        val overlay = ImageViewerOverlayBinding.inflate(LayoutInflater.from(binding.root.context))
                        overlay.title = title
                        overlay.maxPosition = listItems.size.toString()
                        overlay.selectedPosition = (adapterPosition + 1).toString()

                        StfalconImageViewer.Builder(binding.root.context, listItems) { imageView: ImageView, url: String ->
                            GlideApp
                                .with(imageView)
                                .load(TmdbNetworkConstant.BASE_IMAGE_URL + url)
                                .transition(DrawableTransitionOptions.withCrossFade(GlideModule.IMAGE_ANIM_DURUTATION))
                                .into(imageView)
                        }
                            .withTransitionFrom(binding.content)
                            .withStartPosition(adapterPosition)
                            .withOverlayView(overlay.root)
                            .withImageChangeListener { position ->
                                overlay.selectedPosition = (position + 1).toString()
                                if (this@ImageViewPager::moveListener.isInitialized) {
                                    moveListener.click(position)
                                }
                            }
                            .show()
                    }, pagerScope
                )
            }
        }

        override fun unBind() {

        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        pagerJob.cancel()
        super.onDetachedFromRecyclerView(recyclerView)
    }
}