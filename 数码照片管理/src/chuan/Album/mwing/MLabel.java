package chuan.Album.mwing;

//Graphics ��������ͼ�������ĵĳ�����࣬
//����Ӧ�ó�����������Ѿ��ڸ����豸��ʵ�֣��Լ�����ͼ���Ͻ��л��ơ�
import java.awt.Graphics;
//������ Image �Ǳ�ʾͼ��ͼ���������ĳ��ࡣ
import java.awt.Image;
//һ�� Icon �ӿڵ�ʵ�֣������� Image ���� Icon
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
