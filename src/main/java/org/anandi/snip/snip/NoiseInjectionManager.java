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
    protected String logEntry = "";

    public String getLogEntry() {
        return logEntry;
    }

    public EventLog generateNoisyLogWithReplacement(EventLog cleanLog, double noiseLevel, Set<NoiseType> noiseTypes) {
        logEntry += initialLogEntry(cleanLog, noiseLevel);

        isNoiseTypesAllowed(noiseTypes);
        this.activities = cleanLog.getActivities();

        int numOfNoisyTraces = (int) (noiseLevel * cleanLog.size());

        Random random = new Random();

        for (int i = 0; i < numOfNoisyTraces; i++) {
            try {
                int index = random.nextInt(cleanLog.size());
                Trace cleanTrace = cleanLog.get(index);

                int bound = Math.max(1, cleanTrace.size() / 3);
                int length = random.nextInt(bound) + 1;
                generateNoisyTrace(cleanTrace, length, noiseTypes);
            } catch (IllegalArgumentException e) {
                System.out.println("Skipping trace due to: " + e.getMessage());
                i--;
            }
        }
        logEntry += finalLogEntry(cleanLog);
        return cleanLog;
    }

    public EventLog generateNoisyLogWithoutReplacement(EventLog cleanLog, double noiseLevel, Set<NoiseType> noiseTypes) {
        logEntry += initialLogEntry(cleanLog, noiseLevel);

        isNoiseTypesAllowed(noiseTypes);
        this.activities = cleanLog.getActivities();
        EventLog traceRemovedList = new EventLog(cleanLog);
        EventLog noiseList = new EventLog();

        int numOfNoisyTraces = (int) (noiseLevel * cleanLog.size());

        Random random = new Random();

        for (int i = 0; i < numOfNoisyTraces; i++) {
            try {
                int index = random.nextInt(traceRemovedList.size());
                Trace cleanTrace = traceRemovedList.get(index);

                int bound = Math.max(1, cleanTrace.size() / 3);
                int length = random.nextInt(bound) + 1;
                generateNoisyTrace(cleanTrace, length, noiseTypes);

                // do these only if there's no exception
                noiseList.add(cleanTrace);
                traceRemovedList.remove(index);
            } catch (IllegalArgumentException e) {
                System.out.println("Skipping trace due to: " + e.getMessage());
                i--;
            }
        }

        EventLog noisyLog = new EventLog(traceRemovedList);
        noisyLog.addAll(noiseList);
        logEntry += finalLogEntry(noisyLog);
        return noisyLog;
    }

    protected void isNoiseTypesAllowed(Set<NoiseType> noiseTypes) {
        if (!getNoiseTypes().containsAll(noiseTypes)) {
            throw new IllegalArgumentException("Specified noise type is not allowed by the injector");
        }
    }

    public EventLog generateNoisyLogWithoutReplacement(EventLog cleanLog, double noisePercentage) {
        return generateNoisyLogWithoutReplacement(cleanLog, noisePercentage, getNoiseTypes());
    }

    public String generateNoisyTrace(Trace cleanTrace, int length, Set<NoiseType> noiseTypeSet) { // set to ensure no duplicates
        isNoiseTypesAllowed(noiseTypeSet);
        List<NoiseType> noiseTypeList = new ArrayList<>(noiseTypeSet);
        Random random = new Random();
        int noiseTypeIndex = random.nextInt(noiseTypeList.size());
        NoiseType noiseType = noiseTypeList.get(noiseTypeIndex);
        return generateNoisyTrace(cleanTrace, length, noiseType);
    }

    public String generateNoisyTrace(Trace cleanTrace, int length) {
        return generateNoisyTrace(cleanTrace, length, getNoiseTypes());
    }

    protected Set<NoiseType> getNoiseTypes() {
        Set<NoiseType> noiseTypes = new HashSet<>();
        noiseTypes.add(NoiseType.ABSENCE);
        noiseTypes.add(NoiseType.INSERTION);
        noiseTypes.add(NoiseType.ORDERING);
        noiseTypes.add(NoiseType.SUBSTITUTION);
        return noiseTypes;
    }

    private String generateNoisyTrace(Trace cleanTrace, int length, NoiseType noiseType) {
        String logMessage = "{\n\"original_trace\": \"" + cleanTrace + "\",\n";
        logMessage += "\"length\": \"" + length + "\",\n";
        logMessage += "\"noise_type\": \"" + noiseType.toString() + "\",\n";
        switch (noiseType) {
            case ABSENCE:
                logMessage += new AbsenceNoiseInjectionManager().generateNoisyTrace(cleanTrace, length);
                break;
            case INSERTION:
                logMessage += new InsertionNoiseInjectionManager(activities).generateNoisyTrace(cleanTrace, length);
                break;
            case ORDERING:
                logMessage += new OrderingNoiseInjectionManager().generateNoisyTrace(cleanTrace, length);
                break;
            case SUBSTITUTION:
                logMessage += new SubstitutionNoiseInjectionManager(activities).generateNoisyTrace(cleanTrace, length);
                break;
        }
        logMessage += "\"noisy_trace\": \"" + cleanTrace + "\"\n},\n";
        logEntry += logMessage + "\n";
        return logMessage;
    }

    private String initialLogEntry(EventLog cleanLog, double noiseLevel) {
        return "{\n\"log_size\": " + cleanLog.getNumOfTraces() +
                ",\n\"initial_num_of_distinct_traces\": " + cleanLog.getNumOfDistinctTraces() +
                ",\n\"initial_num_of_distinct_activities\": " + cleanLog.getNumOfActivities() +
                ",\n\"initial_distinct_activities\": " + cleanLog.getActivities() +
                ",\n\"noise_level\": " + noiseLevel + "\n},\n";
    }

    private String finalLogEntry(EventLog log) {
        return "{\n\"log_size\": " + log.getNumOfTraces() +
                ",\n\"final_num_of_distinct_traces\": " + log.getNumOfDistinctTraces() +
                ",\n\"final_num_of_distinct_activities\": " + log.getNumOfActivities() +
                ",\n\"final_distinct_activities\": " + log.getActivities() + "\n}";
    }

}
