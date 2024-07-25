package Modelo

data class DataClassPacientes(
    var id_paciente: Int,
    var nombres_paciente: String,
    var apellidos_paciente: String,
    var edad_paciente: Int,
    var enfermedad_paciente: String,
    var num_habitacion: Int,
    var num_cama: Int,
    var medicamento: String,
    var fecha_ingreso: String,
    var hora_aplicacion: String
)
