package org.anandi.snip.snip;

import org.anandi.snip.eventlog.Trace;

public interface NoiseInjector {

    void injectNoise(Trace cleanTrace, int length);
    void injectNoise(Trace cleanTrace, int length, double probability);

}
