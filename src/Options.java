import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Options extends JPanel {
	private RoomGrid roomGrid;
	private File file;
	private boolean displayMatches;
	private boolean comboBoxListening;
	private String starterItem;
	private JButton buttonLeft;
	private JButton buttonRight;
	private JComboBox<String> comboBoxCourse;
	private JComboBox<String> comboBoxSection;
	private JComboBox<String> comboBoxType;
	private JComboBox<String> comboBoxProfessor;
	private JComboBox<String> comboBoxColor;
	private JTextField textFieldStatus;
	private ArrayList<ArrayList<String>> classes;

	/**
	 * Create the panel.
	 */
	public Options() {
		displayMatches = false;
		comboBoxListening = true;
		starterItem = "-- All --";

		setLayout(new GridLayout(2, 0, 0, 0));

		JPanel panelNorth0_0 = new JPanel();
		add(panelNorth0_0);

		JCheckBox checkboxDisplayMatches = new JCheckBox("Display Matches");
		checkboxDisplayMatches.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleDisplayMatches();
			}
		});
		panelNorth0_0.add(checkboxDisplayMatches);

		JPanel panelNorth0_1 = new JPanel();
		add(panelNorth0_1);

		buttonLeft = new JButton("< Left");
		buttonLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				roomGrid.changeRoomView("-");
			}
		});
		panelNorth0_1.setLayout(new GridLayout(0, 2, 0, 0));
		panelNorth0_1.add(buttonLeft);

		buttonRight = new JButton("Right >");
		buttonRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				roomGrid.changeRoomView("+");
			}
		});
		panelNorth0_1.add(buttonRight);

		JPanel panelNorth0_2 = new JPanel();
		add(panelNorth0_2);

		JLabel labelCourse = new JLabel("Course");
		panelNorth0_2.add(labelCourse);

		JPanel panelNorth0_3 = new JPanel();
		add(panelNorth0_3);

		JLabel labelSection = new JLabel("Section");
		panelNorth0_3.add(labelSection);

		JPanel panelNorth0_4 = new JPanel();
		add(panelNorth0_4);

		JLabel labelType = new JLabel("Type");
		panelNorth0_4.add(labelType);

		JPanel panelNorth0_5 = new JPanel();
		add(panelNorth0_5);

		JLabel labelProfessor = new JLabel("Professor");
		panelNorth0_5.add(labelProfessor);

		JPanel panelNorth0_6 = new JPanel();
		add(panelNorth0_6);

		JLabel labelColor = new JLabel("Color");
		panelNorth0_6.add(labelColor);

		JPanel panelNorth0_7 = new JPanel();
		add(panelNorth0_7);

		JLabel labelStatus = new JLabel("Status");
		panelNorth0_7.add(labelStatus);

		JPanel panelNorth1_0 = new JPanel();
		add(panelNorth1_0);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		JRadioButton radioButtonReserve = new JRadioButton("Reserve");
		buttonGroup.add(radioButtonReserve);
		radioButtonReserve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room.changeMode("Reserve");
			}
		});
		radioButtonReserve.setSelected(true);
		panelNorth1_0.add(radioButtonReserve);

		JPanel panelNorth1_1 = new JPanel();
		add(panelNorth1_1);

		JRadioButton radioButtonUnreserve = new JRadioButton("Unreserve");
		buttonGroup.add(radioButtonUnreserve);
		radioButtonUnreserve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room.changeMode("Unreserve");
			}
		});
		panelNorth1_1.add(radioButtonUnreserve);

		JPanel panelNorth1_2 = new JPanel();
		add(panelNorth1_2);

		comboBoxCourse = new JComboBox<String>();
		comboBoxCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBoxListening) {
					updateComboBoxes(0, (String) comboBoxCourse.getSelectedItem());
				}
			}
		});
		panelNorth1_2.add(comboBoxCourse);

		JPanel panelNorth1_3 = new JPanel();
		add(panelNorth1_3);

		comboBoxSection = new JComboBox<String>();
		comboBoxSection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBoxListening) {
					updateComboBoxes(1, (String) comboBoxSection.getSelectedItem());
				}
			}
		});
		panelNorth1_3.add(comboBoxSection);

		JPanel panelNorth1_4 = new JPanel();
		add(panelNorth1_4);

		comboBoxType = new JComboBox<String>();
		comboBoxType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBoxListening) {
					updateComboBoxes(2, (String) comboBoxType.getSelectedItem());
				}
			}
		});
		panelNorth1_4.add(comboBoxType);

		JPanel panelNorth1_5 = new JPanel();
		add(panelNorth1_5);

		comboBoxProfessor = new JComboBox<String>();
		comboBoxProfessor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBoxListening) {
					updateComboBoxes(3, (String) comboBoxProfessor.getSelectedItem());
				}
			}
		});
		panelNorth1_5.add(comboBoxProfessor);

		JPanel panelNorth1_6 = new JPanel();
		add(panelNorth1_6);

		comboBoxColor = new JComboBox<String>();
		comboBoxColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room.setColor((String) comboBoxColor.getSelectedItem());
			}
		});
		panelNorth1_6.add(comboBoxColor);

		JPanel panelNorth1_7 = new JPanel();
		add(panelNorth1_7);

		textFieldStatus = new JTextField();
		textFieldStatus.setEditable(false);
		panelNorth1_7.add(textFieldStatus);
		textFieldStatus.setColumns(10);
	}

	public void update(Scheduler scheduler) {
		roomGrid = scheduler.getRoomGrid();
		file = scheduler.getFile();

		resetComboBoxes();
		ArrayList<String> colors = file.getColors();
		for (String color : colors) {
			comboBoxColor.addItem(color);
		}
	}

	public void setButtonLeft(boolean isEnabled) {
		buttonLeft.setEnabled(isEnabled);
	}

	public void setButtonRight(boolean isEnabled) {
		buttonRight.setEnabled(isEnabled);
	}

	public boolean fieldsFilled() {
		JComboBox[] comboBoxes = { comboBoxCourse, comboBoxSection, comboBoxType, comboBoxProfessor };
		for (JComboBox<String> comboBox : comboBoxes) {
			if (comboBox.getSelectedItem().equals(starterItem)) {
				return false;
			}
		}
		return true;
	}

	public void updateStatus(String message) {
		textFieldStatus.setText(message);
	}

	public boolean isDisplayMatches() {
		return displayMatches;
	}

	public void toggleDisplayMatches() {
		displayMatches = !displayMatches;
	}

	public String[] getInfo() {
		String course = (String) comboBoxCourse.getSelectedItem();
		String section = (String) comboBoxSection.getSelectedItem();
		String type = (String) comboBoxType.getSelectedItem();
		String professor = (String) comboBoxProfessor.getSelectedItem();
		String color = (String) comboBoxColor.getSelectedItem();
		return new String[] { course, section, type, professor, color };
	}

	private void deepCopyClasses() {
		ArrayList<ArrayList<String>> classesOriginal = file.getClassesOriginal();
		classes = new ArrayList<ArrayList<String>>();
		for (ArrayList<String> list : classesOriginal) {
			ArrayList<String> row = new ArrayList<String>();
			for (String item : list) {
				row.add(new String(item));
			}
			classes.add(row);
		}
	}

	private void resetComboBoxes() {
		deepCopyClasses();
		updateComboBoxes();
	}

	private void clearComboBoxes() {
		comboBoxListening = false;
		comboBoxCourse.removeAllItems();
		comboBoxSection.removeAllItems();
		comboBoxType.removeAllItems();
		comboBoxProfessor.removeAllItems();
		comboBoxListening = true;
	}

	private void addStarterItemToComboBoxes() {
		JComboBox[] comboBoxes = { comboBoxCourse, comboBoxSection, comboBoxType, comboBoxProfessor };
		comboBoxListening = false;
		for (JComboBox<String> comboBox : comboBoxes) {
			comboBox.addItem(starterItem);
		}
		comboBoxListening = true;
	}

	private void updateComboBoxes() {
		clearComboBoxes();
		addStarterItemToComboBoxes();
		ArrayList<ArrayList<String>> classesOriginal = file.getClassesOriginal();
		for (ArrayList<String> sClass : classesOriginal) {
			addDistinctItemToComboBox(sClass.get(0), comboBoxCourse);
			addDistinctItemToComboBox(sClass.get(1), comboBoxSection);
			addDistinctItemToComboBox(sClass.get(2), comboBoxType);
			addDistinctItemToComboBox(sClass.get(3), comboBoxProfessor);
		}
	}

	private void updateComboBoxes(int index, String item) {
		clearComboBoxes();
		if (item.equals(starterItem)) {
			resetComboBoxes();
		} else {
			ArrayList<ArrayList<String>> rowsToDelete = new ArrayList<ArrayList<String>>();
			for (ArrayList<String> sClass : classes) {
				if (sClass.get(index).equals(item)) {
					addDistinctItemToComboBox(sClass.get(0), comboBoxCourse);
					addDistinctItemToComboBox(sClass.get(1), comboBoxSection);
					addDistinctItemToComboBox(sClass.get(2), comboBoxType);
					addDistinctItemToComboBox(sClass.get(3), comboBoxProfessor);
				} else {
					rowsToDelete.add(sClass);
				}
			}
			classes.removeAll(rowsToDelete);
			addStarterItemToComboBoxes();
		}
	}

	private void addDistinctItemToComboBox(String item, JComboBox<String> comboBox) {
		comboBoxListening = false;
		if (!itemInComboBox(item, comboBox)) {
			comboBox.addItem(item);
		}
		comboBoxListening = true;
	}

	private boolean itemInComboBox(String item, JComboBox<String> comboBox) {
		for (int i = 0; i < comboBox.getItemCount(); i++) {
			if (item.equals(comboBox.getItemAt(i))) {
				return true;
			}
		}
		return false;
	}
}
