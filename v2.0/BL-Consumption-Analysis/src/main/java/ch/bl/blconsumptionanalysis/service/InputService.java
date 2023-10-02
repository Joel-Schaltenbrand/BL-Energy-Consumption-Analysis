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

package ch.bl.blconsumptionanalysis.service;

import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * This class is used to read the user input.
 *
 * @author Joel Schaltenbrand, Leon Hochwimmer
 * @version 2.0
 */
@Service
public class InputService implements IInputService {
	private final Scanner scanner;

	public InputService() {
		this.scanner = new Scanner(System.in);
	}

	/**
	 * This method is used to read the user input.
	 *
	 * @param message The message to be displayed to the user.
	 * @return The user input as a String.
	 */
	@Override
	public String readString(String message) {
		System.out.print(message);
		return scanner.next();
	}

	/**
	 * This method is used to read the user input.
	 *
	 * @param message The message to be displayed to the user.
	 * @return The user input as an int.
	 */
	@Override
	public int readInt(String message) {
		System.out.print(message);
		return scanner.nextInt();
	}
}
