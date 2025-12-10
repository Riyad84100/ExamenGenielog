package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class Termination {

    private final LocalDate terminationDateInclusive;
    private final long numberOfOccurrences; // 0 means "unspecified/infinite"

    public LocalDate terminationDateInclusive() {
        return terminationDateInclusive;
    }

    public long numberOfOccurrences() {
        return numberOfOccurrences;
    }

    /**
     * Constructs a termination at a given date
     *
     * @param start the start time of this event (date)
     * @param frequency one of :
     * <UL>
     *<LI>ChronoUnit.DAYS for daily repetitions</LI>
     *<LI>ChronoUnit.WEEKS for weekly repetitions</LI>
     *<LI>ChronoUnit.MONTHS for monthly repetitions</LI>
     *</UL>
     * @param terminationInclusive the date when this event ends
     * @see ChronoUnit#between(Temporal, Temporal)
     */
    public Termination(LocalDate start, ChronoUnit frequency, LocalDate terminationInclusive) {
        if (start == null || frequency == null || terminationInclusive == null) {
            throw new IllegalArgumentException("start, frequency and terminationInclusive must be non-null");
        }
        this.terminationDateInclusive = terminationInclusive;
        long between = frequency.between(start, terminationInclusive);
        this.numberOfOccurrences = between + 1; // include the first occurrence
    }

    /**
     * Constructs a fixed termination event ending after a number of iterations
     *
     * @param start the start time of this event
     * @param frequency one of :
     *<UL>
     *<LI>ChronoUnit.DAYS for daily repetitions</LI>
     *<LI>ChronoUnit.WEEKS for weekly repetitions</LI>
     *<LI>ChronoUnit.MONTHS for monthly repetitions</LI>
     *</UL>
     * @param numberOfOccurrences the number of occurrences of this repetitive event
     */
    public Termination(LocalDate start, ChronoUnit frequency, long numberOfOccurrences) {
        if (start == null || frequency == null || numberOfOccurrences <= 0) {
            throw new IllegalArgumentException("start and frequency must be non-null and numberOfOccurrences > 0");
        }
        this.numberOfOccurrences = numberOfOccurrences;
        // last occurrence index = numberOfOccurrences - 1
        this.terminationDateInclusive = start.plus(numberOfOccurrences - 1, frequency);
    }
}
