package org.anandi.nip.nip.ordering;

import org.anandi.nip.eventlog.Trace;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertThrows;

public class ActivitySwapperTest {

    ActivitySwapper activitySwapper = new ActivitySwapper();
    Trace cleanTrace = new Trace(Arrays.asList("a", "b", "c", "d", "e", "f"));
    Trace originalTrace = new Trace(cleanTrace);

    @Test
    public void testSwapActivities() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapActivities(new Trace(), 2, 0, 1));
        String expectedMessage1 = "The trace be longer than twice the length of the subtrace to swap.";
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapActivities(cleanTrace, 2, -1, 2));
        String expectedMessage2 = "Invalid first index.";
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage2));

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapActivities(cleanTrace, 2, 4, 5));
        String expectedMessage3 = "Invalid first index.";
        String actualMessage3 = exception3.getMessage();
        assertTrue(actualMessage3.contains(expectedMessage3));

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapActivities(cleanTrace, 2, 0, 1));
        String expectedMessage4 = "Invalid second index.";
        String actualMessage4 = exception4.getMessage();
        assertTrue(actualMessage4.contains(expectedMessage4));

        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapActivities(cleanTrace, 2, 2, 2));
        String expectedMessage5 = "Invalid second index.";
        String actualMessage5 = exception5.getMessage();
        assertTrue(actualMessage5.contains(expectedMessage5));

        Exception exception6 = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapActivities(cleanTrace, 2, 2, 5));
        String expectedMessage6 = "Invalid second index.";
        String actualMessage6 = exception6.getMessage();
        assertTrue(actualMessage6.contains(expectedMessage6));

        activitySwapper.swapActivities(cleanTrace, 3, 0, 3);
        assertEquals(cleanTrace, new Trace(Arrays.asList("d", "e", "f", "a", "b", "c")));
    }

    @Test
    public void testSwapRandomActivities() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapActivities(new Trace(), 2));
        String expectedMessage = "The trace must be longer than twice the length of the subtrace to swap.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        int length = 2;
        activitySwapper.swapActivities(cleanTrace, length);
        int changedActivities = 0;

        List<String> noisySubtrace1 = new ArrayList<>();
        List<String> noisySubtrace2 = new ArrayList<>();
        List<String> originalSubtrace1 = new ArrayList<>();
        List<String> originalSubtrace2 = new ArrayList<>();

        for (int i = 0; i < cleanTrace.size(); i++) {
            if (!cleanTrace.get(i).equals(originalTrace.get(i))) {
                if (changedActivities / length == 0) {
                    noisySubtrace1.add(cleanTrace.get(i));
                    originalSubtrace2.add(originalTrace.get(i));
                } else {
                    noisySubtrace2.add(cleanTrace.get(i));
                    originalSubtrace1.add(originalTrace.get(i));
                }
                changedActivities++;
            }
        }
        assertEquals(4, changedActivities);
        assertEquals(noisySubtrace1, originalSubtrace1);
        assertEquals(noisySubtrace2, originalSubtrace2);
    }

    @Test
    public void testSwapTwoRandomActivities() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapTwoActivities(new Trace(), 0, 1));
        String expectedMessage = "The trace must be longer than two to swap.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        activitySwapper.swapTwoActivities(cleanTrace, 0, 4);
        assertEquals(cleanTrace, new Trace(Arrays.asList("e", "b", "c", "d", "a", "f")));
    }

    @Test
    public void testSwapAdjacentActivities() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapAdjacentActivities(cleanTrace, 5, 1));
        String expectedMessage1 = "The trace be longer than twice the length of the subtrace to swap.";
        String actualMessage1 = exception1.getMessage();
        assertEquals(actualMessage1, expectedMessage1);

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapAdjacentActivities(cleanTrace, 3, -1));
        String expectedMessage2 = "Invalid index provided.";
        String actualMessage2 = exception2.getMessage();
        assertEquals(actualMessage2, expectedMessage2);

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapAdjacentActivities(cleanTrace, 3, 1));
        String expectedMessage3 = "Invalid index provided.";
        String actualMessage3 = exception3.getMessage();
        assertEquals(actualMessage3, expectedMessage3);

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapAdjacentActivities(cleanTrace, 3, 7));
        String expectedMessage4 = "Invalid index provided.";
        String actualMessage4 = exception4.getMessage();
        assertEquals(actualMessage4, expectedMessage4);

        activitySwapper.swapAdjacentActivities(cleanTrace, 3, 0);
        assertEquals(cleanTrace, new Trace(Arrays.asList("d", "e", "f", "a", "b", "c")));

        activitySwapper.swapAdjacentActivities(cleanTrace, 3, 3);
        assertEquals(cleanTrace, new Trace(Arrays.asList("a", "b", "c", "d", "e", "f")));
    }

    @Test
    public void testSwapTwoAdjacentActivities() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapTwoAdjacentActivities(new Trace(), 1));
        String expectedMessage1 = "The trace must be longer than two to swap.";
        String actualMessage1 = exception1.getMessage();
        assertEquals(actualMessage1, expectedMessage1);

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapTwoAdjacentActivities(cleanTrace, cleanTrace.size()));
        String expectedMessage2 = "Invalid index.";
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage2));

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> activitySwapper.swapTwoAdjacentActivities(cleanTrace, -1));
        String expectedMessage3 = "Invalid index.";
        String actualMessage3 = exception3.getMessage();
        assertTrue(actualMessage3.contains(expectedMessage3));

        activitySwapper.swapTwoAdjacentActivities(cleanTrace, 1);
        assertEquals(cleanTrace, new Trace(Arrays.asList("a", "c", "b", "d", "e", "f")));

        activitySwapper.swapTwoAdjacentActivities(cleanTrace, 5);
        assertEquals(cleanTrace, new Trace(Arrays.asList("a", "c", "b", "d", "f", "e")));
    }

}
