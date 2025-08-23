package org.anandi.snip.snip.insertion;

import org.anandi.snip.eventlog.Trace;
import org.anandi.snip.snip.NoiseInjector;

import java.util.Random;
import java.util.Set;

public class InsertionNoiseInjector extends NoiseInjector {

    Set<String> activities;

    /**
     * Decisions of Insertion noise.
     * Every decision has an equal probability by default.
     */
    private double positionProbability = 0.5; // If set to 0, insertion will happen only random in positions; If 1, only sequentially.
    private double processActivityProbability = 0.5; // If set to 0, only process activities will be inserted.

    // Default decisions
    public InsertionNoiseInjector(Set<String> activities){
        this.activities = activities;
    }

    // Manual decisions
    public InsertionNoiseInjector(Set<String> activities, double positionProbability, double processActivityProbability) {
        this.activities = activities;
        this.positionProbability = positionProbability;
        this.processActivityProbability = processActivityProbability;
    }

    @Override
    public String injectNoise(Trace cleanTrace, int length) {
        String logMessage = "\"position\": ";
        double methodDecider = Math.random();
        if (methodDecider < positionProbability) {
            logMessage += "\"consecutive\",\n";
            insertConsecutiveActivities(cleanTrace, length);
        } else {
            insertRandomActivities(cleanTrace, length);
            logMessage += "\"random\",\n";
        }
        return logMessage;
    }

    public void insertConsecutiveActivities(Trace cleanTrace, int length) {
        Random random = new Random();
        int startIndex = random.nextInt(cleanTrace.size() + 1);
        insertConsecutiveActivities(cleanTrace, length, startIndex);
    }

    public void insertConsecutiveActivities(Trace cleanTrace, int length, int startIndex) {
        for (int i = 0; i < length; i++) {
            injectActivity(cleanTrace, i + startIndex);
        }
    }

    void insertRandomActivities(Trace cleanTrace, int length) {
        Random random = new Random();
        int insertIndex;
        for (int i = 0; i < length; i++) {
            insertIndex = random.nextInt(cleanTrace.size() + 1);
            injectActivity(cleanTrace, insertIndex);
        }
    }

    void injectActivity(Trace cleanTrace, int insertIndex) {
        double methodDecider = Math.random();
        if (methodDecider < processActivityProbability) {
            injectProcessActivity(cleanTrace, insertIndex);
        } else {
            injectUnrelatedActivity(cleanTrace, insertIndex);
        }
    }

    /**
     * Process Activity related Methods
     */
    void injectProcessActivity(Trace cleanTrace, int insertIndex) {
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

    /**
     * Unrelated Activity related Methods
     */
    void injectUnrelatedActivity(Trace cleanTrace, int insertIndex) {
        String insertActivity;
        do {
            insertActivity = generateRandomActivityName();
        } while (activities.contains(insertActivity));

        cleanTrace.add(insertIndex, insertActivity);
    }

    private String generateRandomActivityName() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        Random random = new Random();
        int activityNameLength = random.nextInt(20); // generate a random string of this length
        StringBuilder stringBuilder = new StringBuilder(activityNameLength);

        for (int i = 0; i < activityNameLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

}
