package com.example.individualproject03.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.individualproject03.R
import com.example.individualproject03.data.database.AppDatabase
import kotlinx.coroutines.launch

/**
 * This file defines the UI for a registration screen in a Compose-based Android application.
 *
 * - Input fields for user registration: First Name, Last Name, Email, and Password.
 * - Validation logic to provide feedback on user input.
 * - A submit button for form submission, with a loading indicator.
 * - Navigation to the login screen upon successful registration.
 * - Display of error messages when validation or registration fails.
 */

@Composable
fun RegistrationScreen(navHostController: NavHostController){

    val context = LocalContext.current
    val appDatabase = remember { AppDatabase.getDatabase(context) }
    val registerViewModel: RegistrationScreenViewModel = viewModel(
        factory = RegistrationScreenViewModel.provideFactory(appDatabase)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(201, 1, 254),
                        Color(0, 76, 249)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.padding(vertical = 100.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 40.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sign up",
                color = Color(255, 152, 0, 255),
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 10.dp)
            )

            Text(
                text = "Sign in",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.clickable {
                    navHostController.navigate("login")
                }
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 30.dp))

        TextField(
            value = registerViewModel.firstNameState.value,
            onValueChange = { registerViewModel.onFirstNameChange(it) },
            textStyle = TextStyle(
                color = if (registerViewModel.firstNameState.value.isEmpty()) Color.White else Color.Green,
                fontSize = 20.sp
            ),
            placeholder = { Text("First Name", color = Color.White, fontSize = 20.sp) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = if (registerViewModel.firstNameState.value.isEmpty()) Color.White else Color.Green,
                unfocusedIndicatorColor = if (registerViewModel.firstNameState.value.isEmpty()) Color.White else Color.Green
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = if (registerViewModel.firstNameState.value.isEmpty()) R.drawable.baseline_error_24 else R.drawable.baseline_check_circle_24),
                    contentDescription = "status Icon",
                    tint = if (registerViewModel.firstNameState.value.isEmpty()) Color.Red else Color.Green
                )
            },
        )

        Spacer(modifier = Modifier.padding(5.dp))

        TextField(
            value = registerViewModel.lastNameState.value,
            onValueChange = { registerViewModel.onLastNameChange(it) },
            textStyle = TextStyle(
                color = if (registerViewModel.lastNameState.value.isEmpty()) Color.White else Color.Green,
                fontSize = 20.sp
            ),
            placeholder = { Text("Last Name", color = Color.White, fontSize = 20.sp) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = if (registerViewModel.lastNameState.value.isEmpty()) Color.White else Color.Green,
                unfocusedIndicatorColor = if (registerViewModel.lastNameState.value.isEmpty()) Color.White else Color.Green
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = if (registerViewModel.lastNameState.value.isEmpty()) R.drawable.baseline_error_24 else R.drawable.baseline_check_circle_24),
                    contentDescription = "status Icon",
                    tint = if (registerViewModel.lastNameState.value.isEmpty()) Color.Red else Color.Green
                )
            },
        )

        Spacer(modifier = Modifier.padding(5.dp))

        TextField(
            value = registerViewModel.emailState.value,
            onValueChange = { registerViewModel.onEmailChange(it)},
            textStyle = TextStyle(
                color = if (registerViewModel.emailError.isNotEmpty()) Color.White else Color.Green,
                fontSize = 20.sp
            ),
            placeholder = { Text("Email", color = Color.White, fontSize = 20.sp) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = if (registerViewModel.emailError.isNotEmpty()) Color.White else Color.Green,
                unfocusedIndicatorColor = if (registerViewModel.emailError.isNotEmpty()) Color.White else Color.Green
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = if (registerViewModel.emailError.isNotEmpty()) R.drawable.baseline_error_24 else R.drawable.baseline_check_circle_24),
                    contentDescription = "status Icon",
                    tint = if (registerViewModel.emailError.isNotEmpty()) Color.Red else Color.Green
                )
            },
        )


        Spacer(modifier = Modifier.padding(5.dp))

        TextField(
            value = registerViewModel.passwordState.value,
            onValueChange = { registerViewModel.onPasswordChange(it) },
            textStyle = TextStyle(
                color = if (registerViewModel.passwordError.isNotEmpty()) Color.White else Color.Green,
                fontSize = 20.sp
            ),
            placeholder = { Text("Password", color = Color.White, fontSize = 20.sp) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = if (registerViewModel.passwordError.isNotEmpty()) Color.White else Color.Green,
                unfocusedIndicatorColor = if (registerViewModel.passwordError.isNotEmpty()) Color.White else Color.Green
            ),
            trailingIcon = {

                Row(
                    modifier = Modifier.padding(end = 12.dp),

                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically

                ){
                    IconButton(
                        onClick = { registerViewModel.onPasswordVisibility() },
                    ) {
                        Icon(
                            painter = painterResource(id = if (registerViewModel.passwordVisiblity.value) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24),
                            contentDescription = if (registerViewModel.passwordVisiblity.value) "Show Password" else "Hide Password",
                            tint = Color.White
                        )
                    }

                    Icon(
                        painter = painterResource(id = if (registerViewModel.passwordError.isNotEmpty()) R.drawable.baseline_error_24 else R.drawable.baseline_check_circle_24),
                        contentDescription = "status Icon",
                        tint = if (registerViewModel.passwordError.isNotEmpty()) Color.Red else Color.Green
                    )
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            visualTransformation = if (registerViewModel.passwordVisiblity.value) VisualTransformation.None else PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.padding(15.dp))

        Button(
            onClick = {
                // Check all validation and the checkbox condition
                if (registerViewModel.registrationError.value.isEmpty() && registerViewModel.lastNameError.isEmpty() &&
                    registerViewModel.emailError.isEmpty() && registerViewModel.passwordError.isEmpty()) {
                    registerViewModel.onIsLoadingChange(true)

                    registerViewModel.registerUser{
                        navHostController.navigate("login")
                    }

                } else {
                    registerViewModel.onRegistrationError("Registration failed. Please try again.")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier.size(150.dp, 50.dp)
        ) {
            if (registerViewModel.isLoading.value) {
                CircularProgressIndicator(color = Color(0, 76, 249), modifier = Modifier.size(20.dp))
            } else {
                Text(
                    text = "Submit",
                    color = Color(0, 76, 249),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 5.dp))

        if (registerViewModel.registrationError.value.isNotEmpty()) {
            Surface(
                modifier = Modifier.padding(20.dp),
                color = Color.Red,
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = registerViewModel.registrationError.value,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(15.dp),

                    )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen(){
    RegistrationScreen(navHostController = rememberNavController())
}