package org.anandi.snip;

import org.anandi.snip.eventlog.EventLog;
import org.anandi.snip.eventlog.EventLogUtils;
import org.anandi.snip.snip.NoiseInjectionManager;
import org.anandi.snip.snip.NoiseType;
import org.apache.commons.cli.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// handles the main pipeline
public class Main {

    private static final Set<NoiseType> NOISE_TYPES = new HashSet<>();

    public static void main(String[] args) {

        EventLog cleanLog = null;
        ModificationLength modificationLength = new ModificationLength(LengthMode.FIXED_LENGTH, 1);
        int modificationLengthForFractionLengthMode = 3; // default value for the other length mode

        // stop libraries from printing out "INFO" and "WARNING" logs to console
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.SEVERE);

        // read parameters from the CLI
        CommandLineParser parser = new DefaultParser();

        Options options;
        try {
            options = new Options();

            Option help		    = Option.builder("h").longOpt("help").numberOfArgs(0).required(false).desc("print help message").hasArg(false).build();
            Option version  	= Option.builder("v").longOpt("version").numberOfArgs(0).required(false).desc("get version of this tool").hasArg(false).build();
            Option quiet        = Option.builder("q").longOpt("quiet").numberOfArgs(0).required(false).desc("do not print").hasArg(false).build();

            Option absence      = Option.builder("a").longOpt("absence").numberOfArgs(0).required(false).desc("inject absence noise").hasArg(false).build();
            Option insertion    = Option.builder("i").longOpt("insertion").numberOfArgs(0).required(false).desc("inject insertion noise").hasArg(false).build();
            Option ordering     = Option.builder("o").longOpt("ordering").numberOfArgs(0).required(false).desc("inject ordering noise").hasArg(false).build();
            Option substitution = Option.builder("s").longOpt("substitution").numberOfArgs(0).required(false).desc("inject substitution noise").hasArg(false).build();
            Option log		    = Option.builder("l").longOpt("log").hasArg(true).optionalArg(false).valueSeparator('=').argName("file path").required(false).desc("clean event log").build();
            Option noiseLevel   = Option.builder("n").longOpt("noise-level").hasArg(true).optionalArg(false).valueSeparator('=').argName("percentage").required(false).desc("noise level percentage").build();
            Option samplingMode = Option.builder("m").longOpt("sampling-mode").hasArg(true).optionalArg(false).valueSeparator('=').argName("sampling mode").required(false).desc("sampling mode: with replacement (wr) or without replacement (wor)").build();
            Option numAlts      = Option.builder("k").longOpt("num-alts").hasArg(true).optionalArg(false).valueSeparator('=').argName("maximum number of alterations").required(false).desc("maximum number of alterations made in a trace: +k for the exact number, -k for 1/k of the trace size").build();

            options.addOption(help);
            options.addOption(version);
            options.addOption(quiet);
            options.addOption(absence);
            options.addOption(insertion);
            options.addOption(ordering);
            options.addOption(substitution);
            options.addOption(log);
            options.addOption(noiseLevel);
            options.addOption(samplingMode);
            options.addOption(numAlts);

            CommandLine cmd = parser.parse(options, args);
            boolean isQuiet = false;

            if (cmd.hasOption(quiet)) {
                isQuiet = true;
                System.setOut(new PrintStream(new ByteArrayOutputStream()));
            }
            if (cmd.hasOption(help) || cmd.getOptions().length == 0) { // handle help
                showHelp(options);
            } else if (cmd.hasOption(version)) {
                System.out.println(getVersion());
            } else {
                showHeader();
                String inputFilePath = "";
                if (cmd.hasOption(log)) {
                    inputFilePath = cmd.getOptionValue(log);
                    if (!extractFileExtension(inputFilePath).equals("xes")) {
                        throw new ParseException("Wrong input format");
                    } else {
                        System.out.println("Reading event log: " + inputFilePath + "\n");
                        cleanLog = new EventLogUtils().readXES(inputFilePath);
                    }
                }

                double noisePercentage = 0.0;
                String noiseDescText = "";

                if (cmd.hasOption(insertion)) {
                    NOISE_TYPES.add(NoiseType.INSERTION);
                    noiseDescText += "i-";
                }

                if (cmd.hasOption(absence)) {
                    NOISE_TYPES.add(NoiseType.ABSENCE);
                    noiseDescText += "a-";
                }

                if (cmd.hasOption(ordering)) {
                    NOISE_TYPES.add(NoiseType.ORDERING);
                    noiseDescText += "o-";
                }

                if (cmd.hasOption(substitution)) {
                    NOISE_TYPES.add(NoiseType.SUBSTITUTION);
                    noiseDescText += "s-";
                }

                if (NOISE_TYPES.isEmpty()) {
                    NOISE_TYPES.add(NoiseType.ABSENCE);
                    NOISE_TYPES.add(NoiseType.INSERTION);
                    NOISE_TYPES.add(NoiseType.ORDERING);
                    NOISE_TYPES.add(NoiseType.SUBSTITUTION);
                    noiseDescText += "i-a-o-s-";
                }

                if (cmd.hasOption(noiseLevel)) {
                    noisePercentage = Double.parseDouble(cmd.getOptionValue(noiseLevel));
                    noiseDescText += noisePercentage;
                }

                if (cmd.hasOption(numAlts)) {
                    String alterations = cmd.getOptionValue(numAlts);
                    if (alterations.charAt(0) == '+') {
                        System.out.println("num alts +k detected" + numAlts);
                        if (alterations.length() > 1) {
                            modificationLength = new ModificationLength(LengthMode.FIXED_LENGTH, Integer.parseInt(alterations.substring(1)));
                        }
                    } else if (alterations.charAt(0) == '-'){
                        System.out.println("num alts -k detected" + numAlts);
                        if (alterations.length() > 1) {
                            modificationLength = new ModificationLength(LengthMode.FRACTION_OF_TRACE_LENGTH, Integer.parseInt(alterations.substring(1)));
                        } else {
                            modificationLength = new ModificationLength(LengthMode.FRACTION_OF_TRACE_LENGTH, modificationLengthForFractionLengthMode);
                        }
                    }
                }

                String noiseTypesString = NOISE_TYPES.stream().map(NoiseType::name).collect(Collectors.joining(", "));
                System.out.println("================================================================================\n" +
                        "Injecting " + noisePercentage + "% noise to the event log with noise types: " + noiseTypesString + ".\n");
                NoiseInjectionManager noiseInjectionManager= new NoiseInjectionManager();
                EventLog noisyLog;
                if (cmd.hasOption(samplingMode) && cmd.getOptionValue(samplingMode).equals("wor")) {
                    noisyLog = noiseInjectionManager.generateNoisyLogWithoutReplacement(cleanLog, noisePercentage / 100, NOISE_TYPES, modificationLength);
                } else {
                    noisyLog = noiseInjectionManager.generateNoisyLogWithReplacement(cleanLog, noisePercentage / 100, NOISE_TYPES, modificationLength);
                }

                String outputFilePath = extractFolderPath(inputFilePath) + "/" + extractFileNameWithoutExtension(inputFilePath) + "-noise-" + noiseDescText + ".xes";
                System.out.println("================================================================================\n\n" +
                        "Writing noise injected event log: " + outputFilePath);
                new EventLogUtils().generateXES(noisyLog, outputFilePath);
                overrideQuietMode();
                System.out.println("Noisy log generated: " + outputFilePath);
                if(!isQuiet) System.out.println("\n================================================================================");
                generateLogOfPerformedActions(noiseInjectionManager.getLogEntry(), extractFolderPath(inputFilePath) + "/" + extractFileNameWithoutExtension(inputFilePath) + "-noise-" + noiseDescText + "-log.txt");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    private static void overrideQuietMode() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

    private static String extractFileExtension(String filePath) {
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        int pos = fileName.lastIndexOf('.');
        if (pos > 0 && pos < fileName.length() - 1) {
            return fileName.substring(pos + 1);
        } else {
            return ""; // no extension found or filename starts with a dot
        }
    }

    private static String extractFolderPath(String filePath) {
        Path path = Paths.get(filePath);
        Path parent = path.getParent();
        return parent.toString();
    }

    private static String extractFileNameWithoutExtension(String filePath) {
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            return fileName.substring(0, pos);
        } else {
            return fileName; // Return full filename if no extension found
        }
    }

    private static void showHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();

        showHeader();
        formatter.printHelp(120, "java -jar SNIP-" + getVersion() + ".jar <options>",
                "", options,
                "================================================================================\n");
    }

    private static String getVersion() {
        String versionProperties = "project.properties";
        String versionKey = "version";
        Properties properties = new Properties();
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(versionProperties)) {
            if (inputStream != null) {
                properties.load(inputStream);
                return properties.getProperty(versionKey);
            } else {
                throw new IOException("Resource file not found: " + versionProperties);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    private static void generateLogOfPerformedActions(String logEntry, String outputPath) {
        try {
            FileWriter writer = new FileWriter(outputPath, true); // true = append mode
            writer.write(logEntry + System.lineSeparator());
            writer.close();
            System.out.println("Log entry written successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the log file.");
            e.printStackTrace();
        }
    }

    private static void showHeader() {
        System.out.println("================================================================================\n"+
                "Tool to generate a noisy event log for Process Mining ver. " + getVersion() + ".\n"+
                "================================================================================\n" +
                "XES format:	https://xes-standard.org/\n" +
                "================================================================================\n");

    }

}