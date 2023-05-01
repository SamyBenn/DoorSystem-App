package door.system

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

public class LogAdapter(val context: Context, val logList: List<Log>) :
    RecyclerView.Adapter<LogViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.log_layout, parent, false)
        return LogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val currentItem = logList[position]

        holder.logDoor.text = currentItem.doorName.toString()
        holder.logCard.text = currentItem.cardUid.toString()
        holder.logTime.text = currentItem.time.toString()
        holder.logAccess.text = currentItem.accessGranted.toString()
    }

    override fun getItemCount() = logList.size
}