package com.example.projectpam.uicontroller

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectpam.modeldata.Product
import com.example.projectpam.repositori.ProductRepository
import com.example.projectpam.uicontroller.route.DestinasiNavigasi
import com.example.projectpam.uicontroller.view.*

import com.example.projectpam.uicontroller.viewmodel.AuthViewModel
import com.example.projectpam.uicontroller.viewmodel.AdminProductViewModel

@Composable
fun EcommerceApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    productRepository: ProductRepository
) {
    HostNavigasi(
        navController = navController,
        authViewModel = authViewModel,
        productRepository = productRepository,
        modifier = modifier
    )
}

@Composable
fun HostNavigasi(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    productRepository: ProductRepository,
    modifier: Modifier = Modifier
) {
    val adminProductViewModel: AdminProductViewModel = viewModel(
        factory = AdminProductViewModelFactory(productRepository)
    )

    NavHost(
        navController = navController,
        startDestination = DestinasiNavigasi.LOGIN,
        modifier = modifier
    ) {
        // ================= LOGIN =================
        composable(DestinasiNavigasi.LOGIN) {
            HalamanLogin(
                viewModel = authViewModel,
                onLoginUserSuccess = {
                    navController.navigate(DestinasiNavigasi.HOME) {
                        popUpTo(DestinasiNavigasi.LOGIN) { inclusive = true }
                    }
                },
                onLoginAdminSuccess = {
                    navController.navigate(DestinasiNavigasi.MANAGE_PRODUCT) {
                        popUpTo(DestinasiNavigasi.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(DestinasiNavigasi.REGISTER) }
            )
        }

        // ================= REGISTER =================
        composable(DestinasiNavigasi.REGISTER) {
            HalamanRegister(
                viewModel = authViewModel,
                onSuccess = {
                    navController.navigate(DestinasiNavigasi.LOGIN) {
                        popUpTo(DestinasiNavigasi.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        // ================= HOME =================
        composable(DestinasiNavigasi.HOME) {
            HalamanHome()
        }

        // ================= ADMIN MANAGE PRODUCT =================
        composable(DestinasiNavigasi.MANAGE_PRODUCT) {
            HalamanManageProduct(
                viewModel = adminProductViewModel,
                onNavigateToForm = { product ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
                    navController.navigate(DestinasiNavigasi.FORM_PRODUCT)
                }
            )
        }

        // ================= ADMIN FORM PRODUCT =================
        composable(DestinasiNavigasi.FORM_PRODUCT) {
            val product = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Product>("product")

            HalamanFormProduct(
                viewModel = adminProductViewModel,
                product = product,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

// ================= FACTORY VIEWMODEL =================
class AdminProductViewModelFactory(
    private val repository: ProductRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminProductViewModel::class.java)) {
            return AdminProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
