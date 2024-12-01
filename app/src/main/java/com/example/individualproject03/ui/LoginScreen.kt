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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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


@Composable
fun LoginScreen(navHostController: NavHostController){

    val context = LocalContext.current
    val appDatabase = remember { AppDatabase.getDatabase(context) }
    val loginViewModel: LoginScreenViewModel = viewModel(
        factory = LoginScreenViewModel.provideFactory(appDatabase)
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
            )
            .verticalScroll(rememberScrollState()),
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
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable {
                        navHostController.navigate("registration")
                    }
            )

            Text(
                text = "Sign in",
                color = Color(255, 152, 0, 255),
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 50.dp))

        TextField(
            value = loginViewModel.emailState.value,
            onValueChange = { loginViewModel.onEmailChange(it)},
            textStyle = TextStyle(color = Color.White, fontSize = 20.sp),
            placeholder = {
                Text(
                    text = "Email",
                    color = Color.White,
                    fontSize = 20.sp
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color(255, 152, 0, 255),
                unfocusedIndicatorColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        )

        Spacer(modifier = Modifier.padding(5.dp))

        TextField(
            value = loginViewModel.passwordState.value,
            onValueChange = { loginViewModel.onPasswordChange(it)},
            textStyle = TextStyle(color = Color.White, fontSize = 20.sp),
            placeholder = {
                Text(
                    text = "Password",
                    color = Color.White,
                    fontSize = 20.sp
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color(255, 152, 0, 255),
                unfocusedIndicatorColor = Color.White
            ),
            trailingIcon = {
                IconButton(
                    onClick = { loginViewModel.togglePasswordVisibility()}
                ) {
                    val iconChange =
                        painterResource(id = if (loginViewModel.passwordVisibility.value) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24)
                    Icon(
                        painter = iconChange,
                        contentDescription = if (loginViewModel.passwordVisibility.value) "Show Password" else "Hide Password",
                        tint = Color.White
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            visualTransformation = if (loginViewModel.passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
        )

        if (loginViewModel.loginError.value.isNotEmpty()) {
            Text(
                text = loginViewModel.loginError.value,
                color = Color.Red,
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.padding(15.dp))

        Button(
            onClick = {

                loginViewModel.login {
                    navHostController.navigate("home")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
            ),
            modifier = Modifier.size(150.dp, 50.dp)
        ) {
            Text(
                text = "Submit",
                color = Color(0, 76, 249),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen(){
    LoginScreen(navHostController = rememberNavController())
}