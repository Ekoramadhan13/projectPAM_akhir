package com.example.projectpam.uicontroller

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projectpam.modeldata.Product
import com.example.projectpam.repositori.ProductRepository
import com.example.projectpam.repositori.ProductUserRepository
import com.example.projectpam.uicontroller.route.DestinasiNavigasi
import com.example.projectpam.uicontroller.view.*
import com.example.projectpam.uicontroller.viewmodel.*
import com.example.projectpam.apiservice.ServiceApiEcommerce
import com.example.projectpam.utils.SessionManager

@Composable
fun EcommerceApp(
    navController: NavHostController = androidx.navigation.compose.rememberNavController(),
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    productRepository: ProductRepository,
    sessionManager: SessionManager
) {
    HostNavigasi(
        navController = navController,
        authViewModel = authViewModel,
        productRepository = productRepository,
        sessionManager = sessionManager,
        modifier = modifier
    )
}

@Composable
fun HostNavigasi(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    productRepository: ProductRepository,
    sessionManager: SessionManager,
    modifier: Modifier = Modifier
) {
    // ================== ADMIN ==================
    val adminProductViewModel: AdminProductViewModel = viewModel(
        factory = AdminProductViewModelFactory(productRepository)
    )

    // ================== USER ==================
    val userProductRepository = ProductUserRepository(ServiceApiEcommerce.create(), sessionManager)
    val productUserViewModel: ProductUserViewModel = viewModel(
        factory = ProductUserViewModelFactory(userProductRepository)
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
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ================= HOME =================
        composable(DestinasiNavigasi.HOME) {
            HalamanHome(
                onNavigateToProducts = { navController.navigate(DestinasiNavigasi.HOME_PRODUCT) }
            )
        }

        // ================= ADMIN MANAGE PRODUCT =================
        composable(DestinasiNavigasi.MANAGE_PRODUCT) {
            HalamanManageProduct(
                viewModel = adminProductViewModel,
                onNavigateToForm = { product ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
                    navController.navigate(DestinasiNavigasi.FORM_PRODUCT)
                },
                onBackToLogin = {
                    // Kembali ke halaman login dan hapus stack ManageProduct
                    navController.navigate(DestinasiNavigasi.LOGIN) {
                        popUpTo(DestinasiNavigasi.MANAGE_PRODUCT) { inclusive = true }
                    }
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

        // ================= USER HOME PRODUCT =================
        composable(DestinasiNavigasi.HOME_PRODUCT) {
            HalamanProductUser(
                productUserViewModel = productUserViewModel,
                onProductClick = { product ->
                    navController.navigate("${DestinasiNavigasi.HOME_PRODUCT}_detail/${product.product_id}")
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ================= USER DETAIL PRODUCT =================
        composable("${DestinasiNavigasi.HOME_PRODUCT}_detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            if (productId != null) {
                HalamanDetail(
                    productId = productId,
                    viewModel = productUserViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

// ================== FACTORY ==================
class AdminProductViewModelFactory(
    private val repository: ProductRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ProductUserViewModelFactory(
    private val repository: ProductUserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductUserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
