package door.system

import ApiReq
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SimpleAdapter
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray

class LogsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)

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

        val linearLayout = findViewById<LinearLayout>(R.id.logLinearLayout)
        val tableLayout = TableLayout(this)
        tableLayout.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        val headerRow = TableRow(this)
        headerRow.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val header1 = TextView(this)
        header1.text = "Door Name"
        header1.textSize = 24f
        header1.gravity = Gravity.CENTER
        header1.setPadding(16, 16, 16, 16)

        val header2 = TextView(this)
        header2.text = "Card UID"
        header2.textSize = 24f
        header2.gravity = Gravity.CENTER
        header2.setPadding(16, 16, 16, 16)

        val header3 = TextView(this)
        header3.text = "Time"
        header3.textSize = 24f
        header3.gravity = Gravity.CENTER
        header3.setPadding(16, 16, 16, 16)

        headerRow.addView(header1)
        headerRow.addView(header2)
        headerRow.addView(header3)

        headerRow.setBackgroundColor(Color.parseColor("#6200EE"))
        tableLayout.addView(headerRow)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val apiUrl = prefs.getString("apiUrl", "default_value")
        val apiPort = prefs.getString("apiPort", "default_value")

        val apiReq = ApiReq
        apiReq.post("http://$apiUrl:$apiPort/api/log/all", "") { response, error ->
            if (response == null) {
                //Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()
                Log.d("LoginActivity", "Server Error")
            }
            else {
                val jsonArray = JSONArray(response)
                val Logs = ArrayList<HashMap<String, String>>()
                for (i in 0 until jsonArray.length()) {
                    val log = jsonArray.getJSONObject(i)
                    val logMap = HashMap<String, String>()
                    logMap["doorName"] = log.getString("doorName")
                    logMap["cardUid"] = log.getString("cardUid")
                    logMap["time"] = log.getString("time")
                    logMap["accessGranted"] = log.getString("accessGranted")
                    Logs.add(logMap)
                }

                for (log in Logs){
                    Log.d("Log: ", log.toString())
                    val dataRow = TableRow(this)
                    dataRow.layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )

                    val doorName = TextView(this)
                    doorName.text = log["doorName"]
                    doorName.textSize = 24f
                    doorName.gravity = Gravity.CENTER
                    doorName.setPadding(16, 16, 16, 16)

                    val cardUid = TextView(this)
                    cardUid.text = log["cardUid"]
                    cardUid.textSize = 24f
                    cardUid.gravity = Gravity.CENTER
                    cardUid.setPadding(16, 16, 16, 16)

                    val time = TextView(this)
                    time.text = log["time"]
                    time.textSize = 18f
                    time.gravity = Gravity.CENTER
                    time.setPadding(16, 16, 16, 16)

                    dataRow.addView(doorName)
                    dataRow.addView(cardUid)
                    dataRow.addView(time)

                    if (log["accessGranted"] == "1"){
                        dataRow.setBackgroundColor(Color.parseColor("#00FF00"))
                    }
                    else{
                        dataRow.setBackgroundColor(Color.parseColor("#FF0000"))
                    }

                    tableLayout.addView(dataRow)

                }

                runOnUiThread {linearLayout.addView(tableLayout)}

            }
        }
    }
}