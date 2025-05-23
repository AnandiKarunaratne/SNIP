package org.anandi.snip.snip.ordering;

import org.anandi.snip.eventlog.Trace;
import org.anandi.snip.snip.NoiseInjectionManager;
import org.anandi.snip.snip.NoiseType;

import java.util.HashSet;
import java.util.Set;

public class OrderingNoiseInjectionManager extends NoiseInjectionManager {

    @Override
    public String generateNoisyTrace(Trace cleanTrace, int length) {
        double probability = 0.5; // shifting activities and swapping activities have equal probabilities.
        return generateNoisyTrace(cleanTrace, length, probability);
    }

    public String generateNoisyTrace(Trace cleanTrace, int length, double probability) {
        String logMessage = "\"operation\": ";
        double classDecider = Math.random();
        if (classDecider < probability) {
            logMessage += "\"shift\",\n";
            logMessage += new ActivityShifter().injectNoise(cleanTrace, length);
        } else {
            logMessage += "\"swap\",\n";
            logMessage += new ActivitySwapper().injectNoise(cleanTrace, length);
        }
        return logMessage;
    }

    @Override
    protected Set<NoiseType> getNoiseTypes() {
        Set<NoiseType> noiseTypes = new HashSet<>();
        noiseTypes.add(NoiseType.ORDERING);
        return noiseTypes;
    }

}
