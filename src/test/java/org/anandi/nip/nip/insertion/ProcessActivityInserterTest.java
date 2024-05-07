package org.anandi.nip.nip.insertion;

import org.anandi.nip.eventlog.Trace;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ProcessActivityInserterTest {

    Set<String> activities = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
    ProcessActivityInserter processActivityInserter = new ProcessActivityInserter(activities);
    Trace baseTrace = new Trace(Arrays.asList("a", "b", "c"));
    Trace cleanTrace = new Trace(baseTrace);

    @Test
    public void testInjectActivity() {
        int insertIndex = 3;
        processActivityInserter.injectActivity(cleanTrace, insertIndex);
        assertEquals(cleanTrace.subList(0, 3), baseTrace);
        assertEquals(cleanTrace.size(), baseTrace.size() + 1);
        assertTrue(activities.contains(cleanTrace.get(insertIndex)));
    }

    @Test
    public void testDuplicateActivity() {
        int insertIndex = 3;
        processActivityInserter.duplicateActivity(cleanTrace, insertIndex);
    }
}
