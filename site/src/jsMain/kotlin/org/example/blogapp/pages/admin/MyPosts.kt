package org.example.blogapp.pages.admin

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.example.blogapp.Constants.POSTS_PER_PAGE
import org.example.blogapp.Constants.QUERY_PARAM
import org.example.blogapp.components.AdminPageLayout
import org.example.blogapp.components.PostsView
import org.example.blogapp.components.SearchBar
import org.example.blogapp.models.ApiListResponse
import org.example.blogapp.models.PostWithoutDetails
import org.example.blogapp.models.Theme
import org.example.blogapp.navigation.Screen
import org.example.blogapp.util.*
import org.example.blogapp.util.Constants.FONT_FAMILY
import org.example.blogapp.util.Constants.SIDE_PANEL_WIDTH
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.w3c.dom.HTMLInputElement

@Page
@Composable
fun MyPostsPage() {
    isUserLoggedIn {
        MyPostsScreen()
    }
}

@Composable
fun MyPostsScreen() {
    val context = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    val scope = rememberCoroutineScope()
    val myPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    val selectedPosts = remember { mutableStateListOf<String>() }

    var postsToSkip by remember { mutableStateOf(0) }
    var showMoreVisibility by remember { mutableStateOf(false) }
    var selectableMode by remember { mutableStateOf(false) }
    var switchText by remember { mutableStateOf("Select") }

    val hasParams = remember(key1 = context.route) { context.route.params.containsKey(QUERY_PARAM) }
    val query = remember(key1 = context.route) { context.route.params[QUERY_PARAM] ?: "" }

    LaunchedEffect(context.route) {
        postsToSkip = 0
        if (hasParams) {
            (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value = query.replace("%20", " ")
            searchPostsByTitle(query = query, skip = postsToSkip, onSuccess = {
                if (it is ApiListResponse.Success) {
                    myPosts.clear()
                    myPosts.addAll(it.data)
                    postsToSkip += POSTS_PER_PAGE
                    showMoreVisibility = it.data.size >= POSTS_PER_PAGE
                }
            }, onError = {
                println(it)
            })
        } else {
            fetchMyPosts(skip = postsToSkip, onSuccess = {
                if (it is ApiListResponse.Success) {
                    myPosts.clear()
                    myPosts.addAll(it.data)
                    postsToSkip += POSTS_PER_PAGE
                    showMoreVisibility = it.data.size >= POSTS_PER_PAGE
                }
            }, onError = {
                println(it)
            })
        }
    }

    AdminPageLayout {
        Column(
            modifier = Modifier.margin(topBottom = 50.px).fillMaxSize()
                .padding(left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(
                    if (breakpoint > Breakpoint.MD) 30.percent
                    else 50.percent
                ).margin(bottom = 24.px), contentAlignment = Alignment.Center
            ) {
                SearchBar(breakpoint = breakpoint,
                    modifier = Modifier.visibility(if (selectableMode) Visibility.Hidden else Visibility.Visible)
                        .transition(CSSTransition(property = TransitionProperty.All, duration = 200.ms)),
                    onEnterClick = {
                        val searchInput = (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value
                        if (searchInput.isNotEmpty()) {
                            context.router.navigateTo(Screen.AdminMyPosts.searchByTitle(query = searchInput))
                        } else {
                            context.router.navigateTo(Screen.AdminMyPosts.route)
                        }
                    },
                    onSearchIconClick = {})
            }
            Row(
                modifier = Modifier.fillMaxWidth(
                    if (breakpoint > Breakpoint.MD) 80.percent
                    else 90.percent
                ).margin(bottom = 24.px),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(modifier = Modifier.margin(right = 8.px),
                        size = SwitchSize.LG,
                        checked = selectableMode,
                        onCheckedChange = {
                            selectableMode = it
                            if (!selectableMode) {
                                switchText = "Select"
                                selectedPosts.clear()
                            } else {
                                switchText = "0 Posts Selected"
                            }
                        })
                    SpanText(
                        modifier = Modifier.color(if (selectableMode) Colors.Black else Theme.HalfBlack.rgb),
                        text = switchText
                    )
                }
                Button(attrs = Modifier.margin(right = 20.px).height(54.px).padding(leftRight = 24.px)
                    .backgroundColor(Theme.Red.rgb).color(Colors.White).noBorder().borderRadius(r = 4.px)
                    .fontFamily(FONT_FAMILY).fontSize(14.px).fontWeight(FontWeight.Medium)
                    .visibility(if (selectedPosts.isNotEmpty()) Visibility.Visible else Visibility.Hidden).onClick {
                        scope.launch {
                            val result = deleteSelectedPosts(ids = selectedPosts)
                            if (result) {
                                selectableMode = false
                                switchText = "Select"
                                postsToSkip -= selectedPosts.size
                                selectedPosts.forEach { deletedPostId ->
                                    myPosts.removeAll {
                                        it._id == deletedPostId
                                    }
                                }
                                selectedPosts.clear()
                            }
                        }
                    }.toAttrs()
                ) {
                    SpanText(text = "Delete")
                }
            }
            PostsView(breakpoint = breakpoint, posts = myPosts, selectableMode = selectableMode, onSelect = {
                selectedPosts.add(it)
                switchText = parseSwitchText(selectedPosts.toList())
            }, onDeselect = {
                selectedPosts.remove(it)
                switchText = parseSwitchText(selectedPosts.toList())
            }, showMoreVisibility = showMoreVisibility, onShowMore = {
                scope.launch {
                    if (hasParams) {
                        searchPostsByTitle(query = query, skip = postsToSkip, onSuccess = {
                            if (it is ApiListResponse.Success) {
                                if (it.data.isNotEmpty()) {
                                    myPosts.addAll(it.data)
                                    postsToSkip += POSTS_PER_PAGE
                                    showMoreVisibility = it.data.size >= POSTS_PER_PAGE
                                } else {
                                    showMoreVisibility = false
                                }
                            }
                        }, onError = {
                            println(it)
                        })
                    } else {
                        fetchMyPosts(skip = postsToSkip, onSuccess = {
                            if (it is ApiListResponse.Success) {
                                if (it.data.isNotEmpty()) {
                                    myPosts.addAll(it.data)
                                    postsToSkip += POSTS_PER_PAGE
                                    showMoreVisibility = it.data.size >= POSTS_PER_PAGE
                                } else {
                                    showMoreVisibility = false
                                }
                            }
                        }, onError = {
                            println(it)
                        })
                    }
                }
            }, onClick = { context.router.navigateTo(Screen.AdminCreate.passPostId(id = it)) })
        }
    }
}