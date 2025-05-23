package org.anandi.snip.snip.insertion;

import org.anandi.snip.eventlog.Trace;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class InsertionNoiseInjectorTest {

    Set<String> activities = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
    InsertionNoiseInjector insertionNoiseInserter = new InsertionNoiseInjector(activities);
    Trace baseTrace = new Trace(Arrays.asList("a", "b", "c", "a", "b", "d"));
    Trace cleanTrace = new Trace(baseTrace);
    Random random = new Random();

    @Test
    public void testManualSequentialProcessInsertion() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;

        InsertionNoiseInjector sequentialProcessInserter = new InsertionNoiseInjector(activities, 1, 1);
        sequentialProcessInserter.injectNoise(cleanTrace, length);
        assertEquals(cleanTrace.size(), baseTrace.size() + length);
        assertTrue(activities.containsAll(cleanTrace)); // only contains process activities
        assertTrue(isConsecutive()); // is consecutive
    }

    @Test
    public void testManualRandomProcessInsertion() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;

        InsertionNoiseInjector randomProcessInserter = new InsertionNoiseInjector(activities, 0, 1);
        randomProcessInserter.injectNoise(cleanTrace, length);
        assertEquals(cleanTrace.size(), baseTrace.size() + length);
        assertTrue(activities.containsAll(cleanTrace)); // only contains process activities
        // can't test for random positions
    }

    @Test
    public void testManualSequentialUnrelatedInsertion() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;

        InsertionNoiseInjector sequentialUnrelatedInserter = new InsertionNoiseInjector(activities, 1, 0);
        sequentialUnrelatedInserter.injectNoise(cleanTrace, length);
        assertEquals(cleanTrace.size(), baseTrace.size() + length);
        assertFalse(activities.containsAll(cleanTrace)); // only contains process activities
        assertTrue(isConsecutive()); // is consecutive
    }

    @Test
    public void testManualRandomUnrelatedInsertion() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;

        InsertionNoiseInjector randomProcessInserter = new InsertionNoiseInjector(activities, 0, 0);
        randomProcessInserter.injectNoise(cleanTrace, length);
        assertEquals(cleanTrace.size(), baseTrace.size() + length);
        assertFalse(activities.containsAll(cleanTrace)); // only contains process activities
        // can't test for random positions
    }

    @Test
    public void testInjectNoise() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;
        insertionNoiseInserter.injectNoise(cleanTrace, length);
        assertEquals(cleanTrace.size(), baseTrace.size() + length);
    }

    @Test
    public void testInsertRandomConsecutiveActivities() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;
        insertionNoiseInserter.insertConsecutiveActivities(cleanTrace, length);
        assertEquals(cleanTrace.size(), baseTrace.size() + length);
        assertTrue(isConsecutive());
    }

    private boolean isConsecutive() {
        boolean isConsecutive = false;
        for (int i = 1; i < baseTrace.size(); i++) {
            List<String> subtrace1 = baseTrace.subList(0, i);
            List<String> subtrace2 = baseTrace.subList(i, baseTrace.size());
            if (isSubtracePresent(cleanTrace, subtrace1) && isSubtracePresent(cleanTrace, subtrace2)) {
                isConsecutive = true;
                break;
            }
        }
        return isConsecutive;
    }

    private boolean isSubtracePresent(Trace trace, List<String> subtrace) {
        return Collections.indexOfSubList(trace, subtrace) != -1;
    }

    @Test
    public void testInjectProcessActivity() {
        int insertIndex = random.nextInt(cleanTrace.size()) + 1;
        insertionNoiseInserter.injectProcessActivity(cleanTrace, insertIndex);
        assertEquals(cleanTrace.size(), baseTrace.size() + 1);
        Assert.assertTrue(activities.contains(cleanTrace.get(insertIndex)));
    }

    @Test
    public void testDuplicateActivity() {
        Trace baseTrace = new Trace(Arrays.asList("a", "b", "c"));
        Trace cleanTrace = new Trace(baseTrace);
        int insertIndex = 3;
        insertionNoiseInserter.duplicateActivity(cleanTrace, insertIndex);
    }

    @Test
    public void testInjectUnrelatedActivity() {
        int insertIndex = random.nextInt(cleanTrace.size()) + 1;
        insertionNoiseInserter.injectUnrelatedActivity(cleanTrace, insertIndex);
        assertEquals(cleanTrace.size(), baseTrace.size() + 1);
        assertFalse(activities.contains(cleanTrace.get(insertIndex)));
    }

}
