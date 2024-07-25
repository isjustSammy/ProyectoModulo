package RecyclerViewHelpers

import Modelo.ClaseConexion
import Modelo.DataClassPacientes
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import oscar.rauda.kevin.alvarado.proyectomodulo.R
import java.sql.SQLException

class AdaptadorPacientes(var Datos: List<DataClassPacientes>): RecyclerView.Adapter<ViewHolderPacientes>() {

    fun refrestLista(ListaNueva: List<DataClassPacientes>) {
        Datos = ListaNueva
        notifyDataSetChanged()
    }

    fun refrestPantalla(
        id_paciente: Int,
        Nuevo_Nombres_paciente: String,
        Nuevo_apellidos_paciente: String,
        Nuevo_edad_paciente: Int,
        Nuevo_enfermedad_paciente: String,
        Nuevo_num_habitacion: Int,
        Nuevo_num_cama: Int,
        Nuevo_medicamentos: String,
        Nuevo_fecha_ingreso: String,
        Nuevo_hora_aplicacion: String
    ) {
        val index = Datos.indexOfFirst { it.id_paciente == id_paciente }
        if (index != -1) {
            Datos[index].nombres_paciente = Nuevo_Nombres_paciente
            Datos[index].apellidos_paciente = Nuevo_apellidos_paciente
            Datos[index].edad_paciente = Nuevo_edad_paciente
            Datos[index].enfermedad_paciente = Nuevo_enfermedad_paciente
            Datos[index].num_habitacion = Nuevo_num_habitacion
            Datos[index].num_cama = Nuevo_num_cama
            Datos[index].medicamento = Nuevo_medicamentos
            Datos[index].fecha_ingreso = Nuevo_fecha_ingreso
            Datos[index].hora_aplicacion = Nuevo_hora_aplicacion
            notifyItemChanged(index)
        }
    }

    fun BorrarData(context: Context, id_paciente: Int, position: Int) {
        val dataList = Datos.toMutableList()
        dataList.removeAt(position)

        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()
            if (objConexion != null) {
                try {
                    objConexion.autoCommit = false

                    val borrarPaciente =
                        objConexion.prepareStatement("delete from Pacientes where id_paciente = ?")!!
                    borrarPaciente.setInt(1, id_paciente)
                    val pacienteEliminado = borrarPaciente.executeUpdate()
                    Log.d("BorrarData", "Eliminado correctamente: $pacienteEliminado")

                    objConexion.commit()
                    Log.d("BorrarData", "Commit exitoso")

                    withContext(Dispatchers.Main) {
                        Datos = dataList.toList()
                        notifyItemRemoved(position)
                        notifyDataSetChanged()
                        Toast.makeText(
                            context,
                            "Paciente borrado correctamente",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } catch (e: SQLException) {
                    Log.e("BorrarData", "error sql", e)
                }
            }

            fun refrestData(
                context: Context,
                NuevoNombres_paciente: String,
                Nuevoapellidos_paciente: String,
                Nuevoedad_paciente: Int,
                Nuevoenfermedad_paciente: String,
                Nuevonum_habitacion: Int,
                Nuevonum_cama: Int,
                Nuevomedicamentos: String,
                Nuevofecha_ingreso: String,
                Nuevohora_aplicacion: String,
                id_paciente: Int
            ) {
                GlobalScope.launch(Dispatchers.IO) {
                    val objConexion = ClaseConexion().cadenaConexion()
                    if (objConexion != null) {
                        try {
                            val actualizarPaciente =
                                objConexion.prepareStatement("update Pacientes set nombres =?, apellidos = ?, edad = ?, enfermedad = ?, num_habitacion = ?, num_cama = ?, medicamentos = ?, fecha_ingreso = ?, hora_aplicacion = ? where id_paciente = ?")!!
                            actualizarPaciente.setString(1, NuevoNombres_paciente)
                            actualizarPaciente.setString(2, Nuevoapellidos_paciente)
                            actualizarPaciente.setInt(3, Nuevoedad_paciente)
                            actualizarPaciente.setString(4, Nuevoenfermedad_paciente)
                            actualizarPaciente.setInt(5, Nuevonum_habitacion)
                            actualizarPaciente.setInt(6, Nuevonum_cama)
                            actualizarPaciente.setString(7, Nuevomedicamentos)
                            actualizarPaciente.setString(8, Nuevofecha_ingreso)
                            actualizarPaciente.setString(9, Nuevohora_aplicacion)
                            actualizarPaciente.setInt(10, id_paciente)
                            actualizarPaciente.executeUpdate()

                            withContext(Dispatchers.Main) {
                                refrestPantalla(
                                    id_paciente,
                                    NuevoNombres_paciente,
                                    Nuevoapellidos_paciente,
                                    Nuevoedad_paciente,
                                    Nuevoenfermedad_paciente,
                                    Nuevonum_habitacion,
                                    Nuevonum_cama,
                                    Nuevomedicamentos,
                                    Nuevofecha_ingreso,
                                    Nuevohora_aplicacion
                                )
                                Toast.makeText(
                                    context,
                                    "Todo se actualizo joya",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Revisa bien eso", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                    }
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPacientes {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout., parent, false)
        return ViewHolderPacientes(vista)
    }

}

