package org.givehim.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GivehimColors = lightColorScheme(primary = Color(0xFF225B3D), onPrimary = Color.White, secondary = Color(0xFFDCE96B), onSecondary = Color(0xFF17231C), background = Color(0xFFF5F2E9), onBackground = Color(0xFF17231C), surface = Color(0xFFFFFDF7), onSurface = Color(0xFF17231C), outline = Color(0xFFD8D5CA))

@Composable fun GivehimTheme(content: @Composable () -> Unit) = MaterialTheme(colorScheme = GivehimColors, content = content)
