package oscar.rauda.kevin.alvarado.proyectomodulo

import Modelo.ClaseConexion
import Modelo.DataClassPacientes
import RecyclerViewHelpers.AdaptadorPacientes
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class lista_p : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.lista_p)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAgregar = findViewById<Button>(R.id.btnAgregarP)
        btnAgregar.setOnClickListener {
            val AgregarP = Intent(this, MainActivity::class.java)
            startActivity(AgregarP)
            overridePendingTransition(0, 0)
        }

        val rcvPacientes = findViewById<RecyclerView>(R.id.rcvPacientes)

        rcvPacientes.layoutManager = LinearLayoutManager(this)

        fun obtenerPacientes(): List<DataClassPacientes> {
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resulSet = statement?.executeQuery("SELECT * FROM Pacientes")!!
            val listaP = mutableListOf<DataClassPacientes>()
            while (resulSet.next()) {
                val id_paciente = resulSet.getInt("id_paciente")
                val nombres = resulSet.getString("nombres")
                val apellidos = resulSet.getString("apellidos")
                val edad = resulSet.getInt("edad")
                val enfermedad = resulSet.getString("enfermedad")
                val habitacion = resulSet.getInt("habitacion")
                val cama = resulSet.getInt("cama")
                val medicamento = resulSet.getString("medicamentos")
                val ingreso = resulSet.getString("ingreso")
                val hora_aplicacion = resulSet.getString("hora_aplicacion")
                val valoresJuntos = DataClassPacientes(
                    id_paciente,
                    nombres,
                    apellidos,
                    edad,
                    enfermedad,
                    habitacion,
                    cama,
                    medicamento,
                    ingreso,
                    hora_aplicacion
                )
                listaP.add(valoresJuntos)
            }
            return listaP
        }

        CoroutineScope(Dispatchers.IO).launch {
            val nuevosPacientes = obtenerPacientes()
            withContext(Dispatchers.IO){
                (rcvPacientes.adapter as? AdaptadorPacientes)?.refrestLista(nuevosPacientes)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val pacientesBD = obtenerPacientes()
            withContext(Dispatchers.Main) {
                val adapter = AdaptadorPacientes(pacientesBD)
                rcvPacientes.adapter = adapter
            }
        }
    }
}

