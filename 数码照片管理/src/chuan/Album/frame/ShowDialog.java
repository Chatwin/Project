package chuan.Album.frame;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import chuan.Album.dao.Dao;
import chuan.Album.mwing.MLabel;
import chuan.Album.tool.*;

public class ShowDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ShowDialog(File photoFile) {
		super();
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ShowDialog.this.dispose();	//销毁对话框窗体
				}
			}
		});
		setModal(true);		//设置对话框为有模式
		setUndecorated(true);	//设置不显示对话框的标题栏
		setBounds(0, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		final MLabel photoLabel = new MLabel();
		photoLabel.setIcon(new ImageIcon(photoFile.getPath()));
		Vector<?> photoV = Dao.getInstance().selectPhoto(photoFile.getName());
		ToolTip.set(photoLabel, photoV);
		photoLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {	//如果双击鼠标
					ShowDialog.this.dispose();	//则销毁对话框窗体
				}
			}
		});
		getContentPane().add(photoLabel, BorderLayout.CENTER);
	}
	public static void main(String args[]) {
		try {
			ShowDialog dialog = new ShowDialog(null);
			dialog.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
