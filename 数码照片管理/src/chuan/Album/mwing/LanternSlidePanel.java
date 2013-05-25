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
		add(showPhotoLabel, BorderLayout.CENTER);	// 将幻灯片标签添加到边框布局的中心
		final JScrollPane scrollPane = new JScrollPane();	//创建一个滚动窗口
		scrollPane.setPreferredSize(new Dimension(0, 180));	//设置滚动窗口的首选大小
		scrollPane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);	//水平滚动条永远可见
		scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_NEVER);		//垂直滚动条永远不可见
		add(scrollPane, BorderLayout.SOUTH);	// 将滚动窗格添加到边框布局的南方
		final FlowLayout flowLayout = new FlowLayout();	//创建一个流式布局
		flowLayout.setHgap(2);	//设置水平间隔
		flowLayout.setVgap(2);	//设置垂直间隔
		photoBoxPanel.setLayout(flowLayout);	//采用流式布局
		scrollPane.setViewportView(photoBoxPanel);	// 将图片箱面板添加到滚动窗格
	}

}
