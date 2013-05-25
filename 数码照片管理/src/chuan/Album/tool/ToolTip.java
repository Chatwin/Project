package chuan.Album.tool;

import java.util.Vector;
import javax.swing.JComponent;

public class ToolTip {
	private static final String TITLE = "照片标题：";
	private static final String DATE = "<br>拍摄日期：";
	private static final String NOTE = "<br>图片描述：";
	private static final String SPACE =
			"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	
	public static void set(JComponent comp, Vector<?> photoV) {
		StringBuffer toolTip = new StringBuffer();		//创建工具提示对象
		
		toolTip.append("<html>");	//添加HTML标签头
		toolTip.append(TITLE);		//添加标题标签
		toolTip.append(photoV.get(3));	//添加标题内容
		toolTip.append(DATE);			//添加日期标签
		toolTip.append(photoV.get(2));	//添加日期内容
		toolTip.append(NOTE);			//添加描述标签
		
		String note = photoV.get(4).toString();		//获得描述内容
		StringBuffer rowBuf = new StringBuffer();	//创建工具提示行对象
		int rowBytes = 0;	//行字节数
		String c = "";		//字符
		
		for(int i = 0; i < note.length(); i ++) {
			c = note.substring(i, i + 1);	//获得字符
			rowBuf.append(c);				//添加到工具提示行
			rowBytes += (c.getBytes()[0] > 0 ? 1 : 2);	//计算行字节数
			
			if (rowBytes > 18) {	//如果行字节数大于18
				toolTip.append(rowBuf.toString());	//添加工具提示行到工具提示
				toolTip.append(SPACE);				//添加回行空格
				rowBuf = new StringBuffer();		//重建工具提示行对象
				rowBytes = 0;						//恢复行字节数
			}
		}
		if (rowBytes == 0) {
			if (note.length() != 0) {
				int length = toolTip.length();
				toolTip.delete(length - SPACE.length(), length);	//移除最后添加的回行空格
			}
		} else {	//最后一行工具提示尚未添加
			toolTip.append(rowBuf.toString());	//添加工具提示行到工具提示
		}
		
		toolTip.append("</html>"); //添加HTML标签尾
		
		comp.setToolTipText(toolTip.toString());	//设置工具提示
	}
}
