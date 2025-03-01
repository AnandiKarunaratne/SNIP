package org.anandi.snip.snip.absence;

import org.anandi.snip.eventlog.Trace;
import org.anandi.snip.snip.NoiseInjectionManager;
import org.anandi.snip.snip.NoiseType;

import java.util.HashSet;
import java.util.Set;

public class AbsenceNoiseInjectionManager extends NoiseInjectionManager {

    @Override
    public void generateNoisyTrace(Trace cleanTrace, int length) {
        new AbsenceNoiseInjector().injectNoise(cleanTrace, length);
    }

    @Override
    protected Set<NoiseType> getNoiseTypes() {
        Set<NoiseType> noiseTypes = new HashSet<>();
        noiseTypes.add(NoiseType.ABSENCE);
        return noiseTypes;
    }

}
