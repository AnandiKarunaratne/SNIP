package org.anandi.snip.snip.ordering;

import org.anandi.snip.eventlog.Trace;
import org.anandi.snip.snip.NoiseInjectionManager;
import org.anandi.snip.snip.NoiseType;

import java.util.HashSet;
import java.util.Set;

public class OrderingNoiseInjectionManager extends NoiseInjectionManager {

    private double operationProbability = 0.5; // If 0, swap

    public OrderingNoiseInjectionManager() {}

    public OrderingNoiseInjectionManager(double operationProbability) {
        this.operationProbability = operationProbability;
    }

    @Override
    public String generateNoisyTrace(Trace cleanTrace, int length) {
        return generateNoisyTrace(cleanTrace, length, operationProbability);
    }

    public String generateNoisyTrace(Trace cleanTrace, int length, double probability) {
        String logMessage = "\"operation\": ";
        double classDecider = Math.random();
        if (classDecider < probability) {
            logMessage += "\"shift\",\n";
            logMessage += new ShiftingNoiseInjector().injectNoise(cleanTrace, length);
        } else {
            logMessage += "\"swap\",\n";
            logMessage += new SwappingNoiseInjector().injectNoise(cleanTrace, length);
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
