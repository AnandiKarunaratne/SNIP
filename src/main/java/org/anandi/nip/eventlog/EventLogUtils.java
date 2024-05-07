package org.anandi.nip.eventlog;

import org.deckfour.xes.in.XParser;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EventLogUtils {

    public EventLog readXES(String filePath) {
        EventLog eventLog = new EventLog();

        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            XParser parser = new XesXmlParser();
            List<XLog> logs = parser.parse(fileInputStream);

            if (!logs.isEmpty()) {
                XLog log = logs.get(0); // Assuming there's only one log in the XES file

                for (XTrace xTrace : log) {
                    Trace testTrace = new Trace();
                    for (org.deckfour.xes.model.XEvent xEvent : xTrace) {
                        String activityName = xEvent.getAttributes().get("concept:name").toString();
                        testTrace.add(activityName);
                    }
                    eventLog.add(testTrace);
                }
            } else {
                System.out.println("No logs found in the XES file.");
            }
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return eventLog;
    }

    public void generateXES(EventLog log, String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);

            fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            fileWriter.write("<log xes.version=\"1.0\" xes.features=\"nested-attributes\" openxes.version=\"1.0RC7\">\n");
            fileWriter.write("<extension name=\"Lifecycle\" prefix=\"lifecycle\" uri=\"http://www.xes-standard.org/lifecycle.xesext\"/>\n");
            fileWriter.write("<extension name=\"Organizational\" prefix=\"org\" uri=\"http://www.xes-standard.org/org.xesext\"/>\n");
            fileWriter.write("<extension name=\"Time\" prefix=\"time\" uri=\"http://www.xes-standard.org/time.xesext\"/>\n");
            fileWriter.write("<extension name=\"Concept\" prefix=\"concept\" uri=\"http://www.xes-standard.org/concept.xesext\"/>\n");
            fileWriter.write("<extension name=\"Semantic\" prefix=\"semantic\" uri=\"http://www.xes-standard.org/semantic.xesext\"/>\n");
            fileWriter.write("<global scope=\"trace\">\n");
            fileWriter.write("<string key=\"concept:name\" value=\"UNKNOWN\"/>\n");
            fileWriter.write("</global>\n");
            fileWriter.write("<global scope=\"event\">\n");
            fileWriter.write("<string key=\"concept:name\" value=\"UNKNOWN\"/>\n");
            fileWriter.write("</global>\n");
            fileWriter.write("<classifier name=\"Activity classifier\" keys=\"concept:name\"/>\n");
            fileWriter.write("<string key=\"concept:name\" value=\"log\"/>\n");

            int traceNumber = 1;
            for (List<String> trace : log) {
                fileWriter.write("<trace>\n");
                fileWriter.write("<string key=\"concept:name\" value=\"" + traceNumber + "\"/>\n");

                for (String activity : trace) {
                    fileWriter.write("<event><string key=\"concept:name\" value=\"" + activity + "\"/></event>\n");
                }

                fileWriter.write("</trace>\n");
                traceNumber++;
            }
            fileWriter.write("</log>\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
