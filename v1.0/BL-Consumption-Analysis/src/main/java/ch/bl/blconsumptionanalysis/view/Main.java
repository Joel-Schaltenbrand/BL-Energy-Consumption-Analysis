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
import ch.bl.blconsumptionanalysis.model.Options;
import ch.bl.blconsumptionanalysis.repository.EnergyRepository;
import ch.bl.blconsumptionanalysis.service.IInputService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Main {

	private final EnergyRepository energyRepository;
	private final IInputService inputService;

	public Main(EnergyRepository energyRepository, IInputService inputService) {
		this.energyRepository = energyRepository;
		this.inputService = inputService;
	}

	@PostConstruct
	public void run() {
		cleanUp();
		startScreen();
	}

	private void startScreen() {
		System.out.println("Welcome to the BL Consumption Analysis!\n\n");
		System.out.println("1. Display the average consumption for all commune per year.");
		System.out.println("2. Display the average consumption for all years per commune.");
		System.out.println("3. Identify the highest consumption for a commune in a chosen year.");
		System.out.println("4. Identify the lowest consumption for a commune in a chosen year.");
		System.out.println("5. List the top 10 highest consumers overall.");
		System.out.println("6. List the top 10 lowest consumers overall.");
		System.out.println("7. Compare two municipalities.\n\n");
		switch (inputService.readInt("Please enter your choice: ")) {
			case 1:
				getAverageConsumptionPerYear();
				break;
			case 2:
				getAverageConsumptionPerCommune();
				break;
			default:
				System.out.println("Not implemented yet.");
				break;
		}
	}

	private void getAverageConsumptionPerYear() {
		String mode = "Year";
		cleanUp();
		printList(energyRepository.getAverageConsumptionPerYear(getOptions(mode)), mode);
	}

	private void getAverageConsumptionPerCommune() {
		String mode = "Commune";
		cleanUp();
		printList(energyRepository.getAverageConsumptionPerCommune(getOptions(mode)), mode);
	}

	private Options getOptions(String sortBy) {
		int sort = inputService.readInt(String.format("Sort by (1 = %s, 2 = average consumption): ", sortBy));
		int order = inputService.readInt("Order (1 = ↓, 2 = ↑): ");
		return new Options(sort, order);
	}

	private void printList(List<Entry> list, String mode) {
		cleanUp();
		System.out.printf("%-20s %12s%n", mode, "Average consumption");
		System.out.println("-----------------------------------------------------------");
		for (Entry entry : list) {
			String label = (mode.equals("Commune")) ? entry.getCommune() : Integer.toString(entry.getYear());
			Double averageConsumption = entry.getMwh();
			System.out.printf("%-20s %12.2f MWh%n", label, averageConsumption);
		}
	}

	private void cleanUp() {
		for (int i = 0; i < 200; ++i) {
			System.out.println();
		}
	}
}
