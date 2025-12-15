package com.example.apponlykick.data.model

import com.google.gson.annotations.SerializedName

//Data class para ENVIAR una nueva venta
data class SaleRequest(
    @SerializedName("usuario") val usuario: UserId,
    @SerializedName("totalVenta") val totalVenta: Double,
    @SerializedName("productosVenta") val productosVenta: List<SaleProductRequest>,
    @SerializedName("direccion") val direccion: DireccionId = DireccionId(1),
    @SerializedName("metodoPago") val metodoPago: MetodoPagoId = MetodoPagoId(1),
    @SerializedName("metodoEnvio") val metodoEnvio: MetodoEnvioId = MetodoEnvioId(1),
    @SerializedName("estadoVenta") val estadoVenta: EstadoVentaId = EstadoVentaId(1)
)

data class SaleProductRequest(
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("precioUnitario") val precioUnitario: Double,
    @SerializedName("producto") val producto: ProductId,
    @SerializedName("talla") val talla: TallaId = TallaId(1),
    @SerializedName("color") val color: ColorId = ColorId(1)
)

// Clases auxiliares para anidar los IDs en el JSON
data class UserId(@SerializedName("id_usuario") val id: Int)
data class ProductId(@SerializedName("id_producto") val id: Int)
data class DireccionId(@SerializedName("id_direccion") val id: Int)
data class MetodoPagoId(@SerializedName("id_metodo_pago") val id: Int)
data class MetodoEnvioId(@SerializedName("id_metodo_envio") val id: Int)
data class EstadoVentaId(@SerializedName("id_estado") val id: Int)
data class TallaId(@SerializedName("id_talla") val id: Int)
data class ColorId(@SerializedName("id_color") val id: Int)


//Data classes para RECIBIR el historial de ventas

data class SaleResponse(
    @SerializedName("idVenta") val idVenta: Int,
    @SerializedName("fechaVenta") val fechaVenta: String,
    @SerializedName("totalVenta") val totalVenta: Double,
    @SerializedName("estadoVenta") val estadoVenta: EstadoVentaResponse,
    @SerializedName("productosVenta") val productosVenta: List<SaleProductResponse>
)

data class SaleProductResponse(
    @SerializedName("id_productos_venta") val id: Int,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("precioUnitario") val precioUnitario: Double,
    @SerializedName("producto") val producto: Product,
    @SerializedName("talla") val talla: TallaResponse,
    @SerializedName("color") val color: ColorResponse
)

data class EstadoVentaResponse(
    @SerializedName("nombre_estado") val nombre: String
)

data class TallaResponse(
    @SerializedName("nombre_talla") val nombre: String
)

data class ColorResponse(
    @SerializedName("nombre_color") val nombre: String
)
