package agenda;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TerminationTest {

    @Test
    public void dateConstructor_computesNumberOfOccurrences_and_getters() {
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 1, 3);

        Termination t = new Termination(start, ChronoUnit.DAYS, end);

        assertEquals(end, t.terminationDateInclusive());
        // between(start, end) = 2 days -> occurrences = 3
        assertEquals(3, t.numberOfOccurrences());
    }

    @Test
    public void dateConstructor_allows_same_day_as_oneOccurrence() {
        LocalDate start = LocalDate.of(2023, 5, 5);
        LocalDate end = LocalDate.of(2023, 5, 5);

        Termination t = new Termination(start, ChronoUnit.DAYS, end);

        assertEquals(end, t.terminationDateInclusive());
        assertEquals(1, t.numberOfOccurrences());
    }

    @Test
    public void dateConstructor_nullArguments_throw() {
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 1, 2);

        assertThrows(IllegalArgumentException.class, () -> new Termination(null, ChronoUnit.DAYS, end));
        assertThrows(IllegalArgumentException.class, () -> new Termination(start, null, end));
        assertThrows(IllegalArgumentException.class, () -> new Termination(start, ChronoUnit.DAYS, null));
    }

    @Test
    public void fixedConstructor_computesTerminationDate_and_getters() {
        LocalDate start = LocalDate.of(2022, 12, 1);

        Termination t = new Termination(start, ChronoUnit.WEEKS, 3);

        // last occurrence index = 3 - 1 = 2 -> plus 2 weeks
        assertEquals(start.plus(2, ChronoUnit.WEEKS), t.terminationDateInclusive());
        assertEquals(3, t.numberOfOccurrences());
    }

    @Test
    public void fixedConstructor_oneOccurrence_results_same_start_date() {
        LocalDate start = LocalDate.of(2024, 2, 29);
        Termination t = new Termination(start, ChronoUnit.MONTHS, 1);

        assertEquals(start, t.terminationDateInclusive());
        assertEquals(1, t.numberOfOccurrences());
    }

    @Test
    public void fixedConstructor_invalidArguments_throw() {
        LocalDate start = LocalDate.of(2023, 6, 1);

        assertThrows(IllegalArgumentException.class, () -> new Termination(null, ChronoUnit.DAYS, 1));
        assertThrows(IllegalArgumentException.class, () -> new Termination(start, null, 1));
        assertThrows(IllegalArgumentException.class, () -> new Termination(start, ChronoUnit.DAYS, 0));
        assertThrows(IllegalArgumentException.class, () -> new Termination(start, ChronoUnit.DAYS, -5));
    }
}
