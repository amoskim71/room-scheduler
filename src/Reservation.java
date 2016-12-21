import java.util.ArrayList;

public class Reservation extends ArrayList<String> {
	private boolean reserved;

	/**
	 * Initializes a reservation.
	 * 
	 * @param time
	 *            the time corresponding to the reservation
	 */
	public Reservation(String day, String time) {
		reserved = false;
		add(day);
		add(time);
	}

	/**
	 * Reserves reservation. Adds information to it.
	 * 
	 * @param info
	 *            information of reservation; { course, section, type,
	 *            professor, color }
	 */
	public void reserve(String[] info) {
		if (isReserved()) {
			unreserve();
		}
		reserved = true;
		for (int k = 0; k < info.length; k++) {
			add(info[k]);
		}
	}

	/**
	 * Unreserves reservation. Removes all but the time from it.
	 */
	public void unreserve() {
		reserved = false;
		for (int k = size() - 2; k > 0; k--) {
			remove(k);
		}
	}

	/**
	 * Returns reserved.
	 * 
	 * @return reserved, true if reservation is reserved; false otherwise
	 */
	public boolean isReserved() {
		return reserved;
	}
}
