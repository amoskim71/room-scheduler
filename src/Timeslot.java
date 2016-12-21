import java.awt.Color;
import java.awt.FlowLayout;
import java.lang.reflect.Field;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Timeslot extends JPanel {
	private int i;
	private int j;
	private boolean reserved;
	private boolean borderChanged;
	private boolean backgroundChanged;
	private Border border;
	private Color defaultBorderColor;
	private Color borderColor;
	private JLabel label;
	private Color defaultBackgroundColor;
	private Color backgroundColor;

	/**
	 * Create the panel.
	 */
	public Timeslot(int i, int j) {
		this.i = i;
		this.j = j;
		reserved = false;
		
		FlowLayout layout = (FlowLayout) getLayout();
        layout.setVgap(0);
        defaultBorderColor = Color.gray;
		borderColor = defaultBorderColor;
		border = BorderFactory.createLineBorder(borderColor);
		setBorder(border);
		label = new JLabel();
		add(label);
		defaultBackgroundColor = getBackground();
		backgroundColor = defaultBackgroundColor;
	}

	/**
	 * Reserves timeslot. Changes text and background color.
	 * 
	 * @param info
	 *            information of reservation; { course, section, type,
	 *            professor, color }
	 * 
	 */
	public void reserve(String[] info) {
		reserved = true;
		borderChanged = false;
		backgroundChanged = false;
		String color = info[4].toLowerCase();
		Field field = null;

		try {
			field = Class.forName("java.awt.Color").getField(color);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			backgroundColor = (Color) field.get(null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		label.setText(info[1]);
		setBackground(backgroundColor);
	}

	/**
	 * Unreserves timeslot. Resets text and background color.
	 */
	public void unreserve() {
		reserved = false;
		borderChanged = false;
		backgroundChanged = false;
		label.setText("");
		backgroundColor = defaultBackgroundColor;
		setBackground(backgroundColor);
	}

	/**
	 * Returns i.
	 * 
	 * @return i, row in which the timeslot resides
	 */
	public int getI() {
		return i;
	}

	/**
	 * Returns j.
	 * 
	 * @return j, column in which the timeslot resides
	 */
	public int getJ() {
		return j;
	}

	/**
	 * Returns reserved.
	 * 
	 * @return reserved, true if timeslot is reserved; false otherwise
	 */
	public boolean isReserved() {
		return reserved;
	}
	
	public boolean isBorderChanged() {
		return borderChanged;
	}
	
	public void setBorderChanged(boolean borderChanged) {
		this.borderChanged = borderChanged;
	}
	
	public boolean isBackgroundChanged() {
		return backgroundChanged;
	}
	
	public void setBackgroundChanged(boolean backgroundChanged) {
		this.backgroundChanged = backgroundChanged;
	}

	/**
	 * Changes color of border.
	 * 
	 * @param color
	 *            border color to be shown
	 */
	public void setBorderColor(String color) {
		Field field = null;

		if (color == null) {
			borderChanged = false;
			borderColor = defaultBorderColor;
		} else {
			borderChanged = true;
			color = color.toLowerCase();
			try {
				field = Class.forName("java.awt.Color").getField(color);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			try {
				borderColor = (Color) field.get(null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		border = BorderFactory.createLineBorder(borderColor);
		setBorder(border);
	}

	/**
	 * Changes color of background.
	 * 
	 * @param color
	 *            background color to be shown
	 */
	public void setBackgroundColor(String color) {
		Field field = null;

		if (color == null) {
			backgroundChanged = false;
			backgroundColor = defaultBackgroundColor;
		} else {
			backgroundChanged = true;
			color = color.toLowerCase();
			try {
				field = Class.forName("java.awt.Color").getField(color);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			try {
				backgroundColor = (Color) field.get(null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		setBackground(backgroundColor);
	}
}
