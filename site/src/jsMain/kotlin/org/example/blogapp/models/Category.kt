package org.example.blogapp.models

import kotlinx.serialization.Serializable

@Serializable
actual enum class Category(val color :String) {
    Android(color = Theme.Green.hex),
    Spring(color = Theme.Yellow.hex),
    DataScience(color= Theme.Purple.hex)
}