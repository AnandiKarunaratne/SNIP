package org.anandi.nip.nip.ordering;

import org.anandi.nip.eventlog.Trace;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertThrows;

public class ActivityShifterTest {

    ActivityShifter activityShifter = new ActivityShifter();
    Trace cleanTrace = new Trace(Arrays.asList("a", "b", "c", "d", "e", "f"));

    @Test
    public void testShiftRandomActivitiesToLeft() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> activityShifter.shiftActivitiesToLeft(new Trace(), 2));
        String expectedMessage = "The trace should be longer than the length of the subtrace to be shifted.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        activityShifter.shiftActivitiesToLeft(cleanTrace, 5);
        assertEquals(cleanTrace, new Trace(Arrays.asList("b", "c", "d", "e", "f", "a")));
    }

    @Test
    public void testShiftActivitiesToLeft() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> activityShifter.shiftActivitiesToLeft(new Trace(), 3, 0, 1));
        String expectedMessage1 = "The trace should be longer than the length of the subtrace to be shifted.";
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> activityShifter.shiftActivitiesToLeft(cleanTrace, 3, 0, 1));
        String expectedMessage2 = "Start index should be larger than shift index.";
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage2));

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> activityShifter.shiftActivitiesToLeft(cleanTrace, 2, 2, -1));
        String expectedMessage3 = "Invalid shift index.";
        String actualMessage3 = exception3.getMessage();
        assertTrue(actualMessage3.contains(expectedMessage3));

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> activityShifter.shiftActivitiesToLeft(cleanTrace, 2, 5, 4));
        String expectedMessage4 = "Invalid shift index.";
        String actualMessage4 = exception4.getMessage();
        assertTrue(actualMessage4.contains(expectedMessage4));

        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> activityShifter.shiftActivitiesToLeft(cleanTrace, 2, 6, 3));
        String expectedMessage5 = "Invalid start index.";
        String actualMessage5 = exception5.getMessage();
        assertTrue(actualMessage5.contains(expectedMessage5));

        activityShifter.shiftActivitiesToLeft(cleanTrace, 2, 3, 0);
        assertEquals(cleanTrace, new Trace(Arrays.asList("d", "e", "a", "b", "c", "f")));
    }

    @Test
    public void testShiftRandomActivitiesToRight() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> activityShifter.shiftActivitiesToRight(new Trace(), 2));
        String expectedMessage = "The trace should be longer than the length of the subtrace to be shifted.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        activityShifter.shiftActivitiesToRight(cleanTrace, 5);
        assertEquals(cleanTrace, new Trace(Arrays.asList("f", "a", "b", "c", "d", "e")));
    }

    @Test
    public void testShiftActivitiesToRight() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> activityShifter.shiftActivitiesToRight(new Trace(), 3, 0, 1));
        String expectedMessage1 = "The trace should be longer than the length of the subtrace to be shifted.";
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> activityShifter.shiftActivitiesToRight(cleanTrace, 3, 2, 1));
        String expectedMessage2 = "Start index should be smaller than shift index.";
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage2));

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> activityShifter.shiftActivitiesToRight(cleanTrace, 2, -1, 1));
        String expectedMessage3 = "Invalid start index.";
        String actualMessage3 = exception3.getMessage();
        assertTrue(actualMessage3.contains(expectedMessage3));

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> activityShifter.shiftActivitiesToRight(cleanTrace, 2, 4, 5));
        String expectedMessage4 = "Invalid start index.";
        String actualMessage4 = exception4.getMessage();
        assertTrue(actualMessage4.contains(expectedMessage4));

        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> activityShifter.shiftActivitiesToRight(cleanTrace, 2, 0, 5));
        String expectedMessage5 = "Invalid shift index.";
        String actualMessage5 = exception5.getMessage();
        assertTrue(actualMessage5.contains(expectedMessage5));

        activityShifter.shiftActivitiesToRight(cleanTrace, 2, 1, 4);
        assertEquals(cleanTrace, new Trace(Arrays.asList("a", "d", "e", "f", "b", "c")));
    }

}
