package org.anandi.snip.eventlog;

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

    public Set<Trace> getDistinctTraces() {
        return new HashSet<>(this);
    }

    public int getNumOfActivities() {
        return getActivities().size();
    }

    public int getNumOfDistinctTraces() {
        return getDistinctTraces().size();
    }

    public int getNumOfTraces() {
        return this.size();
    }

}
