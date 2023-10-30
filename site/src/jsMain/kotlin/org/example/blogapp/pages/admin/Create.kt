package org.example.blogapp.pages.admin

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.file.loadDataUrlFromDisk
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.blogapp.Constants.POST_ID_PARAM
import org.example.blogapp.components.AdminPageLayout
import org.example.blogapp.components.ControlPopup
import org.example.blogapp.components.MessagePopup
import org.example.blogapp.models.*
import org.example.blogapp.navigation.Screen
import org.example.blogapp.styles.EditorKeyStyle
import org.example.blogapp.util.*
import org.example.blogapp.util.Constants.FONT_FAMILY
import org.example.blogapp.util.Constants.SIDE_PANEL_WIDTH
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.get
import kotlin.js.Date

data class CreatePageUiState(
    var id: String = "",
    var title: String = "",
    var subtitle: String = "",
    var thumbnail: String = "",
    var thumbnailInputDisabled: Boolean = true,
    var content: String = "",
    var category: Category = Category.Android,
    var buttonText: String = "Create",
    var popular: Boolean = false,
    var main: Boolean = false,
    var sponsored: Boolean = false,
    var editorVisibility: Boolean = true,
    var messagePopup: Boolean = false,
    var linkPopup: Boolean = false,
    var imagePopup: Boolean = false
) {
    fun reset() = this.copy(
        id = "",
        title = "",
        subtitle = "",
        thumbnail = "",
        content = "",
        category = Category.Android,
        buttonText = "Create",
        main = false,
        popular = false,
        sponsored = false,
        editorVisibility = true,
        messagePopup = false,
        linkPopup = false,
        imagePopup = false
    )
}

@Page
@Composable
fun CreatePage() {
    isUserLoggedIn {
        CreateScreen()
    }
}

