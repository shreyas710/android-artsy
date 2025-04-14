package com.example.assignment4.auth.presentation.login

import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.assignment4.core.presentation.viewModel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController, sharedViewModel: SharedViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            "Login",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        }
    ) { innerPadding ->

        val (email, setEmail) = remember { mutableStateOf("") }
        var emailError by remember { mutableStateOf<String?>(null) }

        val (password, setPassword) = remember { mutableStateOf("") }
        var passwordError by remember { mutableStateOf<String?>(null) }

        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = {
                    setEmail(it)
                    emailError = null
                },
                label = { Text("Email") },
                isError = emailError != null,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused && email.isNotEmpty()) {
                            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                emailError = "Invalid email format"
                            } else {
                                emailError = null
                            }
                        }
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.secondary
                )
            )

            if (emailError != null) {
                Text(
                    text = emailError!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp, bottom = 8.dp),
                    fontSize = 15.sp
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = password,
                onValueChange = {
                    setPassword(it)
                    passwordError = null
                },
                label = { Text("Password") },
                isError = passwordError != null,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.secondary
                )
            )

            if (passwordError != null) {
                Text(
                    text = passwordError!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp, bottom = 8.dp),
                    fontSize = 15.sp
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    focusManager.clearFocus()
                    val validEmail =
                        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    val validPassword = password.isNotEmpty() && password.length >= 6

                    if (email.isEmpty()) {
                        emailError = "Email cannot be empty"
                    }
                    if (password.isEmpty()) {
                        passwordError = "Password cannot be empty"
                    } else {
                        if (!validEmail) {
                            emailError = "Invalid email format"
                        }
                        if (!validPassword) {
                            passwordError = "Invalid password"
                        }
                    }

                    if (validEmail && validPassword) {
                        // Handle successful login, e.g., call onLoginClick, etc.
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
            ) {
                Text("Login", color = Color.White, fontSize = 17.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register Link
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Don't have an account yet? ")
                Text(
                    text = "Register",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                    }
                )
            }
        }
    }
}
