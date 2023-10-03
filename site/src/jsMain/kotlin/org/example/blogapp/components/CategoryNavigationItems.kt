package org.example.blogapp.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.style.toModifier
import org.example.blogapp.models.Category
import org.example.blogapp.models.Theme
import org.example.blogapp.navigation.Screen
import org.example.blogapp.styles.CategoryItemStyle
import org.example.blogapp.util.Constants
import org.jetbrains.compose.web.css.px


@Composable
fun CategoryNavigationItems(
    selectedCategory: Category? = null, vertical: Boolean = false
) {
    val context = rememberPageContext()
    Category.entries.forEach { category ->
        Link(
            modifier = CategoryItemStyle.toModifier().thenIf(
                    condition = vertical, other = Modifier.margin(bottom = 24.px)
                ).thenIf(
                    condition = !vertical, other = Modifier.margin(right = 24.px)
                ).thenIf(
                    condition = selectedCategory == category, other = Modifier.color(Theme.Primary.rgb)
                ).fontFamily(Constants.FONT_FAMILY).fontSize(16.px).fontWeight(FontWeight.Medium)
                .textDecorationLine(TextDecorationLine.None)
                .onClick { context.router.navigateTo(Screen.SearchPage.searchByCategory(category)) },
            path = "",
            text = category.name
        )
    }
}