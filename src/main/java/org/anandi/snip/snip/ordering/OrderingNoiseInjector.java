package org.anandi.snip.snip.ordering;

import org.anandi.snip.eventlog.Trace;
import org.anandi.snip.snip.NoiseInjector;

public abstract class OrderingNoiseInjector implements NoiseInjector {

//    @Override
//    public void injectNoise(Trace cleanTrace, int length) {
//        double probability = 0.5; // shifting activities to left or right has equal probability
//        injectNoise(cleanTrace, length, probability);
//    }

    @Override
    public String injectNoise(Trace cleanTrace, int length) {
        double probability = 0.5; // sequential or random
//        System.out.println(length);
        return injectNoise(cleanTrace, length, probability);
    }

}
