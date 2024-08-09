package com.macrew.medirydes.requestTimeOff.model

import com.google.gson.JsonObject
import com.macrew.medirydes.dashboard.model.avalibilty.WorkDay

data class WorkDayObj(
    val obj: HashMap<JsonObject,ArrayList<WorkDay>>
)
