package com.example.apponlykick.repository

import com.example.apponlykick.data.model.ProductId
import com.example.apponlykick.data.model.SaleProductRequest
import com.example.apponlykick.data.model.SaleRequest
import com.example.apponlykick.data.model.SaleResponse
import com.example.apponlykick.data.model.UserId
import com.example.apponlykick.data.remote.ApiClient
import com.example.apponlykick.ui.screens.CartItemState
import com.example.apponlykick.ui.viewmodel.User

class SaleRepository {
    private val api = ApiClient.service

    suspend fun createSale(user: User, cartItems: List<CartItemState>, total: Double): SaleResponse? {
        return try {
            //Mapear los items del carrito al formato que espera la API
            val saleProducts = cartItems.map {
                SaleProductRequest(
                    cantidad = it.quantity,
                    precioUnitario = it.product.price,
                    producto = ProductId(it.product.id)
                )
            }

            //Construir el objeto de la petición de venta
            val saleRequest = SaleRequest(
                usuario = UserId(user.id.toInt()),
                totalVenta = total,
                productosVenta = saleProducts
            )

            //Realizar la llamada a la API
            val response = api.createSale(saleRequest)

            //Devolver el cuerpo de la respuesta si fue exitosa
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getSalesHistory(userId: Int): List<SaleResponse>? {
        return try {
            val response = api.getSalesHistory(userId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
