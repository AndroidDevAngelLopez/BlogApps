package org.example.blogapp.pages.posts

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.blogapp.Constants.POST_ID_PARAM
import org.example.blogapp.Constants.SHOW_SECTIONS_PARAM
import org.example.blogapp.components.CategoryNavigationItems
import org.example.blogapp.components.ErrorView
import org.example.blogapp.components.LoadingIndicator
import org.example.blogapp.components.OverFlowSidePanel
import org.example.blogapp.models.*
import org.example.blogapp.sections.FooterSection
import org.example.blogapp.sections.HeaderSection
import org.example.blogapp.util.*
import org.example.blogapp.util.Constants.FONT_FAMILY
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Page(routeOverride = "post")
@Composable
fun PostPage() {
    val scope = rememberCoroutineScope()
    val context = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    var overflowOpened by remember { mutableStateOf(false) }
    var showSections by remember { mutableStateOf(true) }
    var apiResponse by remember { mutableStateOf<ApiResponse>(ApiResponse.Idle) }
    val hasPostIdParam = remember(key1 = context.route) {
        context.route.params.containsKey(POST_ID_PARAM)
    }

    LaunchedEffect(key1 = context.route) {
        showSections = if (context.route.params.containsKey(SHOW_SECTIONS_PARAM)) {
            context.route.params.getValue(SHOW_SECTIONS_PARAM).toBoolean()
        } else true
        if (hasPostIdParam) {
            val postId = context.route.params.getValue(POST_ID_PARAM)
            apiResponse = fetchSelectedPost(id = postId)
        }
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
        if (showSections) {
            HeaderSection(breakpoint = breakpoint, logo = Res.Image.logo, onMenuOpen = { overflowOpened = true })
        }
        when (apiResponse) {
            is ApiResponse.Success -> {
                PostContent(
                    post = (apiResponse as ApiResponse.Success).data, breakpoint = breakpoint
                )
                scope.launch {
                    delay(50)
                    try {
                        js("hljs.highlightAll()") as Unit
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
            }

            is ApiResponse.Idle -> {
                LoadingIndicator()
            }

            is ApiResponse.Error -> {
                ErrorView(message = (apiResponse as ApiResponse.Error).message)
            }
        }
        if (showSections) {
            FooterSection()
        }
    }
}

@Composable
fun PostContent(
    post: Post, breakpoint: Breakpoint
) {
    LaunchedEffect(post) {
        (document.getElementById(Id.postContent) as HTMLDivElement).innerHTML = post.content
    }
    Column(
        modifier = Modifier.margin(top = 50.px, bottom = 200.px).padding(leftRight = 24.px).fillMaxWidth()
            .maxWidth(800.px), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpanText(
            modifier = Modifier.fillMaxWidth().color(Theme.HalfBlack.rgb).fontFamily(FONT_FAMILY).fontSize(14.px),
            text = post.date.toLong().parseDateString()
        )
        SpanText(
            modifier = Modifier.fillMaxWidth().margin(bottom = 20.px).color(Colors.Black).fontFamily(FONT_FAMILY)
                .fontSize(40.px).fontWeight(FontWeight.Bold).overflow(Overflow.Hidden)
                .textOverflow(TextOverflow.Ellipsis).styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "2")
                    property("line-clamp", "2")
                    property("-webkit-box-orient", "vertical")
                }, text = post.title
        )
        Image(
            modifier = Modifier.margin(bottom = 40.px).fillMaxWidth().objectFit(ObjectFit.Cover).height(
                    if (breakpoint <= Breakpoint.SM) 250.px
                    else if (breakpoint <= Breakpoint.MD) 400.px
                    else 600.px
                ),
            src = post.thumbnail,
        )
        Div(
            attrs = Modifier.id(Id.postContent).fontFamily(FONT_FAMILY).fillMaxWidth().toAttrs()
        )
    }
}