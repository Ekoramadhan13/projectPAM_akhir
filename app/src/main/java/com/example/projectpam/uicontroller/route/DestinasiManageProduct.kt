package com.example.projectpam.uicontroller.route

import androidx.compose.runtime.Composable
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
    composable("manage_product") {
        HalamanManageProduct(viewModel = adminProductViewModel, onNavigateToForm = { product ->
            navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
            navController.navigate("form_product")
        })
    }

    composable("form_product") {
        val product = navController.previousBackStackEntry?.savedStateHandle?.get<Product>("product")
        HalamanFormProduct(
            viewModel = adminProductViewModel,
            product = product,
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
