package org.example.blogapp.sections

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.blogapp.components.MessagePopup
import org.example.blogapp.models.Newsletter
import org.example.blogapp.models.Theme
import org.example.blogapp.styles.NewsletterInputStyle
import org.example.blogapp.util.Constants.FONT_FAMILY
import org.example.blogapp.util.Constants.PAGE_WIDTH
import org.example.blogapp.util.Id
import org.example.blogapp.util.noBorder
import org.example.blogapp.util.subscribeToNewsletter
import org.example.blogapp.util.validateEmail
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement

@Composable
fun NewsletterSection(breakpoint: Breakpoint) {
    val scope = rememberCoroutineScope()
    var responseMessage by remember { mutableStateOf("") }
    var invalidEmailPopup by remember { mutableStateOf(false) }
    var subscribedPopup by remember { mutableStateOf(false) }

    if (invalidEmailPopup) {
        MessagePopup(message = "Email Address is not valid.", onDialogDismiss = {
            invalidEmailPopup = false
        })
    }
    if (subscribedPopup) {
        MessagePopup(message = responseMessage, onDialogDismiss = {
            subscribedPopup = false
        })
    }

    Box(
        modifier = Modifier.margin(topBottom = 250.px).fillMaxWidth().maxWidth(PAGE_WIDTH.px)
    ) {
        NewsletterContent(breakpoint = breakpoint, onSubscribed = {
            responseMessage = it
            scope.launch {
                subscribedPopup = true
                delay(2000)
                subscribedPopup = false
            }
        }, onInvalidEmail = {
            scope.launch {
                invalidEmailPopup = true
                delay(2000)
                invalidEmailPopup = false
            }
        })
    }
}

@Composable
fun NewsletterContent(
    breakpoint: Breakpoint,
    onSubscribed: (String) -> Unit,
    onInvalidEmail: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SpanText(
            modifier = Modifier.fillMaxWidth().fontFamily(FONT_FAMILY).fontSize(36.px).fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Center), text = "Leave me your email"
        )
        SpanText(
            modifier = Modifier.fillMaxWidth().fontFamily(FONT_FAMILY).fontSize(36.px).fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Center), text = "and i will contact you"
        )
        SpanText(
            modifier = Modifier.margin(top = 6.px).fillMaxWidth().fontFamily(FONT_FAMILY).fontSize(18.px)
                .fontWeight(FontWeight.Normal).color(Theme.HalfBlack.rgb).textAlign(TextAlign.Center),
            text = "Let's build amazing apps together!"
        )
        if (breakpoint > Breakpoint.SM) {
            Row(
                modifier = Modifier.fillMaxWidth().margin(top = 40.px),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubscriptionForm(
                    vertical = false, onSubscribed = onSubscribed, onInvalidEmail = onInvalidEmail
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth().margin(top = 40.px),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SubscriptionForm(
                    vertical = true, onSubscribed = onSubscribed, onInvalidEmail = onInvalidEmail
                )
            }
        }
    }
}

@Composable
fun SubscriptionForm(
    vertical: Boolean,
    onSubscribed: (String) -> Unit,
    onInvalidEmail: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Input(type = InputType.Text,
        attrs = NewsletterInputStyle.toModifier().id(Id.emailInput).width(320.px).height(54.px)
            .color(Theme.DarkGray.rgb).backgroundColor(Theme.Gray.rgb).padding(leftRight = 24.px).margin(
                right = if (vertical) 0.px else 20.px, bottom = if (vertical) 20.px else 0.px
            ).fontFamily(FONT_FAMILY).fontSize(16.px).borderRadius(100.px).toAttrs {
                attr("placeholder", "Your Email")
            })
    Button(attrs = Modifier.onClick {
        val email = (document.getElementById(Id.emailInput) as HTMLInputElement).value
        if (validateEmail(email = email)) {
            scope.launch {
                onSubscribed(
                    subscribeToNewsletter(
                        newsletter = Newsletter(email = email)
                    )
                )
            }
        } else {
            onInvalidEmail()
        }
    }.height(54.px).backgroundColor(Theme.Primary.rgb).borderRadius(100.px).padding(leftRight = 50.px).noBorder()
        .cursor(Cursor.Pointer).toAttrs()
    ) {
        SpanText(
            modifier = Modifier.fontFamily(FONT_FAMILY).fontSize(18.px).fontWeight(FontWeight.Medium)
                .color(Colors.White), text = "Send"
        )
    }
}