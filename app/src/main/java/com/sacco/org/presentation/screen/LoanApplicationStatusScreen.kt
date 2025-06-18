package com.sacco.org.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sacco.org.data.api.model.RepaymentPeriod
import com.sacco.org.data.api.model.StatusOption
import com.sacco.org.presentation.viewmodel.LoanViewModel
import com.sacco.org.util.Resource
import java.text.SimpleDateFormat


@Composable
fun LoanApplicationStatusScreen(
    loanViewModel: LoanViewModel
) {
    val loanStatusState by loanViewModel.loanRepaymentState.collectAsState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        loanViewModel.getLoanApplicationStatus(22) // Using your test loanId
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
            when (loanStatusState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is Resource.Success -> {
                    val loanData = (loanStatusState as Resource.Success).data.data
                    val currentStatus = loanData.loanStatus.label
                    val statusOptions = loanData.statusOptions.values.toList()

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
                            // Header
                            Text(
                                text = "MPANZI SACCO",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Loan Application Status",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Loan Summary Card
                            LoanSummaryCard(
                                loanAmount = loanData.loanAmount,
                                interestRate = loanData.interestRate,
                                repaymentPeriod = loanData.repaymentPeriod,
                                currentStatus = currentStatus
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Status Timeline
                            StatusTimeline(
                                statusOptions = statusOptions,
                                currentStatus = currentStatus
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    ErrorMessage((loanStatusState as Resource.Error).message)
                }

                else -> {
                    LoadingMessage()
                }
            }
        }
    }
}

@Composable
private fun LoanSummaryCard(
    loanAmount: String,
    interestRate: String,
    repaymentPeriod: RepaymentPeriod,
    currentStatus: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoRow("Loan Amount", "%,.2f KES".format(loanAmount.toDoubleOrNull() ?: 0.0))
            InfoRow("Interest Rate", "${interestRate}%")
            InfoRow(
                "Repayment Period",
                "${repaymentPeriod.repayEvery} ${getFrequencyLabel(repaymentPeriod.frequencyEnum)}"
            )
            InfoRow("Status", currentStatus)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.DarkGray
        )
        Text(
            text = value,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}


@Composable
private fun StatusTimeline(
    statusOptions: List<StatusOption>,
    currentStatus: String
) {
    val sortedSteps = statusOptions
        .sortedBy { it.date?.let { date -> SimpleDateFormat("MMM d, yyyy").parse(date) } }
        .map { it.label to it.date }

    Column {
        Text("Progress", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            sortedSteps.forEachIndexed { index, (label, date) ->
                LoanTimelineStep(
                    label = label,
                    date = date,
                    isActive = label == currentStatus,
                    isCompleted = index < sortedSteps.indexOfFirst { it.first == currentStatus }
                )
            }
        }
    }
}

@Composable
fun LoanTimelineStep(
    label: String,
    date: String?,
    isActive: Boolean,
    isCompleted: Boolean
) {
    val pulseAnim = rememberInfiniteTransition()
    val scale by pulseAnim.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val animatedLineHeight = remember { Animatable(0f) }

    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            animatedLineHeight.animateTo(
                targetValue = 32f,
                animationSpec = tween(durationMillis = 500)
            )
        }
    }

    Row(modifier = Modifier.padding(start = 8.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Timeline dot indicator
            Canvas(
                modifier = Modifier
                    .size(if (isActive) 18.dp else 16.dp)
                    .graphicsLayer {
                        if (isActive) {
                            scaleX = scale
                            scaleY = scale
                        }
                    }
            ) {
                drawCircle(
                    color = when {
                        isActive -> Color(0xFF1B5E20) // Active green
                        isCompleted -> Color(0xFF81C784) // Completed light green
                        else -> Color.LightGray // Not completed
                    }
                )
            }

            // Connector line between steps
            if (date != null) { // Only show connector if not the last step
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(animatedLineHeight.value.dp)
                        .background(
                            color = if (isCompleted) Color(0xFF81C784) else Color.LightGray
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Status label and date
        Column {
            Text(
                text = label,
                fontWeight = FontWeight.Medium,
                color = if (isActive) Color(0xFF1B5E20) else Color.Black
            )
            if (date != null) {
                Text(
                    text = date,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun LoadingMessage() {
    Text(
        text = "Loading loan details...",
        modifier = Modifier
    )
}

@Composable
private fun ErrorMessage(message: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: ${message ?: "Unknown error"}",
            color = Color.Red
        )
    }
}

private fun getFrequencyLabel(frequencyEnum: Int): String {
    return when (frequencyEnum) {
        1 -> "Week(s)"
        2 -> "Month(s)"
        3 -> "Year(s)"
        else -> "Period(s)"
    }
}

