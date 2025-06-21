package com.moksh.kontext.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.moksh.kontext.R

val googleLogo: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.google_logo)

val sendIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.send_icon)

val rightArrowIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.right_arrow_icon)

val projectIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.project_icon)

val backArrowIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.back_arrow)