package org.anandi.nip.eventlog;

import org.junit.AfterClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class EventLogUtilsTest {

    static String xesFilePath = "./src/test/resources/testXesFile.xes";
    EventLog sampleEventLog = new EventLog(new ArrayList<>(Arrays.asList(
            new Trace(new ArrayList<>(Arrays.asList("a", "b", "c", "d"))),
            new Trace(new ArrayList<>(Arrays.asList("a", "b", "e", "f")))
    )));

    @Test
    public void testGenerateXES() {
        new EventLogUtils().generateXES(sampleEventLog, xesFilePath);
        assertTrue(Files.exists(Paths.get(xesFilePath)));
    }

    @Test
    public void testReadXES() {
        EventLog fileContent = new EventLogUtils().readXES(xesFilePath);
        assertEquals(sampleEventLog, fileContent);
    }

    @AfterClass
    public static void deleteGeneratedFile() {
        File generatedXes = new File(xesFilePath);

        if (generatedXes.exists()) {
            generatedXes.delete();
        }
    }

}
