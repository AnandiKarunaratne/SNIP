package org.anandi.nip.nip.insertion;

import org.anandi.nip.eventlog.Trace;

import java.util.Random;
import java.util.Set;

public class ProcessActivityInserter extends InsertionNoiseInjector {

    public ProcessActivityInserter(Set<String> activities) {
        super(activities);
    }

    @Override
    public void injectActivity(Trace cleanTrace, int insertIndex) {
        Random random = new Random();

        // Select a random activity from the activity set
        String insertActivity = null;
        if (!this.activities.isEmpty()) {
            int insertActivityIndex = random.nextInt(this.activities.size());
            String[] processActivitiesArray = this.activities.toArray(new String[this.activities.size()]);
            insertActivity = processActivitiesArray[insertActivityIndex];
        }

        // Insert the selected activity
        cleanTrace.add(insertIndex, insertActivity);
    }

    void duplicateActivity(Trace cleanTrace, int insertIndex) {
        String insertActivity = cleanTrace.get(insertIndex);
        cleanTrace.add(insertIndex, insertActivity);
    }
}
