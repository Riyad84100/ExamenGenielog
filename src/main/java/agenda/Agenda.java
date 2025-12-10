package agenda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description : An agenda that stores events
 */
public class Agenda {
    private final List<Event> myEvents = new ArrayList<>();

    /**
     * Adds an event to this agenda
     *
     * @param e the event to add
     */
    public void addEvent(Event e) {
        myEvents.add(e);
    }

    /**
     * Computes the events that occur on a given day
     *
     * @param day the day to test
     * @return a list of events that occur on that day
     */
    public List<Event> eventsInDay(LocalDate day) {
        List<Event> res = new ArrayList<>();
        for (Event e : myEvents) {
            if (e.isInDay(day)) {
                res.add(e);
            }
        }
        return res;
    }

    /**
     * Trouver les événements de l'agenda en fonction de leur titre
     *
     * @param title le titre à rechercher
     * @return les événements qui ont le même titre
     */
    public List<Event> findByTitle(String title) {
        List<Event> res = new ArrayList<>();
        if (title == null) return res;
        for (Event e : myEvents) {
            if (title.equals(e.getTitle())) {
                res.add(e);
            }
        }
        return res;
    }
    /**
     * Déterminer s’il y a de la place dans l'agenda pour un événement (aucun autre événement au même moment)
     * On se limite aux événements sans répétition pour cette méthode (comme demandé).
     *
     * @param e L'événement à tester (on se limitera aux événements sans répétition)
     * @return vrai s’il y a de la place dans l'agenda pour cet événement
     */
    public boolean isFreeFor(Event e) {
        if (e == null) return true;
        if (e.hasRepetition()) {
            // méthode limitée aux événements sans répétition
            throw new UnsupportedOperationException("isFreeFor is limited to non-repetitive events");
        }
        // interval [start, end)
        var startA = e.getStart();
        var endA = e.getStart().plus(e.getDuration());

        for (Event other : myEvents) {
            if (other == e) continue;
            if (other.hasRepetition()) continue; // on ignore répétitifs ici
            var startB = other.getStart();
            var endB = startB.plus(other.getDuration());
            // overlap test
            if (startA.isBefore(endB) && startB.isBefore(endA)) {
                return false;
            }
        }
        return true;
    }
}
