package com.sacco.org.presentation.screen

//@Composable
//fun ApplyLoanScreen(
//    navController: NavController,
//    authViewModel: AuthViewModel,
//    loanViewModel: LoanViewModel
//) {
//    val loanTypes = listOf(
//        "Emergency Loan" to 1,  // Mapping display names to loan_type_enum values
//        "Education Loan" to 2,
//        "Development Loan" to 3
//    )
//    val context = LocalContext.current
//    val dateFormatter =
//        remember { SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH) } // API-friendly format
//    var selectedLoanType by remember { mutableStateOf(loanTypes.first()) }
//    var loanAmount by remember { mutableStateOf("") }
//    var repaymentMonths by remember { mutableStateOf("12") } // Default to 12 months
//    var applicationDate by remember { mutableStateOf(dateFormatter.format(Date())) }
//    var expanded by remember { mutableStateOf(false) }
//
//    // Get authenticated user data
//    val signInResponse by authViewModel.userFlow.collectAsState(initial = null)
//    val clientId = signInResponse?.m_client_id ?: 0
//
//    // Observe loan creation state
//    val loanState by loanViewModel.createLoanState.collectAsState()
//
//    val calendar = Calendar.getInstance()
//
//    val datePickerDialog = remember {
//        DatePickerDialog(
//            context,
//            { _, year, month, dayOfMonth ->
//                calendar.set(year, month, dayOfMonth)
//                applicationDate = dateFormatter.format(calendar.time)
//            },
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH)
//        )
//    }
//
//
//
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F5F5))
//            .padding(horizontal = 16.dp)
//    ) {
//        Spacer(
//            modifier = Modifier.height(
//                WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 32.dp
//            )
//        )
//
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "MPANZI SACCO",
//                style = MaterialTheme.typography.headlineSmall.copy(
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF1B5E20)
//                )
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = "Apply for Loan",
//                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(12.dp)),
//            colors = CardDefaults.cardColors(containerColor = Color.White),
//            elevation = CardDefaults.cardElevation(4.dp)
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//
//                // Loan Type Dropdown
//                Text("Loan Type", fontWeight = FontWeight.SemiBold)
//                Box(modifier = Modifier.fillMaxWidth()) {
//                    OutlinedButton(
//                        onClick = { expanded = true },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(selectedLoanType.first)
//                    }
//
//                    DropdownMenu(
//                        expanded = expanded,
//                        onDismissRequest = { expanded = false }
//                    ) {
//                        loanTypes.forEach { (name, type) ->
//                            DropdownMenuItem(
//                                text = { Text(name) },
//                                onClick = {
//                                    selectedLoanType = name to type
//                                    expanded = false
//                                }
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Loan Amount
//                Text("Loan Amount", fontWeight = FontWeight.SemiBold)
//                OutlinedTextField(
//                    value = loanAmount,
//                    onValueChange = { loanAmount = it },
//                    modifier = Modifier.fillMaxWidth(),
//                    placeholder = { Text("Ksh 100,000") },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                    singleLine = true
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Repayment Period
//                Text("Repayment Period (Months)", fontWeight = FontWeight.SemiBold)
//                OutlinedTextField(
//                    value = repaymentMonths,
//                    onValueChange = { repaymentMonths = it },
//                    modifier = Modifier.fillMaxWidth(),
//                    //  placeholder = { Text("12") },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                    singleLine = true
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Application Date
//                Text("Application Date", fontWeight = FontWeight.SemiBold)
//                OutlinedTextField(
//                    value = applicationDate,
//                    onValueChange = {},
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { datePickerDialog.show() },
//                    readOnly = true,
//                    singleLine = true
//                )
//
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                // Submit Button
//                Button(
//                    onClick = {
//                        if (clientId != 0) {
//                            loanViewModel.createLoan(
//                                repaymentFrequency = repaymentMonths,
//                                submittedOnDate = applicationDate,
//                                principalAmount = loanAmount.toDoubleOrNull() ?: 0.0,
//                                loanType = selectedLoanType.second,
//                                clientId = clientId
//                            )
//                        } else {
//                            Toast.makeText(context, "Authentication required", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
//                    enabled = loanAmount.isNotBlank() && repaymentMonths.isNotBlank()
//                ) {
//                    Text(text = "SUBMIT", color = Color.White, fontWeight = FontWeight.Bold)
//                }
//            }
//        }
//    }
//}


