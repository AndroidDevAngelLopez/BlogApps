package org.example.blogapp.models

import kotlinx.serialization.Serializable

@Serializable
data class Newsletter(
    val _id: String = "",
    val email: String = ""
)