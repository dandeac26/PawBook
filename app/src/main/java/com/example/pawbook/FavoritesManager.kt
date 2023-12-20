package com.example.pawbook

object FavoritesManager {
    private val favoriteImages = mutableSetOf<String>()

    fun addToFavorites(imageUrl: String) {
        favoriteImages.add(imageUrl)
    }

    fun removeFromFavorites(imageUrl: String) {
        favoriteImages.remove(imageUrl)
    }

    fun isFavorite(imageUrl: String): Boolean {
        return imageUrl in favoriteImages
    }

    fun getFavorites(): Set<String> {
        return favoriteImages
    }
}