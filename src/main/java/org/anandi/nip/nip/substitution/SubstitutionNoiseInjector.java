package org.anandi.nip.nip.substitution;

import org.anandi.nip.eventlog.Trace;
import org.anandi.nip.nip.NoiseInjector;
import org.anandi.nip.nip.absence.AbsenceNoiseInjector;
import org.anandi.nip.nip.insertion.ProcessActivityInserter;

import java.util.Random;
import java.util.Set;

public class SubstitutionNoiseInjector implements NoiseInjector {

    private ProcessActivityInserter processActivityInserter;
    private AbsenceNoiseInjector absenceNoiseInjector;

    SubstitutionNoiseInjector(Set<String> activities) {
        this.processActivityInserter = new ProcessActivityInserter(activities);
        this.absenceNoiseInjector = new AbsenceNoiseInjector();
    }
    @Override
    public void injectNoise(Trace cleanTrace, int length) {

    }

    public void substituteRandomActivities(Trace cleanTrace, int length) {
        if (length >= cleanTrace.size()) {
            throw new IllegalArgumentException("The noise length should be smaller than the trace length.");
        }
        int index;
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            index = random.nextInt(cleanTrace.size());
            absenceNoiseInjector.removeActivity(cleanTrace, index);
            processActivityInserter.injectActivity(cleanTrace, index);
        }
    }

    public void substituteConsecutiveActivities(Trace cleanTrace, int length) {
        Random random = new Random();
        int startIndex = random.nextInt(cleanTrace.size() - length + 1);
        substituteConsecutiveActivities(cleanTrace, length, startIndex);
    }

    public void substituteConsecutiveActivities(Trace cleanTrace, int length, int index) {
        if (index >= cleanTrace.size() - length) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for the trace size " + cleanTrace.size());
        }
        absenceNoiseInjector.removeConsecutiveActivities(cleanTrace, length, index);
        processActivityInserter.insertConsecutiveActivities(cleanTrace, length, index);
    }

    public void substituteOneActivity(Trace cleanTrace, int substituteIndex) {
        if (substituteIndex >= cleanTrace.size()) {
            throw new IndexOutOfBoundsException("Index " + substituteIndex + " is out of bounds for the trace size " + cleanTrace.size());
        }
        absenceNoiseInjector.removeActivity(cleanTrace, substituteIndex);
        processActivityInserter.injectActivity(cleanTrace, substituteIndex);
    }
}
