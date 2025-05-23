package org.anandi.snip.snip.substitution;

import org.anandi.snip.eventlog.Trace;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class SubstitutionNoiseInjectorTest {

    Set<String> activities = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
    SubstitutionNoiseInjector substitutionNoiseInjector = new SubstitutionNoiseInjector(activities);
    Trace baseTrace = new Trace(Arrays.asList("a", "b", "c", "a", "b", "d"));
    Trace cleanTrace = new Trace(baseTrace);
    Random random = new Random();

    @Test
    public void testManualSequentialProcess() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;;
        SubstitutionNoiseInjector sequentialProcessSubstitutionNoiseInjector = new SubstitutionNoiseInjector(activities, 1, 1);
        sequentialProcessSubstitutionNoiseInjector.injectNoise(cleanTrace, length);
        assertEquals(baseTrace.size(), cleanTrace.size());
        assertTrue(isSequentiallySubstituted(length));
        assertTrue(activities.containsAll(cleanTrace));
    }

    @Test
    public void testManualSequentialUnrelated() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;;
        SubstitutionNoiseInjector sequentialUnrelatedSubstitutionNoiseInjector = new SubstitutionNoiseInjector(activities, 1, 0);
        sequentialUnrelatedSubstitutionNoiseInjector.injectNoise(cleanTrace, length);
        assertEquals(baseTrace.size(), cleanTrace.size());
        assertTrue(isSequentiallySubstituted(length));
        assertFalse(activities.containsAll(cleanTrace));
    }

    @Test
    public void testManualRandomProcess() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;;
        SubstitutionNoiseInjector randomProcessSubstitutionNoiseInjector = new SubstitutionNoiseInjector(activities, 0, 1);
        randomProcessSubstitutionNoiseInjector.injectNoise(cleanTrace, length);
        assertEquals(baseTrace.size(), cleanTrace.size());
        assertTrue(activities.containsAll(cleanTrace));
    }

    @Test
    public void testManualRandomUnrelated() {
        int length = random.nextInt(cleanTrace.size() / 3) + 1;;
        SubstitutionNoiseInjector randomUnrelatedSubstitutionNoiseInjector = new SubstitutionNoiseInjector(activities, 0, 0);
        randomUnrelatedSubstitutionNoiseInjector.injectNoise(cleanTrace, length);
        assertEquals(baseTrace.size(), cleanTrace.size());
        assertFalse(activities.containsAll(cleanTrace));
    }

    private boolean isSequentiallySubstituted(int length) {
        boolean isSequential = false;
        for (int i = 0; i < baseTrace.size() - length; i++) {
            List<String> noisySubtrace1 = cleanTrace.subList(0, i);
            List<String> noisySubtrace2 = cleanTrace.subList(i + length, baseTrace.size());
            List<String> cleanSubtrace1 = baseTrace.subList(0, i);
            List<String> cleanSubtrace2 = baseTrace.subList(i + length, baseTrace.size());

            if (noisySubtrace1.equals(cleanSubtrace1) && noisySubtrace2.equals(cleanSubtrace2)) {
                isSequential = true;
                break;
            }
        }
        return isSequential;
    }

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
