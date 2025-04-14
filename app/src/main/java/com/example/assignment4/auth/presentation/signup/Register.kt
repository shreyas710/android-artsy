package com.example.assignment4.auth.presentation.signup

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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.assignment4.auth.data.models.RegisterRequest
import com.example.assignment4.core.data.api.LoginDataStoreManager
import com.example.assignment4.core.data.api.RetrofitInstance
import com.example.assignment4.core.presentation.viewModel.SharedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navController: NavController, sharedViewModel: SharedViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }

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
                            "Register",
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        val coroutineScope = rememberCoroutineScope()

        val (name, setName) = remember { mutableStateOf("") }
        var nameError by remember { mutableStateOf<String?>(null) }

        val (email, setEmail) = remember { mutableStateOf("") }
        var emailError by remember { mutableStateOf<String?>(null) }

        val (password, setPassword) = remember { mutableStateOf("") }
        var passwordError by remember { mutableStateOf<String?>(null) }

        val focusManager = LocalFocusManager.current

        val (loading, setLoading) = remember { mutableStateOf(false) }

        val context = LocalContext.current

        suspend fun handleRegister() {
            setLoading(true)
            try {
                val registerRequest =
                    RegisterRequest(name = name, email = email, password = password)
                val response = RetrofitInstance.userApi.register(registerRequest)
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        LoginDataStoreManager.saveLoginResponse(context, registerResponse)
                        sharedViewModel.user.value = registerResponse

                        snackbarHostState.showSnackbar("Registered successfully")
                        navController.navigate("home")
                    } else {
                        emailError = "User with this email already exists"
                        return
                    }
                } else {
                    emailError = "User with this email already exists"
                    return
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                setLoading(false)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    setName(it)
                    nameError = null
                },
                label = { Text("Enter Full Name") },
                isError = nameError != null,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.secondary
                )
            )

            if (nameError != null) {
                Text(
                    text = nameError!!,
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
                value = email,
                onValueChange = {
                    setEmail(it)
                    emailError = null
                },
                label = { Text("Enter Email") },
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
                    val validPassword = password.isNotEmpty()

                    if (name.isEmpty()) {
                        nameError = "Full name cannot be empty"
                    }
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

                    if (validEmail && validPassword && name.isNotEmpty()) {
                        coroutineScope.launch {
                            handleRegister()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(if (!loading) MaterialTheme.colorScheme.secondary else Color.LightGray),
                enabled = !loading
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Register", color = Color.White, fontSize = 17.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Already have an account? ")
                Text(
                    text = "Login",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate("login")
                    }
                )
            }
        }
    }
}