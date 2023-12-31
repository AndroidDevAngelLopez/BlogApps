package org.example.blogapp.util

import com.varabyte.kobweb.browser.api
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.blogapp.Constants.AUTHOR_PARAM
import org.example.blogapp.Constants.CATEGORY_PARAM
import org.example.blogapp.Constants.POST_ID_PARAM
import org.example.blogapp.Constants.QUERY_PARAM
import org.example.blogapp.Constants.SKIP_PARAM
import org.example.blogapp.models.*
import org.w3c.dom.get

suspend fun checkUserExistence(user: User): UserWithoutPassword? {
    return try {
        window.api.tryPost(
            apiPath = "usercheck", body = Json.encodeToString(user).encodeToByteArray()
        )?.decodeToString().parseData()
    } catch (e: Exception) {
        println("CURRENT_USER")
        println(e.message)
        null
    }
}

suspend fun checkUserId(id: String): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "checkuserid", body = Json.encodeToString(id).encodeToByteArray()
        )?.decodeToString().parseData()
    } catch (e: Exception) {
        println(e.message.toString())
        false
    }
}

suspend fun addPost(post: Post): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "addpost", body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString().toBoolean()
    } catch (e: Exception) {
        println(e.message)
        false
    }
}

suspend fun updatePost(post: Post): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "updatepost", body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString().toBoolean()
    } catch (e: Exception) {
        println(e.message)
        false
    }
}

suspend fun fetchMyPosts(
    skip: Int, onSuccess: (ApiListResponse) -> Unit, onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "readmyposts?${SKIP_PARAM}=$skip&${AUTHOR_PARAM}=${localStorage["username"]}"
        )?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun fetchMainPosts(
    onSuccess: (ApiListResponse) -> Unit, onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(apiPath = "readmainposts")?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun fetchLatestPosts(
    skip: Int, onSuccess: (ApiListResponse) -> Unit, onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(apiPath = "readlatestposts?${SKIP_PARAM}=$skip")?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun fetchSponsoredPosts(
    onSuccess: (ApiListResponse) -> Unit, onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(apiPath = "readsponsoredposts")?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun fetchPopularPosts(
    skip: Int, onSuccess: (ApiListResponse) -> Unit, onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(apiPath = "readpopularposts?${SKIP_PARAM}=$skip")?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun deleteSelectedPosts(ids: List<String>): Boolean {
    return try {
        val result = window.api.tryPost(
            apiPath = "deleteselectedposts", body = Json.encodeToString(ids).encodeToByteArray()
        )?.decodeToString()
        result.toBoolean()
    } catch (e: Exception) {
        println(e.message)
        false
    }
}

suspend fun searchPostsByTitle(
    query: String, skip: Int, onSuccess: (ApiListResponse) -> Unit, onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "searchposts?${QUERY_PARAM}=$query&${SKIP_PARAM}=$skip"
        )?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e.message)
        onError(e)
    }
}

suspend fun searchPostsByCategory(
    category: Category, skip: Int, onSuccess: (ApiListResponse) -> Unit, onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "searchpostsbycategory?${CATEGORY_PARAM}=${category.name}&${SKIP_PARAM}=$skip"
        )?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e.message)
        onError(e)
    }
}

suspend fun fetchSelectedPost(id: String): ApiResponse {
    return try {
        val result = window.api.tryGet(
            apiPath = "readselectedpost?${POST_ID_PARAM}=$id"
        )?.decodeToString()
        result?.parseData() ?: ApiResponse.Error(message = "Result is null")
    } catch (e: Exception) {
        println(e)
        ApiResponse.Error(message = e.message.toString())
    }
}

suspend fun subscribeToNewsletter(newsletter: Newsletter): String {
    return window.api.tryPost(
        apiPath = "subscribe", body = Json.encodeToString(newsletter).encodeToByteArray()
    )?.decodeToString().toString().replace("\"", "")
}

inline fun <reified T> String?.parseData(): T {
    return Json.decodeFromString(this.toString())
}



