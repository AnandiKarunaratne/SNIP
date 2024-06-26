package org.anandi.nip.eventlog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EventLog extends ArrayList<Trace> {

    public EventLog() { super(); }

    public EventLog(Collection<? extends Trace> c) {
        super(c);
    }

    public Set<String> getActivities() {
        Set<String> activities = new HashSet<>();
        for (Trace trace : this) {
            activities.addAll(trace);
        }
        return activities;
    }

}
