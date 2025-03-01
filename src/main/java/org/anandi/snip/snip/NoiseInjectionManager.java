package org.anandi.snip.snip;

import org.anandi.snip.eventlog.EventLog;
import org.anandi.snip.eventlog.Trace;
import org.anandi.snip.snip.absence.AbsenceNoiseInjectionManager;
import org.anandi.snip.snip.insertion.InsertionNoiseInjectionManager;
import org.anandi.snip.snip.ordering.OrderingNoiseInjectionManager;
import org.anandi.snip.snip.substitution.SubstitutionNoiseInjectionManager;

import java.util.*;

public class NoiseInjectionManager {

    private Set<String> activities = new HashSet<>();

    public EventLog generateNoisyLog(EventLog cleanLog, double noiseLevel, Set<NoiseType> noiseTypes) {
        isNoiseTypesAllowed(noiseTypes);
        this.activities = cleanLog.getActivities();
        EventLog traceRemovedList = new EventLog(cleanLog);
        EventLog noiseList = new EventLog();

        int numOfNoisyTraces = (int) (noiseLevel * cleanLog.size());

        Random random = new Random();

        for (int i = 0; i < numOfNoisyTraces; i++) {
            int index = random.nextInt(traceRemovedList.size());
            Trace cleanTrace = traceRemovedList.get(index);
            traceRemovedList.remove(index);
            int length = random.nextInt(cleanTrace.size() / 3) + 1;
            generateNoisyTrace(cleanTrace, length, noiseTypes);
            noiseList.add(cleanTrace);
        }

        EventLog noisyLog = new EventLog(traceRemovedList);
        noisyLog.addAll(noiseList);
        return noisyLog;
    }

    protected void isNoiseTypesAllowed(Set<NoiseType> noiseTypes) {
        if (!getNoiseTypes().containsAll(noiseTypes)) {
            throw new IllegalArgumentException("Specified noise type is now allowed by the injector");
        }
    }

    public EventLog generateNoisyLog(EventLog cleanLog, double noisePercentage) {
        return generateNoisyLog(cleanLog, noisePercentage, getNoiseTypes());
    }

    public void generateNoisyTrace(Trace cleanTrace, int length, Set<NoiseType> noiseTypeSet) { // set to ensure no duplicates
        isNoiseTypesAllowed(noiseTypeSet);
        List<NoiseType> noiseTypeList = new ArrayList<>(noiseTypeSet);
        Random random = new Random();
        int noiseTypeIndex = random.nextInt(noiseTypeList.size());
        NoiseType noiseType = noiseTypeList.get(noiseTypeIndex);
        generateNoisyTrace(cleanTrace, length, noiseType);
    }

    public void generateNoisyTrace(Trace cleanTrace, int length) {
        generateNoisyTrace(cleanTrace, length, getNoiseTypes());
    }

    protected Set<NoiseType> getNoiseTypes() {
        Set<NoiseType> noiseTypes = new HashSet<>();
        noiseTypes.add(NoiseType.ABSENCE);
        noiseTypes.add(NoiseType.INSERTION);
        noiseTypes.add(NoiseType.ORDERING);
        noiseTypes.add(NoiseType.SUBSTITUTION);
        return noiseTypes;
    }

    private void generateNoisyTrace(Trace cleanTrace, int length, NoiseType noiseType) {
        System.out.println("Injecting " + noiseType + " noise to trace: " + cleanTrace);
        switch (noiseType) {
            case ABSENCE:
                new AbsenceNoiseInjectionManager().generateNoisyTrace(cleanTrace, length);
                break;
            case INSERTION:
                new InsertionNoiseInjectionManager(activities).generateNoisyTrace(cleanTrace, length);
                break;
            case ORDERING:
                new OrderingNoiseInjectionManager().generateNoisyTrace(cleanTrace, length);
                break;
            case SUBSTITUTION:
                new SubstitutionNoiseInjectionManager(activities).generateNoisyTrace(cleanTrace, length);
                break;
        }
        System.out.println("Injected " + noiseType + " noise:           " + cleanTrace + "\n");
    }

}
