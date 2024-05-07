package org.anandi.nip.nip;

import org.anandi.nip.eventlog.Trace;

public interface NoiseInjector {

    void injectNoise(Trace cleanTrace, int length);

}
