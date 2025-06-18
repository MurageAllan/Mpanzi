package com.sacco.org.presentation.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import com.sacco.org.presentation.viewmodel.AuthViewModel
import com.sacco.org.presentation.viewmodel.LoanUiState
import com.sacco.org.presentation.viewmodel.LoanViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale



@Composable
fun ApplyLoanScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    loanViewModel: LoanViewModel
) {
    val loanTypes = listOf(
        "Emergency Loan" to 1,
        "Education Loan" to 2,
        "Development Loan" to 3
    )

    val context = LocalContext.current
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH) }
    var selectedLoanType by remember { mutableStateOf(loanTypes.first()) }
    var loanAmount by remember { mutableStateOf("") }
    var repaymentMonths by remember { mutableStateOf("12") }
    var applicationDate by remember { mutableStateOf(dateFormatter.format(Date())) }
    var expanded by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val signInResponse by authViewModel.userFlow.collectAsState(initial = null)
    val clientId = signInResponse?.m_client_id ?: 0
    val loanState by loanViewModel.createLoanState.collectAsState()

    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                applicationDate = dateFormatter.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    // Handle loan creation result
    LaunchedEffect(loanState) {
        when (loanState) {
            is LoanUiState.Success -> {
                snackbarMessage = "Loan application submitted successfully!"
                showSnackbar = true
                // Auto-reset the form after successful submission
                loanAmount = ""
                repaymentMonths = "12"
                applicationDate = dateFormatter.format(Date())
                // Optional: Navigate back after delay
                // delay(2000)
                // navController.popBackStack()
            }

            is LoanUiState.Error -> {
                snackbarMessage =
                    (loanState as LoanUiState.Error).message ?: "Failed to submit loan application"
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
                    text = "Apply for Loan",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black // Changed to black
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (loanState is LoanUiState.Loading) {
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
                    // Loan Type Dropdown
                    Text(
                        "Loan Type",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                selectedLoanType.first,
                                color = Color.Black
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            loanTypes.forEach { (name, type) ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            name,
                                            color = Color.White
                                        )
                                    },
                                    onClick = {
                                        selectedLoanType = name to type
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Loan Amount
                    Text(
                        "Loan Amount (KES)",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black // Changed to black
                    )
                    OutlinedTextField(
                        value = loanAmount,
                        onValueChange = { loanAmount = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Repayment Period
                    Text(
                        "Repayment Period (Months)",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black // Changed to black
                    )
                    OutlinedTextField(
                        value = repaymentMonths,
                        onValueChange = { repaymentMonths = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp
                        )

                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Application Date
                    Text(
                        "Application Date",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black // Changed to black
                    )
                    OutlinedTextField(
                        value = applicationDate,
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { datePickerDialog.show() },
                        readOnly = true,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (clientId != 0) {
                                loanViewModel.createLoan(
                                    repaymentFrequency = repaymentMonths,
                                    submittedOnDate = applicationDate,
                                    principalAmount = loanAmount.toDoubleOrNull() ?: 0.0,
                                    loanType = selectedLoanType.second,
                                    clientId = clientId
                                )

                            } else {
                                snackbarMessage = "Authentication required. Please login"
                                showSnackbar = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1B5E20),
                            contentColor = Color.White
                        ),
                        // enabled = loanAmount.isNotBlank() && repaymentMonths.isNotBlank() && loanState !is LoanUiState.Loading
                    ) {
                        Text(text = "SUBMIT", fontWeight = FontWeight.Bold)
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