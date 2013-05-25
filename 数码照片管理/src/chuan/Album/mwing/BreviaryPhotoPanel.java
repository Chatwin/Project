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
		final JScrollPane scrollPane = new JScrollPane();	//创建一个滚动窗格
		scrollPane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);//水平滚动条需要时可见
		scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);	//垂直滚动条永远可见
		add(scrollPane, BorderLayout.CENTER);	//将滚动窗格添加到边框布局的中心
		final JPanel panel = new JPanel();	//创建一个面板
		final FlowLayout flowLayout = new FlowLayout();		//创建一个流式布局
		flowLayout.setHgap(2);	//设置水平间隙
		flowLayout.setVgap(2);	//设置垂直间隙
		panel.setLayout(flowLayout);	//采用流式布局
		scrollPane.setViewportView(panel);	//将面板添加到滚动窗格
		photoBoxPanel.setLayout(new GridLayout(0, 6, 0, 0));	//采用网格式布局
		panel.add(photoBoxPanel);
	}
}
