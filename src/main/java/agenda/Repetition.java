package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class Repetition {
    public ChronoUnit getFrequency() {
        return myFrequency;
    }

    /**
     * Stores the frequency of this repetition, one of :
     * <UL>
     * <LI>ChronoUnit.DAYS for daily repetitions</LI>
     * <LI>ChronoUnit.WEEKS for weekly repetitions</LI>
     * <LI>ChronoUnit.MONTHS for monthly repetitions</LI>
     * </UL>
     */
    private final ChronoUnit myFrequency;

    private final Set<LocalDate> myExceptions = new HashSet<>();

    private Termination myTermination = null;

    public Repetition(ChronoUnit myFrequency) {
        this.myFrequency = myFrequency;
    }

    /**
     * Les exceptions à la répétition
     *
     * @param date un date à laquelle l'événement ne doit pas se répéter
     */
    public void addException(LocalDate date) {
        if (date != null) {
            myExceptions.add(date);
        }
    }

    /**
     * La terminaison d'une répétition (optionnelle)
     *
     * @param termination la terminaison de la répétition
     */
    public void setTermination(Termination termination) {
        this.myTermination = termination;
    }

    public boolean isException(LocalDate d) {
        return myExceptions.contains(d);
    }

    public Termination getTermination() {
        return myTermination;
    }
}
