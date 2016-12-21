import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Room extends JPanel implements MouseListener {
	private static Options options;
	private static RoomGrid roomGrid;
	private static File file;
	private static String mode;
	private static int numberOfTimes;
	private static int[] currentTime;
	private static String currentPhase;
	private static ArrayList<String> days;
	private static ArrayList<String> times;
	private static String color;
	private ArrayList<ArrayList<Timeslot>> timeslots;
	private JLabel roomName;
	private ReservationList reservationList;

	/**
	 * Create the panel.
	 */
	public Room() {
		setLayout(new GridLayout(numberOfTimes + 2, 0, 0, 0));

		add(new JLabel());

		// Add all day labels on top row
		for (String day : days) {
			add(new JLabel(day, SwingConstants.CENTER));
		}

		// Populate timeslots and add time labels on first column
		timeslots = new ArrayList<ArrayList<Timeslot>>();
		for (int i = 0; i < numberOfTimes; i++) {
			ArrayList<Timeslot> timeslotRow = new ArrayList<Timeslot>();
			for (int j = -1; j < days.size(); j++) {
				if (j == -1) {
					add(new JLabel(times.get(i), SwingConstants.CENTER));
				} else {
					Timeslot timeslot = new Timeslot(i, j);
					timeslot.addMouseListener(this);
					timeslotRow.add(timeslot);
					add(timeslot);
				}
			}
			timeslots.add(timeslotRow);
		}

		// Add label with room name on bottom
		roomName = new JLabel();
		add(roomName);
	}

	/**
	 * Sets references to other JPanels and calculates times.
	 *
	 * @param scheduler
	 */
	public static void update(Scheduler scheduler) {
		options = scheduler.getOptions();
		roomGrid = scheduler.getRoomGrid();
		file = scheduler.getFile();
		days = file.getDays();
		changeMode("Reserve");
		setNumberOfTimes();
		setTimes();
	}

	/**
	 * Changes reservation mode to allow user to reserve or unreserve.
	 * 
	 * @param newMode
	 */
	public static void changeMode(String newMode) {
		if (newMode == "Reserve" || newMode == "Unreserve") {
			mode = newMode;
		}
	}

	/**
	 * Returns the number of rows in the room table.
	 * 
	 * @return number of rows in the room table based on the times read in from
	 *         file.
	 */
	public static int getNumberOfTimes() {
		return numberOfTimes;
	}

	/**
	 * Returns the list of time Strings.
	 * 
	 * @return list of time Strings to be put in as labels in the first column
	 *         of the room table
	 */
	public static ArrayList<String> getTimes() {
		return times;
	}

	/**
	 * Reads times from file and sets number of rows in the room table
	 * accordingly. Converts times to 24 hour format and calculates the number
	 * of 30 minute intervals there are between the start and end times.
	 */
	public static void setNumberOfTimes() {
		ArrayList<String> sTimes = file.getTimes();

		int startHour = Integer.parseInt(sTimes.get(0));
		int startMinute = Integer.parseInt(sTimes.get(1));
		String startPhase = sTimes.get(2);
		int endHour = Integer.parseInt(sTimes.get(3));
		int endMinute = Integer.parseInt(sTimes.get(4));
		String endPhase = sTimes.get(5);
		int[] startTime = convertTo24Hour(startHour, startMinute, startPhase);
		int[] endTime = convertTo24Hour(endHour, endMinute, endPhase);

		int hour = startTime[0];
		int minute = startTime[1];
		int count = 0;
		while (hour < endTime[0]) {
			hour++;
			count += 2;
		}
		while (minute != endTime[1]) {
			if (minute < endTime[1]) {
				minute += 30;
				count++;
			} else {
				minute -= 30;
				count--;
			}
		}

		numberOfTimes = count;
		currentTime = new int[2];
		System.arraycopy(startTime, 0, currentTime, 0, startTime.length);

		currentPhase = startPhase;
	}

	/**
	 * Calculates the 24 hour format of a time in 12 hour format.
	 * 
	 * @param hour
	 *            the hour in 12 hour format
	 * @param minute
	 *            the minute in 12 hour format
	 * @param phase
	 *            "a" or "p" based on if it's AM or PM respectively
	 * @return the time in 24 hour format as an array containing the hour and
	 *         minute
	 */
	public static int[] convertTo24Hour(int hour, int minute, String phase) {
		if (phase.equalsIgnoreCase("a") && hour == 12) {
			hour -= 12;
		} else if (phase.equalsIgnoreCase("p") && hour >= 1 && hour <= 11) {
			hour += 12;
		}
		return new int[] { hour, minute };
	}

	/**
	 * Populates the times list with time Strings.
	 */
	public static void setTimes() {
		String sTime = "" + currentTime[0] + ":" + (currentTime[1] == 0 ? "00" : currentTime[1]) + currentPhase;
		times = new ArrayList<String>();
		times.add(sTime);
		for (int i = 0; i < numberOfTimes; i++) {
			sTime = getNextTime();
			times.add(sTime);
		}
	}

	/**
	 * Sets and returns the other phase, which correspond to AM or PM. Phase is
	 * "a" if it's AM or "p" if it's PM.
	 * 
	 * @return the other phase
	 */
	private static String setOtherPhase() {
		if (currentPhase.equals("a")) {
			currentPhase = "p";
		} else {
			currentPhase = "a";
		}
		return currentPhase;
	}

	/**
	 * Calculates the next time in the sequence. 30 minutes is added to the
	 * current time to get next time.
	 * 
	 * @return the next time in the sequence in String format.
	 */
	public static String getNextTime() {
		int hour = currentTime[0];
		int minute = currentTime[1];
		String phase = currentPhase;
		minute += 30;
		minute %= 60;
		if (minute == 0) {
			hour++;
		}
		hour %= 12;
		String sHour;
		if (hour == 0) {
			sHour = "12";
			if (minute == 0) {
				phase = setOtherPhase();
			}
		} else {
			sHour = "" + hour;
		}
		String sMinute;
		if (minute == 0) {
			sMinute = "00";
		} else {
			sMinute = "" + minute;
		}
		currentTime[0] = hour;
		currentTime[1] = minute;
		currentPhase = phase;
		return "" + sHour + ":" + sMinute + phase;
	}

	/**
	 * Returns the name of the room.
	 * 
	 * @return name of room
	 */
	public String getRoomName() {
		return roomName.getText();
	}

	/**
	 * Sets the name of the room.
	 * 
	 * @param name
	 *            of room
	 */
	public void setRoomName(String roomName) {
		this.roomName.setText(roomName);
	}

	/**
	 * Sets reservation list and updates timeslots based off that.
	 * 
	 * @param reservationList
	 *            configuration of room
	 */
	public void setReservationList(ReservationList reservationList) {
		this.reservationList = reservationList;
		updateTimeslots();
	}

	/**
	 * Goes through each timeslot and reserves or unreserves it based off
	 * reservationList.
	 */
	public void updateTimeslots() {
		for (int i = 0; i < timeslots.size(); i++) {
			for (int j = 0; j < timeslots.get(i).size(); j++) {
				Timeslot timeslot = timeslots.get(i).get(j);
				// reservation = { day, time, course, section, type,
				// professor, color }
				Reservation reservation = reservationList.get(i).get(j);
				if (reservation.isReserved()) {
					// info = { course, section, type, professor, color }
					String[] info = new String[5];
					for (int k = 2; k < reservation.size(); k++) {
						info[k - 2] = reservation.get(k);
					}
					timeslot.reserve(info);
				} else {
					if (timeslot.isReserved()) {
						timeslot.unreserve();
					}
				}
			}
		}
	}

	/**
	 * Returns timeslots.
	 * 
	 * @return timeslots, an array of timeslots that populate the room table
	 */
	public ArrayList<ArrayList<Timeslot>> getTimeslots() {
		return timeslots;
	}

	/**
	 * Sets color that timeslots display when the user mouses over or reserves
	 * it.
	 * 
	 * @param reservation
	 *            color of timeslots
	 */
	public static void setColor(String color) {
		Room.color = color;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * Reserves or unreserves timeslot clicked. Notifies user if there is any
	 * problem or conflict.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		Timeslot timeslot = (Timeslot) e.getSource();
		int i = timeslot.getI();
		int j = timeslot.getJ();
		boolean reserved = timeslot.isReserved();
		// reservation = { day, time, course, section, type,
		// professor, color }
		Reservation reservation = reservationList.get(i).get(j);

		if (options.isDisplayMatches()) {
			if (!reserved) {
				options.updateStatus("Not reserved");
				return;
			}

			String course = reservation.get(2);
			String section = reservation.get(3);
			String[] info = { course, section };

			String[] timeslotLabels = { "Room", "Day", "Time"};
			String output = "";
			for (int a = 0; a < timeslotLabels.length; a++) {
				output += String.format("%-15s", timeslotLabels[a]);
			}
			output += "\n";
			
			ArrayList<ArrayList<String>> relatedTimes = roomGrid.getRelatedTimes(info);
			for (int a = 0; a < relatedTimes.size(); a++) {
				for (int b = 0; b < relatedTimes.get(a).size(); b++) {
					output += String.format("%-15s", relatedTimes.get(a).get(b));
				}
				output += "\n";
			}
			
			JOptionPane.showMessageDialog(null, output);

			options.updateStatus("Matches displayed!");
			return;
		} else {
			if (mode == "Reserve") {
				if (!options.fieldsFilled()) {
					options.updateStatus("Field(s) empty");
					return;
				} else if (reserved) {
					options.updateStatus("Already reserved");
					return;
				}

				// info = { course, section, type, professor, color }
				String[] info = options.getInfo();
				timeslot.reserve(info);
				reservation.reserve(info);

				options.updateStatus("Reserved!");
				return;
			} else if (mode == "Unreserve") {
				if (!reserved) {
					options.updateStatus("Already unreserved");
					return;
				}

				timeslot.unreserve();
				reservation.unreserve();

				options.updateStatus("Unreserved!");
				return;
			} else {
				options.updateStatus("Error");
				return;
			}
		}
	}

	/**
	 * Changes background color or border of timeslot in certain cases when
	 * mouse is over it.
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		Timeslot timeslot = (Timeslot) e.getSource();
		if (options.isDisplayMatches()) {
			if (!timeslot.isReserved()) {
				timeslot.setBorderColor("red");
			}
		} else {
			if ((mode.equals("Reserve") && (!options.fieldsFilled() ||  timeslot.isReserved())) || (mode.equals("Unreserve") && !timeslot.isReserved())) {
				timeslot.setBorderColor("red");
			} else if (mode.equals("Reserve") && !timeslot.isReserved()) {
				timeslot.setBackgroundColor(color);
			}
		}
	}

	/**
	 * Resets background color or border of timeslot when mouse is no longer
	 * over it.
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		Timeslot timeslot = (Timeslot) e.getSource();
		if (timeslot.isBorderChanged()) {
			timeslot.setBorderColor(null);
		}

		if (timeslot.isBackgroundChanged()) {
			timeslot.setBackgroundColor(null);
		}
	}
}
