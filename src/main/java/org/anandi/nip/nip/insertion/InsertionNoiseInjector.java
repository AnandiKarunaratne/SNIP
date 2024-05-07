package org.anandi.nip.nip.insertion;

import org.anandi.nip.eventlog.Trace;
import org.anandi.nip.nip.NoiseInjector;

import java.util.Random;
import java.util.Set;

public abstract class InsertionNoiseInjector implements NoiseInjector {

    InsertionNoiseInjector(Set<String> activities){ }

    abstract Trace injectActivity(Trace cleanTrace, int insertIndex);

    /**
     * can we have mixed situations too?
     * some noise consecutively, and other at random places?
     */
    @Override
    public Trace injectNoise(Trace cleanTrace, int length) {
        double methodDecider = Math.random();
        double probability = 0.5;
        if (methodDecider < probability) {
            cleanTrace = insertConsecutiveActivities(cleanTrace, length);
        } else {
            cleanTrace = insertRandomActivities(cleanTrace, length);
        }
        return cleanTrace;
    }

    /**
     * test this
     */
    Trace insertConsecutiveActivities(Trace cleanTrace, int length) {
        Random random = new Random();
        int startIndex = random.nextInt(cleanTrace.size() - length); // recheck edge cases
        for (int i = 0; i < length; i++) {
            cleanTrace = injectActivity(cleanTrace, i + startIndex);
        }
        return cleanTrace;
    }
    Trace insertRandomActivities(Trace cleanTrace, int length) {
        Random random = new Random();
        int insertIndex;
        for (int i = 0; i < length; i++) {
            insertIndex = random.nextInt(cleanTrace.size());
            cleanTrace = injectActivity(cleanTrace, insertIndex);
        }
        return cleanTrace;
    }

}
