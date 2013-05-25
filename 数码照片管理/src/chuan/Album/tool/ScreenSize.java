package chuan.Album.tool;

//һ��� Abstract Window Toolkit(AWT) ����������һ���ɰ������� AWT ����������
import java.awt.Container;	
//Dimension ���װ��������������Ŀ�Ⱥ͸߶ȣ���ȷ����������
import java.awt.Dimension;
//���������� Abstract Window Toolkit ʵ��ʵ�ֵĳ����ࡣ
import java.awt.Toolkit;

public class ScreenSize {

	private static int height;
	private static int width;
	
	static {
		Toolkit toolkit = Toolkit.getDefaultToolkit(); //��ȡĬ�Ϲ��߰���
		Dimension screenSize = toolkit.getScreenSize();
		width = (int)screenSize.getWidth();
		height = (int)screenSize.getHeight();
	}
	
	public static int getHeight() {
		return height;
	}
	
	public static int getWidth() {
		return width;
	}
	
	public static void centered(Container container) {
		int w = container.getWidth();
		int h = container.getHeight();
		//�ƶ�������������С��
		container.setBounds((width - w) / 2, (height - h) / 2, w, h);
	}
}
