import java.util.ArrayList;

public class ReservationList extends ArrayList<ArrayList<Reservation>> {
	private static File file;

	public ReservationList() {
		int numberOfTimes = Room.getNumberOfTimes();
		ArrayList<String> days = file.getDays();
		ArrayList<String> times = Room.getTimes();

		for (int i = 0; i < numberOfTimes; i++) {
			ArrayList<Reservation> reservationRow = new ArrayList<Reservation>();
			for (int j = 0; j < days.size(); j++) {
				Reservation reservation = new Reservation(days.get(j), times.get(i));
				reservationRow.add(reservation);
			}
			add(reservationRow);
		}
	}

	public static void update(Scheduler scheduler) {
		file = scheduler.getFile();
	}
}
