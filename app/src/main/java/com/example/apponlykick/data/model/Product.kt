package com.example.apponlykick.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("idProducto") val id: Int,
    @SerializedName("nombreProducto") val name: String,
    @SerializedName("precio") val precioInt: Int? = null,
    @SerializedName("descripcion") val description: String = "",
    @SerializedName("marca") val marcaObj: MarcaDto? = null,
    @SerializedName("imagenes") val imagenesList: List<ImagenDto>? = null
) {

    val brand: String
        get() = marcaObj?.nombre ?: "OnlyKick Original"

    val price: Double
        get() {
            val realPrice = precioInt?.toDouble() ?: 0.0
            return if (realPrice > 0) realPrice else 50000.0 + (id * 1500)
        }

    val imageUrl: String
        get() {
            if (!imagenesList.isNullOrEmpty() && imagenesList[0].url.startsWith("http")) {
                return imagenesList[0].url
            }
            return if (id % 2 == 0) {
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=1000&auto=format&fit=crop" // Zapatilla Roja
            } else {
                "https://images.unsplash.com/photo-1607522370275-f14206abe5d3?q=80&w=1000&auto=format&fit=crop" // Zapatilla Convers
            }
        }
}

data class MarcaDto(@SerializedName("nombreMarca") val nombre: String)
data class ImagenDto(@SerializedName("urlImagen") val url: String)