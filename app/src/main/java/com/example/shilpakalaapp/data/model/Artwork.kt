package com.example.shilpakalaapp.data.model

data class Artwork(

    val id: String = "",

    val title: String = "",

    val description: String = "",

    val category: String = "",

    val artist: String = "",

    val imageUrl: String = "",

    val timestamp: Long = System.currentTimeMillis()
)