package com.example.projectpam.uicontroller

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectpam.apiservice.ServiceApiEcommerce
import com.example.projectpam.modeldata.Product
import com.example.projectpam.repositori.*
import com.example.projectpam.uicontroller.route.DestinasiNavigasi
import com.example.projectpam.uicontroller.view.*
import com.example.projectpam.uicontroller.viewmodel.*
import com.example.projectpam.utils.SessionManager

@Composable
fun EcommerceApp(
    authViewModel: AuthViewModel,
    productRepository: ProductRepository,
    sessionManager: SessionManager,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
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

    // ===== ADMIN VIEWMODEL =====
    val adminProductViewModel: AdminProductViewModel = viewModel(
        initializer = { AdminProductViewModel(productRepository) }
    )

    // ===== USER VIEWMODEL =====
    val productUserRepository = ProductUserRepository(ServiceApiEcommerce.create(), sessionManager)
    val productUserViewModel: ProductUserViewModel = viewModel(
        initializer = { ProductUserViewModel(productUserRepository) }
    )

    // ===== CART VIEWMODEL =====
    val cartRepository = CartRepository(ServiceApiEcommerce.create(), sessionManager)
    val cartViewModel: CartViewModel = viewModel(
        initializer = { CartViewModel(cartRepository) }
    )

    NavHost(
        navController = navController,
        startDestination = DestinasiNavigasi.LOGIN,
        modifier = modifier
    ) {

        /* ================= LOGIN ================= */
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

        /* ================= REGISTER ================= */
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

        /* ================= HOME USER ================= */
        composable(DestinasiNavigasi.HOME) {
            HalamanHome(
                onNavigateToProducts = { navController.navigate(DestinasiNavigasi.HOME_PRODUCT) },
                onNavigateToCart = { navController.navigate(DestinasiNavigasi.CART) }, // tombol keranjang di Home
                onBackToLogin = {
                    navController.navigate(DestinasiNavigasi.LOGIN) {
                        popUpTo(DestinasiNavigasi.HOME) { inclusive = true }
                    }
                }
            )
        }

        /* ================= USER PRODUCT LIST ================= */
        composable(DestinasiNavigasi.HOME_PRODUCT) {
            HalamanProductUser(
                productUserViewModel = productUserViewModel,
                onProductClick = { product ->
                    navController.navigate("${DestinasiNavigasi.DETAIL_PRODUCT}/${product.product_id}")
                },
                onBack = { navController.popBackStack() },
                onGoToCart = { navController.navigate(DestinasiNavigasi.CART) } // optional tombol cart
            )
        }

        /* ================= USER PRODUCT DETAIL ================= */
        composable("${DestinasiNavigasi.DETAIL_PRODUCT}/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull() ?: return@composable
            HalamanDetail(
                productId = productId,
                productViewModel = productUserViewModel,
                cartViewModel = cartViewModel,
                onBack = { navController.popBackStack() },
                onGoToCart = { navController.navigate(DestinasiNavigasi.CART) } // navigasi ke cart setelah tambah
            )
        }

        /* ================= USER CART ================= */
        composable(DestinasiNavigasi.CART) {
            HalamanCart(
                viewModel = cartViewModel,
                onCheckout = {
                    // TODO: navigasi ke halaman Checkout
                },
                onBack = { navController.popBackStack() }
            )
        }

        /* ================= ADMIN MANAGE PRODUCT ================= */
        composable(DestinasiNavigasi.MANAGE_PRODUCT) {
            HalamanManageProduct(
                viewModel = adminProductViewModel,
                onNavigateToForm = { product ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("product", product)
                    navController.navigate(DestinasiNavigasi.FORM_PRODUCT)
                },
                onBackToLogin = {
                    navController.navigate(DestinasiNavigasi.LOGIN) {
                        popUpTo(DestinasiNavigasi.MANAGE_PRODUCT) { inclusive = true }
                    }
                }
            )
        }

        /* ================= ADMIN FORM PRODUCT ================= */
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