//@Composable
//fun LoanApplicationStatusScreen() {
//    val currentStatus = "Under Review"
//
//    val timelineSteps = listOf(
//        "Submitted" to "Mar 1, 2024",
//        "Under Review" to "Mar 3, 2024",
//        "Approved" to "Mar 7, 2024",
//        "Disbursed" to null
//    )
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F5F5))
//            .padding(horizontal = 16.dp)
//    ) {
//        Spacer(
//            modifier = Modifier.height(
//                WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 32.dp
//            )
//        )
//
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "MPANZI SACCO",
//                style = MaterialTheme.typography.headlineSmall.copy(
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF1B5E20)
//                )
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = "Loan Application Status",
//                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Loan summary info
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(12.dp)),
//            colors = CardDefaults.cardColors(containerColor = Color.White),
//            elevation = CardDefaults.cardElevation(4.dp)
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                InfoRow("Loan Amount", "15,000 KES")
//                InfoRow("Interest Rate", "12.0%")
//                InfoRow("Repayment Period", "12 Months")
//                InfoRow("Status", currentStatus)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Timeline
//        Text("Progress", fontWeight = FontWeight.Bold, fontSize = 16.sp)
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 12.dp)
//        ) {
//            timelineSteps.forEachIndexed { index, (label, date) ->
//                LoanTimelineStep(
//                    label = label,
//                    date = date,
//                    isActive = label == currentStatus,
//                    isCompleted = index < timelineSteps.indexOfFirst { it.first == currentStatus }
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//fun InfoRow(label: String, value: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 6.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(label, fontSize = 14.sp, color = Color.DarkGray)
//        Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp)
//    }
//}
//
//
//
//@SuppressLint("RememberReturnType")
//@Composable
//fun LoanTimelineStep(
//    label: String,
//    date: String?,
//    isActive: Boolean,
//    isCompleted: Boolean
//) {
//    val pulseAnim = rememberInfiniteTransition()
//    val scale by pulseAnim.animateFloat(
//        initialValue = 1f,
//        targetValue = 1.3f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
//            repeatMode = RepeatMode.Reverse
//        )
//    )
//
//    val animatedLineHeight = remember { Animatable(0f) }
//
//    LaunchedEffect(isCompleted) {
//        if (isCompleted) {
//            animatedLineHeight.animateTo(
//                targetValue = 32f,
//                animationSpec = tween(durationMillis = 500)
//            )
//        }
//    }
//
//    Row(modifier = Modifier.padding(start = 8.dp)) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Canvas(
//                modifier = Modifier
//                    .size(if (isActive) 18.dp else 16.dp)
//                    .graphicsLayer {
//                        if (isActive) {
//                            scaleX = scale
//                            scaleY = scale
//                        }
//                    }
//            ) {
//                drawCircle(
//                    color = when {
//                        isActive -> Color(0xFF1B5E20)
//                        isCompleted -> Color(0xFF81C784)
//                        else -> Color.LightGray
//                    }
//                )
//            }
//
//            // Animated connector line
//            if (label != "Disbursed") {
//                Box(
//                    modifier = Modifier
//                        .width(2.dp)
//                        .height(animatedLineHeight.value.dp)
//                        .background(
//                            color = if (isCompleted) Color(0xFF81C784) else Color.LightGray
//                        )
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.width(12.dp))
//
//        Column {
//            Text(label, fontWeight = FontWeight.Medium)
//            if (date != null) {
//                Text(date, fontSize = 13.sp, color = Color.Gray)
//            }
//        }
//    }
//}



//@Composable
//fun DepositFundsScreen(navController: NavController) {
//    val accountOptions = listOf("Savings Account", "Shares Account")
//    val paymentMethods = listOf("M-Pesa", "Bank Transfer", "Card")
//
//    var selectedAccount by remember { mutableStateOf(accountOptions.first()) }
//    var selectedMethod by remember { mutableStateOf(paymentMethods.first()) }
//    var amount by remember { mutableStateOf("") }
//    var accountExpanded by remember { mutableStateOf(false) }
//    var methodExpanded by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F5F5))
//            .padding(horizontal = 16.dp)
//    ) {
//        Spacer(
//            modifier = Modifier.height(
//                WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 32.dp
//            )
//        )
//
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(
//                text = "Mpanzi SACCO",
//                style = MaterialTheme.typography.headlineSmall.copy(
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF1B5E20)
//                )
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = "Deposit Funds",
//                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(12.dp)),
//            colors = CardDefaults.cardColors(containerColor = Color.White),
//            elevation = CardDefaults.cardElevation(4.dp)
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//
//                // Select Account
//                Text("Select Account", fontWeight = FontWeight.SemiBold)
//                Box(modifier = Modifier.fillMaxWidth()) {
//                    OutlinedButton(
//                        onClick = { accountExpanded = true },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(selectedAccount)
//                    }
//                    DropdownMenu(
//                        expanded = accountExpanded,
//                        onDismissRequest = { accountExpanded = false }
//                    ) {
//                        accountOptions.forEach { option ->
//                            DropdownMenuItem(
//                                text = { Text(option) },
//                                onClick = {
//                                    selectedAccount = option
//                                    accountExpanded = false
//                                }
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Amount
//                Text("Amount", fontWeight = FontWeight.SemiBold)
//                OutlinedTextField(
//                    value = amount,
//                    onValueChange = { amount = it },
//                    placeholder = { Text("KES 10,000.00") },
//                    modifier = Modifier.fillMaxWidth(),
//                    singleLine = true,
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Payment Method
//                Text("Payment Method", fontWeight = FontWeight.SemiBold)
//                Box(modifier = Modifier.fillMaxWidth()) {
//                    OutlinedButton(
//                        onClick = { methodExpanded = true },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(selectedMethod)
//                    }
//                    DropdownMenu(
//                        expanded = methodExpanded,
//                        onDismissRequest = { methodExpanded = false }
//                    ) {
//                        paymentMethods.forEach { method ->
//                            DropdownMenuItem(
//                                text = { Text(method) },
//                                onClick = {
//                                    selectedMethod = method
//                                    methodExpanded = false
//                                }
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                Button(
//                    onClick = {
//                        // TODO: Handle deposit submission
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
//                ) {
//                    Text("DEPOSIT", color = Color.White, fontWeight = FontWeight.Bold)
//                }
//            }
//        }
//    }
//}
