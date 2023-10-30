package org.example.blogapp.models

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val _id: String = "",
    val author: String = "",
    val date: Long = 0L,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val content: String,
    val category: Category,
    val popularApp: Boolean = false,
    val mainPost: Boolean = false,
    val inDevelopment: Boolean = false
)

@Serializable
data class PostWithoutDetails(
    val _id: String = "",
    val author: String,
    val date: Long,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val category: Category,
    val popularApp: Boolean = false,
    val mainPost: Boolean = false,
    val inDevelopment: Boolean = false
)