package com.example.apponlykick.model

import androidx.annotation.DrawableRes
import com.example.apponlykick.R
import com.example.apponlykick.model.mockProductList

data class Product(
    val id: Int,
    val name: String,
    val brand: String,
    val price: Double,
    @DrawableRes val imageResId: Int  // Se le asigna un int para el ID
)

val mockProductList = listOf(
    Product(
        id = 1,
        name = "Nike Air Force 1 Black", // Nombre de la zapatilla (zapatilla_1.webp)
        brand = "Nike",
        price = 129.99,
        imageResId = R.drawable.zapatilla_1 // Le asignamos la Imagen de la zapatilla
    ),
    Product(
        id = 2,
        name = "Jordan Retro IV", // Nombre de la zapatilla (zapatilla_2.webp)
        brand = "Jordan",
        price = 100.00,
        imageResId = R.drawable.zapatilla_2
    ),
    Product(
        id = 3,
        name = "Vans Upland 'Black White'", // Nombre de la zapatilla (zapatilla_3.webp)
        brand = "Vans",
        price = 110.00,
        imageResId = R.drawable.zapatilla_3
    ),
    Product(
        id = 4,
        name = "Nike Air Force 1 White", // Nombre de la zapatilla (zapatilla_4.webp)
        brand = "Nike",
        price = 65.00,
        imageResId = R.drawable.zapatilla_4
    )
)