package org.anandi.nip.nip.absence;

import org.anandi.nip.eventlog.Trace;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;

public class AbsenceNoiseInjectorTest {

    AbsenceNoiseInjector absenceNoiseInjector = new AbsenceNoiseInjector();
    Trace baseTrace = new Trace(Arrays.asList("a", "b", "c", "a", "b", "d"));
    Trace cleanTrace = new Trace(baseTrace);
    Random random = new Random();

    @Test
    public void testRemoveRandomActivities() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;;
        absenceNoiseInjector.removeRandomActivities(cleanTrace, length);
        assertEquals(baseTrace.size() - length, cleanTrace.size());
    }

    @Test
    public void testRemoveHead() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;;
        absenceNoiseInjector.removeHead(cleanTrace, length);
        assertTrue(isRemovalConsecutive(baseTrace, cleanTrace));
        assertTrue(isSubtracePresent(baseTrace, cleanTrace));
        assertEquals(baseTrace.subList(length, baseTrace.size()), cleanTrace);
    }

    @Test
    public void testRemoveTail() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;;
        absenceNoiseInjector.removeTail(cleanTrace, length);
        assertTrue(isRemovalConsecutive(baseTrace, cleanTrace));
        assertTrue(isSubtracePresent(baseTrace, cleanTrace));
        assertEquals(baseTrace.subList(0, baseTrace.size() - length), cleanTrace);
    }

    @Test
    public void testRemoveBody() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;;
        absenceNoiseInjector.removeBody(cleanTrace, length);
        boolean isConsecutive = isRemovalConsecutive(baseTrace, cleanTrace);
        assertTrue(isConsecutive);
        assertFalse(isSubtracePresent(baseTrace, cleanTrace));
    }

    @Test
    public void testRemoveConsecutiveActivitiesWithoutIndex() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;;
        absenceNoiseInjector.removeConsecutiveActivities(cleanTrace, length);
        assertTrue(isRemovalConsecutive(baseTrace, cleanTrace));
    }

    @Test
    public void testRemoveConsecutiveActivitiesWithIndex() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;
        int startIndex = random.nextInt(cleanTrace.size() - length);
        absenceNoiseInjector.removeConsecutiveActivities(cleanTrace, length, startIndex);
        assertTrue(isRemovalConsecutive(baseTrace, cleanTrace));
    }

    @Test
    public void testRemoveActivity() {
        int removeIndex = random.nextInt(cleanTrace.size());
        absenceNoiseInjector.removeActivity(cleanTrace, removeIndex);
        assertTrue(isRemovalConsecutive(baseTrace, cleanTrace));
        assertEquals(baseTrace.size() - 1, cleanTrace.size());
    }

    private boolean isRemovalConsecutive(Trace initialTrace, Trace removedTrace) {
        for (int i = 1; i < removedTrace.size(); i++) {
            List<String> subtrace1 = removedTrace.subList(0, i);
            List<String> subtrace2 = removedTrace.subList(i, removedTrace.size());
            if (isSubtracePresent(initialTrace, subtrace1) && isSubtracePresent(initialTrace, subtrace2)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSubtracePresent(Trace trace, List<String> subtrace) {
        return Collections.indexOfSubList(trace, subtrace) != -1;
    }

}
