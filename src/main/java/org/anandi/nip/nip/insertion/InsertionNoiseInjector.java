package org.anandi.nip.nip.insertion;

import org.anandi.nip.eventlog.Trace;
import org.anandi.nip.nip.NoiseInjector;

import java.util.Random;
import java.util.Set;

public abstract class InsertionNoiseInjector implements NoiseInjector {

    Set<String> activities;

    InsertionNoiseInjector(Set<String> activities){
        this.activities = activities;
    }

    abstract void injectActivity(Trace cleanTrace, int insertIndex);

    /**
     * can we have mixed situations too?
     * some noise consecutively, and other at random places?
     */
    @Override
    public void injectNoise(Trace cleanTrace, int length) {
        double probability = 0.5; // inserting activities consecutively or randomly has equal probability
        injectNoise(cleanTrace, length, probability);
    }

    public void injectNoise(Trace cleanTrace, int length, double probability) {
        double methodDecider = Math.random();
        if (methodDecider < probability) {
            insertConsecutiveActivities(cleanTrace, length);
        } else {
            insertRandomActivities(cleanTrace, length);
        }
    }

    void insertConsecutiveActivities(Trace cleanTrace, int length) {
        Random random = new Random();
        int startIndex = random.nextInt(cleanTrace.size() + 1);
        for (int i = 0; i < length; i++) {
            injectActivity(cleanTrace, i + startIndex);
        }
    }
    void insertRandomActivities(Trace cleanTrace, int length) {
        Random random = new Random();
        int insertIndex;
        for (int i = 0; i < length; i++) {
            insertIndex = random.nextInt(cleanTrace.size() + 1);
            injectActivity(cleanTrace, insertIndex);
        }
    }

}
