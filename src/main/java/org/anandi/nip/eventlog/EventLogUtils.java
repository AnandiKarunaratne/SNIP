package org.anandi.nip.eventlog;

import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.in.XParser;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EventLogUtils {

    public static List<String> getActivities(XLog log) {
        List<String> activities = new ArrayList<>();
        for (XTrace trace : log) {
            for (XEvent event : trace) {
                activities.add(event.getAttributes().get("concept:name").toString());
            }
        }
        return activities;
    }

    public static Trace getTrace(XTrace trace) {
        Trace traceList = new Trace();
        for (XEvent event : trace) {
            traceList.add(event.getAttributes().get("concept:name").toString());
        }
        return traceList;
    }

    public static EventLog getEventLog(XLog log) {
        EventLog eventLog = new EventLog();
        for (XTrace trace : log) {
            eventLog.add(getTrace(trace));
        }
        return eventLog;
    }

    public EventLog readFile(String xesFile) {
        XFactory factory = XFactoryRegistry.instance().currentDefault();
        XParser parser = new XesXmlParser(factory);
        EventLog eventLog = new EventLog();
        try (FileInputStream fis = new FileInputStream(xesFile)) {
            if (parser.canParse(new File(xesFile))) {
                List<XLog> logs = parser.parse(fis);
                if (!logs.isEmpty()) {
                    XLog log = logs.get(0);

                    // Get all activities
                    List<String> activities = getActivities(log);
                    System.out.println("Activities: " + activities);

                    // Get the first trace
                    if (!log.isEmpty()) {
                        List<String> firstTrace = getTrace(log.get(0));
                        System.out.println("First trace: " + firstTrace);
                    }

                    // Get the entire event log
                    eventLog = getEventLog(log);
                    System.out.println("Event log: " + eventLog);
                }
            } else {
                System.out.println("Cannot parse the provided XES file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return eventLog;
    }

    public EventLog readXES(String filePath) {
        PrintStream currentOut = System.out;
        EventLog traces = new EventLog();
        try {
            System.setOut(new PrintStream(new ByteArrayOutputStream()));
            FileInputStream fileInputStream = new FileInputStream(filePath);
            XParser parser = new XesXmlParser();
            List<XLog> logs = parser.parse(fileInputStream);

            if (!logs.isEmpty()) {
                XLog log = logs.get(0); // Assuming there's only one log in the XES file

                for (XTrace xTrace : log) {
                    Trace trace = new Trace();
                    for (org.deckfour.xes.model.XEvent xEvent : xTrace) {
                        String activityName = xEvent.getAttributes().get("concept:name").toString();
                        trace.add(activityName);
                    }
                    traces.add(trace);
                }
            } else {
                System.out.println("No logs found in the XES file.");
            }
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.setOut(currentOut);
        }
        return traces;
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
