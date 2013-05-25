package chuan.Album.calendar;

import java.awt.*;
import javax.swing.*;
public class MButton extends JButton{
	public MButton(int day) {
		super(day + "");
		setBorderPainted(false);
		setBackground(Color.WHITE);
		setMargin(new Insets(0, 0, 0, 0));
		setFont(new Font("ËÎÌו", Font.BOLD, 16));
	}
}
