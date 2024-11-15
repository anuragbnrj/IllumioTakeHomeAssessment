# VPC Flow Log Analyzer

## Project Overview
This project implements a VPC (Virtual Private Cloud) flow log analyzer that processes AWS VPC flow logs, maps them to tags based on destination port and protocol combinations, and generates statistical analysis of the traffic patterns.

### Key Features
- Parses AWS VPC flow logs (Version 2 format)
- Maps traffic to tags based on port/protocol combinations
- Generates statistical analysis including:
    - Tag occurrence counts
    - Port/protocol combination counts
- Supports large log files (up to 10MB)
- Handles extensive lookup tables (up to 10,000 mappings)
- Interactive command-line interface
- Flexible file path configuration

## Technical Design

### Architecture
The application follows clean object-oriented design principles and implements several design patterns:
- Strategy Pattern for flexible log parsing, allowing easy addition of new log formats
- Factory Pattern for creating appropriate parsing strategies
- Builder Pattern for log entry construction
- Mapper Pattern for tag associations

The use of the Strategy pattern makes the application highly extensible - to add support for a new log format:
1. Create a new class implementing the `LogParsingStrategy` interface
2. Add the new strategy to `LogParsingStrategyFactory`
3. No other code changes required

### Core Components

#### 1. Main Application (Application.java)
- Interactive command-line interface
- Flexible file path configuration
- Comprehensive error handling
- Default and custom path support

#### 2. Flow Log Analyzer (FlowLogAnalyzer.java)
- Main orchestrator class coordinating the analysis workflow
- Manages parsing, mapping, and output generation
- Supports runtime log format switching
- Robust error handling and reporting

#### 3. Log Entry Model (LogEntry.java)
- Builder pattern implementation for flexible construction
- Comprehensive protocol mapping (40+ protocols)
- Input validation and default value handling
- Memory-efficient design

#### 4. Parsing Framework
- **LogParser**: Implements Strategy pattern for flexible parsing
- **LogParsingStrategy**: Interface defining parsing contract
- **DefaultVPCFlowLogStrategy**: Implementation for AWS VPC Flow Logs V2
- **LogParsingStrategyFactory**: Factory for creating parsing strategies

This modular parsing framework makes it easy to extend the application to support new log formats:
- Each log format gets its own strategy implementation
- New strategies can be added without modifying existing code
- The factory pattern provides a clean way to instantiate appropriate strategies
- Runtime switching between different parsing strategies is supported

#### 5. Tag Mapping (TagMapper.java)
- Case-insensitive tag mapping
- Multiple tags per port/protocol support
- Efficient lookup using composite key
- Robust error handling

#### 6. Output Generation (OutputGenerator.java)
- CSV format output
- Statistical analysis generation
- Clean separation of formatting logic
- Proper resource management

## Setup Instructions

### Prerequisites
- Java 17 JDK (I have used: java 17.0.5 2022-10-18 LTS)
- Apache Maven 3.8 or higher (I have used: Apache Maven 3.8.6)
- IntelliJ IDEA

## Running the Application

There are two ways to run the application:

### 1. Using IntelliJ IDEA (Preferred)
1. Open the project in IntelliJ IDEA
2. Navigate to `src/main/java/in/anuragbanerjee/Application.java`
3. Click the green "Run" button next to the main class
4. The application will start and present the interactive menu in the IDE's console

### 2. Using Maven from Terminal
```bash
# From project root directory
mvn clean compile exec:java
```

After starting the application using either method, you will be presented with an interactive menu:
1. Enter custom file paths
2. Use default file paths

### Input Files

