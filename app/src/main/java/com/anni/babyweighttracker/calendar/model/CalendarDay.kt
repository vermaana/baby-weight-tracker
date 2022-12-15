package com.anni.babyweighttracker.calendar.model

import kotlinx.serialization.Serializable
import java.time.DayOfWeek

@Serializable
data class CalendarDay(val date: Int, val month: Int, val year: Int, val day: DayOfWeek)
