package door.system

import com.beust.klaxon.Json

public class Log{

    val doorName: String
    val cardUid: String
    val time: String
    val accessGranted: Int

    constructor(doorName: String, cardUid: String, time: String, accessGranted: Int) {
        this.doorName = doorName
        this.cardUid = cardUid
        this.time = time
        this.accessGranted = accessGranted
    }

}