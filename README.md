# Energy Consumption BL Analysis

## Project Title

Energy Consumption BL

## Project Details
This project is a collaborative effort between [@Joel-Schaltenbrand](https://github.com/Joel-Schaltenbrand) and [@Leonden](https://github.com/Leonden), who are currently enrolled in the M323 Functional Programming module at BBZBL (Berufsschule Baselland). The module focuses on applying functional programming concepts to address real-world data analysis challenges.

## Subprojects

This project contains two subprojects:

1. [Normal Project](/v1.0/README.md): This is the normal version of the Energy Consumption Analysis project.

2. [Refactored Project](/v2.0/README.md): This is the refactored version of the Energy Consumption Analysis project using functional programming in Java.

## Overview / Problem Statement

The Canton of Basellandschaft currently consumes a significant amount of energy. The goal of this analysis program, built with the Spring framework, is to correctly and effectively evaluate the data to make informed energy-related decisions.

## Data Source

The data has been sourced from the website [www.opendata.swiss](https://www.opendata.swiss) and comprises accurate data from 1990 to 2020. This data is retrieved from a JSON file that can be downloaded from the website and integrated into the program.

Sample data record:

```json
{
  "year": 1990,
  "bfs_number": 2761,
  "municipality": "Aesch",
  "indicator": "Electricity_Consumption_MWh",
  "value": 53693.0
}
```

## Features

1. Display the average consumption for all municipalities per year.
2. Display the average consumption for all years per municipality.
3. Identify the highest consumption for a municipality in a chosen year.
4. Identify the lowest consumption for a municipality in a chosen year.
5. List the top 10 highest consumers overall.
6. List the top 10 lowest consumers overall.
7. Compare two municipalities.

## Technologies
- Programming Language: Java
- Framework: Spring
- UI: Console output using built-in tools
- Type of Application: Console application
- Functional Elements: Streams API, Lambda Expressions, GSON

## Output

Formatted output on the console.

### Examples

- Average consumption for all municipalities per year:
  ```
  - 1990 – Aesch: .... MWh
  - 1990 – Laufen: .... MWh
  ```
- Top 10 highest consumptions from 1990-2020:
  ```
  1. Laufen - ...MWh
  2. Lampenberg - ...MWh
  ```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