@Composable
fun CreateScreen() {
    val scope = rememberCoroutineScope()
    val context = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    var uiState by remember { mutableStateOf(CreatePageUiState()) }

    val hasPostIdParam = remember(key1 = context.route) {
        context.route.params.containsKey(POST_ID_PARAM)
    }

    LaunchedEffect(hasPostIdParam) {
        if (hasPostIdParam) {
            val postId = context.route.params[POST_ID_PARAM] ?: ""
            val response = fetchSelectedPost(id = postId)
            if (response is ApiResponse.Success) {
                (document.getElementById(Id.editor) as HTMLTextAreaElement).value = response.data.content
                uiState = uiState.copy(
                    id = response.data._id,
                    title = response.data.title,
                    subtitle = response.data.subtitle,
                    content = response.data.content,
                    category = response.data.category,
                    thumbnail = response.data.thumbnail,
                    buttonText = "Update",
                    main = response.data.mainPost,
                    popular = response.data.popularApp,
                    sponsored = response.data.inDevelopment
                )
            }
        } else {
            (document.getElementById(Id.editor) as HTMLTextAreaElement).value = ""
            uiState = uiState.reset()
        }
    }

    AdminPageLayout {
        Box(
            modifier = Modifier.fillMaxSize().margin(topBottom = 50.px)
                .padding(left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxSize().maxWidth(700.px),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SimpleGrid(numColumns = numColumns(base = 1, sm = 3)) {
                    Row(
                        modifier = Modifier.margin(
                            right = 24.px, bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                        ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier.margin(right = 8.px),
                            checked = uiState.popular,
                            onCheckedChange = { uiState = uiState.copy(popular = it) },
                            size = SwitchSize.LG
                        )
                        SpanText(
                            modifier = Modifier.fontSize(14.px).fontFamily(FONT_FAMILY).color(Theme.HalfBlack.rgb),
                            text = "Popular App"
                        )
                    }
                    Row(
                        modifier = Modifier.margin(
                            right = 24.px, bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                        ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier.margin(right = 8.px),
                            checked = uiState.main,
                            onCheckedChange = { uiState = uiState.copy(main = it) },
                            size = SwitchSize.LG
                        )
                        SpanText(
                            modifier = Modifier.fontSize(14.px).fontFamily(FONT_FAMILY).color(Theme.HalfBlack.rgb),
                            text = "Main Post"
                        )
                    }
                    Row(
                        modifier = Modifier.margin(bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier.margin(right = 8.px),
                            checked = uiState.sponsored,
                            onCheckedChange = { uiState = uiState.copy(sponsored = it) },
                            size = SwitchSize.LG
                        )
                        SpanText(
                            modifier = Modifier.fontSize(14.px).fontFamily(FONT_FAMILY).color(Theme.HalfBlack.rgb),
                            text = "In Development App"
                        )
                    }
                }
                Input(type = InputType.Text,
                    attrs = Modifier.id(Id.titleInput).fillMaxWidth().height(54.px).margin(topBottom = 12.px)
                        .padding(leftRight = 20.px).backgroundColor(Theme.LightGray.rgb).borderRadius(r = 4.px)
                        .noBorder().fontFamily(FONT_FAMILY).fontSize(16.px).toAttrs {
                            attr("placeholder", "Title")
                            attr("value", uiState.title)
                        })
                Input(type = InputType.Text,
                    attrs = Modifier.id(Id.subtitleInput).fillMaxWidth().height(54.px).padding(leftRight = 20.px)
                        .backgroundColor(Theme.LightGray.rgb).borderRadius(r = 4.px).noBorder().fontFamily(FONT_FAMILY)
                        .fontSize(16.px).toAttrs {
                            attr("placeholder", "Subtitle")
                            attr("value", uiState.subtitle)
                        })
                CategoryDropdown(
                    selectedCategory = uiState.category,
                    onCategorySelect = { uiState = uiState.copy(category = it) })
                Row(
                    modifier = Modifier.fillMaxWidth().margin(topBottom = 12.px),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        modifier = Modifier.margin(right = 8.px),
                        checked = !uiState.thumbnailInputDisabled,
                        onCheckedChange = { uiState = uiState.copy(thumbnailInputDisabled = !it) },
                        size = SwitchSize.MD
                    )
                    SpanText(
                        modifier = Modifier.fontSize(14.px).fontFamily(FONT_FAMILY).color(Theme.HalfBlack.rgb),
                        text = "Paste an Image URL instead"
                    )
                }
                ThumbnailUploader(thumbnail = uiState.thumbnail,
                    thumbnailInputDisabled = uiState.thumbnailInputDisabled,
                    onThumbnailSelect = { filename, file ->
                        (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value = filename
                        uiState = uiState.copy(thumbnail = file)
                    })
                EditorControls(breakpoint = breakpoint,
                    editorVisibility = uiState.editorVisibility,
                    onEditorVisibilityChange = {
                        uiState = uiState.copy(
                            editorVisibility = !uiState.editorVisibility
                        )
                    },
                    onLinkClick = {
                        uiState = uiState.copy(linkPopup = true)
                    },
                    onImageClick = {
                        uiState = uiState.copy(imagePopup = true)
                    })
                Editor(editorVisibility = uiState.editorVisibility)
                CreateButton(text = uiState.buttonText, onClick = {
                    uiState = uiState.copy(title = (document.getElementById(Id.titleInput) as HTMLInputElement).value)
                    uiState =
                        uiState.copy(subtitle = (document.getElementById(Id.subtitleInput) as HTMLInputElement).value)
                    uiState = uiState.copy(content = (document.getElementById(Id.editor) as HTMLTextAreaElement).value)
                    if (!uiState.thumbnailInputDisabled) {
                        uiState =
                            uiState.copy(thumbnail = (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value)
                    }
                    if (uiState.title.isNotEmpty() && uiState.subtitle.isNotEmpty() && uiState.thumbnail.isNotEmpty() && uiState.content.isNotEmpty()) {
                        scope.launch {
                            if (hasPostIdParam) {
                                val result = updatePost(
                                    Post(
                                        _id = uiState.id,
                                        title = uiState.title,
                                        subtitle = uiState.subtitle,
                                        thumbnail = uiState.thumbnail,
                                        content = uiState.content,
                                        category = uiState.category,
                                        popularApp = uiState.popular,
                                        mainPost = uiState.main,
                                        inDevelopment = uiState.sponsored
                                    )
                                )
                                if (result) {
                                    context.router.navigateTo(Screen.AdminSuccess.postUpdated())
                                }
                            } else {
                                val result = addPost(
                                    Post(
                                        author = localStorage["username"].toString(),
                                        title = uiState.title,
                                        subtitle = uiState.subtitle,
                                        date = Date.now().toLong(),
                                        thumbnail = uiState.thumbnail,
                                        content = uiState.content,
                                        category = uiState.category,
                                        popularApp = uiState.popular,
                                        mainPost = uiState.main,
                                        inDevelopment = uiState.sponsored
                                    )
                                )
                                if (result) {
                                    context.router.navigateTo(Screen.AdminSuccess.route)
                                }
                            }
                        }
                    } else {
                        scope.launch {
                            uiState = uiState.copy(messagePopup = true)
                            delay(2000)
                            uiState = uiState.copy(messagePopup = false)
                        }
                    }
                })
            }
        }
    }
    if (uiState.messagePopup) {
        MessagePopup(message = "Please fill out all fields.",
            onDialogDismiss = { uiState = uiState.copy(messagePopup = false) })
    }
    if (uiState.linkPopup) {
        ControlPopup(editorControl = EditorControl.Link,
            onDialogDismiss = { uiState = uiState.copy(linkPopup = false) },
            onAddClick = { href, title ->
                applyStyle(
                    ControlStyle.Link(
                        selectedText = getSelectedText(), href = href, title = title
                    )
                )
            })
    }
    if (uiState.imagePopup) {
        ControlPopup(editorControl = EditorControl.Image,
            onDialogDismiss = { uiState = uiState.copy(imagePopup = false) },
            onAddClick = { imageUrl, description ->
                applyStyle(
                    ControlStyle.Image(
                        selectedText = getSelectedText(), imageUrl = imageUrl, desc = description
                    )
                )
            })
    }
}

@Composable
fun CategoryDropdown(
    selectedCategory: Category, onCategorySelect: (Category) -> Unit
) {
    Box(modifier = Modifier.margin(topBottom = 12.px).classNames("dropdown").fillMaxWidth().height(54.px)
        .backgroundColor(Theme.LightGray.rgb).cursor(Cursor.Pointer).attrsModifier {
            attr("data-bs-toggle", "dropdown")
        }) {
        Row(
            modifier = Modifier.fillMaxSize().padding(leftRight = 20.px),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SpanText(
                modifier = Modifier.fillMaxWidth().fontSize(16.px).fontFamily(FONT_FAMILY), text = selectedCategory.name
            )
            Box(modifier = Modifier.classNames("dropdown-toggle"))
        }
        Ul(
            attrs = Modifier.fillMaxWidth().classNames("dropdown-menu").toAttrs()
        ) {
            Category.entries.forEach { category ->
                Li {
                    A(
                        attrs = Modifier.classNames("dropdown-item").color(Colors.Black).fontFamily(FONT_FAMILY)
                            .fontSize(16.px).onClick { onCategorySelect(category) }.toAttrs()
                    ) {
                        Text(value = category.name)
                    }
                }
            }
        }
    }
}

@Composable
fun ThumbnailUploader(
    thumbnail: String, thumbnailInputDisabled: Boolean, onThumbnailSelect: (String, String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().margin(bottom = 20.px).height(54.px)
    ) {
        Input(type = InputType.Text,
            attrs = Modifier.id(Id.thumbnailInput).fillMaxSize().margin(right = 12.px).padding(leftRight = 20.px)
                .backgroundColor(Theme.LightGray.rgb).borderRadius(r = 4.px).noBorder().fontFamily(FONT_FAMILY)
                .fontSize(16.px).thenIf(
                    condition = thumbnailInputDisabled, other = Modifier.disabled()
                ).toAttrs {
                    attr("placeholder", "Thumbnail")
                    attr("value", thumbnail)
                })
        Button(
            attrs = Modifier.onClick {
                document.loadDataUrlFromDisk(accept = "image/png, image/jpeg", onLoaded = {
                    onThumbnailSelect(filename, it)
                })
            }.fillMaxHeight().padding(leftRight = 24.px)
                .backgroundColor(if (!thumbnailInputDisabled) Theme.Gray.rgb else Theme.Primary.rgb)
                .color(if (!thumbnailInputDisabled) Theme.DarkGray.rgb else Colors.White).borderRadius(r = 4.px)
                .noBorder().fontFamily(FONT_FAMILY).fontWeight(FontWeight.Medium).fontSize(14.px).thenIf(
                    condition = !thumbnailInputDisabled, other = Modifier.disabled()
                ).toAttrs()
        ) {
            SpanText(text = "Upload")
        }
    }
}

@Composable
fun EditorControls(
    breakpoint: Breakpoint,
    editorVisibility: Boolean,
    onLinkClick: () -> Unit,
    onImageClick: () -> Unit,
    onEditorVisibilityChange: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        SimpleGrid(
            modifier = Modifier.fillMaxWidth(), numColumns = numColumns(base = 1, sm = 2)
        ) {
            Row(
                modifier = Modifier.backgroundColor(Theme.LightGray.rgb).borderRadius(r = 4.px).height(54.px)
            ) {
                EditorControl.entries.forEach {
                    EditorControlView(control = it, onClick = {
                        applyControlStyle(
                            editorControl = it, onLinkClick = onLinkClick, onImageClick = onImageClick
                        )
                    })
                }
            }
            Box(contentAlignment = Alignment.CenterEnd) {
                Button(
                    attrs = Modifier.height(54.px).thenIf(
                        condition = breakpoint < Breakpoint.SM, other = Modifier.fillMaxWidth()
                    ).margin(topBottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px).padding(leftRight = 24.px)
                        .borderRadius(r = 4.px).backgroundColor(
                            if (editorVisibility) Theme.LightGray.rgb
                            else Theme.Primary.rgb
                        ).color(
                            if (editorVisibility) Theme.DarkGray.rgb
                            else Colors.White
                        ).noBorder().onClick {
                            onEditorVisibilityChange()
                            document.getElementById(Id.editorPreview)?.innerHTML = getEditor().value
                            js("hljs.highlightAll()") as Unit
                        }.toAttrs()
                ) {
                    SpanText(
                        modifier = Modifier.fontFamily(FONT_FAMILY).fontWeight(FontWeight.Medium).fontSize(14.px),
                        text = "Preview"
                    )
                }
            }
        }
    }
}