#### Flow Log File Format
The program expects AWS VPC Flow Logs in Version 2 format with space-separated fields (format specification from [AWS VPC Flow Logs documentation](https://docs.aws.amazon.com/vpc/latest/userguide/flow-log-records.html)):
```
version account-id interface-id srcaddr dstaddr srcport dstport protocol packets bytes start end action log-status
```

The program supports the following field handling:
- Required fields (must have valid values):
    - version (must be 2)
    - dstport (valid port number)
    - protocol (valid protocol number)
- Optional fields (can contain "-" as placeholder):
    - account-id
    - interface-id
    - srcaddr
    - dstaddr
    - srcport
    - packets
    - bytes
    - start
    - end
    - action
    - log-status

#### Lookup Table Format
Text file with the following columns in a comma-separated manner:
```
dstport,protocol,tag
```

Example:
```
25,tcp,sv_P1
68,udp,sv_P2
443,tcp,sv_P2
```

### Output Format
The program generates a CSV file containing:

1. Tag Counts:
```
Tag,Count
sv_P2,1
sv_P1,2
...
Untagged,9
```

2. Port/Protocol Combination Counts:
```
Port,Protocol,Count
22,tcp,1
23,tcp,1
...
```

## Implementation Details

### Protocol Handling
The program implements comprehensive protocol mapping based on [IANA protocol numbers](https://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml). This ensures accurate identification and mapping of network protocols in the flow logs.

### Data Validation & Processing
- Port number validation (0-65535)
- Protocol number validation and mapping based on IANA protocol numbers
- IP address format validation
- Version validation (must be 2)
- CSV format validation for lookup table
- Case-insensitive tag matching (e.g., "sv_p1" and "sv_P1" are treated as identical)
- Flexible handling of missing values (accepts "-" for most fields except version, dstport, and protocol)

### Error Handling
- Invalid log entries are skipped with detailed error logging
- File I/O errors include meaningful messages
- Malformed lookup table entries are logged
- Resource cleanup in error scenarios
- Detailed error reporting for debugging
- Graceful handling of missing values (marked with "-")
- Continues processing after encountering invalid entries
- Output is generated for all valid entries, even if some entries are skipped

## Assumptions
1. Log format is space-separated Version 2 AWS VPC flow logs
2. Protocol names are case-insensitive
3. Tags are case-insensitive
4. Input files are ASCII encoded
5. Flow log file contains valid IPv4 addresses
6. Each line in the lookup table represents a unique port/protocol combination

## Future Improvements
1. Support for additional log formats (easily achievable through the Strategy pattern):
    - Custom VPC flow log formats
2. Support for IPv6 addresses (Just need to change the parseIPAddressMethod in the FlowParsingStrategy implementation)


# Testing

## Test Files Organization
The project includes several test files located in `src/test/resources/` to verify different scenarios and edge cases. Each test file focuses on specific test scenarios:

### 1. Basic Test Cases (`test_flowlogs_basic.txt`)
```
2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761 1620140821 ACCEPT OK
2 123456789012 eni-4d3c2b1a 192.168.1.100 203.0.113.101 23 49154 17 15 12000 1620140761 1620140821 REJECT OK
2 123456789012 eni-5e6f7g8h 192.168.1.101 198.51.100.3 25 49155 1 10 8000 1620140761 1620140821 ACCEPT OK
```
Tests:
- Standard valid log entries
- Different protocols (TCP=6, UDP=17, ICMP=1)
- Various port combinations

### 2. Edge Cases (`test_flowlogs_edge_cases.txt`)
```
3 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761 1620140821 ACCEPT OK
2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761
2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 65536 6 25 20000 1620140761 1620140821 ACCEPT OK
```
Tests:
- Invalid version numbers (only version 2 is supported)
- Incomplete log entries
- Out-of-range port numbers (>65535)
- Invalid protocol numbers
- Malformed lines

### 3. Mixed Valid and Invalid Entries (`test_flowlogs_mixed.txt`)
```
2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761 1620140821 ACCEPT OK
3 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761 1620140821 ACCEPT OK
2 123456789012 invalid_line
2 123456789012 eni-5e6f7g8h 192.168.1.101 198.51.100.3 25 49155 6 10 8000 1620140761 1620140821 ACCEPT OK
```
Tests:
- Error recovery and continuation
- Output generation with partial failures
- Processing valid entries despite surrounding invalid ones

### 4. Optional Fields and Placeholders (`test_flowlogs_optional_fields.txt`)
```
2 - eni-0a1b2c3d - 198.51.100.2 - 49153 6 - - 1620140761 1620140821 ACCEPT OK
2 123456789012 - 10.0.1.201 - 443 49153 6 25 - - - - OK
2 123456789012 eni-4d3c2b1a - - - 49154 6 15 12000 1620140761 1620140821 REJECT -
```
Tests:
- Optional field handling with "-" placeholders
- Missing timestamps
- Missing IP addresses
- Empty interface IDs

## Test Coverage

### Data Validation
- Port numbers (0-65535)
- Known protocol numbers (1-142)
- IPv4 address format
- Version number validation
- Field count validation
- Data type validation for each field

### Error Handling
- Graceful handling of invalid entries
- Proper error messages for each validation failure
- Continued processing after encountering errors
- Resource cleanup in error scenarios

### Optional Fields
- Handling of missing fields marked with "-"
- Required vs optional field validation
- Default value handling