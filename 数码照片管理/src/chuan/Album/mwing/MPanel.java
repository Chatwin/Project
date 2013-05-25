package chuan.Album.mwing;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class MPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final JPanel photoBoxPanel = new JPanel();	//图片箱面板
	protected static final MLabel showPhotoLabel = new MLabel();	//幻灯片标签
	
	public MPanel() {
		setLayout(new BorderLayout());	//采用边框式布局
	}
	
	public JPanel getPhotoBoxPanel() {
		return photoBoxPanel;
	}
	
	public MLabel getShowPhotoLabel() {
		return showPhotoLabel;
	}
}
