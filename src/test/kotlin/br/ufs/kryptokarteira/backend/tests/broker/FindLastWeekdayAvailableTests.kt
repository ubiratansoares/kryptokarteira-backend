package br.ufs.kryptokarteira.backend.tests.broker

import br.ufs.kryptokarteira.backend.infrastructure.util.FindLastWeekdayAvailable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class FindLastWeekdayAvailableTests {

    @Test fun `should use yesterday, when today not sunday not monday`() {
        val reference = LocalDate.of(2017, 12, 12)
        val datetime = reference.atTime(10, 0)

        assertThat(DayOfWeek.from(datetime)).isEqualTo(DayOfWeek.TUESDAY)
        val yesterday = "11-12-2017"
        assertThat(FindLastWeekdayAvailable(datetime)).isEqualTo(yesterday)
    }

    @Test fun `should use last friday, today is monday`() {
        val reference = LocalDate.of(2017, 12, 11)
        val datetime = reference.atTime(10, 0)

        assertThat(DayOfWeek.from(datetime)).isEqualTo(DayOfWeek.MONDAY)

        val lastFriday = "08-12-2017"
        assertThat(FindLastWeekdayAvailable(datetime)).isEqualTo(lastFriday)
    }

    @Test fun `should use last friday, today is sunday`() {
        val reference = LocalDate.of(2017, 12, 10)
        val datetime = reference.atTime(10, 0)

        assertThat(DayOfWeek.from(datetime)).isEqualTo(DayOfWeek.SUNDAY)

        val lastFriday = "08-12-2017"
        assertThat(FindLastWeekdayAvailable(datetime)).isEqualTo(lastFriday)
    }
}