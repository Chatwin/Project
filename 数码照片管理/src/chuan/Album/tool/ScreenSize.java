package chuan.Album.tool;

//一般的 Abstract Window Toolkit(AWT) 容器对象是一个可包含其他 AWT 组件的组件。
import java.awt.Container;	
//Dimension 类封装单个对象中组件的宽度和高度（精确到整数）。
import java.awt.Dimension;
//此类是所有 Abstract Window Toolkit 实际实现的抽象超类。
import java.awt.Toolkit;

public class ScreenSize {

	private static int height;
	private static int width;
	
	static {
		Toolkit toolkit = Toolkit.getDefaultToolkit(); //获取默认工具包。
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
		//移动组件并调整其大小。
		container.setBounds((width - w) / 2, (height - h) / 2, w, h);
	}
}
