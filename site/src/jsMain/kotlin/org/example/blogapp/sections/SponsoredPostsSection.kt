package org.example.blogapp.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.icons.fa.FaCode
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.blogapp.components.PostPreview
import org.example.blogapp.models.PostWithoutDetails
import org.example.blogapp.models.Theme
import org.example.blogapp.util.Constants.FONT_FAMILY
import org.example.blogapp.util.Constants.PAGE_WIDTH
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun SponsoredPostsSection(
    breakpoint: Breakpoint, posts: List<PostWithoutDetails>, onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().margin(bottom = 100.px).backgroundColor(Theme.LightGray.rgb),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().maxWidth(PAGE_WIDTH.px).margin(topBottom = 50.px),
            contentAlignment = Alignment.TopCenter
        ) {
            SponsoredPosts(
                breakpoint = breakpoint, posts = posts, onClick = onClick
            )
        }
    }
}

@Composable
fun SponsoredPosts(
    breakpoint: Breakpoint, posts: List<PostWithoutDetails>, onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(
            if (breakpoint > Breakpoint.MD) 80.percent
            else 90.percent
        ), verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.margin(bottom = 30.px), verticalAlignment = Alignment.CenterVertically
        ) {
            FaCode(
                modifier = Modifier.margin(right = 10.px).color(Theme.Sponsored.rgb), size = IconSize.XL
            )
            SpanText(
                modifier = Modifier.fontFamily(FONT_FAMILY).fontSize(18.px).fontWeight(FontWeight.Medium)
                    .color(Theme.Sponsored.rgb), text = "In Development Apps"
            )
        }
        SimpleGrid(
            modifier = Modifier.fillMaxWidth(), numColumns = numColumns(base = 1, xl = 2)
        ) {
            posts.forEach { post ->
                PostPreview(
                    modifier = Modifier.margin(right = 50.px),
                    post = post,
                    vertical = breakpoint < Breakpoint.MD,
                    titleMaxLines = 1,
                    titleColor = Theme.Sponsored.rgb,
                    thumbnailHeight = if (breakpoint >= Breakpoint.MD) 200.px else 300.px,
                    onClick = onClick
                )
            }
        }
    }
}