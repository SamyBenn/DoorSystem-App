package door.system

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

public open class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val logDoor : TextView
    val logCard : TextView
    val logTime : TextView
    val logAccess : TextView


    init {
        logDoor = itemView.findViewById(R.id.logDoorName)
        logCard = itemView.findViewById(R.id.logCardUid)
        logTime = itemView.findViewById(R.id.logTime)
        logAccess = itemView.findViewById(R.id.logAccessGranted)
    }
}