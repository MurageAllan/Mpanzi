package com.sacco.org.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sacco.org.presentation.viewmodel.AccountViewModel
import com.sacco.org.presentation.viewmodel.AuthViewModel
import com.sacco.org.util.Resource


@Composable
fun MiniStatementScreen(
    authViewModel: AuthViewModel,
    viewModel: AccountViewModel
) {
    val signInResponse by authViewModel.userFlow.collectAsState(initial = null)
    val miniStatementsState by viewModel.miniStatementsState.collectAsState()
    var visible by remember { mutableStateOf(false) }


    LaunchedEffect(signInResponse) {
        signInResponse?.m_client_id?.let { mclientId ->
            viewModel.fetchSavingsAccountMiniStatements(mclientId)
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
            when (miniStatementsState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is Resource.Success -> {
                    val data = (miniStatementsState as Resource.Success).data.data
                    val transactions = data.recent_transactions.map {
                        Triple(
                            it.transaction_date,
                            when (it.transaction_type) {
                                1 -> "Deposit"
                                2 -> "Withdrawal"
                                else -> "Unknown"
                            },
                            it.amount.toDoubleOrNull() ?: 0.0
                        )
                    }

                    val balance = data.account_balance.toDoubleOrNull()

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(600)) +
                                slideInVertically(
                                    initialOffsetY = { it / 2 },
                                    animationSpec = tween(600, easing = FastOutSlowInEasing)
                                )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "MPANZI SACCO",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Mini Statements",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Gray
                                )
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Balance Card
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp)),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Savings Account", fontWeight = FontWeight.SemiBold)
                                    Text("BALANCE", color = Color.Gray, fontSize = 12.sp)
                                    Text(
                                        text = if (balance != null) "KES %, .2f".format(balance) else "KES 0.00",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B5E20),
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "TRANSACTIONS",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp)),
                                elevation = CardDefaults.cardElevation(4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                LazyColumn(modifier = Modifier.padding(8.dp)) {
                                    itemsIndexed(transactions) { index, (date, type, amount) ->
                                        TransactionRow(date, type, amount)
                                        if (index < transactions.lastIndex) {
                                            HorizontalDivider(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                thickness = 1.dp,
                                                color = Color(0xFFDDDDDD)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    Text(
                        text = "Error: ${(miniStatementsState as Resource.Error).message}",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                null -> {
                    Text(
                        text = "Loading statements...",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionRow(date: String, type: String, amount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = date, fontSize = 13.sp, color = Color.DarkGray)
            Text(text = type, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
        Text(
            text = formatAmount(amount),
            color = if (amount < 0) Color.Red else Color(0xFF1B5E20),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

fun formatAmount(amount: Double): String {
    return if (amount < 0) "- Ksh %.2f".format(-amount) else "Ksh %.2f".format(amount)
}

