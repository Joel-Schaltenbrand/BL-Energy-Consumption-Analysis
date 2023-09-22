# Energy Consumption Analysis - BL

## Project Title
Energy Consumption BL

## Overview / Problem Statement
The Canton of Basellandschaft currently consumes a significant amount of energy. The goal of this analysis program is to correctly and effectively evaluate the data to make informed energy-related decisions.

## Data Source
The data has been sourced from the website [www.opendata.swiss](https://www.opendata.swiss) and comprises accurate data from 1990 to 2020. This data is retrieved from a JSON file that can be downloaded from the website and integrated into the program.

Sample data record:
```
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
