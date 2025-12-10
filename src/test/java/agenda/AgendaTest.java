package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AgendaTest {
    Agenda agenda;

    // November 1st, 2020
    LocalDate nov_1_2020 = LocalDate.of(2020, 11, 1);

    // January 5, 2021
    LocalDate jan_5_2021 = LocalDate.of(2021, 1, 5);

    // November 1st, 2020, 22:30
    LocalDateTime nov_1_2020_22_30 = LocalDateTime.of(2020, 11, 1, 22, 30);

    // 120 minutes
    Duration min_120 = Duration.ofMinutes(120);

    // Un événement simple
    // November 1st, 2020, 22:30, 120 minutes
    Event simple;

    // Un événement qui se répète toutes les semaines et se termine à une date
    // donnée
    Event fixedTermination;

    // Un événement qui se répète toutes les semaines et se termine après un nombre
    // donné d'occurrences
    Event fixedRepetitions;

    // A daily repetitive event, never ending
    // Un événement répétitif quotidien, sans fin
    // November 1st, 2020, 22:30, 120 minutes
    Event neverEnding;

    @BeforeEach
    public void setUp() {
        simple = new Event("Simple event", nov_1_2020_22_30, min_120);

        fixedTermination = new Event("Fixed termination weekly", nov_1_2020_22_30, min_120);
        fixedTermination.setRepetition(ChronoUnit.WEEKS);
        fixedTermination.setTermination(jan_5_2021);

        fixedRepetitions = new Event("Fixed termination weekly", nov_1_2020_22_30, min_120);
        fixedRepetitions.setRepetition(ChronoUnit.WEEKS);
        fixedRepetitions.setTermination(10);

        neverEnding = new Event("Never Ending", nov_1_2020_22_30, min_120);
        neverEnding.setRepetition(ChronoUnit.DAYS);

        agenda = new Agenda();
        agenda.addEvent(simple);
        agenda.addEvent(fixedTermination);
        agenda.addEvent(fixedRepetitions);
        agenda.addEvent(neverEnding);
    }

    @Test
    public void testMultipleEventsInDay() {
        assertEquals(4, agenda.eventsInDay(nov_1_2020).size(),
                "Il y a 4 événements ce jour là");
        assertTrue(agenda.eventsInDay(nov_1_2020).contains(neverEnding));
    }

    @Test
    public void testAddEvent() {
    Event newEvent = new Event("New Event", LocalDateTime.now(), Duration.ofHours(1));
    agenda.addEvent(newEvent);
    assertTrue(agenda.eventsInDay(newEvent.getStart().toLocalDate()).contains(newEvent));
}

    @Test
    public void testFindByTitle() {
    List<Event> found = agenda.findByTitle("Simple event");
    assertEquals(1, found.size());
    assertTrue(found.contains(simple));

    List<Event> notFound = agenda.findByTitle("Inexistant");
    assertTrue(notFound.isEmpty());

    List<Event> nullTitle = agenda.findByTitle(null);
    assertTrue(nullTitle.isEmpty());
    }

    @Test
    public void testIsFreeFor() {
    // null => libre
    assertTrue(agenda.isFreeFor(null));

    // événement répétitif => exception
    assertThrows(UnsupportedOperationException.class, () -> agenda.isFreeFor(neverEnding));

    // chevauchement avec simple
    Event overlap = new Event("Overlap", nov_1_2020_22_30.plusMinutes(30), Duration.ofMinutes(60));
    assertFalse(agenda.isFreeFor(overlap));

    // pas de chevauchement
    Event freeEvent = new Event("Free", nov_1_2020_22_30.plusHours(3), Duration.ofMinutes(60));
    assertTrue(agenda.isFreeFor(freeEvent));
    }

    @Test
    public void testEventsInEmptyDay() {
    Agenda emptyAgenda = new Agenda(); // agenda vide
    LocalDate someDay = LocalDate.of(2025, 1, 1);
    assertTrue(emptyAgenda.eventsInDay(someDay).isEmpty());
}

}
