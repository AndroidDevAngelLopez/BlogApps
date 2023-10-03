package org.example.blogapp.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch
import org.example.blogapp.Constants.POSTS_PER_PAGE
import org.example.blogapp.components.CategoryNavigationItems
import org.example.blogapp.components.OverFlowSidePanel
import org.example.blogapp.models.ApiListResponse
import org.example.blogapp.models.PostWithoutDetails
import org.example.blogapp.navigation.Screen
import org.example.blogapp.sections.*
import org.example.blogapp.util.fetchLatestPosts
import org.example.blogapp.util.fetchMainPosts
import org.example.blogapp.util.fetchPopularPosts
import org.example.blogapp.util.fetchSponsoredPosts

@Page
@Composable
fun HomePage() {
    val context = rememberPageContext()
    val scope = rememberCoroutineScope()
    val breakpoint = rememberBreakpoint()
    var overflowOpened by remember { mutableStateOf(false) }
    var mainPosts by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    val latestPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    val sponsoredPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    val popularPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var latestPostsToSkip by remember { mutableStateOf(0) }
    var popularPostsToSkip by remember { mutableStateOf(0) }
    var showMoreLatest by remember { mutableStateOf(false) }
    var showMorePopular by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fetchMainPosts(onSuccess = { mainPosts = it }, onError = {})
        fetchLatestPosts(skip = latestPostsToSkip, onSuccess = { response ->
            if (response is ApiListResponse.Success) {
                latestPosts.addAll(response.data)
                latestPostsToSkip += POSTS_PER_PAGE
                if (response.data.size >= POSTS_PER_PAGE) showMoreLatest = true
            }
        }, onError = {})
        fetchSponsoredPosts(onSuccess = { response ->
            if (response is ApiListResponse.Success) {
                sponsoredPosts.addAll(response.data)
            }
        }, onError = {})
        fetchPopularPosts(skip = popularPostsToSkip, onSuccess = { response ->
            if (response is ApiListResponse.Success) {
                popularPosts.addAll(response.data)
                popularPostsToSkip += POSTS_PER_PAGE
                if (response.data.size >= POSTS_PER_PAGE) showMorePopular = true
            }
        }, onError = {})
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overflowOpened) {
            OverFlowSidePanel(onMenuClose = { overflowOpened = false },
                content = { CategoryNavigationItems(vertical = true) })
        }
        HeaderSection(breakpoint = breakpoint, onMenuOpen = { overflowOpened = true })
        MainSection(breakpoint = breakpoint,
            posts = mainPosts,
            onClick = { context.router.navigateTo(Screen.PostPage.getPost(id = it)) })
        PostsSection(breakpoint = breakpoint,
            posts = latestPosts,
            title = "Latest Posts",
            showMoreVisibility = showMoreLatest,
            onShowMore = {
                scope.launch {
                    fetchLatestPosts(skip = latestPostsToSkip, onSuccess = { response ->
                        if (response is ApiListResponse.Success) {
                            if (response.data.isNotEmpty()) {
                                if (response.data.size < POSTS_PER_PAGE) {
                                    showMoreLatest = false
                                }
                                latestPosts.addAll(response.data)
                                latestPostsToSkip += POSTS_PER_PAGE
                            } else {
                                showMoreLatest = false
                            }
                        }
                    }, onError = {})
                }
            },
            onClick = { context.router.navigateTo(Screen.PostPage.getPost(id = it)) })
        SponsoredPostsSection(breakpoint = breakpoint,
            posts = sponsoredPosts,
            onClick = { context.router.navigateTo(Screen.PostPage.getPost(id = it)) })
        PostsSection(breakpoint = breakpoint,
            posts = popularPosts,
            title = "Popular Posts",
            showMoreVisibility = showMorePopular,
            onShowMore = {
                scope.launch {
                    fetchPopularPosts(skip = popularPostsToSkip, onSuccess = { response ->
                        if (response is ApiListResponse.Success) {
                            if (response.data.isNotEmpty()) {
                                if (response.data.size < POSTS_PER_PAGE) {
                                    showMorePopular = false
                                }
                                popularPosts.addAll(response.data)
                                popularPostsToSkip += POSTS_PER_PAGE
                            } else {
                                showMorePopular = false
                            }
                        }
                    }, onError = {})
                }
            },
            onClick = { context.router.navigateTo(Screen.PostPage.getPost(id = it)) })
        NewsletterSection(breakpoint = breakpoint)
        FooterSection()
    }
}