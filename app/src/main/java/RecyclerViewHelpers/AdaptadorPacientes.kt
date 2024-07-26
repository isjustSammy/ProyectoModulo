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
        N_NombresP: String,
        N_apellidosP: String,
        N_edadP: Int,
        N_enfermedadP: String,
        N_habitacion: Int,
        N_cama: Int,
        N_medicamento: String,
        N_ingreso: String,
        N_hora_aplicacion: String
    ) {

        val index = Datos.indexOfFirst { it.id_paciente == id_paciente }
        if (index != -1) {
            Datos[index].nombres_p = N_NombresP
            Datos[index].apellidos_p = N_apellidosP
            Datos[index].edad_p = N_edadP
            Datos[index].enfermedad_p = N_enfermedadP
            Datos[index].habitacion = N_habitacion
            Datos[index].cama = N_cama
            Datos[index].medicamento = N_medicamento
            Datos[index].ingreso = N_ingreso
            Datos[index].hora_aplicacion = N_hora_aplicacion
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
                Nnombres_paciente: String,
                Napellidos_paciente: String,
                Nedad_paciente: Int,
                Nenfermedad_paciente: String,
                Nnum_habitacion: Int,
                Nnum_cama: Int,
                Nmedicamentos: String,
                Nfecha_ingreso: String,
                Nhora_aplicacion: String,
                id_paciente: Int
            ) {
                GlobalScope.launch(Dispatchers.IO) {
                    val objConexion = ClaseConexion().cadenaConexion()
                    if (objConexion != null) {
                        try {
                            val actualizarPaciente =
                                objConexion.prepareStatement("update Paciente set nombres =?, apellidos = ?, edad = ?, enfermedad = ?, habitacion = ?, cama = ?, medicamento = ?, ingreso = ?, hora_aplicacion = ? where id_paciente = ?")!!
                            actualizarPaciente.setString(1, Nnombres_paciente)
                            actualizarPaciente.setString(2, Napellidos_paciente)
                            actualizarPaciente.setInt(3, Nedad_paciente)
                            actualizarPaciente.setString(4, Nenfermedad_paciente)
                            actualizarPaciente.setInt(5, Nnum_habitacion)
                            actualizarPaciente.setInt(6, Nnum_cama)
                            actualizarPaciente.setString(7, Nmedicamentos)
                            actualizarPaciente.setString(8, Nfecha_ingreso)
                            actualizarPaciente.setString(9, Nhora_aplicacion)
                            actualizarPaciente.setInt(10, id_paciente)
                            actualizarPaciente.executeUpdate()

                            withContext(Dispatchers.Main) {
                                refrestPantalla(
                                    id_paciente,
                                    Nnombres_paciente,
                                    Napellidos_paciente,
                                    Nedad_paciente,
                                    Nenfermedad_paciente,
                                    Nnum_habitacion,
                                    Nnum_cama,
                                    Nmedicamentos,
                                    Nfecha_ingreso,
                                    Nhora_aplicacion
                                )
                                Toast.makeText(
                                    context,
                                    "Todo se actualizo joya",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Revise los datos", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                    }
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPacientes {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.carta_p , parent, false)
        return ViewHolderPacientes(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderPacientes, position: Int) {
        val item = Datos[position]
        holder.txtPacienteNombre.text = item.nombres_p
        holder.txtPacienteApellido.text = item.apellidos_p

        holder.btnBorrar.setOnClickListener {
            val context = holder.itemView.context

            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            builder.setTitle("Borrar")
            builder.setMessage("Estás seguro de borrar este paciente?")

            builder.setPositiveButton("Si") { dialog, which ->
                BorrarData(context, item.id_paciente, position)
            }

            builder.setNeutralButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        }

    }

}

