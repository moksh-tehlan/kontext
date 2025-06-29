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

val infoIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.info_icon)

val profileIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.profile_icon)

val billingIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.billing_icon)

val hapticFeedback: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.haptic_feedback_icon)

val logoutIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.logout_icon)

val deleteIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.delete_icon)

val openViewIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.open_view_logo)
