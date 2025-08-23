package org.anandi.snip.snip;

import org.anandi.snip.eventlog.Trace;

public abstract class NoiseInjector {

    public abstract String injectNoise(Trace cleanTrace, int length);

}
