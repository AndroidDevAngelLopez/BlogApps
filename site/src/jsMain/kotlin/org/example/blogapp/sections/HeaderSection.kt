package org.example.blogapp.sections

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.components.icons.fa.FaXmark
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import kotlinx.browser.document
import org.example.blogapp.components.CategoryNavigationItems
import org.example.blogapp.components.SearchBar
import org.example.blogapp.models.Category
import org.example.blogapp.models.Theme
import org.example.blogapp.navigation.Screen
import org.example.blogapp.util.Constants.HEADER_HEIGHT
import org.example.blogapp.util.Constants.PAGE_WIDTH
import org.example.blogapp.util.Id
import org.example.blogapp.util.Res
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLInputElement

@Composable
fun HeaderSection(
    breakpoint: Breakpoint,
    selectedCategory: Category? = null,
    logo: String = Res.Image.logoHome,
    onMenuOpen: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().backgroundColor(Theme.Secondary.rgb), contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().backgroundColor(Theme.Secondary.rgb).maxWidth(PAGE_WIDTH.px),
            contentAlignment = Alignment.TopCenter
        ) {
            Header(
                breakpoint = breakpoint, logo = logo, selectedCategory = selectedCategory, onMenuOpen = onMenuOpen
            )
        }
    }
}

@Composable
fun Header(
    breakpoint: Breakpoint, logo: String, selectedCategory: Category?, onMenuOpen: () -> Unit
) {
    val context = rememberPageContext()
    var fullSearchBarOpened by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
            .height(HEADER_HEIGHT.px), verticalAlignment = Alignment.CenterVertically
    ) {
        if (breakpoint <= Breakpoint.MD) {
            if (fullSearchBarOpened) {
                FaXmark(
                    modifier = Modifier.margin(right = 24.px).color(Colors.White).cursor(Cursor.Pointer)
                        .onClick { fullSearchBarOpened = false }, size = IconSize.XL
                )
            }
            if (!fullSearchBarOpened) {
                FaBars(
                    modifier = Modifier.margin(right = 24.px).color(Colors.White).cursor(Cursor.Pointer)
                        .onClick { onMenuOpen() }, size = IconSize.XL
                )
            }
        }
        if (!fullSearchBarOpened) {
            Image(
                modifier = Modifier.margin(right = 50.px).width(if (breakpoint >= Breakpoint.SM) 100.px else 70.px)
                    .cursor(Cursor.Pointer).onClick { context.router.navigateTo(Screen.HomePage.route) },
                src = logo,
                desc = "Logo Image"
            )
        }
        if (breakpoint >= Breakpoint.LG) {
            CategoryNavigationItems(selectedCategory = selectedCategory)
        }
        Spacer()
        SearchBar(breakpoint = breakpoint, fullWidth = fullSearchBarOpened, darkTheme = true, onEnterClick = {
            val query = (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value
            context.router.navigateTo(Screen.SearchPage.searchByTitle(query = query))
        }, onSearchIconClick = { fullSearchBarOpened = it })
    }
}