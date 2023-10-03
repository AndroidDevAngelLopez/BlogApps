package org.example.blogapp.components

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import org.example.blogapp.util.Constants
import org.jetbrains.compose.web.css.px

@Composable
fun AdminPageLayout(content: @Composable () -> Unit) {
    var overFlowMenuOpened by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().maxWidth(Constants.PAGE_WIDTH.px)
        ) {
            SidePanel(onMenuClick = { overFlowMenuOpened = true })
            if (overFlowMenuOpened) {
                OverFlowSidePanel(onMenuClose = { overFlowMenuOpened = false }){
                    NavigationItems()
                }
            }
            content()
        }
    }
}