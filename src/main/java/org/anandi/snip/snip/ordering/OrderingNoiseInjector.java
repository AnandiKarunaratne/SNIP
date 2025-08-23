package org.anandi.snip.snip.ordering;

import org.anandi.snip.eventlog.Trace;
import org.anandi.snip.snip.NoiseInjector;

public abstract class OrderingNoiseInjector extends NoiseInjector {

    private double positionProbability = 0.5;

    public OrderingNoiseInjector() {}

    public OrderingNoiseInjector(double positionProbability) {
        this.positionProbability = positionProbability;
    }

    public double getPositionProbability() {
        return positionProbability;
    }

}
