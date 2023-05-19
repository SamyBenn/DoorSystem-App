package door.system

import ApiReq
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.content.Intent
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build()
        )
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        //language
        if (prefs.getString("Lang","En").toString() == "Fr"){
            val txtSingin = findViewById<TextView>(R.id.signin)
            val txtforpass = findViewById<TextView>(R.id.forgotpass)
            runOnUiThread{
                edtEmail.hint = "Courriel"
                edtPassword.hint = "Mot de passe"
                btnLogin.text = "Se Connecter"
                txtSingin.text = "Connexion"
                txtforpass.text = "Mot de passe oublié"
            }
        }


        val apiReq = ApiReq

        val apiUrl = prefs.getString("apiUrl", "137.184.168.254")
        val apiPort = prefs.getString("apiPort", "8080")

        btnLogin.setOnClickListener {
            val doorAct = Intent(this, DoorActivity::class.java)
            //startActivity(doorAct)
            apiReq.post("http://$apiUrl:$apiPort/api/user/auth", "{\"email\":\""+edtEmail.text.toString()+"\",\"password\":\""+edtPassword.text.toString()+"\"}") { response, error ->
                if (response == null) {
                    //Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()
                    Log.d("LoginActivity", "Server Error")
                }
                else {
                    if (response.toString() == "1") {
                        val doorAct = Intent(this, DoorActivity::class.java)
                        startActivity(doorAct)
                    }
                    else {
                        //Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                        Log.d("LoginActivity", "Login Failed")
                    }
                }
            }
        }

    }
}