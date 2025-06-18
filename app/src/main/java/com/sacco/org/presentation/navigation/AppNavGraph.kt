package com.sacco.org.presentation.navigation

sealed class Screen(val route: String) {
    object SignInScreen : Screen("sign_in")
    object SignUpScreen : Screen("sign_up")
    object RepayLoanScreen : Screen("repay_loan")
    object MiniStatementScreen : Screen("mini_statement")
    object MainScreen : Screen("main")
    object LoanApplicationStatusScreen : Screen("loan_application_status")
    object HomeScreen : Screen("home")
    object EditProfileScreen : Screen("edit_profile")
    object DepositFundsScreen : Screen("deposit_funds")
    object DashboardScreen : Screen("dash_board")
    object ChangePasswordScreen : Screen("change_password")
    object ApplyLoanScreen : Screen("apply_loan")
}