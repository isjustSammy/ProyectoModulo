package oscar.rauda.kevin.alvarado.proyectomodulo

import Modelo.ClaseConexion
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        val txtnombre = findViewById<EditText>(R.id.txtNombreInicio)
        val txtContrasena = findViewById<EditText>(R.id.txtContrasenaInicio)
        val btnLogin = findViewById<Button>(R.id.btnIniciarSesion)

        btnLogin.setOnClickListener {

            val nombre = txtnombre.text.toString()
            val contrasena = txtContrasena.text.toString()

            if (nombre.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(
                    this,
                    "Error, para acceder debes llenar todas las casillas.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val pantallaInicio = Intent(this, activity_Inicio::class.java)

                CoroutineScope(Dispatchers.IO).launch {

                    val objConexion = ClaseConexion().cadenaConexion()

                    val comprobarUsuario =
                        objConexion?.prepareStatement("SELECT * FROM Usuarios WHERE nombre = ? AND contrasena    = ?")!!
                    comprobarUsuario.setString(1, txtnombre.text.toString())
                    comprobarUsuario.setString(2, txtContrasena.text.toString())
                }
            }
        }

        fun onBackPressed() {
            super.onBackPressed()
            Toast.makeText(this@MainActivity, "Chao", Toast.LENGTH_SHORT).show()
            finishAffinity()
        }
    }
}