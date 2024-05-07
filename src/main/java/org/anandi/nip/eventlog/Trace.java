package org.anandi.nip.eventlog;

import java.util.ArrayList;
import java.util.Collection;

public class Trace extends ArrayList<String> {

    public Trace() { super(); }

    public Trace(Collection<? extends String> c) {
        super(c);
    }

}
