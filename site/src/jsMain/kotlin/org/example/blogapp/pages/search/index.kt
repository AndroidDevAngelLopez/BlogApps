package org.example.blogapp.pages.search

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.example.blogapp.Constants.CATEGORY_PARAM
import org.example.blogapp.Constants.POSTS_PER_PAGE
import org.example.blogapp.Constants.QUERY_PARAM
import org.example.blogapp.components.CategoryNavigationItems
import org.example.blogapp.components.LoadingIndicator
import org.example.blogapp.components.OverFlowSidePanel
import org.example.blogapp.models.ApiListResponse
import org.example.blogapp.models.Category
import org.example.blogapp.models.PostWithoutDetails
import org.example.blogapp.navigation.Screen
import org.example.blogapp.sections.FooterSection
import org.example.blogapp.sections.HeaderSection
import org.example.blogapp.sections.PostsSection
import org.example.blogapp.util.Constants.FONT_FAMILY
import org.example.blogapp.util.Id
import org.example.blogapp.util.Res
import org.example.blogapp.util.searchPostsByCategory
import org.example.blogapp.util.searchPostsByTitle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLInputElement

@Page(routeOverride = "query")
@Composable
fun SearchPage() {
    val breakpoint = rememberBreakpoint()
    val context = rememberPageContext()
    val scope = rememberCoroutineScope()

    var apiResponse by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    var overflowOpened by remember { mutableStateOf(false) }
    val searchedPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var postsToSkip by remember { mutableStateOf(0) }
    var showMorePosts by remember { mutableStateOf(false) }

    val hasCategoryParam = remember(key1 = context.route) {
        context.route.params.containsKey(CATEGORY_PARAM)
    }
    val hasQueryParam = remember(key1 = context.route) {
        context.route.params.containsKey(QUERY_PARAM)
    }
    val value = remember(key1 = context.route) {
        if (hasCategoryParam) {
            context.route.params.getValue(CATEGORY_PARAM)
        } else if (hasQueryParam) {
            context.route.params.getValue(QUERY_PARAM)
        } else {
            ""
        }
    }

    LaunchedEffect(key1 = context.route) {
        (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value = ""
        showMorePosts = false
        postsToSkip = 0
        if (hasCategoryParam) {
            searchPostsByCategory(category = runCatching { Category.valueOf(value) }.getOrElse { Category.Programming },
                skip = postsToSkip,
                onSuccess = { response ->
                    apiResponse = response
                    if (response is ApiListResponse.Success) {
                        searchedPosts.clear()
                        searchedPosts.addAll(response.data)
                        postsToSkip += POSTS_PER_PAGE
                        if (response.data.size >= POSTS_PER_PAGE) showMorePosts = true
                    }
                },
                onError = {})
        } else if (hasQueryParam) {
            (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value = value
            searchPostsByTitle(query = value, skip = postsToSkip, onSuccess = { response ->
                apiResponse = response
                if (response is ApiListResponse.Success) {
                    searchedPosts.clear()
                    searchedPosts.addAll(response.data)
                    postsToSkip += POSTS_PER_PAGE
                    if (response.data.size >= POSTS_PER_PAGE) showMorePosts = true
                }
            }, onError = {})
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overflowOpened) {
            OverFlowSidePanel(onMenuClose = { overflowOpened = false }, content = {
                CategoryNavigationItems(selectedCategory = if (hasCategoryParam) runCatching {
                    Category.valueOf(value)
                }.getOrElse { Category.Programming } else null, vertical = true)
            })
        }
        HeaderSection(breakpoint = breakpoint,
            selectedCategory = if (hasCategoryParam) runCatching {
                Category.valueOf(value)
            }.getOrElse { Category.Programming } else null,
            logo = Res.Image.logo,
            onMenuOpen = { overflowOpened = true })
        if (apiResponse is ApiListResponse.Success) {
            if (hasCategoryParam) {
                SpanText(modifier = Modifier.fillMaxWidth().textAlign(TextAlign.Center)
                    .margin(top = 100.px, bottom = 40.px).fontFamily(FONT_FAMILY).fontSize(36.px),
                    text = value.ifEmpty { Category.Programming.name })
            }
            PostsSection(breakpoint = breakpoint,
                posts = searchedPosts,
                showMoreVisibility = showMorePosts,
                onShowMore = {
                    scope.launch {
                        if (hasCategoryParam) {
                            searchPostsByCategory(category = runCatching { Category.valueOf(value) }.getOrElse { Category.Programming },
                                skip = postsToSkip,
                                onSuccess = { response ->
                                    if (response is ApiListResponse.Success) {
                                        if (response.data.isNotEmpty()) {
                                            if (response.data.size < POSTS_PER_PAGE) {
                                                showMorePosts = false
                                            }
                                            searchedPosts.addAll(response.data)
                                            postsToSkip += POSTS_PER_PAGE
                                        } else {
                                            showMorePosts = false
                                        }
                                    }
                                },
                                onError = {})
                        } else if (hasQueryParam) {
                            searchPostsByTitle(query = value, skip = postsToSkip, onSuccess = { response ->
                                if (response is ApiListResponse.Success) {
                                    if (response.data.isNotEmpty()) {
                                        if (response.data.size < POSTS_PER_PAGE) {
                                            showMorePosts = false
                                        }
                                        searchedPosts.addAll(response.data)
                                        postsToSkip += POSTS_PER_PAGE
                                    } else {
                                        showMorePosts = false
                                    }
                                }
                            }, onError = {})
                        }
                    }
                },
                onClick = { context.router.navigateTo(Screen.PostPage.getPost(id = it)) })
        } else {
            LoadingIndicator()
        }
        FooterSection()
    }
}