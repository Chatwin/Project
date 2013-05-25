package chuan.Album.calendar;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CalendarPanel extends JPanel{
	private JTextField textField;
	private CalendarDialog dialog;
	private JButton button;
	public CalendarPanel () {
		super();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		setLayout(flowLayout);
		textField = new JTextField();
		textField.setColumns(11);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setFocusable(false);
		textField.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				showCalendarDialog();
			}
		});
		add(textField);
		button = new JButton();
		button.setText("¨‹");
		button.setMargin(new Insets(0, 6, 0, 6));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCalendarDialog();
			}
		});
		add(button);
		textField.setPreferredSize(new Dimension(136, (int)button.getPreferredSize().getHeight()));
	}
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		textField.setEnabled(enabled);
		button.setEnabled(enabled);
	}
	private void showCalendarDialog() {
		Dimension preferredSize = textField.getPreferredSize();
		Point locationOnScreen = textField.getLocationOnScreen();
		int x = (int)locationOnScreen.getX();
		int y = (int)(locationOnScreen.getY() + preferredSize.getHeight());
		int width = 274;
		int height = 251;
		JRootPane rootPane = textField.getRootPane();
		Point rootPaneLocationOnScreen = rootPane.getLocationOnScreen();
		dialog = new CalendarDialog();
		dialog.setBounds(x, y, width, height);
		new Thread() {
			public void run() {
				while (true) {
					if (MTableCell.getSelectedDay() != null) {
						textField.setText(CalendarDialog.getYear() + "-" + CalendarDialog.getMonth() + "-" + MTableCell.getSelectedDay());
						dialog.dispose();
						MTableCell.setSelectedDay(null);
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		dialog.setVisible(true);
	}
	public CalendarDialog getDialog() {
		return dialog;
	}
	public JTextField getTextField() {
		return textField;
	}
}
