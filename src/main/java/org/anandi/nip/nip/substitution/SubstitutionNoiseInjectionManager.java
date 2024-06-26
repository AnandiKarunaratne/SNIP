package org.anandi.nip.nip.substitution;

import org.anandi.nip.eventlog.Trace;
import org.anandi.nip.nip.NoiseInjectionManager;
import org.anandi.nip.nip.NoiseType;

import java.util.HashSet;
import java.util.Set;

public class SubstitutionNoiseInjectionManager extends NoiseInjectionManager {

    private final Set<String> activities;

    public SubstitutionNoiseInjectionManager(Set<String> activities){
        this.activities = activities;
    }

    @Override
    public void generateNoisyTrace(Trace cleanTrace, int length) {
        new SubstitutionNoiseInjector(activities).injectNoise(cleanTrace, length);
    }

    @Override
    protected Set<NoiseType> getNoiseTypes() {
        Set<NoiseType> noiseTypes = new HashSet<>();
        noiseTypes.add(NoiseType.SUBSTITUTION);
        return noiseTypes;
    }

}
