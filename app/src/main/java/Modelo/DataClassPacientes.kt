package Modelo

data class DataClassPacientes(
    var id_paciente: Int,
    var nombres_p: String,
    var apellidos_p: String,
    var edad_p: Int,
    var enfermedad_p: String,
    var habitacion: Int,
    var cama: Int,
    var medicamento: String,
    var ingreso: String,
    var hora_aplicacion: String
)
