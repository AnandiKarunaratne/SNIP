package org.anandi.nip.nip.insertion;

import org.anandi.nip.eventlog.Trace;

import java.util.Random;
import java.util.Set;

public class UnrelatedActivityInserter extends InsertionNoiseInjector {

    UnrelatedActivityInserter(Set<String> activities) {
        super(activities);
    }

    @Override
    void injectActivity(Trace cleanTrace, int insertIndex) {
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
