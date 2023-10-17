/*
 * MIT License
 *
 * Copyright (c) 2023 Joel Schaltenbrand & Leon Hochwimmer for BBZBL-IT.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ch.bl.blconsumptionanalysis.view;

import ch.bl.blconsumptionanalysis.model.Entry;
import ch.bl.blconsumptionanalysis.model.Functions;
import ch.bl.blconsumptionanalysis.model.Options;
import ch.bl.blconsumptionanalysis.model.Pair;
import ch.bl.blconsumptionanalysis.repository.EnergyRepository;
import ch.bl.blconsumptionanalysis.service.IInputService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * This class is used to display the main menu and to call the appropriate methods based on the user input.
 *
 * @author Joel Schaltenbrand, Leon Hochwimmer
 * @version 1.0
 */
@Component
public class Main {
	private final EnergyRepository energyRepository;
	private final IInputService inputService;
	private Functions function;
	private List<Entry> list;

	/**
	 * This constructor is used to inject the EnergyRepository and the InputService.
	 *
	 * @param energyRepository The EnergyRepository.
	 * @param inputService     The InputService.
	 */
	public Main(EnergyRepository energyRepository, IInputService inputService) {
		this.energyRepository = energyRepository;
		this.inputService = inputService;
	}

	/**
	 * This method is used to display the main menu and to call the appropriate methods based on the user input.
	 */
	@PostConstruct
	public void run() {
		cleanUp();
		startScreen();
	}

	private void startScreen() {
		System.out.println("Welcome to the BL Consumption Analysis!\n\n");
		System.out.println("Please choose one of the following options:");
		for (int i = 0; i < Functions.values().length; ++i) {
			System.out.printf("%d. %s%n", i + 1, Functions.values()[i].getDescription());
		}
		int choice = inputService.readInt("Your choice: ");
		cleanUp();
		if (choice == 0) {
			System.exit(0);
		} else if (choice < 0 || choice > Functions.values().length) {
			System.out.println("Invalid input!");
			pause();
		}
		function = Functions.values()[choice - 1];
		switch (function) {
			case AVERAGE_CONSUMPTION_PER_YEAR:
				list = energyRepository.getAverageConsumptionPerYear(getOptions("Year"));
				printListAverage();
				break;
			case AVERAGE_CONSUMPTION_PER_COMMUNE:
				list = energyRepository.getAverageConsumptionPerCommune(getOptions("Commune"));
				printListAverage();
				break;
			case HIGHEST_CONSUMERS:
				list = energyRepository.getHighestConsumers();
				printListHighestConsumers();
				break;
			case COMPARISON_OF_TWO_COMMUNES:
				String commune1 = inputService.readString("First commune: ");
				String commune2 = inputService.readString("Second commune: ");
				cleanUp();
				Map<Integer, Pair> list2 = energyRepository.getComparisonOfTwoCommunes(commune1, commune2);
				printListComparisonOfTwoCommunes(list2, commune1, commune2);
				break;
			default:
				System.out.println("Invalid input!");
				break;
		}
	}

	private Options getOptions(String sortBy) {
		int sort = inputService.readInt(String.format("Sort by (1 = %s, 2 = average consumption): ", sortBy));
		int order = inputService.readInt("Order (1 = ↓, 2 = ↑): ");
		cleanUp();
		return new Options(sort, order);
	}

	private void printListAverage() {
		String mode = (function == Functions.AVERAGE_CONSUMPTION_PER_YEAR) ? "Year" : "Commune";
		System.out.printf("%-30s %12s%n", mode, "Average consumption");
		System.out.println("-----------------------------------------------------------");
		for (Entry entry : list) {
			String label;
			if ("Commune".equals(mode)) {
				label = entry.getCommune();
			} else {
				label = Integer.toString(entry.getYear());
			}
			Double averageConsumption = entry.getMwh();
			System.out.printf("%-30s %12.2f MWh%n", label, averageConsumption);
		}
		pause();
	}

	private void printListComparisonOfTwoCommunes(Map<Integer, Pair> list, String commune1, String commune2) {
		System.out.printf("%-10s %-30s %-30s%n", "Year", String.format("%s (total MWh)", commune1), String.format("%s (total MWh)", commune2));
		System.out.println("--------------------------------------------------------------");
		for (Map.Entry<Integer, Pair> entry : list.entrySet()) {
			int year = entry.getKey();
			Pair consumptionPair = entry.getValue();
			System.out.printf("%-10d %-30.2f %-30.2f%n", year, consumptionPair.getFirst(), consumptionPair.getSecond());
		}
		pause();
	}

	private void printListHighestConsumers() {
		System.out.printf("%-5s %-20s %-12s%n", "Nr", "Commune", "Total consumption");
		System.out.println("--------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			String label = list.get(i).getCommune();
			Double totalConsumption = list.get(i).getMwh();
			System.out.printf("%-5d %-20s %12.2f MWh%n", i + 1, label, totalConsumption);
		}
		pause();
	}

	private void cleanUp() {
		for (int i = 0; i < 200; ++i) {
			System.out.println();
		}
	}

	private void pause() {
		try {
			Thread.sleep(10000);
			run();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
