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
    public String generateNoisyTrace(Trace cleanTrace, int length) {
        return new InsertionNoiseInjector(activities).injectNoise(cleanTrace, length);
    }

    @Override
    protected Set<NoiseType> getNoiseTypes() {
        Set<NoiseType> noiseTypes = new HashSet<>();
        noiseTypes.add(NoiseType.INSERTION);
        return noiseTypes;
    }
}
