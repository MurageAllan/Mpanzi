package com.sacco.org.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sacco.org.presentation.viewmodel.AuthViewModel
import com.sacco.org.presentation.viewmodel.LoanUiState
import com.sacco.org.presentation.viewmodel.LoanViewModel
import kotlinx.coroutines.delay


@Composable
fun RepayLoanScreen(
    authViewModel: AuthViewModel,
    loanViewModel: LoanViewModel
) {
    val paymentMethods = listOf("M-Pesa", "Bank Transfer", "Card")
    var selectedMethod by remember { mutableStateOf(paymentMethods.first()) }
    var amountToRepay by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val signInResponse by authViewModel.userFlow.collectAsState(initial = null)
    val clientId = signInResponse?.m_client_id ?: 0

    // Collect loan repayment state
    val repaymentState by loanViewModel.repayLoanState.collectAsState()

    // Collect loan details (you'll need to add this to your ViewModel)
//    val loanDetails by loanViewModel.loanDetails.collectAsState()
//    val outstandingBalance = loanDetails?.principal_completed_derived?.toDoubleOrNull() ?: 0.0

    // Handle repayment result
    LaunchedEffect(repaymentState) {
        when (repaymentState) {
            is LoanUiState.Success -> {
                snackbarMessage = "Loan repayment submitted successfully!"
                showSnackbar = true
                // Reset form after successful submission
                amountToRepay = ""
                // Optional: Navigate back after delay
                // delay(2000)
                // navController.popBackStack()
            }

            is LoanUiState.Error -> {
                snackbarMessage =
                    (repaymentState as LoanUiState.Error).message
                        ?: "Failed to submit loan repayment"
                showSnackbar = true
            }

            else -> {}
        }
    }

    // Auto-dismiss snackbar after 3 seconds
    if (showSnackbar) {
        LaunchedEffect(showSnackbar) {
            delay(3000)
            showSnackbar = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(
                modifier = Modifier.height(
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp
                )
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MPANZI SACCO",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Repay Loan",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (repaymentState is LoanUiState.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Outstanding Balance
                    Text("Outstanding Balance", fontWeight = FontWeight.SemiBold)
                    Text(
//                        text = "KES ${"%.2f".format(outstandingBalance)}",
                        text = "KES",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Amount to Repay
                    Text("Amount to Repay", fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = amountToRepay,
                        onValueChange = { amountToRepay = it },
                        placeholder = { Text("KES 5,000.00") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Payment Method
                    Text("Payment Method", fontWeight = FontWeight.SemiBold)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Black
                            )
                        ) {
                            Text(selectedMethod)
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            paymentMethods.forEach { method ->
                                DropdownMenuItem(
                                    text = { Text(method) },
                                    onClick = {
                                        selectedMethod = method
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Confirm & Pay Button
                    Button(
                        onClick = {
                            if (clientId != 0) {
                                val amount = amountToRepay.toDoubleOrNull()
                                if (amount != null && amount > 0) {
                                    loanViewModel.repayLoan(
                                        // loanId = loanDetails?.loan_id ?: 0,
                                        amount = amount,
                                        paymentMethod = selectedMethod
                                    )
                                } else {
                                    snackbarMessage = "Please enter a valid amount"
                                    showSnackbar = true
                                }
                            } else {
                                snackbarMessage = "Authentication required. Please login"
                                showSnackbar = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1B5E20),
                            contentColor = Color.White
                        ),
                        enabled = amountToRepay.isNotBlank() && repaymentState !is LoanUiState.Loading
                    ) {
                        Text("CONFIRM & PAY", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Snackbar for showing messages
        if (showSnackbar) {
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                action = {
                    IconButton(
                        onClick = { showSnackbar = false }
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                shape = RoundedCornerShape(8.dp),
                containerColor = when {
                    snackbarMessage.contains("success", ignoreCase = true) -> Color(0xFF1B5E20)
                    else -> Color.Red
                }
            ) {
                Text(
                    text = snackbarMessage,
                    color = Color.White
                )
            }
        }
    }
}
