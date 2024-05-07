package org.anandi.nip.nip.insertion;

import org.anandi.nip.eventlog.Trace;

import java.util.Random;
import java.util.Set;

public class UnrelatedActivityInserter extends InsertionNoiseInjector {

    Set<String> activities;

    UnrelatedActivityInserter(Set<String> activities) {
        super(activities);
        this.activities = activities;
    }

    @Override
    Trace injectActivity(Trace cleanTrace, int insertIndex) {
        Random random = new Random();
        String insertActivity = generateRandomActivityName();
        cleanTrace.add(insertIndex, insertActivity);
        return cleanTrace;
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
