package org.anandi.snip.snip.substitution;

import org.anandi.snip.eventlog.Trace;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

public class SubstitutionNoiseInjectorTest {

    Set<String> activities = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
    SubstitutionNoiseInjector substitutionNoiseInjector = new SubstitutionNoiseInjector(activities);
    Trace baseTrace = new Trace(Arrays.asList("a", "b", "c", "a", "b", "d"));
    Trace cleanTrace = new Trace(baseTrace);

    @Test
    public void testSubstituteRandomActivities() {
        int length = 4;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                substitutionNoiseInjector.substituteRandomActivities(cleanTrace, cleanTrace.size() + 1));
        String expectedMessage = "The substitution length (" + (cleanTrace.size() + 1) + ") should not be larger than the trace length (" + cleanTrace.size() + ").";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        substitutionNoiseInjector.substituteRandomActivities(cleanTrace, length);
        assertEquals(baseTrace.size(), cleanTrace.size());
        int substitutions = 0;
        for (int i = 0; i < cleanTrace.size(); i++) {
            if (!cleanTrace.get(i).equals(baseTrace.get(i))) {
                substitutions++;
            }
        }
        assertEquals(length, substitutions);
    }

    @Test
    public void testSubstituteConsecutiveActivitiesWithoutIndex() {
        int length = 4;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                substitutionNoiseInjector.substituteConsecutiveActivities(cleanTrace, cleanTrace.size() + 1));
        String expectedMessage = "The substitution length (" + (cleanTrace.size() + 1) + ") should not be larger than the trace length (" + cleanTrace.size() + ").";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        substitutionNoiseInjector.substituteConsecutiveActivities(cleanTrace, length);
        assertEquals(baseTrace.size(), cleanTrace.size());
        boolean isNoisy = false;
        for (int i = 0; i <= cleanTrace.size() - length; i++) {
            if (!cleanTrace.subList(i, i + length).equals(baseTrace.subList(i, i + length))) {
                isNoisy = true;
                break;
            }
        }
        assertTrue(isNoisy);
    }

    @Test
    public void testSubstituteConsecutiveActivitiesWithIndex() {
        int index = 2;
        int length = 3;

        // check exception
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                substitutionNoiseInjector.substituteConsecutiveActivities(cleanTrace, cleanTrace.size(), index));
        String expectedMessage = "Unable to substitute a sequence of length " + cleanTrace.size() + " at the index " +
                index +  " for the trace size " + cleanTrace.size() + ".";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        substitutionNoiseInjector.substituteConsecutiveActivities(cleanTrace, length, index);
        assertEquals(baseTrace.size(), cleanTrace.size());
        assertEquals(baseTrace.subList(0, index), cleanTrace.subList(0, index));
        assertEquals(baseTrace.subList(index + length, baseTrace.size()),
                cleanTrace.subList(index + length, cleanTrace.size()));
        assertNotEquals(baseTrace.subList(index, index + length), cleanTrace.subList(index, index + length));
    }

    @Test
    public void testSubstituteOneActivity() {
        int index = 2;

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () ->
                substitutionNoiseInjector.substituteOneActivity(new Trace(), index));
        String expectedMessage = "Index " + index + " is out of bounds for the trace size " + new Trace().size() + ".";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        substitutionNoiseInjector.substituteOneActivity(cleanTrace, index);
        assertEquals(baseTrace.size(), cleanTrace.size());
        assertEquals(baseTrace.subList(0, index), cleanTrace.subList(0, index));
        assertEquals(baseTrace.subList(index + 1, baseTrace.size()), cleanTrace.subList(index + 1, cleanTrace.size()));
        assertNotEquals(baseTrace.get(index), cleanTrace.get(index));
    }


}
