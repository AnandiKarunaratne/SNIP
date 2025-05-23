package org.anandi.snip.snip.ordering;

import org.anandi.snip.eventlog.Trace;

import java.util.Random;

public class ShiftingNoiseInjector extends OrderingNoiseInjector {

    @Override
    public String injectNoise(Trace cleanTrace, int length, double probability) {
        String logMessage = "\"position\": ";
        double methodDecider = Math.random();
        if (methodDecider < probability) {
            logMessage += "\"random\",\n";
            shiftActivitiesRandomly(cleanTrace, length);
        } else {
            logMessage += "\"consecutive\",\n";
            shiftActivitiesConsecutively(cleanTrace, length);
        }
        return logMessage;
    }

    public String shiftActivitiesRandomly(Trace cleanTrace, int length) {
        String logMessage = "\"direction\": ";
        double probability = 0.5; // similar probability to shift left or right
        for (int i = 0; i < length; i++) {
            double methodDecider = Math.random();
            if (methodDecider < probability) {
                shiftActivitiesToLeft(cleanTrace, 1);
                logMessage += "\"left\",\n";
            } else {
                shiftActivitiesToRight(cleanTrace, 1);
                logMessage += "\"right\",\n";
            }
        }
        return logMessage;
    }

    public String shiftActivitiesConsecutively(Trace cleanTrace, int length) {
        String logMessage = "\"direction\": ";
        double probability = 0.5; // similar probability to shift left or right
        double methodDecider = Math.random();
        if (methodDecider < probability) {
            shiftActivitiesToLeft(cleanTrace, length);
            logMessage += "\"left\",\n";
        } else {
            shiftActivitiesToRight(cleanTrace, length);
            logMessage += "\"right\",\n";
        }
        return logMessage;
    }

    public void shiftActivitiesToLeft(Trace cleanTrace, int length) {
        if (cleanTrace.size() <= length) {
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
        if (cleanTrace.size() <= length) {
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
        if (cleanTrace.size() <= length) {
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
        if (cleanTrace.size() <= length) {
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
