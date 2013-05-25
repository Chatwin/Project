package chuan.Album.mwing;

//Graphics 类是所有图形上下文的抽象基类，
//允许应用程序在组件（已经在各种设备上实现）以及闭屏图像上进行绘制。
import java.awt.Graphics;
//抽象类 Image 是表示图形图像的所有类的超类。
import java.awt.Image;
//一个 Icon 接口的实现，它根据 Image 绘制 Icon
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MLabel extends JLabel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent (Graphics g) {
		ImageIcon icon = (ImageIcon)getIcon();
		if (icon != null) {
			icon.setImage(icon.getImage().getScaledInstance(getWidth()
					, getHeight(), Image.SCALE_DEFAULT));
			setIcon(icon);
		}
		super.paintComponent(g);
	}
}
