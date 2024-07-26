package Modelo

data class DetallesPacientes(
    val nombres_p: String,
    val apellidos_p: String,
    val edad_p: Int,
    val enfermedad_p: String,
    val habitacion: Int,
    val cama: Int,
    val medicamento: String,
    val ingreso: String,
    val hora_aplicacion: String
)
