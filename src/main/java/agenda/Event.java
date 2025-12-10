package agenda;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class Event {

    /**
     * The myTitle of this event
     */
    private String myTitle;

    /**
     * The starting time of the event
     */
    private LocalDateTime myStart;

    /**
     * The durarion of the event
     */
    private Duration myDuration;

    /**
     * Optional repetition data (null si événement simple)
     */
    private Repetition myRepetition;

    /**
     * Constructs an event
     *
     * @param title    the title of this event
     * @param start    the start time of this event
     * @param duration the duration of this event
     */
    public Event(String title, LocalDateTime start, Duration duration) {
        this.myTitle = title;
        this.myStart = start;
        this.myDuration = duration;
    }

    public void setRepetition(ChronoUnit frequency) {
        if (frequency == null) {
            myRepetition = null;
        } else {
            myRepetition = new Repetition(frequency);
        }
    }

    public void addException(LocalDate date) {
        if (myRepetition == null) {
            // pas de répétition -> rien à faire
            return;
        }
        myRepetition.addException(date);
    }

    public void setTermination(LocalDate terminationInclusive) {
        if (myRepetition == null) {
            // pas de répétition -> rien à faire
            return;
        }
        Termination t = new Termination(myStart.toLocalDate(), myRepetition.getFrequency(), terminationInclusive);
        myRepetition.setTermination(t);
    }

    public void setTermination(long numberOfOccurrences) {
        if (myRepetition == null) {
            // pas de répétition -> rien à faire
            return;
        }
        Termination t = new Termination(myStart.toLocalDate(), myRepetition.getFrequency(), numberOfOccurrences);
        myRepetition.setTermination(t);
    }

    public int getNumberOfOccurrences() {
    // Cas dun événement simple sans répét
    if (myRepetition == null) return 1; 

    Termination t = myRepetition.getTermination();

    // Cas dune répét illimitée ac pas de terminaison déf
    if (t == null) {
        
        return -1; 
    }
    
    // 3. Cas dune terminaison fixe
    long n = t.numberOfOccurrences();
    
    
    return (int) n;
}

    public LocalDate getTerminationDate() {
        if (myRepetition == null) return null;
        Termination t = myRepetition.getTermination();
        if (t == null) return null;
        return t.terminationDateInclusive();
    }

    /**
     * Tests if an event occurs on a given day
     *
     * @param aDay the day to test
     * @return true if the event occurs on that day, false otherwise
     */
    public boolean isInDay(LocalDate aDay) {
    if (aDay == null) return false;

    // si la répétition existe et que ce jour est déclaré comme exception, alors
    // le jour est exclu, même si une occurrence précédente déborde sur lui
    if (myRepetition != null && myRepetition.isException(aDay)) {
        return false;
    }

    LocalDate startDate = myStart.toLocalDate();
    LocalDateTime startDateTime = myStart;
    LocalDateTime endDateTime = myStart.plus(myDuration);

    // If no repetition: just test overlap with [start, end)
    if (myRepetition == null) {
        LocalDate endDate = endDateTime.toLocalDate();
        // event occurs on day if aDay is between startDate and endDate inclusive
        return !(aDay.isBefore(startDate)) && !(aDay.isAfter(endDate));
    }

    // With repetition: need to check if any occurrence (including the one preceding aDay)
    ChronoUnit freq = myRepetition.getFrequency();
    LocalDate base = startDate;

    // if aDay is before start -> can't occur
    if (aDay.isBefore(base)) return false;

    // compute the index of occurrence that is "aligned" with aDay (could be 0)
    long idx = freq.between(base, aDay);

    // check candidate occurrences: idx and idx-1 (previous one may spill into aDay)
    for (long k = Math.max(0, idx - 1); k <= idx; k++) {
        LocalDateTime occStart = myStart.plus(k, freq);
        if (occStart.toLocalDate().isAfter(aDay)) continue; // occurrence starts after the day

        // Check termination if present
        Termination term = myRepetition.getTermination();
        if (term != null) {
            // If termination is defined by date, ensure occStart.date <= termination date
            LocalDate termDate = term.terminationDateInclusive();
            if (termDate != null && occStart.toLocalDate().isAfter(termDate)) {
                continue;
            }
            // If termination is defined by numberOfOccurrences, ensure k < numberOfOccurrences
            long nb = term.numberOfOccurrences();
            if (nb > 0 && k >= nb) continue;
        }

        // Check exceptions for the occurrence start date: skip if the occurrence itself is excluded
        if (myRepetition.isException(occStart.toLocalDate())) {
            continue;
        }

        // compute occ end date
        LocalDateTime occEnd = occStart.plus(myDuration);
        LocalDate occStartDate = occStart.toLocalDate();
        LocalDate occEndDate = occEnd.toLocalDate();

        if (!(aDay.isBefore(occStartDate)) && !(aDay.isAfter(occEndDate))) {
            return true;
        }
    }

    return false;
}


    /**
     * @return the myTitle
     */
    public String getTitle() {
        return myTitle;
    }

    /**
     * @return the myStart
     */
    public LocalDateTime getStart() {
        return myStart;
    }


    /**
     * @return the myDuration
     */
    public Duration getDuration() {
        return myDuration;
    }

    public boolean hasRepetition() {
        return myRepetition != null;
    }

    /* package-private getters for repetition (useful if needed elsewhere) */
    Repetition getRepetition() {
        return myRepetition;
    }

    @Override
    public String toString() {
        return "Event{title='%s', start=%s, duration=%s}".formatted(myTitle, myStart, myDuration);
    }
}
