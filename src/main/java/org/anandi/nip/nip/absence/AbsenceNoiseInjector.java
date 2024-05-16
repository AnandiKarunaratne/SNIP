package org.anandi.nip.nip.absence;

import org.anandi.nip.eventlog.Trace;
import org.anandi.nip.nip.NoiseInjector;

import java.util.Random;

public class AbsenceNoiseInjector implements NoiseInjector {

    @Override
    public void injectNoise(Trace cleanTrace, int length) {
        double probability = 0.5; // removing activities consecutively or randomly has equal probability
        injectNoise(cleanTrace, length, probability);
    }

    public void injectNoise(Trace cleanTrace, int length, double probability) {
        double methodDecider = Math.random();
        if (methodDecider < probability) {
            consecutiveActivityRemovalManager(cleanTrace, length);
        } else {
            removeActivity(cleanTrace, length);
        }
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

    protected void consecutiveActivityRemovalManager(Trace cleanTrace, int length) {
        double probability = 1.0 / 3; // removing activities from head/tail/body has equal probability
        double methodDecider = Math.random();
        if (methodDecider < probability) {
            removeHead(cleanTrace, length);
        } else if (methodDecider < 2 * probability) {
            removeTail(cleanTrace, length);
        } else {
            removeBody(cleanTrace, length);
        }
    }

    protected void removeConsecutiveActivities(Trace cleanTrace, int length, int startIndex) {
        for (int i = 0; i < length; i++) {
            removeActivity(cleanTrace, startIndex);
        }
    }

    protected void removeActivity(Trace cleanTrace, int removeIndex) {
        if (removeIndex >= cleanTrace.size()) {
            throw new IndexOutOfBoundsException("Index " + removeIndex + " is out of bounds for the trace size " + cleanTrace.size());
        }
        cleanTrace.remove(removeIndex);
    }

}
