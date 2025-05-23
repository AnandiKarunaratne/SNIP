package org.anandi.snip.snip.absence;

import org.anandi.snip.eventlog.Trace;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class AbsenceNoiseInjectionManagerTest {

    AbsenceNoiseInjectionManager absenceNoiseInjectionManager = new AbsenceNoiseInjectionManager();
    Trace baseTrace = new Trace(Arrays.asList("a", "b", "c", "a", "b", "d"));
    Trace cleanTrace = new Trace(baseTrace);
    Random random = new Random();

    @Test
    public void testRemoveRandomActivities() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;;
        absenceNoiseInjectionManager.generateNoisyTrace(cleanTrace, length);
        assertEquals(baseTrace.size() - length, cleanTrace.size());
    }

}
