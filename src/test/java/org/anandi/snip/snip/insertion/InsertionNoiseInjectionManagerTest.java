package org.anandi.snip.snip.insertion;

import org.anandi.snip.eventlog.Trace;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class InsertionNoiseInjectionManagerTest {

    Set<String> activities = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
    InsertionNoiseInjectionManager insertionNoiseInjectionManager = new InsertionNoiseInjectionManager(activities);
    Trace baseTrace = new Trace(Arrays.asList("a", "b", "c", "a", "b", "d"));
    Trace cleanTrace = new Trace(baseTrace);

    @Test
    public void testManualSequentialProcessInsertion() {
        Random random = new Random();
        int length = random.nextInt(cleanTrace.size() / 3) + 1;
        insertionNoiseInjectionManager.generateNoisyTrace(cleanTrace, length);
        assertEquals(cleanTrace.size(), baseTrace.size() + length);
    }

}
