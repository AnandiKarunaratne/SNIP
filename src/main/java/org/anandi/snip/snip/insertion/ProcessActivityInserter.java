package org.anandi.snip.snip.insertion;

import org.anandi.snip.eventlog.Trace;

import java.util.Random;
import java.util.Set;

public class ProcessActivityInserter extends InsertionNoiseInjector {

    public ProcessActivityInserter(Set<String> activities) {
        super(activities);
    }

    @Override
    void injectActivity(Trace cleanTrace, int insertIndex) {
        double probability = 0.5; // inserting a random activity or duplicating has equal probability
        double methodDecider = Math.random();
        if (methodDecider < probability) {
            injectRandomActivity(cleanTrace, insertIndex);
        } else {
            duplicateActivity(cleanTrace, insertIndex);
        }
    }

    void injectRandomActivity(Trace cleanTrace, int insertIndex) {
        Random random = new Random();

        // Select a random activity from the activity set
        String insertActivity = null;
        if (!this.activities.isEmpty()) {
            int insertActivityIndex = random.nextInt(this.activities.size());
            String[] processActivitiesArray = this.activities.toArray(new String[0]);
            insertActivity = processActivitiesArray[insertActivityIndex];
        }

        // Insert the selected activity
        cleanTrace.add(insertIndex, insertActivity);
    }

    void duplicateActivity(Trace cleanTrace, int insertIndex) { // check edge cases
        String insertActivity = (insertIndex == cleanTrace.size()) ? cleanTrace.get(insertIndex - 1) : cleanTrace.get(insertIndex);
        cleanTrace.add(insertIndex, insertActivity);
    }
}
