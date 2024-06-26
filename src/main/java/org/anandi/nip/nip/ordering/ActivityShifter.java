package org.anandi.nip.nip.ordering;

import org.anandi.nip.eventlog.Trace;

import java.util.Random;

public class ActivityShifter extends OrderingNoiseInjector {

    @Override
    public void injectNoise(Trace cleanTrace, int length, double probability) {
        double methodDecider = Math.random();
        if (methodDecider < probability) {
            shiftActivitiesToLeft(cleanTrace, length);
        } else {
            shiftActivitiesToRight(cleanTrace, length);
        }
    }

    public void shiftActivitiesToLeft(Trace cleanTrace, int length) {
        if (cleanTrace.size() < length) {
            throw new IllegalArgumentException("The trace should be longer than the length of the subtrace to be shifted.");
        }
        Random random = new Random();
        int startIndex = random.nextInt(cleanTrace.size() - length) + 1;
        int shiftIndex;
        do {
            shiftIndex = random.nextInt(cleanTrace.size() - length);
        } while (startIndex <= shiftIndex);

        shiftActivitiesToLeft(cleanTrace, length, startIndex, shiftIndex);
    }

    public void shiftActivitiesToLeft(Trace cleanTrace, int length, int startIndex, int shiftIndex) {
        if (cleanTrace.size() < length) {
            throw new IllegalArgumentException("The trace should be longer than the length of the subtrace to be shifted.");
        }
        if (startIndex <= shiftIndex) {
            throw new IllegalArgumentException("Start index should be larger than shift index.");
        }
        if (shiftIndex >= cleanTrace.size() - length || shiftIndex < 0) {
            throw new IllegalArgumentException("Invalid shift index.");
        }
        if (startIndex > cleanTrace.size() - length) {
            throw new IllegalArgumentException("Invalid start index.");
        }
        Trace shiftingSubtrace = new Trace(cleanTrace.subList(startIndex, startIndex + length));
        Trace originalTrace = new Trace(cleanTrace);
        for (int i = shiftIndex; i < startIndex; i++) {
            cleanTrace.set(i + length, originalTrace.get(i));
        }
        for (int i = 0; i < length; i++) {
            cleanTrace.set(shiftIndex + i, shiftingSubtrace.get(i));
        }
    }

    public void shiftActivitiesToRight(Trace cleanTrace, int length) {
        if (cleanTrace.size() < length) {
            throw new IllegalArgumentException("The trace should be longer than the length of the subtrace to be shifted.");
        }
        Random random = new Random();
        int startIndex = random.nextInt(cleanTrace.size() - length);
        int shiftIndex;
        do {
            shiftIndex = random.nextInt(cleanTrace.size() - length) + 1;
        } while (startIndex >= shiftIndex);

        shiftActivitiesToRight(cleanTrace, length, startIndex, shiftIndex);
    }

    public void shiftActivitiesToRight(Trace cleanTrace, int length, int startIndex, int shiftIndex) {
        if (cleanTrace.size() < length) {
            throw new IllegalArgumentException("The trace should be longer than the length of the subtrace to be shifted.");
        }
        if (startIndex >= shiftIndex) {
            throw new IllegalArgumentException("Start index should be smaller than shift index.");
        }
        if (startIndex >= cleanTrace.size() - length || startIndex < 0) {
            throw new IllegalArgumentException("Invalid start index.");
        }
        if (shiftIndex > cleanTrace.size() - length) {
            throw new IllegalArgumentException("Invalid shift index.");
        }

        Trace shiftingSubtrace = new Trace(cleanTrace.subList(startIndex, startIndex + length));
        Trace originalTrace = new Trace(cleanTrace);
        for (int i = startIndex + length; i < shiftIndex + length; i++) {
            cleanTrace.set(i - length, originalTrace.get(i));
        }
        for (int i = 0; i < length; i++) {
            cleanTrace.set(shiftIndex + i, shiftingSubtrace.get(i));
        }
    }

}
