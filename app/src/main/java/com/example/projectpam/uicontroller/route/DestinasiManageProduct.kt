package com.example.projectpam.uicontroller.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.projectpam.modeldata.Product
import com.example.projectpam.uicontroller.view.HalamanFormProduct
import com.example.projectpam.uicontroller.view.HalamanManageProduct
import com.example.projectpam.uicontroller.viewmodel.AdminProductViewModel

fun NavGraphBuilder.manageProductNav(
    navController: NavController,
    adminProductViewModel: AdminProductViewModel
) {

    /* =======================================================
     * ADMIN - MANAGE PRODUCT
     * ======================================================= */
    composable("manage_product") {
        HalamanManageProduct(
            viewModel = adminProductViewModel,

            // ➕ Tambah / ✏️ Edit Produk
            onNavigateToForm = { product ->
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("product", product)

                navController.navigate("form_product")
            },

            // 📦 Kelola Pesanan
            onNavigateToManageOrder = {
                navController.navigate("manage_order")
            },

            // 🚪 Logout / Kembali ke Login
            onBackToLogin = {
                navController.navigate("login") {
                    popUpTo("manage_product") { inclusive = true }
                }
            }
        )
    }

    /* =======================================================
     * ADMIN - FORM PRODUCT (TAMBAH / EDIT)
     * ======================================================= */
    composable("form_product") {

        // Ambil data produk dari halaman Manage Product (bisa null)
        val product = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Product>("product")

        HalamanFormProduct(
            viewModel = adminProductViewModel,
            product = product,

            // ⬅️ Kembali ke Manage Product
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}
