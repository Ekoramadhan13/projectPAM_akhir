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
    // ================= ADMIN MANAGE PRODUCT =================
    composable("manage_product") {
        HalamanManageProduct(
            viewModel = adminProductViewModel,
            onNavigateToForm = { product ->
                // Simpan product yang akan diedit, jika null berarti tambah baru
                navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
                navController.navigate("form_product")
            },
            onBackToLogin = {
                // Navigasi kembali ke halaman login dan hapus manage_product dari backstack
                navController.navigate("login") {
                    popUpTo("manage_product") { inclusive = true }
                }
            }
        )
    }

    // ================= ADMIN FORM PRODUCT =================
    composable("form_product") {
        // Ambil product yang dikirim dari ManageProduct, bisa null untuk tambah produk
        val product = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Product>("product")

        HalamanFormProduct(
            viewModel = adminProductViewModel,
            product = product,
            onNavigateBack = {
                // Kembali ke ManageProduct setelah simpan
                navController.popBackStack()
            }
        )
    }
}
