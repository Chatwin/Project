package chuan.Album.mwing;

import java.awt.*;
import javax.swing.JScrollPane;

public class LanternSlidePanel extends MPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LanternSlidePanel() {
		super();
		add(showPhotoLabel, BorderLayout.CENTER);	// ���õ�Ƭ��ǩ��ӵ��߿򲼾ֵ�����
		final JScrollPane scrollPane = new JScrollPane();	//����һ����������
		scrollPane.setPreferredSize(new Dimension(0, 180));	//���ù������ڵ���ѡ��С
		scrollPane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);	//ˮƽ��������Զ�ɼ�
		scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_NEVER);		//��ֱ��������Զ���ɼ�
		add(scrollPane, BorderLayout.SOUTH);	// ������������ӵ��߿򲼾ֵ��Ϸ�
		final FlowLayout flowLayout = new FlowLayout();	//����һ����ʽ����
		flowLayout.setHgap(2);	//����ˮƽ���
		flowLayout.setVgap(2);	//���ô�ֱ���
		photoBoxPanel.setLayout(flowLayout);	//������ʽ����
		scrollPane.setViewportView(photoBoxPanel);	// ��ͼƬ�������ӵ���������
	}

}
