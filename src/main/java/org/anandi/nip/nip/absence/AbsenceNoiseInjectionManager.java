package org.anandi.nip.nip.absence;

import org.anandi.nip.eventlog.Trace;
import org.anandi.nip.nip.NoiseInjectionManager;
import org.anandi.nip.nip.NoiseType;

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
