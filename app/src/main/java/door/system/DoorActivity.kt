package door.system

import ApiReq
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.preference.PreferenceManager
import org.eclipse.paho.client.mqttv3.*
import org.json.JSONArray

class DoorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_door)

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

        val linearLayout = findViewById<LinearLayout>(R.id.doorLinearLayout)
        val table = TableLayout(this)
        table.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )

        val headerRow = TableRow(this)
        headerRow.setBackgroundColor(Color.parseColor("#888888"))

        val doorNameHeader = TextView(this)
        doorNameHeader.text = "Door Name"
        doorNameHeader.setTextColor(Color.parseColor("#FFFFFF"))
        doorNameHeader.setPadding(10, 10, 10, 10)
        headerRow.addView(doorNameHeader)

        val doorLocationHeader = TextView(this)
        doorLocationHeader.text = "Door Location"
        doorLocationHeader.setTextColor(Color.parseColor("#FFFFFF"))
        doorLocationHeader.setPadding(10, 10, 10, 10)
        headerRow.addView(doorLocationHeader)

        val openHeader = TextView(this)
        openHeader.text = "Open"
        openHeader.setTextColor(Color.parseColor("#FFFFFF"))
        openHeader.setPadding(10, 10, 10, 10)
        headerRow.addView(openHeader)

        table.addView(headerRow)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val apiUrl = prefs.getString("apiUrl", "137.184.168.254")
        val apiPort = prefs.getString("apiPort", "8080")

        val apiReq = ApiReq
        apiReq.post("http://$apiUrl:$apiPort/api/door/all", "") { response, error ->
            if (response == null) {
                //Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()
                Log.d("LoginActivity", "Server Error")
            } else {
                val jsonArray = JSONArray(response)
                val Doors = ArrayList<HashMap<String, String>>()


                for (i in 0 until jsonArray.length()) {
                    val door = jsonArray.getJSONObject(i)
                    val doorMap = HashMap<String, String>()

                    doorMap["name"] = door.getString("name")
                    doorMap["location"] = door.getString("location")
                    doorMap["description"] = door.getString("description")
                    Doors.add(doorMap)
                }
                for (door in Doors) {
                    val row = TableRow(this)
                    row.setBackgroundColor(Color.parseColor("#000000"))

                    // Define the Door Name column
                    val doorName = TextView(this)
                    doorName.text = door["name"]
                    doorName.textSize = 24f
                    doorName.setTextColor(Color.parseColor("#FFFFFF"))
                    doorName.gravity = Gravity.CENTER
                    doorName.setPadding(10, 10, 10, 10)

                    // Define the Door Location column
                    val doorLocation = TextView(this)
                    doorLocation.text = door["location"]
                    doorLocation.textSize = 24f
                    doorLocation.setTextColor(Color.parseColor("#FFFFFF"))
                    doorLocation.gravity = Gravity.CENTER
                    doorLocation.setPadding(10, 10, 10, 10)

                    // Define the Open button
                    val open = Button(this)
                    open.text = "Open"
                    open.textSize = 24f
                    open.setTextColor(Color.parseColor("#000000"))
                    open.gravity = Gravity.CENTER
                    open.setPadding(10, 10, 10, 10)
                    open.setBackgroundColor(Color.parseColor("#00FF00"))
                    open.setOnClickListener {
                        val mqttUrl = prefs.getString("mqttUrl", "default_value")
                        val mqttPort = prefs.getString("mqttPort", "default_value")
                        val mqttUser = prefs.getString("mqttUser", "default_value")
                        val mqttPass = prefs.getString("mqttPass", "default_value")
                        val mqttTopic = prefs.getString("mqttTopic", "default_value")

                        val myMqtt = MyMqtt(applicationContext, mqttUrl.toString(), mqttPort.toString(), mqttUser.toString(), mqttPass.toString())
                        myMqtt.publish(mqttTopic.toString(), "open")
                    }


                    runOnUiThread {
                        row.addView(doorName)
                        row.addView(doorLocation)
                        row.addView(open)
                        table.addView(row)
                    }
                    // Add the row to the table
                }
            }
        }

        runOnUiThread { linearLayout.addView(table)  }

        val mqttUrl = prefs.getString("mqttUrl", "default_value")
        val mqttPort = prefs.getString("mqttPort", "default_value")
        val mqttUser = prefs.getString("mqttUser", "default_value")
        val mqttPass = prefs.getString("mqttPass", "default_value")



        val myMqtt = MyMqtt(applicationContext, mqttUrl.toString(), mqttPort.toString(), mqttUser.toString(), mqttPass.toString())

    }
}