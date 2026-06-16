@file:Suppress(
    "INVISIBLE_REFERENCE",
    "INVISIBLE_MEMBER",
    "OPT_IN_USAGE_ERROR",
    "ERROR_SUPPRESSION"
)

package com.sangtq.weatherappkmp.ui.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import org.jetbrains.compose.resources.ComposeEnvironment
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.LanguageQualifier
import org.jetbrains.compose.resources.LocalComposeEnvironment
import org.jetbrains.compose.resources.ResourceEnvironment

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    VIETNAMESE("vi", "Tiếng Việt");

    companion object {
        fun fromCode(code: String?): AppLanguage =
            entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
    }
}

val LocalAppLanguage = staticCompositionLocalOf { AppLanguage.ENGLISH }

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProvideAppLocale(language: AppLanguage, content: @Composable () -> Unit) {
    val parent = LocalComposeEnvironment.current
    val overridden = remember(language, parent) {
        object : ComposeEnvironment {
            @Composable
            override fun rememberEnvironment(): ResourceEnvironment {
                val base = parent.rememberEnvironment()
                return ResourceEnvironment(
                    language = LanguageQualifier(language.code),
                    region = base.region,
                    theme = base.theme,
                    density = base.density
                )
            }
        }
    }
    CompositionLocalProvider(
        LocalComposeEnvironment provides overridden,
        LocalAppLanguage provides language
    ) {
        key(language) { content() }
    }
}
