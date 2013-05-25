package chuan.Album.UI;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import chuan.Album.tool.ScreenSize;
public class AlbumFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//变量定义
	private AlbumPanel albumPanel1;
	
	public AlbumFrame() {
		initComponents();
		ScreenSize.centered(this);
	}
	private void initComponents() {
		// TODO Auto-generated method stub
		albumPanel1 = new AlbumPanel();
		
		//设置用户在此窗体上发起 "close" 时默认执行的操作。
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("电子相册");
		
		//getContentPane()返回此窗体的 contentPane 对象
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		//设置沿水平轴确定组件位置和大小的 Group。
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(albumPanel1, GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(albumPanel1, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
		);
		//调整此窗口的大小，以适合其子组件的首选大小和布局。
		pack();
	}
	
	public static void main(String args[]) {
		try {
			//UIManager 管理当前外观、可用外观集合、外观更改时被通知的 PropertyChangeListeners、
			//外观默认值以及获取各种默认值的便捷方法
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());		//设置为系统外观
        } catch (ClassNotFoundException ex) {											//如果无法找到 LookAndFeel 类
        	//Logger 对象用来记录特定系统或应用程序组件的日志消息。
        	//getLogger为指定子系统查找或创建一个 logger。
        	//log()记录带有一个对象参数的消息。
            Logger.getLogger(AlbumFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {											//如果无法创建一个该类的新实例
            Logger.getLogger(AlbumFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {											//如果该类或初始化程序不可访问
            Logger.getLogger(AlbumFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {									//如果 lnf.isSupportedLookAndFeel() 为 false
            Logger.getLogger(AlbumFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new AlbumFrame().setVisible(true);
			}
		});
	}
}
