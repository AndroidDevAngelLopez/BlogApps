package org.example.blogapp.pages.admin

import androidx.compose.runtime.*
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
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.blogapp.models.Theme
import org.example.blogapp.models.User
import org.example.blogapp.models.UserWithoutPassword
import org.example.blogapp.navigation.Screen
import org.example.blogapp.styles.LoginInputStyle
import org.example.blogapp.util.Constants.FONT_FAMILY
import org.example.blogapp.util.Id
import org.example.blogapp.util.Id.usernameInput
import org.example.blogapp.util.Res
import org.example.blogapp.util.checkUserExistence
import org.example.blogapp.util.noBorder
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.set

@Page
@Composable
fun LoginScreen() {
    val scope = rememberCoroutineScope()
    val context = rememberPageContext()
    var errorText by remember { mutableStateOf(" ") }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(leftRight = 50.px, top = 80.px, bottom = 24.px)
                .backgroundColor(Theme.LightGray.rgb),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.margin(bottom = 50.px).width(100.px), src = Res.Image.logo, desc = "Logo Image"
            )
            Input(type = InputType.Text,
                attrs = LoginInputStyle.toModifier().id(usernameInput).margin(bottom = 12.px).width(350.px)
                    .height(54.px).padding(leftRight = 20.px).backgroundColor(Colors.White).fontFamily(FONT_FAMILY)
                    .fontSize(14.px).outline(
                        width = 0.px, style = LineStyle.None, color = Colors.Transparent
                    ).toAttrs {
                        attr("placeholder", "Username")
                    })
            Input(type = InputType.Password,
                attrs = LoginInputStyle.toModifier().id(Id.passwordInput).margin(bottom = 20.px).width(350.px)
                    .height(54.px).padding(leftRight = 20.px).backgroundColor(Colors.White).fontFamily(FONT_FAMILY)
                    .fontSize(14.px).outline(
                        width = 0.px, style = LineStyle.None, color = Colors.Transparent
                    ).toAttrs {
                        attr("placeholder", "Password")
                    })
            Button(attrs = Modifier.margin(bottom = 24.px).width(350.px).height(54.px)
                .backgroundColor(Theme.Primary.rgb).color(Colors.White).borderRadius(r = 4.px).fontFamily(FONT_FAMILY)
                .fontWeight(FontWeight.Medium).fontSize(14.px).noBorder().cursor(Cursor.Pointer).onClick {
                    scope.launch {
                        val username = (document.getElementById(usernameInput) as HTMLInputElement).value
                        val password = (document.getElementById(Id.passwordInput) as HTMLInputElement).value
                        if (username.isNotEmpty() && password.isNotEmpty()) {
                            val user = checkUserExistence(
                                user = User(
                                    username = username, password = password
                                )
                            )
                            if (user != null) {
                                rememberLoggedIn(remember = true, user = user)
                                context.router.navigateTo(Screen.AdminHome.route)
                            } else {
                                errorText = "The user doesn't exist."
                                delay(3000)
                                errorText = " "
                            }
                        } else {
                            errorText = "Input fields are empty."
                            delay(3000)
                            errorText = " "
                        }
                    }
                }.toAttrs()) {
                SpanText(text = "Sign in")
            }
            SpanText(
                modifier = Modifier.width(350.px).color(Colors.Red).textAlign(TextAlign.Center).fontFamily(FONT_FAMILY),
                text = errorText
            )
        }
    }
}

private fun rememberLoggedIn(
    remember: Boolean, user: UserWithoutPassword? = null
) {
    localStorage["remember"] = remember.toString()
    if (user != null) {
        localStorage["userId"] = user._id
        localStorage["username"] = user.username
    }
}

