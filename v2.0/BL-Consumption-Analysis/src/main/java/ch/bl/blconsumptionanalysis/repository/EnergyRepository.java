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

package ch.bl.blconsumptionanalysis.repository;

import ch.bl.blconsumptionanalysis.dao.JSONReaderDAO;
import ch.bl.blconsumptionanalysis.model.Entry;
import ch.bl.blconsumptionanalysis.model.Options;
import ch.bl.blconsumptionanalysis.model.Pair;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * This class is used to read the JSON files and to create a list of the corresponding objects.
 *
 * @author Joel Schaltenbrand, Leon Hochwimmer
 * @version 2.0
 */
@Repository
public class EnergyRepository extends AbstractBaseRepository<Entry> {

	public EnergyRepository(JSONReaderDAO<Entry> service) {
		super(service, Entry.class);
	}

	/**
	 * This method is used to return all objects of the corresponding list.
	 *
	 * @param options The options object.
	 * @return The list of objects.
	 */
	public List<Entry> getAverageConsumptionPerMunicipality(Options options) {
		Map<String, List<Entry>> municipalityToEntries = entities.stream()
				.collect(Collectors.groupingBy(Entry::getMunicipality));

		List<Entry> result = municipalityToEntries.entrySet().stream()
				.map(entry -> {
					String commune = entry.getKey();
					List<Entry> entries = entry.getValue();
					double totalConsumption = entries.stream()
							.mapToDouble(Entry::getMwh)
							.sum();
					int count = entries.size();
					double averageConsumption = totalConsumption / count;

					Entry newEntry = new Entry();
					newEntry.setMunicipality(commune);
					newEntry.setMwh(averageConsumption);

					return newEntry;
				})
				.collect(Collectors.toList());
		return applyOptions(options, result, Comparator.comparing(Entry::getMunicipality));
	}

	/**
	 * This method is used to return all objects of the corresponding list.
	 *
	 * @param options The options object.
	 * @return The list of objects.
	 */
	public List<Entry> getAverageConsumptionPerYear(Options options) {
		Map<Integer, List<Entry>> yearToEntries = entities.stream()
				.collect(Collectors.groupingBy(Entry::getYear));

		List<Entry> result = yearToEntries.entrySet().stream()
				.map(entry -> {
					int year = entry.getKey();
					List<Entry> entries = entry.getValue();
					double totalConsumption = entries.stream()
							.mapToDouble(Entry::getMwh)
							.sum();
					int count = entries.size();
					double averageConsumption = totalConsumption / count;

					Entry newEntry = new Entry();
					newEntry.setYear(year);
					newEntry.setMwh(averageConsumption);

					return newEntry;
				})
				.collect(Collectors.toList());

		return applyOptions(options, result, Comparator.comparing(Entry::getYear));
	}

	private List<Entry> applyOptions(Options options, List<Entry> result, Comparator<Entry> comparing) {
		if (options.getSort() == 1) {
			result.sort(comparing);
		} else {
			result.sort(Comparator.comparingDouble(Entry::getMwh).reversed());
		}
		if (options.getOrder() == 2) {
			Collections.reverse(result);
		}
		return result;
	}

	/**
	 * This method is used to return all objects of the corresponding list.
	 *
	 * @return The list of objects.
	 */
	public List<Entry> getHighestConsumers() {
		Map<String, Double> totalConsumptionMap = entities.stream()
				.collect(Collectors.groupingBy(Entry::getMunicipality, Collectors.summingDouble(Entry::getMwh)));

		return totalConsumptionMap.entrySet().stream()
				.map(entry -> {
					String municipality = entry.getKey();
					double totalConsumption = entry.getValue();
					Entry newEntry = new Entry();
					newEntry.setMunicipality(municipality);
					newEntry.setMwh(totalConsumption);
					return newEntry;
				})
				.sorted(Comparator.comparingDouble(Entry::getMwh).reversed())
				.limit(10)
				.collect(Collectors.toList());
	}

	/**
	 * This method is used to return all objects of the corresponding list.
	 *
	 * @param municipality1 The first municipality.
	 * @param municipality2 The second municipality.
	 * @return The list of objects.
	 */
	public Map<Integer, Pair> getComparisonOfTwoMunicipalities(String municipality1, String municipality2) {

		return entities.stream()
				.filter(entry -> entry.getMunicipality().equalsIgnoreCase(municipality1)
						|| entry.getMunicipality().equalsIgnoreCase(municipality2))
				.collect(
						Collectors.groupingBy(
								Entry::getYear,
								TreeMap::new,
								Collectors.collectingAndThen(
										Collectors.toMap(
												Entry::getMunicipality,
												Entry::getMwh,
												Double::sum
										),
										map -> {
											double consumption1 = map.getOrDefault(municipality1, 0.0);
											double consumption2 = map.getOrDefault(municipality2, 0.0);
											return new Pair(consumption1, consumption2);
										}
								)
						)
				);
	}

}
