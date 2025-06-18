package com.sacco.org.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sacco.org.data.api.model.SignInResponse
import com.sacco.org.data.local.DataStoreManager
import com.sacco.org.presentation.viewmodel.AuthUiState
import com.sacco.org.presentation.viewmodel.AuthViewModel


@Composable
fun SignInScreen(
    viewModel: AuthViewModel,
    onSignUpRedirect: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }

    var validationError by remember { mutableStateOf<String?>(null) }

    val signInState by viewModel.signInState.collectAsState()
    val spacing = 24.dp

    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome Back", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(spacing))

        CustomTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = "Phone Number",
            keyboardType = KeyboardType.Phone
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = pin,
            onValueChange = { pin = it },
            label = "PIN",
            keyboardType = KeyboardType.NumberPassword,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Forgot password?",
            modifier = Modifier
                .align(Alignment.End)
                .clickable { /* handle reset */ },
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(spacing))

        // Show validation error
        validationError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Observe ViewModel API result
        when (signInState) {
            is AuthUiState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }

            is AuthUiState.Error -> {
                Text(
                    text = (signInState as AuthUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            is AuthUiState.Success -> {
                LaunchedEffect(Unit) {
                    val result = (signInState as AuthUiState.Success).data
                    // Save SignInResponse data
                    val signInResponse = SignInResponse(
                        token = result.token,
                        expires_at = result.expires_at,
                        user_id = result.user_id,
                        email = result.email,
                        phone_number = result.phone_number,
                        name = result.name,
                        m_client_id = result.m_client_id
                    )
                    dataStoreManager.saveAuthData( signInResponse = signInResponse)
                    viewModel.resetSignInState()

                    onLoginSuccess()
                }
            }

            else -> {}
        }

        Button(
            onClick = {
                validationError = when {
                    phoneNumber.length < 9 -> "Enter a valid phone number."
                    pin.length != 4 -> "PIN must be 4 digits."
                    else -> null
                }

                if (validationError == null) {
                    viewModel.login(phoneNumber, pin)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(spacing))

        Text("or continue with", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(spacing))

        Row {
            Text("Don’t have an account? ")
            Text(
                text = "Sign up",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.clickable { onSignUpRedirect() }
            )
        }
    }
}
