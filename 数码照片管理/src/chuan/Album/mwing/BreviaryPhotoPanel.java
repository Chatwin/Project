package chuan.Album.mwing;

import java.awt.*;
import javax.swing.*;

public class BreviaryPhotoPanel extends MPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BreviaryPhotoPanel() {
		super();
		final JScrollPane scrollPane = new JScrollPane();	//����һ����������
		scrollPane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);//ˮƽ��������Ҫʱ�ɼ�
		scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);	//��ֱ��������Զ�ɼ�
		add(scrollPane, BorderLayout.CENTER);	//������������ӵ��߿򲼾ֵ�����
		final JPanel panel = new JPanel();	//����һ�����
		final FlowLayout flowLayout = new FlowLayout();		//����һ����ʽ����
		flowLayout.setHgap(2);	//����ˮƽ��϶
		flowLayout.setVgap(2);	//���ô�ֱ��϶
		panel.setLayout(flowLayout);	//������ʽ����
		scrollPane.setViewportView(panel);	//�������ӵ���������
		photoBoxPanel.setLayout(new GridLayout(0, 6, 0, 0));	//��������ʽ����
		panel.add(photoBoxPanel);
	}
}
