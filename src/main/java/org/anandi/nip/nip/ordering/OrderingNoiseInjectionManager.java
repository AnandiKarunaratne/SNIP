package org.anandi.nip.nip.ordering;

import org.anandi.nip.eventlog.Trace;
import org.anandi.nip.nip.NoiseInjectionManager;
import org.anandi.nip.nip.NoiseType;

import java.util.HashSet;
import java.util.Set;

public class OrderingNoiseInjectionManager extends NoiseInjectionManager {

    @Override
    public void generateNoisyTrace(Trace cleanTrace, int length) {
        double probability = 0.5; // shifting activities and swapping activities have equal probabilities.
        generateNoisyTrace(cleanTrace, length, probability);
    }

    public void generateNoisyTrace(Trace cleanTrace, int length, double probability) {
        double classDecider = Math.random();
        if (classDecider < probability) {
            new ActivityShifter().injectNoise(cleanTrace, length);
        } else {
            new ActivitySwapper().injectNoise(cleanTrace, length);
        }
    }

    @Override
    protected Set<NoiseType> getNoiseTypes() {
        Set<NoiseType> noiseTypes = new HashSet<>();
        noiseTypes.add(NoiseType.ORDERING);
        return noiseTypes;
    }

}
