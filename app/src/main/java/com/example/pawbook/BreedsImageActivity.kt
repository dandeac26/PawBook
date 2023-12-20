package com.example.pawbook

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawbook.API.RetrofitClient
import com.example.pawbook.DTO.BreedImagesResponse
import com.example.pawbook.adapter.ImagesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BreedsImageActivity : AppCompatActivity() {
    private var selectedImagePosition: Int = -1
    private var imagesAdapter: ImagesAdapter? = null
    private var allImages = listOf<String>()
    private var displayedImages = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breed_images)

        val breed = parseBreedName(intent.getStringExtra("breed"))

        val recyclerView: RecyclerView = findViewById(R.id.imagesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = imagesAdapter


        imagesAdapter = ImagesAdapter { position ->
            selectedImagePosition = position
            openContextMenu(recyclerView)
        }
        recyclerView.adapter = imagesAdapter

        registerForContextMenu(recyclerView)
        if (!breed.isNullOrEmpty()) {
            Log.d("NAME OF BREED", breed)
            fetchImages(breed)
        } else {
            Log.d("NAME OF BREED", "BREED IS NULL")

            finish()
        }

        val loadMoreButton: Button = findViewById(R.id.loadMoreButton)
        loadMoreButton.setOnClickListener {
            displayImages()
        }
    }


    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.image_context_menu, menu)
    }


    private fun parseBreedName(breed: String?): String {
        return breed?.lowercase()?.replace(" ", "/") ?: ""
    }

    private fun fetchImages(breed: String) {
        RetrofitClient.instance.getBreedImages(breed).enqueue(object : Callback<BreedImagesResponse> {
            override fun onResponse(call: Call<BreedImagesResponse>, response: Response<BreedImagesResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    allImages = response.body()!!.message
                    displayImages() // Display the first batch of images
                } else {
                    Log.d("ImageAPI","no success image response")
                }
            }

            override fun onFailure(call: Call<BreedImagesResponse>, t: Throwable) {
                Log.d("onFailure", "Error, failed")
            }
        })
    }

    private fun displayImages() {
        val nextIndex = displayedImages.size + 6
        val nextImages = allImages.take(if (nextIndex < allImages.size) nextIndex else allImages.size)
        val newImages = nextImages.subList(displayedImages.size, nextImages.size)
        displayedImages.addAll(newImages)
        imagesAdapter?.updateData(displayedImages)

        val loadMoreButton: Button = findViewById(R.id.loadMoreButton)
        loadMoreButton.visibility = if (displayedImages.size == allImages.size) View.GONE else View.VISIBLE
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_to_favorites -> {
                // Handle adding to favorites
                addToFavorites(selectedImagePosition)
                return true
            }
            R.id.remove_from_favorites -> {
                // Handle removing from favorites
                removeFromFavorites(selectedImagePosition)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun addToFavorites(position: Int) {
        val imageUrl = displayedImages[position]
        FavoritesManager.addToFavorites(imageUrl)
        imagesAdapter?.notifyItemChanged(position)
    }

    private fun removeFromFavorites(position: Int) {
        val imageUrl = displayedImages[position]
        FavoritesManager.removeFromFavorites(imageUrl)
        imagesAdapter?.notifyItemChanged(position)
    }
//    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//        menuInflater.inflate(R.menu.breeds_item_menu, menu)
//    }
}