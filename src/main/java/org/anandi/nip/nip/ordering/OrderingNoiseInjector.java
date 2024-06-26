package org.anandi.nip.nip.ordering;

import org.anandi.nip.eventlog.Trace;
import org.anandi.nip.nip.NoiseInjector;

public abstract class OrderingNoiseInjector implements NoiseInjector {

    @Override
    public void injectNoise(Trace cleanTrace, int length) {
        double probability = 0.5; // shifting activities to left or right has equal probability
        injectNoise(cleanTrace, length, probability);
    }

}
