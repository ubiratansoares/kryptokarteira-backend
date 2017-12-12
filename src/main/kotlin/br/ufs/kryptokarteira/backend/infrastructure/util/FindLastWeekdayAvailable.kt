package br.ufs.kryptokarteira.backend.infrastructure.util

import java.time.DayOfWeek.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object FindLastWeekdayAvailable {

    operator fun invoke(reference: LocalDateTime): String {
        val weekDay = from(reference)

        val lastWeekDayAvailable = when (weekDay) {
            SUNDAY -> reference.minusDays(2)
            MONDAY -> reference.minusDays(3)
            SATURDAY,
            TUESDAY,
            WEDNESDAY,
            THURSDAY,
            FRIDAY -> reference.minusDays(1)
        }

        val ddMMyyy = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return lastWeekDayAvailable.format(ddMMyyy)
    }

}