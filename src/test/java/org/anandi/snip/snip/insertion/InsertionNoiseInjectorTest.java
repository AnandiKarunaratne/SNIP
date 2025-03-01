package org.anandi.snip.snip.insertion;

import org.anandi.snip.eventlog.Trace;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class InsertionNoiseInjectorTest {

    Set<String> activities = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
    ProcessActivityInserter processActivityInserter = new ProcessActivityInserter(activities);
    Trace baseTrace = new Trace(Arrays.asList("a", "b", "c", "a", "b", "d"));
    Trace cleanTrace = new Trace(baseTrace);

    @Test
    public void testInjectNoise() {
        Random random = new Random();
        int length = random.nextInt(cleanTrace.size() / 3) + 1;
        processActivityInserter.injectNoise(cleanTrace, length);
        assertEquals(cleanTrace.size(), baseTrace.size() + length);
    }

    @Test
    public void testInsertRandomConsecutiveActivities() {
        Random random = new Random();
        int length = random.nextInt(cleanTrace.size() / 3) + 1;
        processActivityInserter.insertConsecutiveActivities(cleanTrace, length);
        boolean isConsecutive = false;
        for (int i = 1; i < baseTrace.size(); i++) {
            List<String> subtrace1 = baseTrace.subList(0, i);
            List<String> subtrace2 = baseTrace.subList(i, baseTrace.size());
            if (isSubtracePresent(cleanTrace, subtrace1) && isSubtracePresent(cleanTrace, subtrace2)) {
                isConsecutive = true;
                break;
            }
        }
        assertTrue(isConsecutive);
    }

    private boolean isSubtracePresent(Trace trace, List<String> subtrace) {
        return Collections.indexOfSubList(trace, subtrace) != -1;
    }

}
