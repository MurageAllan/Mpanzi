package com.sacco.org.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sacco.org.presentation.viewmodel.AccountViewModel
import com.sacco.org.presentation.viewmodel.AuthViewModel
import com.sacco.org.util.Resource

@Composable
fun DashboardScreen(
    authViewModel: AuthViewModel,
    viewModel: AccountViewModel
) {
    val signInResponse by authViewModel.userFlow.collectAsState(initial = null)
    val accountSummaryState by viewModel.accountSummaryState.collectAsState()
    var visible by remember { mutableStateOf(false) }


    LaunchedEffect(signInResponse) {
        signInResponse?.m_client_id?.let { mclientId ->
            viewModel.fetchClientAccountSummary(mclientId)
        }
        visible = true
    }

    Scaffold(
        containerColor = Color(0xFFF5F7FA),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            when (accountSummaryState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is Resource.Success -> {
                    val data = (accountSummaryState as Resource.Success).data.accountSummary
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(600)) +
                                slideInVertically(
                                    initialOffsetY = { it / 2 },
                                    animationSpec = tween(600, easing = FastOutSlowInEasing)
                                )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical =  16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(
                                modifier = Modifier.height(
                                    WindowInsets.statusBars.asPaddingValues()
                                        .calculateTopPadding() + 32.dp
                                )
                            )

                            Text(
                                text = "MPANZI SACCO",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                ),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            // Account Summary Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Account Summary", fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Column {
                                        AccountSummaryRow(
                                            "Savings",
                                            "KES ${
                                                data.savingsAccount.account_balance_derived.toDoubleOrNull()
                                                    ?.formatAsMoney() ?: "0.00"
                                            }"
                                        )
                                        HorizontalDivider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            thickness = 1.dp,
                                            color = Color(0xFFDDDDDD)
                                        )
                                        AccountSummaryRow(
                                            "Loan Principal",
                                            "KES ${
                                                data.principalAmount.principal_amount.toDoubleOrNull()
                                                    ?.formatAsMoney() ?: "0.00"
                                            }"
                                        )
                                        HorizontalDivider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            thickness = 1.dp,
                                            color = Color(0xFFDDDDDD)
                                        )
                                        AccountSummaryRow(
                                            "Approved Shares",
                                            data.totalApprovedShares?.let {
                                                "KES ${
                                                    it.toDoubleOrNull()?.formatAsMoney()
                                                }"
                                            } ?: "Not available")
                                    }


                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Deposit Balance")
                                    Text(
                                        "KSh 15,500",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Quick access buttons
                            ActionButtonsGrid()
                        }
                    }
                }

                is Resource.Error -> {
                    Text(
                        text = "Error: ${(accountSummaryState as Resource.Error).message}",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                null -> {
                    Text(
                        text = "Loading account summary...",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
@Composable
private fun ActionButtonsGrid() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // First row with 2 buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DashboardActionButton("Deposit", Icons.Default.AccountBalance)
            DashboardActionButton("Transfer", Icons.Default.SwapHoriz)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Second row with 2 buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DashboardActionButton("Apply Loan", Icons.Default.RequestQuote)
            DashboardActionButton("Pay Bills", Icons.Default.Receipt)
        }
    }
}

@Composable
fun DashboardActionButton(label: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .width(150.dp) // Fixed width for consistent sizing
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { /* Handle action */ }
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF388E3C),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}
@Composable
fun AccountSummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = value, fontWeight = FontWeight.SemiBold)
    }
}

// Helper extension function for money formatting
fun Double.formatAsMoney(): String {
    return "%,.2f".format(this)
}
