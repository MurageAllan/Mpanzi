package com.sacco.org.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sacco.org.data.api.model.HomeFeature
import com.sacco.org.presentation.navigation.Screen


@Composable
fun HomeScreen(navController: NavController,bottomNavController: NavController , userName: String = "Mary") {
    val featureItems = listOf(
        HomeFeature(
            "Mini Statements",
            Icons.Outlined.AccountBalanceWallet,
            Color(0xFF1B5E20),
            Screen.MiniStatementScreen.route
        ),
        HomeFeature(
            "Dashboard",
            Icons.Filled.Dashboard,
            Color(0xFF1B5E20),
            Screen.DashboardScreen.route
        ),
        HomeFeature(
            "Loan Application Status",
            Icons.Filled.Assignment,
            Color(0xFF1B5E20),
            Screen.LoanApplicationStatusScreen.route
        ),
        HomeFeature(
            "Repay Loan",
            Icons.Filled.MonetizationOn,
            Color(0xFF1B5E20),
            Screen.RepayLoanScreen.route
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE8F5E9), Color(0xFFF5F5F5)),
                    startY = 0f,
                    endY = 800f
                )
            )
            .padding(horizontal = 16.dp)
    ) {
        Spacer(
            modifier = Modifier.height(
                WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 24.dp
            )
        )

        Text(
            text = "Mpanzi SACCO",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome, $userName!",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(featureItems) { feature ->
                HomeFeatureCard(feature = feature) {
                    navController.navigate(feature.route)
                }
            }
        }
    }
}



