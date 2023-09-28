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
import ch.bl.blconsumptionanalysis.repository.EnergyRepository;
import ch.bl.blconsumptionanalysis.service.IInputService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

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
		function = Functions.values()[inputService.readInt("Your choice: ") - 1];
		switch (function) {
			case AVERAGE_CONSUMPTION_PER_YEAR:
				list = energyRepository.getAverageConsumptionPerYear(getOptions("Year"));
				printListAverage();
				break;
			case AVERAGE_CONSUMPTION_PER_MUNICIPALITY:
				list = energyRepository.getAverageConsumptionPerCommune(getOptions("Municipality"));
				printListAverage();
				break;
			case HIGHEST_CONSUMERS:
				list = energyRepository.getHighestConsumers();
				printListHighestConsumers();
				break;
			case COMPARISON_OF_TWO_MUNICIPALITIES:
				list = energyRepository.getComparisonOfTwoMunicipalities(inputService.readString("First municipality: "), inputService.readString("Second municipality: "));
				printListComparisonOfTwoMunicipalities();
				break;
			default:
				System.out.println("Invalid input!");
				break;
		}
	}

	private Options getOptions(String sortBy) {
		int sort = inputService.readInt(String.format("Sort by (1 = %s, 2 = average consumption): ", sortBy));
		int order = inputService.readInt("Order (1 = ↓, 2 = ↑): ");
		return new Options(sort, order);
	}

	private void printListAverage() {
		cleanUp();
		String mode;
		if (function == Functions.AVERAGE_CONSUMPTION_PER_YEAR) {
			mode = "Year";
		} else {
			mode = "Municipality";
		}
		System.out.printf("%-20s %12s%n", mode, "Average consumption");
		System.out.println("-----------------------------------------------------------");
		for (Entry entry : list) {
			String label = ("Municipality".equals(mode)) ? entry.getCommune() : Integer.toString(entry.getYear());
			Double averageConsumption = entry.getMwh();
			System.out.printf("%-20s %12.2f MWh%n", label, averageConsumption);
		}
	}

	private void printListHighestConsumers() {
		cleanUp();
		System.out.printf("%-20s %12s %12s%n", "Nr", "Municipality", "Total consumption");
		System.out.println("-----------------------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			String label = list.get(i).getCommune();
			Double totalConsumption = list.get(i).getMwh();
			System.out.printf("%-20d %-20s %12.2f MWh%n", i + 1, label, totalConsumption);
		}

	}

	private void printListComparisonOfTwoMunicipalities() {
		cleanUp();
		System.out.printf("%-20s %12s %12s%n", "Year", "Municipality 1", "Municipality 2");
		System.out.println("-----------------------------------------------------------");
	}

	private void cleanUp() {
		for (int i = 0; i < 200; ++i) {
			System.out.println();
		}
	}
}
