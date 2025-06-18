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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.navigation.NavController
import com.sacco.org.presentation.viewmodel.AuthViewModel
import com.sacco.org.presentation.viewmodel.SavingsUiState
import com.sacco.org.presentation.viewmodel.SavingsViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DepositFundsScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    savingsViewModel: SavingsViewModel
) {
    val context = LocalContext.current
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH) }

    // Account options with their corresponding product IDs
    val accountOptions = listOf(
        "Savings Account" to 1,
        "Shares Account" to 2
    )

    val paymentMethods = listOf("M-Pesa", "Bank Transfer", "Card", "Cash")

    var selectedAccount by remember { mutableStateOf(accountOptions.first()) }
    var selectedMethod by remember { mutableStateOf(paymentMethods.first()) }
    var amount by remember { mutableStateOf("") }
    var accountExpanded by remember { mutableStateOf(false) }
    var methodExpanded by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    // Get client ID from auth state
    val signInResponse by authViewModel.userFlow.collectAsState(initial = null)
    val clientId = signInResponse?.m_client_id ?: 0

    // Observe savings state
    val savingsState by savingsViewModel.createSavingsState.collectAsState()

    // Handle savings creation result
    LaunchedEffect(savingsState) {
        when (savingsState) {
            is SavingsUiState.Success -> {
                snackbarMessage = "Deposit submitted successfully!"
                showSnackbar = true
                // Reset form after successful submission
                amount = ""
            }

            is SavingsUiState.Error -> {
                snackbarMessage = (savingsState as SavingsUiState.Error).message
                    ?: "Failed to submit deposit"
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
        ) {
            Spacer(
                modifier = Modifier.height(
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 32.dp
                )
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Mpanzi SACCO",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Deposit Funds",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Show loading indicator when processing
            if (savingsState is SavingsUiState.Loading) {
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

                    // Select Account
                    Text("Select Account", fontWeight = FontWeight.SemiBold)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { accountExpanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(selectedAccount.first)
                        }
                        DropdownMenu(
                            expanded = accountExpanded,
                            onDismissRequest = { accountExpanded = false }
                        ) {
                            accountOptions.forEach { (name, id) ->
                                DropdownMenuItem(
                                    text = { Text(name) },
                                    onClick = {
                                        selectedAccount = name to id
                                        accountExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Amount
                    Text("Amount (KES)", fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        placeholder = { Text("Enter amount") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Payment Method
                    Text("Payment Method", fontWeight = FontWeight.SemiBold)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { methodExpanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(selectedMethod)
                        }
                        DropdownMenu(
                            expanded = methodExpanded,
                            onDismissRequest = { methodExpanded = false }
                        ) {
                            paymentMethods.forEach { method ->
                                DropdownMenuItem(
                                    text = { Text(method) },
                                    onClick = {
                                        selectedMethod = method
                                        methodExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (clientId != 0) {
                                val amountValue = amount.toDoubleOrNull()
                                if (amountValue != null && amountValue > 0) {
                                    savingsViewModel.createSavings(
                                        clientId = clientId,
                                        productId = 1,
                                        amount = amountValue,
                                        paymentMethod = selectedMethod,
                                        submittedOnDate = dateFormatter.format(Date())
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
                        //  enabled = amount.isNotBlank() && savingsState !is SavingsUiState.Loading
                    ) {
                        Text("DEPOSIT", fontWeight = FontWeight.Bold)
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
