package chuan.Album.mwing;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class MPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final JPanel photoBoxPanel = new JPanel();	//ͼƬ�����
	protected static final MLabel showPhotoLabel = new MLabel();	//�õ�Ƭ��ǩ
	
	public MPanel() {
		setLayout(new BorderLayout());	//���ñ߿�ʽ����
	}
	
	public JPanel getPhotoBoxPanel() {
		return photoBoxPanel;
	}
	
	public MLabel getShowPhotoLabel() {
		return showPhotoLabel;
	}
}
