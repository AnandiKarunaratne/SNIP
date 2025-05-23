package org.anandi.snip.snip.absence;

import org.anandi.snip.eventlog.Trace;
import org.anandi.snip.snip.NoiseInjector;

import java.util.Random;

public class AbsenceNoiseInjector implements NoiseInjector {

    @Override
    public String injectNoise(Trace cleanTrace, int length) {
        double probability = 0.5; // removing activities consecutively or randomly has equal probability
        return injectNoise(cleanTrace, length, probability);
    }

    @Override
    public String injectNoise(Trace cleanTrace, int length, double probability) {
        String logMessage = "\"position\": ";
        double methodDecider = Math.random();
        if (methodDecider < probability) {
            logMessage += "\"consecutive\",\n";
            String location = consecutiveActivityRemovalManager(cleanTrace, length);
            logMessage += "\"location\": \"" + location + "\",\n";
        } else {
            removeRandomActivities(cleanTrace, length);
            logMessage += "random\",\n";
        }
        return logMessage;
    }

    public void removeRandomActivities(Trace cleanTrace, int length) {
        Random random = new Random();
        int removeIndex;
        for (int i = 0; i < length; i++) {
            removeIndex = random.nextInt(cleanTrace.size());
            removeActivity(cleanTrace, removeIndex);
        }
    }

    public void removeHead(Trace cleanTrace, int length) {
        removeConsecutiveActivities(cleanTrace, length, 0);
    }

    public void removeTail(Trace cleanTrace, int length) {
        int startIndex = cleanTrace.size() - length;
        removeConsecutiveActivities(cleanTrace, length, startIndex);
    }

    public void removeBody(Trace cleanTrace, int length) {
        Random random = new Random();
        int startIndex = random.nextInt(cleanTrace.size() - length - 1) + 1;
        removeConsecutiveActivities(cleanTrace, length, startIndex);
    }

    public void removeConsecutiveActivities(Trace cleanTrace, int length) {
        Random random = new Random();
        int startIndex = random.nextInt(cleanTrace.size() - length + 1);
        removeConsecutiveActivities(cleanTrace, length, startIndex);
    }

    protected String consecutiveActivityRemovalManager(Trace cleanTrace, int length) {
        // one problem we have to consider is, if the trace is too short, for example length = 2, then there can't
        // be body removal.
        if (cleanTrace.size() < 3) {
            double probability = 1.0 / 2; // removing activities from head/tail has equal probability
            double methodDecider = Math.random();
            if (methodDecider < probability) {
                removeHead(cleanTrace, length);
                return "head";
            } else {
                removeTail(cleanTrace, length);
                return "tail";
            }
        }
        double probability = 1.0 / 3; // removing activities from head/tail/body has equal probability
        double methodDecider = Math.random();
        if (methodDecider < probability) {
            removeHead(cleanTrace, length);
            return "head";
        } else if (methodDecider < 2 * probability) {
            removeTail(cleanTrace, length);
            return "tail";
        } else {
            removeBody(cleanTrace, length);
            return "body";
        }
    }

    public void removeConsecutiveActivities(Trace cleanTrace, int length, int startIndex) {
        for (int i = 0; i < length; i++) {
            removeActivity(cleanTrace, startIndex);
        }
    }

    public void removeActivity(Trace cleanTrace, int removeIndex) {
        if (removeIndex >= cleanTrace.size()) {
            throw new IndexOutOfBoundsException("Index " + removeIndex + " is out of bounds for the trace size " + cleanTrace.size());
        }
        cleanTrace.remove(removeIndex);
    }

}
