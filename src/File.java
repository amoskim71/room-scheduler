import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class File extends JPanel {
	private Options options;
	private RoomGrid roomGrid;
	private String configFile;
	private String defaultStoreFile;
	private String defaultOutputFile;
	private JTextField textFieldFilename2;
	private JTextField textFieldFilename;
	private JComboBox<String> comboBoxOutput;
	private ArrayList<String> days;
	private ArrayList<String> times;
	private ArrayList<String> colors;
	private ArrayList<String> roomNames;
	private ArrayList<ArrayList<String>> classesOriginal;

	/**
	 * Create the panel.
	 */
	public File() {
		configFile = "config.txt";
		defaultStoreFile = "contents.txt";
		defaultOutputFile = "output.txt";

		try {
			BufferedReader config = new BufferedReader(new FileReader(configFile));
			String line = "";
			while ((line = config.readLine()) != null) {
				if (line.substring(0, 4).equalsIgnoreCase("days")) {
					break;
				}
			}
			String delim = "[|]+";
			days = new ArrayList<String>();
			while (!((line = config.readLine()).equals(""))) {
				String[] tokens = line.split(delim);
				for (String token : tokens) {
					days.add(token.trim());
				}
			}
			while ((line = config.readLine()) != null) {
				if (line.substring(0, 5).equalsIgnoreCase("times")) {
					break;
				}
			}
			delim = "[:|]+";
			times = new ArrayList<String>();
			while (!((line = config.readLine()).equals(""))) {
				String[] tokens = line.split(delim);
				for (String token : tokens) {
					token = token.trim();
					int length = token.length();
					String lastChar = token.substring(length - 1, length);
					if (lastChar.equals("a") || lastChar.equals("p")) {
						times.add(token.substring(0, length - 1));
						times.add(lastChar);
					} else {
						times.add(token);
					}
				}
			}

			while ((line = config.readLine()) != null) {
				if (line.substring(0, 7).equalsIgnoreCase("classes")) {
					break;
				}
			}
			delim = "[|]+";
			classesOriginal = new ArrayList<ArrayList<String>>();
			while (!((line = config.readLine()).equals(""))) {
				String[] tokens = line.split(delim);
				String course = tokens[0];
				String section = tokens[1];
				String type = tokens[2];
				String professor = tokens[3];
				for (int k = 0; k < tokens.length; k++) {
					ArrayList<String> row = new ArrayList<String>(Arrays.asList(course, section, type, professor));
					classesOriginal.add(row);
				}
			}

			colors = new ArrayList<String>();
			while ((line = config.readLine()) != null) {
				if (line.substring(0, 6).equalsIgnoreCase("colors")) {
					break;
				}
			}
			while (!((line = config.readLine()).equals(""))) {
				colors.add(line.trim());
			}

			roomNames = new ArrayList<String>();
			while ((line = config.readLine()) != null) {
				if (line.substring(0, 5).equalsIgnoreCase("rooms")) {
					break;
				}
			}
			while ((line = config.readLine()) != null) {
				roomNames.add(line.trim());
			}

			config.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		setLayout(new GridLayout(1, 0, 0, 0));

		JPanel panelFile1 = new JPanel();
		add(panelFile1);

		JLabel labelFilename = new JLabel("File:");
		panelFile1.add(labelFilename);

		textFieldFilename = new JTextField(defaultStoreFile);
		textFieldFilename.setColumns(10);
		panelFile1.add(textFieldFilename);
		
		JButton buttonStore = new JButton("Store");
		buttonStore.addActionListener(new ActionListener() {
			/**
			 * Outputs configurations of all rooms to file. Loops through each
			 * timeslot in each room to check if it is reserved. If it is, then
			 * the reservation information is written to file.
			 */
			public void actionPerformed(ActionEvent e) {
				ArrayList<ReservationList> reservationLists = roomGrid.getReservationLists();
				String filename = textFieldFilename.getText();

				try {
					BufferedWriter output = new BufferedWriter(new FileWriter(filename));

					for (int room = 0; room < reservationLists.size(); room++) {
						for (int i = 0; i < reservationLists.get(room).size(); i++) {
							for (int j = 0; j < reservationLists.get(room).get(i).size(); j++) {
								// reservation = { day, time, course, section, type,
								// professor, color }
								Reservation reservation = reservationLists.get(room).get(i).get(j);
								if (reservation.isReserved()) {
									output.write("" + room + "|" + i + "|" + j);
									for (int k = 2; k < reservation.size(); k++) {
										output.write("|" + reservation.get(k));
									}
									output.write("\n");
								}
							}
						}
					}

					output.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				options.updateStatus("Store successful!");
			}
		});
		panelFile1.add(buttonStore);

		JButton buttonRestore = new JButton("Restore");
		buttonRestore.addActionListener(new ActionListener() {
			/**
			 * Resets all rooms to configuration specified in file. Unreserves
			 * all timeslots and then reads each line from file and reserves
			 * appropriate timeslots.
			 */
			public void actionPerformed(ActionEvent e) {

				ArrayList<ReservationList> reservationLists = roomGrid.getReservationLists();
				String filename = textFieldFilename.getText();

				// Unreserve all timeslots in all the rooms
				for (int room = 0; room < reservationLists.size(); room++) {
					for (int i = 0; i < reservationLists.get(room).size(); i++) {
						for (int j = 0; j < reservationLists.get(room).get(i).size(); j++) {
							// reservation = { day, time, course, section, type,
							// professor, color }
							Reservation reservation = reservationLists.get(room).get(i).get(j);
							if (reservation.isReserved()) {
								reservation.unreserve();
							}
						}
					}
				}

				try {
					BufferedReader config = new BufferedReader(new FileReader(filename));
					String line = "";
					String delim = "[|]+";
					while ((line = config.readLine()) != null) {
						// tokens = { room, i, j, course, section, type,
						// professor, color }
						String[] tokens = line.split(delim);
						int room = Integer.parseInt(tokens[0]);
						int i = Integer.parseInt(tokens[1]);
						int j = Integer.parseInt(tokens[2]);

						// info = { course, section, type, professor, color }
						String[] info = new String[5];
						System.arraycopy(tokens, 3, info, 0, tokens.length - 3);
						Reservation reservation = reservationLists.get(room).get(i).get(j);

						// Reserve new timeslot based on line information
						reservation.reserve(info);
					}

					config.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				roomGrid.setReservationLists(reservationLists);

				options.updateStatus("Restore successful!");
			}
		});
		panelFile1.add(buttonRestore);

		JPanel panelFile2 = new JPanel();
		add(panelFile2);

		JLabel labelFilename2 = new JLabel("File:");
		panelFile2.add(labelFilename2);

		textFieldFilename2 = new JTextField(defaultOutputFile);
		textFieldFilename2.setColumns(10);
		panelFile2.add(textFieldFilename2);

		JLabel labelComboBoxOutput = new JLabel("Organize by");
		panelFile2.add(labelComboBoxOutput);
		
		comboBoxOutput = new JComboBox<String>();
		String[] comboBoxOutputItems = { "Course", "Section", "Type", "Professor", "Color" };
		for (String item : comboBoxOutputItems) {
			comboBoxOutput.addItem(item);
		}
		panelFile2.add(comboBoxOutput);
		
		JButton buttonOutput = new JButton("Output");
		buttonOutput.addActionListener(new ActionListener() {
			/**
			 * Outputs all configurations to file organized by user choice in
			 * table format.
			 */
			public void actionPerformed(ActionEvent e) {
				ArrayList<Room> rooms = roomGrid.getRooms();
				ArrayList<String> roomNames = roomGrid.getRoomNames();
				ArrayList<ReservationList> reservationLists = roomGrid.getReservationLists();
				String filename = textFieldFilename2.getText();
				int selectedIndex = comboBoxOutput.getSelectedIndex();
				ArrayList<String> courses = new ArrayList<String>();
				ArrayList<String> sections = new ArrayList<String>();
				ArrayList<String> types = new ArrayList<String>();
				ArrayList<String> professors = new ArrayList<String>();
				for (ArrayList<String> sClass : classesOriginal) {
					addDistinctItemToArrayList(sClass.get(0), courses);
					addDistinctItemToArrayList(sClass.get(1), sections);
					addDistinctItemToArrayList(sClass.get(2), types);
					addDistinctItemToArrayList(sClass.get(3), professors);
				}
				ArrayList<ArrayList<String>> outputs = new ArrayList<ArrayList<String>>();
				outputs.add(courses);
				outputs.add(sections);
				outputs.add(types);
				outputs.add(professors);
				
				ArrayList<String> outputList = outputs.get(selectedIndex);
				ArrayList<ArrayList<ArrayList<String>>> data = new ArrayList<ArrayList<ArrayList<String>>>();
				for (int i = 0; i < outputList.size(); i++) {
					ArrayList<ArrayList<String>> dataRow = new ArrayList<ArrayList<String>>();
					data.add(dataRow);
				}

				for (int room = 0; room < reservationLists.size(); room++) {
					for (int i = 0; i < reservationLists.get(room).size(); i++) {
						for (int j = 0; j < reservationLists.get(room).get(i).size(); j++) {
							// reservation = { day, time, course, section, type,
							// professor, color }
							Reservation reservation = reservationLists.get(room).get(i).get(j);
							if (reservation.isReserved()) {
								String pivot = reservation.get(selectedIndex + 2);
								for (int outputIndex = 0; outputIndex < outputList.size(); outputIndex++) {
									if (pivot.equals(outputList.get(outputIndex))) {
										ArrayList<String> sData = new ArrayList<String>();
										sData.add("" + roomNames.get(room));
										for (int k = 0; k < reservation.size(); k++) {
											sData.add(reservation.get(k));
										}
										data.get(outputIndex).add(sData);
									}
								}
							}
						}
					}
				}
				String[] labels = { "Room", "Day", "Time", "Course", "Section", "Type", "Professor", "Color" };
				try {
					BufferedWriter output = new BufferedWriter(new FileWriter(filename));
					for (int outputIndex = 0; outputIndex < outputList.size(); outputIndex++) {
						output.write(String.format("%-15s", outputList.get(outputIndex)));
						output.write("\n");
						int size = data.get(outputIndex).size();
						for (int i = -1; i < ((size == 0) ? -1 : size); i++) {
							for (int j = 0; j < data.get(outputIndex).get(Math.max(i,  0)).size(); j++) {
								if (j != selectedIndex + 3) {
									if (i == -1) {
										output.write(String.format("%-15s", labels[j]));
									} else {
										String sData = data.get(outputIndex).get(i).get(j);
										output.write(String.format("%-15s", sData));
									}
								}
							}
							output.write("\n");
						}
						output.write("\n");
					}

					output.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				options.updateStatus("Output successful!");
			}
		});
		panelFile2.add(buttonOutput);
	}

	public void update(Scheduler scheduler) {
		options = scheduler.getOptions();
		roomGrid = scheduler.getRoomGrid();
	}

	public ArrayList<String> getDays() {
		return days;
	}

	public ArrayList<String> getTimes() {
		return times;
	}

	public ArrayList<String> getColors() {
		return colors;
	}

	public ArrayList<String> getRoomNames() {
		return roomNames;
	}

	public ArrayList<ArrayList<String>> getClassesOriginal() {
		return classesOriginal;
	}

	public void addDistinctItemToArrayList(String item, ArrayList<String> list) {
		if (!list.contains(item)) {
			list.add(item);
		}
	}
}
