package com.sacco.org.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sacco.org.data.api.AccountRetrofitInstance
import com.sacco.org.data.api.AuthRetrofitInstance
import com.sacco.org.data.api.LoanRetrofitInstance
import com.sacco.org.data.api.SavingsRetrofitInstance
import com.sacco.org.data.api.repository.AccountRepository
import com.sacco.org.data.api.repository.AuthRepository
import com.sacco.org.data.api.repository.LoanRepository
import com.sacco.org.data.api.repository.SavingsRepository
import com.sacco.org.data.local.DataStoreManager
import com.sacco.org.data.local.UserSessionManager
import com.sacco.org.presentation.screen.ChangePasswordScreen
import com.sacco.org.presentation.screen.DashboardScreen
import com.sacco.org.presentation.screen.EditProfileScreen
import com.sacco.org.presentation.screen.LoanApplicationStatusScreen
import com.sacco.org.presentation.screen.MainScreen
import com.sacco.org.presentation.screen.MiniStatementScreen
import com.sacco.org.presentation.screen.RepayLoanScreen
import com.sacco.org.presentation.screen.SignInScreen
import com.sacco.org.presentation.screen.SignUpScreen
import com.sacco.org.presentation.viewmodel.AccountViewModel
import com.sacco.org.presentation.viewmodel.AccountViewModelFactory
import com.sacco.org.presentation.viewmodel.AuthViewModel
import com.sacco.org.presentation.viewmodel.AuthViewModelFactory
import com.sacco.org.presentation.viewmodel.LoanViewModel
import com.sacco.org.presentation.viewmodel.LoanViewModelFactory
import com.sacco.org.presentation.viewmodel.SavingsViewModel
import com.sacco.org.presentation.viewmodel.SavingsViewModelFactory


@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val dataStoreManager = DataStoreManager(context)
    val userSessionManager = UserSessionManager(dataStoreManager)
    val authRepository = AuthRepository(AuthRetrofitInstance.api, dataStoreManager)
    val accRepository = AccountRepository(AccountRetrofitInstance.api)
    val loanRepository = LoanRepository(LoanRetrofitInstance.api)
    val savingsRepository = SavingsRepository(SavingsRetrofitInstance.api)

    // Initialize the ViewModel safely
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(userSessionManager, authRepository)
    )

    val accViewModel: AccountViewModel = viewModel(
        factory = AccountViewModelFactory(accRepository)
    )


    val loanViewModel: LoanViewModel = viewModel(
        factory = LoanViewModelFactory(loanRepository, dataStoreManager)
    )

    val savingsViewModel: SavingsViewModel = viewModel(
        factory = SavingsViewModelFactory(savingsRepository, dataStoreManager)
    )
    NavHost(
        navController = navController,
        startDestination = Screen.SignInScreen.route
    ) {
        composable(Screen.SignInScreen.route) {
            SignInScreen(
                viewModel = authViewModel,
                onSignUpRedirect = { navController.navigate(Screen.SignUpScreen.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.SignInScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.SignUpScreen.route) {
            SignUpScreen(
                viewModel = authViewModel,
                onSignUpSuccess = {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.SignInScreen.route) { inclusive = true }
                    }
                },
                onSignInRedirect = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.MainScreen.route) {

            MainScreen(
                mainNavController = navController,
                authViewModel = authViewModel,
                loanViewModel = loanViewModel,
                savingsViewModel = savingsViewModel
            )
        }

        composable(Screen.EditProfileScreen.route) {

            EditProfileScreen()
        }

        composable(Screen.RepayLoanScreen.route) {

            RepayLoanScreen(
                authViewModel = authViewModel,
                loanViewModel = loanViewModel
            )
        }

        composable(Screen.MiniStatementScreen.route) {

            MiniStatementScreen(authViewModel = authViewModel, viewModel = accViewModel)
        }

        composable(Screen.LoanApplicationStatusScreen.route) {

            LoanApplicationStatusScreen(loanViewModel = loanViewModel)
        }

        composable(Screen.ChangePasswordScreen.route) {

            ChangePasswordScreen()
        }

        composable(Screen.DashboardScreen.route) {

            DashboardScreen(authViewModel = authViewModel, viewModel = accViewModel)
        }

    }
}







