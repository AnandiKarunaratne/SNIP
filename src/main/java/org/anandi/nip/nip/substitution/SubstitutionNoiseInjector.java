package org.anandi.nip.nip.substitution;

import org.anandi.nip.eventlog.Trace;
import org.anandi.nip.nip.NoiseInjector;
import org.anandi.nip.nip.absence.AbsenceNoiseInjector;
import org.anandi.nip.nip.insertion.ProcessActivityInserter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SubstitutionNoiseInjector implements NoiseInjector {

    private final ProcessActivityInserter processActivityInserter;
    private final AbsenceNoiseInjector absenceNoiseInjector;

    SubstitutionNoiseInjector(Set<String> activities) {
        this.processActivityInserter = new ProcessActivityInserter(activities);
        this.absenceNoiseInjector = new AbsenceNoiseInjector();
    }
    @Override
    public void injectNoise(Trace cleanTrace, int length) {
        double probability = 0.5; // substituting activities randomly or consecutively have equal probabilities
        injectNoise(cleanTrace, length, probability);
    }

    @Override
    public void injectNoise(Trace cleanTrace, int length, double probability) {
        double methodDecider = Math.random();
        if (methodDecider < probability) {
            substituteConsecutiveActivities(cleanTrace, length);
        } else {
            substituteRandomActivities(cleanTrace, length);
        }
    }

    public void substituteRandomActivities(Trace cleanTrace, int length) {
        if (length > cleanTrace.size()) {
            throw new IllegalArgumentException("The substitution length (" + length + ") should not be larger than the trace length (" + cleanTrace.size() + ").");
        }

        Random random = new Random();
        Set<Integer> indexes = new HashSet<>();
        do {
            indexes.add(random.nextInt(cleanTrace.size()));
        } while (indexes.size() < length);

        for (int index : indexes) {
            substituteOneActivity(cleanTrace, index);
        }
    }

    public void substituteConsecutiveActivities(Trace cleanTrace, int length) {
        if (length > cleanTrace.size()) {
            throw new IllegalArgumentException("The substitution length (" + length + ") should not be larger than the trace length (" + cleanTrace.size() + ").");
        }
        Random random = new Random();
        int startIndex = random.nextInt(cleanTrace.size() - length + 1);
        substituteConsecutiveActivities(cleanTrace, length, startIndex);
    }

    public void substituteConsecutiveActivities(Trace cleanTrace, int length, int index) {
        if (index > cleanTrace.size() - length) {
            throw new IllegalArgumentException("Unable to substitute a sequence of length " + length + " at the index " + index +  " for the trace size " + cleanTrace.size() + ".");
        }
        Trace baseTrace = new Trace(cleanTrace); // make sure the substitution makes the trace noisy.
        do {
            absenceNoiseInjector.removeConsecutiveActivities(cleanTrace, length, index);
            processActivityInserter.insertConsecutiveActivities(cleanTrace, length, index);
        } while (cleanTrace.equals(baseTrace));
    }

    public void substituteOneActivity(Trace cleanTrace, int index) {
        if (index >= cleanTrace.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for the trace size " + cleanTrace.size() + ".");
        }
        substituteConsecutiveActivities(cleanTrace, 1, index);
    }
}
