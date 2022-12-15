package com.anni.babyweighttracker.calendar.model

import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.Month

@Serializable
data class CalendarDay(val date: Int, val month: Month, val year: Int, val day: DayOfWeek)
