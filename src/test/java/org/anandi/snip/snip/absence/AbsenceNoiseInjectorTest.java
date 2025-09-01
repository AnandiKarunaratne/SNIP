package org.anandi.snip.snip.absence;

import org.anandi.snip.eventlog.Trace;
import org.junit.Assert;
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
    public void testDefaultConstructor() {
        AbsenceNoiseInjector injector = new AbsenceNoiseInjector();
        // use reflection or getter if available, since positionProbability is private
        double value = getPrivateField(injector, "positionProbability");
        assertEquals(0.5, value, 0.01);
    }

    @Test
    public void testParameterizedConstructor() {
        AbsenceNoiseInjector injector = new AbsenceNoiseInjector(0.8);
        double value = getPrivateField(injector, "positionProbability");
        assertEquals(0.8, value, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveLengthLargerThanTraceSize() {
        absenceNoiseInjector.injectNoise(new Trace(Arrays.asList("a")), 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveLengthEqualToTraceSize() {
        absenceNoiseInjector.injectNoise(new Trace(Arrays.asList("a")), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemovalLengthNegative() {
        absenceNoiseInjector.injectNoise(baseTrace, -1);
    }

    @Test
    public void testManualSequentialAbsence() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;
        AbsenceNoiseInjector sequentialAbsenceNoiseInjector = new AbsenceNoiseInjector(1);
        String logMessage = sequentialAbsenceNoiseInjector.injectNoise(cleanTrace, length);

        assertEquals(baseTrace.size() - length, cleanTrace.size()); // length is removed
        assertTrue(isRemovalConsecutive(baseTrace, cleanTrace)); // consecutive removal
        assertTrue(logMessage.contains("\"position\": \"consecutive\""));
        assertTrue(logMessage.contains("head") || logMessage.contains("tail") || logMessage.contains("body"));
    }

    @Test
    public void testManualRandomAbsence() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;
        AbsenceNoiseInjector randomAbsenceNoiseInjector = new AbsenceNoiseInjector(0);
        String logMessage = randomAbsenceNoiseInjector.injectNoise(cleanTrace, length);

        assertEquals(baseTrace.size() - length, cleanTrace.size());
        assertTrue(logMessage.contains("\"position\": \"random\""));
    }

    @Test
    public void testRemoveRandomActivities() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;
        absenceNoiseInjector.removeRandomActivities(cleanTrace, length);
        assertEquals(baseTrace.size() - length, cleanTrace.size());
    }

    @Test
    public void testRemoveHead() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;
        absenceNoiseInjector.removeHead(cleanTrace, length);
        assertTrue(isRemovalConsecutive(baseTrace, cleanTrace));
        assertTrue(isSubtracePresent(baseTrace, cleanTrace));
        assertEquals(baseTrace.subList(length, baseTrace.size()), cleanTrace);
    }

    @Test
    public void testRemoveTail() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;
        absenceNoiseInjector.removeTail(cleanTrace, length);
        assertTrue(isRemovalConsecutive(baseTrace, cleanTrace));
        assertTrue(isSubtracePresent(baseTrace, cleanTrace));
        assertEquals(baseTrace.subList(0, baseTrace.size() - length), cleanTrace);
    }

    @Test
    public void testRemoveBody() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;
        absenceNoiseInjector.removeBody(cleanTrace, length);
        boolean isConsecutive = isRemovalConsecutive(baseTrace, cleanTrace);
        assertTrue(isConsecutive);
        assertFalse(isSubtracePresent(baseTrace, cleanTrace));
    }

    @Test
    public void testRemoveConsecutiveActivitiesWithoutIndex() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;
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

    private double getPrivateField(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (double) field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
