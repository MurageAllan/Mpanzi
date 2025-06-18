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
import com.sacco.org.data.local.DataStoreManager
import com.sacco.org.presentation.viewmodel.AuthUiState
import com.sacco.org.presentation.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onSignUpSuccess: () -> Unit,
    onSignInRedirect: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }

    var validationError by remember { mutableStateOf<String?>(null) }

    val signUpState by viewModel.signUpState.collectAsState()

    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(12.dp))

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
            label = "Enter a 4-digit PIN",
            keyboardType = KeyboardType.NumberPassword,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = confirmPin,
            onValueChange = { confirmPin = it },
            label = "Confirm 4-digit PIN",
            keyboardType = KeyboardType.NumberPassword,
            isPassword = true
        )

        validationError?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        when (signUpState) {
            is AuthUiState.Loading -> {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            is AuthUiState.Error -> {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = (signUpState as AuthUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )

            }

            is AuthUiState.Success -> {
                LaunchedEffect(Unit) {
                    val result = (signUpState as AuthUiState.Success).data
                    val user = result.mobileClient

//                    // Save SignInResponse data
//                    val signInResponse = SignInResponse(
//                        token = result.token,
//                        expires_at = result.expires_at,
//                        user_id = result.user_id,
//                        email = result.email,
//                        phone_number = result.phone_number,
//                        name = result.name,
//                        client_id = result.client_id
//                    )
//
//                    // Save the data to DataStore
//                    dataStoreManager.saveAuthData(signInResponse)

                    viewModel.resetSignUpState()
                    onSignUpSuccess()  // In AppNavHost, navigate to Screen.Home
                }
            }

            else -> Unit
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                validationError = when {
                    phoneNumber.length < 9 -> "Enter a valid phone number."
                    pin.length != 4 -> "PIN must be 4 digits."
                    confirmPin != pin -> "PINs do not match."
                    else -> null
                }

                if (validationError == null) {
                    viewModel.register(phoneNumber, pin, confirmPin)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text("Already have an account? ")
            Text(
                text = "Sign in",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onSignInRedirect() }
            )
        }
    }
}
