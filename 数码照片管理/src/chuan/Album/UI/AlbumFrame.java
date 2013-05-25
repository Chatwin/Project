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
	//��������
	private AlbumPanel albumPanel1;
	
	public AlbumFrame() {
		initComponents();
		ScreenSize.centered(this);
	}
	private void initComponents() {
		// TODO Auto-generated method stub
		albumPanel1 = new AlbumPanel();
		
		//�����û��ڴ˴����Ϸ��� "close" ʱĬ��ִ�еĲ�����
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("�������");
		
		//getContentPane()���ش˴���� contentPane ����
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		//������ˮƽ��ȷ�����λ�úʹ�С�� Group��
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(albumPanel1, GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(albumPanel1, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
		);
		//�����˴��ڵĴ�С�����ʺ������������ѡ��С�Ͳ��֡�
		pack();
	}
	
	public static void main(String args[]) {
		try {
			//UIManager ����ǰ��ۡ�������ۼ��ϡ���۸���ʱ��֪ͨ�� PropertyChangeListeners��
			//���Ĭ��ֵ�Լ���ȡ����Ĭ��ֵ�ı�ݷ���
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());		//����Ϊϵͳ���
        } catch (ClassNotFoundException ex) {											//����޷��ҵ� LookAndFeel ��
        	//Logger ����������¼�ض�ϵͳ��Ӧ�ó����������־��Ϣ��
        	//getLoggerΪָ����ϵͳ���һ򴴽�һ�� logger��
        	//log()��¼����һ�������������Ϣ��
            Logger.getLogger(AlbumFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {											//����޷�����һ���������ʵ��
            Logger.getLogger(AlbumFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {											//���������ʼ�����򲻿ɷ���
            Logger.getLogger(AlbumFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {									//��� lnf.isSupportedLookAndFeel() Ϊ false
            Logger.getLogger(AlbumFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new AlbumFrame().setVisible(true);
			}
		});
	}
}
