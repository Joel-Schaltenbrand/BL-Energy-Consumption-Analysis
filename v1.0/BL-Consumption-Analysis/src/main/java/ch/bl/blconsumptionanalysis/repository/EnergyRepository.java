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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class is used to read the JSON files and to create a list of the corresponding objects.
 *
 * @author Joel Schaltenbrand, Leon Hochwimmer
 * @version 1.0
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
	public List<Entry> getAverageConsumptionPerCommune(Options options) {
		Map<String, Double> averageConsumptionMap = new HashMap<>();
		Map<String, Integer> communeCountMap = new HashMap<>();
		for (Entry entry : entities) {
			String commune = entry.getCommune();
			double mwh = entry.getMwh();
			averageConsumptionMap.put(commune, averageConsumptionMap.getOrDefault(commune, 0.0) + mwh);
			communeCountMap.put(commune, communeCountMap.getOrDefault(commune, 0) + 1);
		}
		List<Entry> result = new ArrayList<>();
		for (Map.Entry<String, Double> entry : averageConsumptionMap.entrySet()) {
			String commune = entry.getKey();
			double totalConsumption = entry.getValue();
			int count = communeCountMap.get(commune);
			Entry entry1 = new Entry();
			entry1.setCommune(commune);
			double averageConsumption = totalConsumption / count;
			entry1.setMwh(averageConsumption);
			result.add(entry1);
		}
		return applyOptions(options, result, Comparator.comparing(Entry::getCommune));
	}

	/**
	 * This method is used to return all objects of the corresponding list.
	 *
	 * @param options The options object.
	 * @return The list of objects.
	 */
	public List<Entry> getAverageConsumptionPerYear(Options options) {
		Map<Integer, Double> averageConsumptionMap = new HashMap<>();
		Map<Integer, Integer> yearCountMap = new HashMap<>();
		for (Entry entry : entities) {
			int year = entry.getYear();
			double mwh = entry.getMwh();
			averageConsumptionMap.put(year, averageConsumptionMap.getOrDefault(year, 0.0) + mwh);
			yearCountMap.put(year, yearCountMap.getOrDefault(year, 0) + 1);
		}
		List<Entry> result = new ArrayList<>();
		for (Map.Entry<Integer, Double> entry : averageConsumptionMap.entrySet()) {
			int year = entry.getKey();
			double totalConsumption = entry.getValue();
			int count = yearCountMap.get(year);
			Entry entry1 = new Entry();
			entry1.setYear(year);
			double averageConsumption = totalConsumption / count;
			entry1.setMwh(averageConsumption);
			result.add(entry1);
		}
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
		Map<String, Double> totalConsumptionMap = new HashMap<>();
		for (Entry entry : entities) {
			String commune = entry.getCommune();
			double consumption = entry.getMwh();
			totalConsumptionMap.put(commune, totalConsumptionMap.getOrDefault(commune, 0.0) + consumption);
		}
		List<Entry> totalConsumptionList = new ArrayList<>();
		for (Map.Entry<String, Double> entry : totalConsumptionMap.entrySet()) {
			String commune = entry.getKey();
			double totalConsumption = entry.getValue();
			Entry entry1 = new Entry();
			entry1.setCommune(commune);
			entry1.setMwh(totalConsumption);
			totalConsumptionList.add(entry1);
		}
		totalConsumptionList.sort(Comparator.comparingDouble(Entry::getMwh).reversed());
		return totalConsumptionList.subList(0, 10);
	}

	/**
	 * This method is used to return all objects of the corresponding list.
	 *
	 * @param commune1 The first commune.
	 * @param commune2 The second commune.
	 * @return The list of objects.
	 */
	public Map<Integer, Pair> getComparisonOfTwoCommunes(String commune1, String commune2) {
		TreeMap<Integer, Pair> consumptionMap = new TreeMap<>();
		for (Entry entry : entities) {
			int year = entry.getYear();
			String commune = entry.getCommune();
			double consumption = entry.getMwh();
			if (commune.equalsIgnoreCase(commune1)) {
				if (consumptionMap.containsKey(year)) {
					Pair pair = consumptionMap.get(year);
					consumptionMap.put(year, new Pair(consumption, pair.getSecond()));
				} else {
					consumptionMap.put(year, new Pair(consumption, 0.0));
				}
			}
			if (commune.equalsIgnoreCase(commune2)) {
				if (consumptionMap.containsKey(year)) {
					Pair pair = consumptionMap.get(year);
					consumptionMap.put(year, new Pair(pair.getFirst(), consumption));
				} else {
					consumptionMap.put(year, new Pair(0.0, consumption));
				}
			}
		}
		return consumptionMap;
	}
}
