package com.example.pawbook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawbook.FavoritesManager
import com.example.pawbook.R

//class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {
class ImagesAdapter(private val longClickListener: (Int) -> Unit) : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {
    private val images = mutableListOf<String>()

    fun updateData(newImages: List<String>) {
        images.clear()
        images.addAll(newImages)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view).apply {
            itemView.setOnLongClickListener {
                longClickListener(adapterPosition)
                true
            }
        }
    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
//        return ImageViewHolder(view)
//    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = images[position]
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.imageView)

        holder.favoriteIcon.visibility = if (FavoritesManager.isFavorite(imageUrl)) View.VISIBLE else View.GONE
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val favoriteIcon: ImageView = view.findViewById(R.id.favoriteIcon)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}
