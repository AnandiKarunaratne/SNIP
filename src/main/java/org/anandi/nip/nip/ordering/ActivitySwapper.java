package org.anandi.nip.nip.ordering;

import org.anandi.nip.eventlog.Trace;

import java.util.Random;

public class ActivitySwapper extends OrderingNoiseInjector {

    @Override
    public void injectNoise(Trace cleanTrace, int length, double probability) {
        double methodDecider = Math.random();
        if (methodDecider < probability) {
            swapActivities(cleanTrace, length);
        } else {
            swapAdjacentActivities(cleanTrace, length);
        }
    }

    public void swapActivities(Trace cleanTrace, int length, int index1, int index2) {
        if (cleanTrace.size() < 2 * length) {
            throw new IllegalArgumentException("The trace be longer than twice the length of the subtrace to swap.");
        }
        if (index1 > cleanTrace.size() - 2 * length || index1 < 0) {
            throw new IllegalArgumentException("Invalid first index.");
        }
        if (index2 > cleanTrace.size() - length || index2 <= index1 || index2 < length) {
            throw new IllegalArgumentException("Invalid second index.");
        }
        for (int i = 0; i < length; i++) {
            swapTwoActivities(cleanTrace, index1 + i, index2 + i);
        }
    }

    public void swapActivities(Trace cleanTrace, int length) {
        if (cleanTrace.size() < 2 * length) {
            throw new IllegalArgumentException("The trace must be longer than twice the length of the subtrace to swap.");
        }
        Random random = new Random();
        int index1 = random.nextInt(cleanTrace.size() - 2 * length + 1);
        int index2;
        do {
            index2 = random.nextInt(cleanTrace.size() - length + 1);
        } while (index2 - index1 < length);

        swapActivities(cleanTrace, length, index1, index2);
    }

    public void swapTwoActivities(Trace cleanTrace, int index1, int index2) {
        if (cleanTrace.size() < 2) {
            throw new IllegalArgumentException("The trace must be longer than two to swap.");
        }
        String temp = cleanTrace.get(index1);
        cleanTrace.set(index1, cleanTrace.get(index2));
        cleanTrace.set(index2, temp);
    }

    public void swapAdjacentActivities(Trace cleanTrace, int length) {
        if (cleanTrace.size() < 2 * length) {
            throw new IllegalArgumentException("The trace be longer than twice the length of the subtrace to swap.");
        }
        Random random = new Random();
        int index = random.nextInt(cleanTrace.size() - length + 1);
        swapAdjacentActivities(cleanTrace, length, index);
    }

    public void swapAdjacentActivities(Trace cleanTrace, int length, int index) {
        if (cleanTrace.size() < 2 * length) {
            throw new IllegalArgumentException("The trace be longer than twice the length of the subtrace to swap.");
        }
        int index1;
        int index2;
        if (index <= cleanTrace.size() - 2 * length && index >= 0) { // check if index qualifies as the first index.
            index1 = index;
            index2 = index1 + length;
        } else if (index <= cleanTrace.size() - length && index >= length) { // check if index qualifies as the second index.
            index2 = index;
            index1 = index2 - length;
        } else { // index does not qualify as the first or second index.
            throw new IllegalArgumentException("Invalid index provided.");
        }
        swapActivities(cleanTrace, length, index1, index2);
    }

    public void swapTwoAdjacentActivities(Trace cleanTrace, int index) {
        if (cleanTrace.size() < 2) {
            throw new IllegalArgumentException("The trace must be longer than two to swap.");
        }
        if (index >= cleanTrace.size() || index < 0) {
            throw new IllegalArgumentException("Invalid index.");
        }
        swapAdjacentActivities(cleanTrace, 1, index);
    }

}