@Composable
fun EditorControlView(
    control: EditorControl, onClick: () -> Unit
) {
    Box(
        modifier = EditorKeyStyle.toModifier().fillMaxHeight().padding(leftRight = 12.px).borderRadius(r = 4.px)
            .cursor(Cursor.Pointer).onClick { onClick() }, contentAlignment = Alignment.Center
    ) {
        Image(
            src = control.icon, desc = "${control.name} Icon"
        )
    }
}

@Composable
fun Editor(editorVisibility: Boolean) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TextArea(attrs = Modifier.id(Id.editor).fillMaxWidth().height(400.px).maxHeight(400.px).resize(Resize.None)
            .margin(top = 8.px).padding(all = 20.px).backgroundColor(Theme.LightGray.rgb).borderRadius(r = 4.px)
            .noBorder().visibility(
                if (editorVisibility) Visibility.Visible
                else Visibility.Hidden
            ).onKeyDown {
                if (it.code == "Enter" && it.shiftKey) {
                    applyStyle(
                        controlStyle = ControlStyle.Break(
                            selectedText = getSelectedText()
                        )
                    )
                }
            }.fontFamily(FONT_FAMILY).fontSize(16.px).toAttrs {
                attr("placeholder", "Type here...")
            })
        Div(
            attrs = Modifier.id(Id.editorPreview).fillMaxWidth().height(400.px).maxHeight(400.px).margin(top = 8.px)
                .padding(all = 20.px).backgroundColor(Theme.LightGray.rgb).borderRadius(r = 4.px).visibility(
                    if (editorVisibility) Visibility.Hidden
                    else Visibility.Visible
                ).overflow(Overflow.Auto).scrollBehavior(ScrollBehavior.Smooth).noBorder().toAttrs()
        )
    }
}

@Composable
fun CreateButton(
    text: String, onClick: () -> Unit
) {
    Button(
        attrs = Modifier.onClick { onClick() }.fillMaxWidth().height(54.px).margin(top = 24.px)
            .backgroundColor(Theme.Primary.rgb).color(Colors.White).borderRadius(r = 4.px).noBorder()
            .fontFamily(FONT_FAMILY).fontSize(16.px).toAttrs()
    ) {
        SpanText(text = text)
    }
}