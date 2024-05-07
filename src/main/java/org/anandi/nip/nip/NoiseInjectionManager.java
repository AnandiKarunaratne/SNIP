package org.anandi.nip.nip;

import org.anandi.nip.eventlog.EventLog;
import org.anandi.nip.eventlog.Trace;

import java.util.ArrayList;
import java.util.List;

public class NoiseInjectionManager {

    /**
     * Hard-coded noise percentage values for now.
     *
     * @param cleanLog
     * @return
     */

    double noisePercentage = 0.01;

    public EventLog injectNoise(EventLog cleanLog) {
        List<Trace> traceRemovedList = cleanLog;
        List<Trace> noiseList = new ArrayList<>();

        return null;
    }

}
