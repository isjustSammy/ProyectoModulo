package Modelo

data class DetallesPacientes(
    val nombres_paciente: String,
    val apellidos_paciente: String,
    val edad_paciente: Int,
    val enfermedad_paciente: String,
    val num_habitacion: Int,
    val num_cama: Int,
    val medicamento: String,
    val fecha_ingreso: String,
    val hora_aplicacion: String
)
