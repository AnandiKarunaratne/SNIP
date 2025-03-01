package org.anandi.snip.snip.insertion;

import org.anandi.snip.eventlog.Trace;
import org.anandi.snip.snip.NoiseInjector;

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

    @Override
    public void injectNoise(Trace cleanTrace, int length, double probability) {
        double methodDecider = Math.random();
        if (methodDecider < probability) {
            insertConsecutiveActivities(cleanTrace, length);
        } else {
            insertRandomActivities(cleanTrace, length);
        }
    }

    public void insertConsecutiveActivities(Trace cleanTrace, int length) {
        Random random = new Random();
        int startIndex = random.nextInt(cleanTrace.size() + 1);
        insertConsecutiveActivities(cleanTrace, length, startIndex);
    }

    public void insertConsecutiveActivities(Trace cleanTrace, int length, int startIndex) {
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
