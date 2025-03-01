package org.anandi.snip.snip.insertion;

import org.anandi.snip.eventlog.Trace;
import org.anandi.snip.snip.NoiseInjectionManager;
import org.anandi.snip.snip.NoiseType;

import java.util.HashSet;
import java.util.Set;

public class InsertionNoiseInjectionManager extends NoiseInjectionManager {

    private final Set<String> activities;

    public InsertionNoiseInjectionManager(Set<String> activities){
        this.activities = activities;
    }

    @Override
    public void generateNoisyTrace(Trace cleanTrace, int length) {
        double probability = 0.5; // inserting process activities and unrelated activities have equal probabilities.
        generateNoisyTrace(cleanTrace, length, probability);
    }

    public void generateNoisyTrace(Trace cleanTrace, int length, double probability) {
        double classDecider = Math.random();
        if (classDecider < probability) {
            new ProcessActivityInserter(activities).injectNoise(cleanTrace, length);
        } else {
            new UnrelatedActivityInserter(activities).injectNoise(cleanTrace, length);
        }
    }

    @Override
    protected Set<NoiseType> getNoiseTypes() {
        Set<NoiseType> noiseTypes = new HashSet<>();
        noiseTypes.add(NoiseType.INSERTION);
        return noiseTypes;
    }
}
