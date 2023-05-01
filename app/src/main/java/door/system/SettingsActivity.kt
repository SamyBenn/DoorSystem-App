package door.system

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceScreen

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        val btnDoor = findViewById<ImageButton>(R.id.btnDoor)
        val btnSettings = findViewById<ImageButton>(R.id.btnSettings)
        val btnLogs = findViewById<ImageButton>(R.id.btnLogs)
        btnDoor.setOnClickListener {
            val doorAct = Intent(this, DoorActivity::class.java)
            startActivity(doorAct)
        }
        btnSettings.setOnClickListener {
            val settingsAct = Intent(this, SettingsActivity::class.java)
            startActivity(settingsAct)
        }
        btnLogs.setOnClickListener {
            val logsAct = Intent(this, LogsActivity::class.java)
            startActivity(logsAct)
        }

        supportFragmentManager.beginTransaction().add(R.id.line_main, SettingsFragment()).commit()

    }
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }
    }
}