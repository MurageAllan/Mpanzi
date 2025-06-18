package com.sacco.org.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import com.sacco.org.data.api.model.BottomNavItem

val bottomNavItems = listOf(
    BottomNavItem(Screen.HomeScreen.route, Icons.Filled.Home, "Home"),
    BottomNavItem(Screen.DepositFundsScreen.route, Icons.Filled.AccountCircle, "Deposit"),
    BottomNavItem(Screen.ApplyLoanScreen.route, Icons.Filled.Money, "Loans"),
    BottomNavItem(Screen.EditProfileScreen.route, Icons.Filled.Person, "Profile")
)