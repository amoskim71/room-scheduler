import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Scheduler extends JFrame {
	private JPanel contentPane;
	private Options options;
	private RoomGrid roomGrid;
	private File file;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Scheduler frame = new Scheduler();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Scheduler() {
		setTitle("Room Scheduler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 2000, 1000);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 10));

		options = new Options();
		contentPane.add(options, BorderLayout.NORTH);

		roomGrid = new RoomGrid();
		contentPane.add(roomGrid, BorderLayout.CENTER);

		file = new File();
		contentPane.add(file, BorderLayout.SOUTH);

		update();
	}

	/**
	 * Return options.
	 * 
	 * @return options, the Options JPanel
	 */
	public Options getOptions() {
		return options;
	}

	/**
	 * Return roomGrid.
	 * 
	 * @return roomGrid, the RoomGrid JPanel
	 */
	public RoomGrid getRoomGrid() {
		return roomGrid;
	}

	/**
	 * Return file.
	 * 
	 * @return file, the File JPanel
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Update all the JPanels with references of scheduler and of each other.
	 */
	private void update() {
		options.update(this);
		roomGrid.update(this);
		file.update(this);
	}
}
