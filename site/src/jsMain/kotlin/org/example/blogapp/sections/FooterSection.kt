package org.example.blogapp.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.blogapp.models.Theme
import org.example.blogapp.navigation.Screen
import org.example.blogapp.util.Constants.FONT_FAMILY
import org.example.blogapp.util.Res
import org.jetbrains.compose.web.css.px

@Composable
fun FooterSection() {
    val context = rememberPageContext()
    Box(
        modifier = Modifier.fillMaxWidth().padding(topBottom = 50.px).backgroundColor(Theme.Secondary.rgb),
        contentAlignment = Alignment.Center
    ) {
        Row {
            SpanText(
                modifier = Modifier.fontFamily(FONT_FAMILY).fontSize(14.px).color(Colors.White),
                text = "Copyright © 2023 • "
            )
            SpanText(
                modifier = Modifier.fontFamily(FONT_FAMILY).fontSize(14.px).color(Colors.White),
                text = "AngelDroid"
            )
            Image(
                modifier = Modifier.margin(left = 5.px).width(20.px).cursor(Cursor.Pointer).onClick {
                        context.router.navigateTo("https://www.linkedin.com/in/%C3%A1ngel-l%C3%B3pez-947683281/")
                    }, src = Res.Image.linkedinLogo, desc = "Logo Image"
            )
            Image(
                modifier = Modifier.margin(left = 5.px).width(20.px).cursor(Cursor.Pointer).onClick {
                    context.router.navigateTo("https://github.com/AndroidDevAngelLopez/")
                }, src = Res.Image.githubLogo, desc = "Logo Image"
            )
        }
    }
}