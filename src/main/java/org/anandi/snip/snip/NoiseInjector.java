package org.anandi.snip.snip;

import org.anandi.snip.eventlog.Trace;

public interface NoiseInjector {

    String injectNoise(Trace cleanTrace, int length);
    String injectNoise(Trace cleanTrace, int length, double probability);

}
