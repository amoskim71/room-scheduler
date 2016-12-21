import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;

public class RoomGrid extends JPanel {
	private Options options;
	private File file;
	private ArrayList<Room> rooms;
	private ArrayList<String> roomNames;
	private ArrayList<ReservationList> reservationLists;
	private int startRoom;

	/**
	 * Create the panel.
	 */
	public RoomGrid() {
		rooms = new ArrayList<Room>();
		reservationLists = new ArrayList<ReservationList>();

		setLayout(new GridLayout(0, 4, 0, 0));
	}

	/**
	 * Sets references to other JPanels and creates four JPanel rooms as well as
	 * reservationList for each room name in file.
	 */
	public void update(Scheduler scheduler) {
		options = scheduler.getOptions();
		file = scheduler.getFile();
		roomNames = file.getRoomNames();

		Room.update(scheduler);
		for (int i = 0; i < 4; i++) {
			Room room = new Room();
			rooms.add(room);
			add(room);
		}

		ReservationList.update(scheduler);
		for (String roomName : roomNames) {
			ReservationList reservationList = new ReservationList();
			reservationLists.add(reservationList);
		}

		changeRoomView("");
	}

	/**
	 * Returns rooms.
	 * 
	 * @return rooms, a list of JPanel room tables.
	 */
	public ArrayList<Room> getRooms() {
		return rooms;
	}
	
	/**
	 * Returns roomNames.
	 * 
	 * @return roomNames, a list of Strings containing room names.
	 */
	public ArrayList<String> getRoomNames() {
		return roomNames;
	}

	/**
	 * Returns reservationLists.
	 * 
	 * @return reservationLists, a list of configurations for each room
	 */
	public ArrayList<ReservationList> getReservationLists() {
		return reservationLists;
	}

	/**
	 * Sets reservationLists and changes room view based off that.
	 * 
	 * @param reservationLists,
	 *            a list of configurations for each room
	 */
	public void setReservationLists(ArrayList<ReservationList> reservationLists) {
		this.reservationLists = reservationLists;
		changeRoomView("");
	}

	/**
	 * Returns a list of reservations that match a certain course and section.
	 * 
	 * @param info
	 *            an array containing a course and a section
	 * @return a list of related reservations that match the course and the
	 *         section
	 */
	public ArrayList<ArrayList<String>> getRelatedTimes(String[] info) {
		ArrayList<ArrayList<String>> relatedTimes = new ArrayList<ArrayList<String>>();
		for (int room = 0; room < reservationLists.size(); room++) {
			for (int i = 0; i < reservationLists.get(room).size(); i++) {
				for (int j = 0; j < reservationLists.get(room).get(i).size(); j++) {
					// reservation = { day, time, course, section, type,
					// professor, color }
					Reservation reservation = reservationLists.get(room).get(i).get(j);
					if (reservation.isReserved()) {
						String course = reservation.get(2);
						String section = reservation.get(3);
						if (course.equals(info[0]) && section.equals(info[1])) {
							ArrayList<String> relatedTimesRow = new ArrayList<String>();
							String day = reservation.get(0);
							String time = reservation.get(1);
							relatedTimesRow.addAll(Arrays.asList("" + room, day, time));
							relatedTimes.add(relatedTimesRow);
						}
					}
				}
			}
		}

		return relatedTimes;
	}

	/**
	 * Changes contents of the JPanel rooms. Sets the reservationList of each
	 * room to another depending on what the user inputs.
	 * 
	 * @param operation
	 */
	public void changeRoomView(String operation) {
		if (operation.equals("-")) {
			startRoom = Math.max(startRoom - 4, 0);
		} else if (operation.equals("+")) {
			startRoom = Math.min(startRoom + 4, reservationLists.size() - 4);
		}

		if (startRoom == 0) {
			options.setButtonLeft(false);
		} else {
			options.setButtonLeft(true);
		}

		if (startRoom == reservationLists.size() - 4) {
			options.setButtonRight(false);
		} else {
			options.setButtonRight(true);
		}

		for (int i = startRoom; i < startRoom + 4; i++) {
			int roomIndex = i - startRoom;
			rooms.get(roomIndex).setRoomName(roomNames.get(i));
			rooms.get(roomIndex).setReservationList(reservationLists.get(i));
		}
	}
}
