package br.ufs.kryptokarteira.backend.infrastructure.util

import br.ufs.kryptokarteira.backend.domain.UnknownInternalError
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
            else -> throw UnknownInternalError()
        }

        val ddMMyyy = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        return lastWeekDayAvailable.format(ddMMyyy)
    }

}