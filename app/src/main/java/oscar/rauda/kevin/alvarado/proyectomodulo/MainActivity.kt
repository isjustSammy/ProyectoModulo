package oscar.rauda.kevin.alvarado.proyectomodulo

import Modelo.ClaseConexion
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val ListaP = findViewById<Button>(R.id.btnListaP)
        ListaP.setOnClickListener {
            val pacientesL = Intent(this,lista_p ::class.java)
            startActivity(pacientesL)
            overridePendingTransition(0, 0)
        }

        val txtNombrePacientes = findViewById<EditText>(R.id.txtNombreP)
        val txtApellidosPacientes = findViewById<EditText>(R.id.txtApellidosP)
        val txtEdadPacientes = findViewById<EditText>(R.id.txtEdadP)
        val txtEnfermedadPacientes = findViewById<EditText>(R.id.txtEnfermedadP)
        val txtNumHabitacion = findViewById<EditText>(R.id.txtHabitacion)
        val txtNumCama = findViewById<EditText>(R.id.txtCama)
        val txtMedicamentos = findViewById<EditText>(R.id.txtMedicamento)
        val txtFechaIngreso = findViewById<EditText>(R.id.txtIngreso)
        val txtHoraAplicacion = findViewById<EditText>(R.id.txtHoraAplicacion)

        txtFechaIngreso.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val calendarioSeleccionado = Calendar.getInstance()
                    calendarioSeleccionado.set(anioSeleccionado, mesSeleccionado, diaSeleccionado)
                    val fechaSeleccionada = "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtFechaIngreso.setText(fechaSeleccionada)
                },
                anio, mes, dia
            )

            datePickerDialog.show()
        }

        val btnAgregarPacientes = findViewById<Button>(R.id.btnAgregarPacientes)

        btnAgregarPacientes.setOnClickListener {

            val nombre = txtNombrePacientes.text.toString()
            val apellido = txtApellidosPacientes.text.toString()
            val edad = txtEdadPacientes.text.toString()
            val enfermedad = txtEnfermedadPacientes.text.toString()
            val habitacion = txtNumHabitacion.text.toString()
            val cama = txtNumCama.text.toString()
            val medicamento = txtMedicamentos.text.toString()
            val ingreso = txtFechaIngreso.text.toString()
            val hora = txtHoraAplicacion.text.toString()

            if(nombre.isEmpty() || apellido.isEmpty() || edad.isEmpty() || enfermedad.isEmpty() || habitacion.isEmpty() || cama.isEmpty() || medicamento.isEmpty() || ingreso.isEmpty() || hora.isEmpty()){
                Toast.makeText(
                    this,
                    "Complete todos los campos antes de continuar",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                CoroutineScope(Dispatchers.IO).launch {
                    val objConexion = ClaseConexion().cadenaConexion()
                    val addPaciente =
                        objConexion?.prepareStatement("INSERT INTO Paciente(nombres, apellidos, edad, enfermedad, habitacion, cama, medicamento, ingreso, hora_aplicacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")!!
                    addPaciente.setString(1, txtNombrePacientes.text.toString())
                    addPaciente.setString(2, txtApellidosPacientes.text.toString())
                    addPaciente.setInt(3, txtEdadPacientes.text.toString().toInt())
                    addPaciente.setString(4, txtEnfermedadPacientes.text.toString())
                    addPaciente.setInt(5, txtNumHabitacion.text.toString().toInt())
                    addPaciente.setInt(6, txtNumCama.text.toString().toInt())
                    addPaciente.setString(7, txtMedicamentos.text.toString())
                    addPaciente.setString(8, txtFechaIngreso.text.toString())
                    addPaciente.setString(9, txtHoraAplicacion.text.toString())
                    addPaciente.executeUpdate()

                    withContext(Dispatchers.Main) {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("Registro de paciente exitoso!")
                        txtNombrePacientes.setText("")
                        txtApellidosPacientes.setText("")
                        txtEdadPacientes.setText("")
                        txtEnfermedadPacientes.setText("")
                        txtNumHabitacion.setText("")
                        txtNumCama.setText("")
                        txtMedicamentos.setText("")
                        txtFechaIngreso.setText("")
                        txtHoraAplicacion.setText("")
                    }
                }
            }
        }
    }
}