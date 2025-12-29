package com.example.projectpam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectpam.apiservice.ServiceApiEcommerce
import com.example.projectpam.repositori.ProductRepository
import com.example.projectpam.ui.theme.ProjectPAMTheme
import com.example.projectpam.uicontroller.EcommerceApp
import com.example.projectpam.uicontroller.viewmodel.AuthViewModel
import com.example.projectpam.utils.SessionManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectPAMTheme {
                val sessionManager = SessionManager(this)

                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModel.Factory(sessionManager)
                )

                val apiService = ServiceApiEcommerce.create()
                val productRepository = ProductRepository(apiService, sessionManager)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EcommerceApp(
                        authViewModel = authViewModel,
                        productRepository = productRepository,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    androidx.compose.material3.Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjectPAMTheme {
        Greeting("Android")
    }
}
