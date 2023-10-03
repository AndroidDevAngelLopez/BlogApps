package org.example.blogapp.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.blogapp.models.Category
import org.example.blogapp.models.Theme
import org.example.blogapp.util.Constants.FONT_FAMILY
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px

@Composable
fun CategoryChip(
    category: Category,
    darkTheme: Boolean = false
) {
    Box(
        modifier = Modifier
            .height(32.px)
            .padding(leftRight = 14.px)
            .borderRadius(r = 100.px)
            .border(
                width = 1.px,
                style = LineStyle.Solid,
                color = if(darkTheme) Theme.entries.find { it.hex == category.color }?.rgb else Theme.HalfBlack.rgb
            ),
        contentAlignment = Alignment.Center
    ) {
        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(12.px)
                .color(
                    if (darkTheme) Theme.entries.find { it.hex == category.color }?.rgb ?: Theme.HalfBlack.rgb
                    else Theme.HalfBlack.rgb
                ),
            text = category.name
        )
    }
}