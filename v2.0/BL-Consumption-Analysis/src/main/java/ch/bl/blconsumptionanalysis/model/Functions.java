package ch.bl.blconsumptionanalysis.model;

/**
 * This enum is used to define the functions of the program.
 *
 * @author Joel Schaltenbrand, Leon Hochwimmer
 * @version 2.0
 */
public enum Functions {
	AVERAGE_CONSUMPTION_PER_YEAR("Display average consumption of the whole canton BL for each year individually."),
	AVERAGE_CONSUMPTION_PER_COMMUNE("Display average consumption of all years summed up per commune issued."),
	HIGHEST_CONSUMERS("Display 10 highest consumers (commune) Total (All years summed up)."),
	COMPARISON_OF_TWO_COMMUNES("Display comparison of 2 communes (All years individually).");

	private final String description;

	/**
	 * This constructor is used to set the description of the function.
	 *
	 * @param description The description of the function.
	 */
	Functions(String description) {
		this.description = description;
	}

	/**
	 * This method is used to return the description of the function.
	 *
	 * @return The description of the function.
	 */
	public String getDescription() {
		return description;
	}
}
