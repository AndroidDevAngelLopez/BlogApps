package org.example.blogapp.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.coroutines.delay
import org.example.blogapp.Constants.UPDATED_PARAM
import org.example.blogapp.models.Theme
import org.example.blogapp.navigation.Screen
import org.example.blogapp.util.Constants.FONT_FAMILY
import org.example.blogapp.util.Res
import org.jetbrains.compose.web.css.px

@Page
@Composable
fun SuccessPage() {
    val context = rememberPageContext()
    val postUpdated = context.route.params.containsKey(UPDATED_PARAM)
    LaunchedEffect(Unit) {
        delay(5000)
        context.router.navigateTo(Screen.AdminCreate.route)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.margin(bottom = 24.px), src = Res.Icon.checkmark, desc = "Checkmark Icon"
        )
        SpanText(
            modifier = Modifier.fontFamily(FONT_FAMILY).fontSize(24.px),
            text = if (postUpdated) "Post Successfully Updated!" else "Post Successfully Created!"
        )
        SpanText(
            modifier = Modifier.color(Theme.HalfBlack.rgb).fontFamily(FONT_FAMILY).fontSize(18.px),
            text = "Redirecting you back..."
        )
    }
}