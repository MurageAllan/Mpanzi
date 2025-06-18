package com.sacco.org.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sacco.org.presentation.navigation.Screen
import com.sacco.org.presentation.viewmodel.AuthViewModel
import com.sacco.org.presentation.viewmodel.LoanViewModel
import com.sacco.org.presentation.viewmodel.SavingsViewModel


@Composable
fun MainScreen(
    mainNavController: NavController, authViewModel: AuthViewModel = viewModel(),
    loanViewModel: LoanViewModel = viewModel(), savingsViewModel: SavingsViewModel = viewModel()
) {

    // This is the local controller for bottom nav only
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.HomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.HomeScreen.route) {
                HomeScreen(
                    navController = mainNavController,
                    bottomNavController = bottomNavController
                )
            }
            composable(Screen.ApplyLoanScreen.route) {
                ApplyLoanScreen(
                    navController = bottomNavController, authViewModel = authViewModel,
                    loanViewModel = loanViewModel
                )
            }
            composable(Screen.DepositFundsScreen.route) {
                DepositFundsScreen(
                    navController = bottomNavController, authViewModel = authViewModel,
                    savingsViewModel = savingsViewModel
                )
            }
            composable(Screen.EditProfileScreen.route) {
                EditProfileScreen()
            }
        }
    }
}