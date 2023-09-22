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

package ch.bl.blconsumptionanalysis.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public abstract class AbstractJSONReaderDAO<T> implements JSONReaderDAO<T> {
	private final File fileName;
	private final Logger logger = Logger.getLogger(AbstractJSONReaderDAO.class.getName());

	protected AbstractJSONReaderDAO(String filePath) {
		this.fileName = new File(filePath);
	}

	@Override
	public List<T> getList(Class<T> clazz) {
		Optional<String> json = loadJSON();
		logger.info("Parsing JSON File");
		List<T> list;
		if (json.isEmpty()) {
			list = new ArrayList<>();
		} else {
			list = new Gson().fromJson(json.get(), TypeToken.getParameterized(List.class, clazz).getType());
			if (list == null) {
				list = new ArrayList<>();
			}
		}
		logger.info("Finished parsing JSON File");
		return list;
	}

	public Optional<String> loadJSON() {
		logger.info("Loading JSON File: " + fileName);
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName.getName())) {
			if (is != null) {
				return Optional.of(new String(is.readAllBytes(), StandardCharsets.UTF_8));
			} else {
				logger.warning("Could not find JSON File: " + fileName);
				return Optional.empty();
			}
		} catch (IOException e) {
			logger.warning("Could not load JSON File: " + fileName);
			return Optional.empty();
		}
	}
}
