package org.example.blogapp.pages.admin

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaPlus
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.example.blogapp.components.AdminPageLayout
import org.example.blogapp.models.Theme
import org.example.blogapp.navigation.Screen
import org.example.blogapp.util.Constants.FONT_FAMILY
import org.example.blogapp.util.Constants.HEADER_HEIGHT
import org.example.blogapp.util.Constants.PAGE_WIDTH
import org.example.blogapp.util.Res
import org.example.blogapp.util.isUserLoggedIn
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh


@Page
@Composable
fun HomePage() {
    isUserLoggedIn {
        HomeScreen()
    }
}


@Composable
fun HomeScreen() {
    AdminPageLayout {
        HomeContent()
        AddButton()
    }
}

@Composable
fun HomeContent() {
    val breakpoint = rememberBreakpoint()
    Box(
        modifier = Modifier.fillMaxSize().padding(left = if (breakpoint > Breakpoint.MD) HEADER_HEIGHT.px else 0.px),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxSize().padding(topBottom = 50.px),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier.height(550.px).margin(bottom = 50.px),
                src = Res.Image.adminLogo,
                desc = "Laugh Image"
            )

            SpanText(
                modifier = Modifier.margin(bottom = 14.px).fillMaxWidth(40.percent).textAlign(TextAlign.Center)
                    .color(Theme.Secondary.rgb).fontFamily(FONT_FAMILY).fontSize(28.px).fontWeight(FontWeight.Bold),
                text = "Welcome back AngelDroid!"
            )
        }
    }
}

@Composable
fun AddButton() {
    val breakpoint = rememberBreakpoint()
    val context = rememberPageContext()
    Box(
        modifier = Modifier.height(100.vh).fillMaxWidth().maxWidth(PAGE_WIDTH.px).position(Position.Fixed)
            .styleModifier {
                property("pointer-events", "none")
            }, contentAlignment = Alignment.BottomEnd
    ) {
        Box(modifier = Modifier.margin(
            right = if (breakpoint > Breakpoint.MD) 40.px else 20.px,
            bottom = if (breakpoint > Breakpoint.MD) 40.px else 20.px
        ).backgroundColor(Theme.Primary.rgb).size(if (breakpoint > Breakpoint.MD) 80.px else 50.px)
            .borderRadius(r = 14.px).cursor(Cursor.Pointer).onClick {
                context.router.navigateTo(Screen.AdminCreate.route)
            }.styleModifier {
                property("pointer-events", "auto")
            }, contentAlignment = Alignment.Center
        ) {
            FaPlus(
                modifier = Modifier.color(Colors.White), size = IconSize.LG
            )
        }

    }
}