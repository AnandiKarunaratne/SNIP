# NIPs (Noise Injection Programs)

## Overview

NIPs is a tool designed to inject noise into a given event log in the XES (https://xes-standard.org/) format. It allows users to specify the noise level and types of noise they want to introduce. The output is a noisy event log in the XES format.

## Features

- Inject different types of noise into an event log.
- Specify the noise level as a percentage.
- Supports the following noise types:
  - ABSENCE
  - INSERTION
  - ORDERING
  - SUBSTITUTION

## Getting Started

### Prerequisites

- Java 11 or later
- Maven 3.6.0 or later

### Building the Project

To build the project, run the following Maven command:

```
mvn clean package
```

After the build completes, you will find the generated JAR file in the `target` folder: `target/NIP-<version>-jar-with-dependencies.jar`.

## Usage

To run the tool, use the following command format:

```
java -jar NIP-<version>-jar-with-dependencies.jar <options>
```

### Options

```
 -a,--absence                    inject absence noise
 -h,--help                       print help message
 -i,--insertion                  inject insertion noise
 -l,--log <file path>            clean event log
 -n,--noise-level <percentage>   noise level percentage
 -o,--ordering                   inject ordering noise
 -q,--quiet                      do not print
 -s,--substitution               inject substitution noise
 -v,--version                    get version of this tool
```

### Examples

1. **Injecting Absence Noise:**

  ```
   java -jar NIP-1.0-SNAPSHOT-jar-with-dependencies.jar -a -l=path/to/event-log.xes -n=10
   ```

   This command injects 10% absence noise into the specified event log.

2. **Injecting Multiple Noise Types:**

   ```
   java -jar NIP-1.0-SNAPSHOT-jar-with-dependencies.jar -a -i -o -s -l=path/to/event-log.xes -n=20
   ```

   This command injects 20% of all specified noise types (absence, insertion, ordering, substitution) into the event log.

   An alternative to injecting all noise types would be not specifying the noise types.
   
  ```
   java -jar NIP-1.0-SNAPSHOT-jar-with-dependencies.jar -l=path/to/event-log.xes -n=20
   ```

4. **Suppressing Output:**

   ```
   java -jar NIP-1.0-SNAPSHOT-jar-with-dependencies.jar -q -l=path/to/event-log.xes -n=15 -a
   ```

   This command injects 15% absence noise and suppresses the output.

## Class Diagram
![output-onlinepngtools](https://github.com/AnandiKarunaratne/NIP/assets/49262441/7ea12fbe-4170-40cd-a4d0-4021a83b753a)

---
