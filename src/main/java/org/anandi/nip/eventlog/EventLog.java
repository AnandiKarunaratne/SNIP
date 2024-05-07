package org.anandi.nip.eventlog;

import java.util.ArrayList;
import java.util.Collection;

public class EventLog extends ArrayList<Trace> {

    public EventLog() { super(); }

    public EventLog(Collection<? extends Trace> c) {
        super(c);
    }

}
