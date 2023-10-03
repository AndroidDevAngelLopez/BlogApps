package org.example.blogapp.components

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.blogapp.models.PostWithoutDetails
import org.example.blogapp.models.Theme
import org.example.blogapp.styles.MainPostPreviewStyle
import org.example.blogapp.styles.PostPreviewStyle
import org.example.blogapp.util.Constants.FONT_FAMILY
import org.example.blogapp.util.parseDateString
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.CheckboxInput

@Composable
fun PostPreview(
    modifier: Modifier = Modifier,
    post: PostWithoutDetails,
    selectableMode: Boolean = false,
    darkTheme: Boolean = false,
    vertical: Boolean = true,
    thumbnailHeight: CSSSizeValue<CSSUnit.px> = 320.px,
    titleMaxLines: Int = 2,
    titleColor: CSSColorValue = Colors.Black,
    onSelect: (String) -> Unit = {},
    onDeselect: (String) -> Unit = {},
    onClick: (String) -> Unit
) {
    var checked by remember(selectableMode) { mutableStateOf(false) }
    if (vertical) {
        Column(modifier = Modifier.thenIf(
            condition = post.main, other = MainPostPreviewStyle.toModifier()
        ).thenIf(
            condition = !post.main, other = PostPreviewStyle.toModifier()
        ).then(modifier).fillMaxWidth(
            if (darkTheme) 100.percent
            else if (titleColor == Theme.Sponsored.rgb) 100.percent
            else 95.percent
        ).margin(bottom = 24.px).padding(all = if (selectableMode) 10.px else 0.px).borderRadius(r = 4.px).border(
            width = if (selectableMode) 4.px else 0.px,
            style = if (selectableMode) LineStyle.Solid else LineStyle.None,
            color = if (checked) Theme.Primary.rgb else Theme.Gray.rgb
        ).onClick {
            if (selectableMode) {
                checked = !checked
                if (checked) {
                    onSelect(post._id)
                } else {
                    onDeselect(post._id)
                }
            } else {
                onClick(post._id)
            }
        }.transition(CSSTransition(property = TransitionProperty.All, duration = 200.ms)).cursor(Cursor.Pointer)
        ) {
            PostContent(
                post = post,
                selectableMode = selectableMode,
                darkTheme = darkTheme,
                vertical = vertical,
                thumbnailHeight = thumbnailHeight,
                titleMaxLines = titleMaxLines,
                titleColor = titleColor,
                checked = checked
            )
        }
    } else {
        Row(modifier = Modifier.thenIf(
            condition = post.main, other = MainPostPreviewStyle.toModifier()
        ).thenIf(
            condition = !post.main, other = PostPreviewStyle.toModifier()
        ).then(modifier).height(thumbnailHeight).onClick { onClick(post._id) }.cursor(Cursor.Pointer)
        ) {
            PostContent(
                post = post,
                selectableMode = selectableMode,
                darkTheme = darkTheme,
                vertical = vertical,
                thumbnailHeight = thumbnailHeight,
                titleMaxLines = titleMaxLines,
                titleColor = titleColor,
                checked = checked
            )
        }
    }
}

@Composable
fun PostContent(
    post: PostWithoutDetails,
    selectableMode: Boolean,
    darkTheme: Boolean,
    vertical: Boolean,
    thumbnailHeight: CSSSizeValue<CSSUnit.px>,
    titleMaxLines: Int,
    titleColor: CSSColorValue,
    checked: Boolean
) {
    Image(
        modifier = Modifier.margin(bottom = if (darkTheme) 20.px else 16.px).height(size = thumbnailHeight)
            .fillMaxWidth().objectFit(ObjectFit.Cover), src = post.thumbnail, desc = "Post Thumbnail Image"
    )
    Column(
        modifier = Modifier.thenIf(
            condition = !vertical, other = Modifier.margin(left = 20.px)
        ).padding(all = 12.px).fillMaxWidth()
    ) {
        SpanText(
            modifier = Modifier.fontFamily(FONT_FAMILY).fontSize(12.px)
                .color(if (darkTheme) Theme.HalfWhite.rgb else Theme.HalfBlack.rgb),
            text = post.date.toLong().parseDateString()
        )
        SpanText(
            modifier = Modifier.margin(bottom = 12.px).fontFamily(FONT_FAMILY).fontSize(20.px)
                .fontWeight(FontWeight.Bold).color(if (darkTheme) Colors.White else titleColor)
                .textOverflow(TextOverflow.Ellipsis).overflow(Overflow.Hidden).styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "$titleMaxLines")
                    property("line-clamp", "$titleMaxLines")
                    property("-webkit-box-orient", "vertical")
                }, text = post.title
        )
        SpanText(
            modifier = Modifier.margin(bottom = 8.px).fontFamily(FONT_FAMILY).fontSize(16.px)
                .color(if (darkTheme) Colors.White else Colors.Black).textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden).styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "3")
                    property("line-clamp", "3")
                    property("-webkit-box-orient", "vertical")
                }, text = post.subtitle
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryChip(category = post.category, darkTheme = darkTheme)
            if (selectableMode) {
                CheckboxInput(
                    checked = checked, attrs = Modifier.size(20.px).toAttrs()
                )
            }
        }
    }
}