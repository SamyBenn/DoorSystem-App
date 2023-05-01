package door.system

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MyMqtt(private val context : Context,
             private val mqttUrl: String,
             private val mqttPort: String,
             private val mqttUsername: String,
             private val mqttPassword: String) {

    private lateinit var mqttClient: MqttAndroidClient

    private val persistence = MemoryPersistence()

    fun connect(callback: (Boolean) -> Unit) {
        val clientId = MqttClient.generateClientId()
        mqttClient = MqttAndroidClient(context , "tcp://$mqttUrl:$mqttPort", clientId, persistence)

        val options = MqttConnectOptions().apply {
            userName = mqttUsername
            password = mqttPassword.toCharArray()
        }

        mqttClient.connect(options, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MyMqtt", "Connected to MQTT broker")
                callback(true)
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.e("MyMqtt", "Failed to connect to MQTT broker: ${exception?.message}")
                callback(false)
            }
        })
    }

    fun disconnect() {
        mqttClient.disconnect(null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MyMqtt", "Disconnected from MQTT broker")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.e("MyMqtt", "Failed to disconnect from MQTT broker: ${exception?.message}")
            }
        })
    }

    fun publish(topic: String, payload: String) {
        val message = MqttMessage(payload.toByteArray(Charsets.UTF_8))
        message.qos = 1
        mqttClient.publish(topic, message)
    }

    fun subscribe(topic: String, callback: (String) -> Unit) {
        mqttClient.subscribe(topic, 1, object : IMqttMessageListener {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                message?.let {
                    val payload = String(it.payload, Charsets.UTF_8)
                    Log.d("MyMqtt", "Received message on topic '$topic': $payload")
                    callback(payload)
                }
            }
        })
    }

}
