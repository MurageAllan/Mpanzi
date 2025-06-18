package com.sacco.org.data.api.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class HomeFeature(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)
