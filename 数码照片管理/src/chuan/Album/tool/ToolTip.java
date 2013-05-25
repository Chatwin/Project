package chuan.Album.tool;

import java.util.Vector;
import javax.swing.JComponent;

public class ToolTip {
	private static final String TITLE = "��Ƭ���⣺";
	private static final String DATE = "<br>�������ڣ�";
	private static final String NOTE = "<br>ͼƬ������";
	private static final String SPACE =
			"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	
	public static void set(JComponent comp, Vector<?> photoV) {
		StringBuffer toolTip = new StringBuffer();		//����������ʾ����
		
		toolTip.append("<html>");	//���HTML��ǩͷ
		toolTip.append(TITLE);		//��ӱ����ǩ
		toolTip.append(photoV.get(3));	//��ӱ�������
		toolTip.append(DATE);			//������ڱ�ǩ
		toolTip.append(photoV.get(2));	//�����������
		toolTip.append(NOTE);			//���������ǩ
		
		String note = photoV.get(4).toString();		//�����������
		StringBuffer rowBuf = new StringBuffer();	//����������ʾ�ж���
		int rowBytes = 0;	//���ֽ���
		String c = "";		//�ַ�
		
		for(int i = 0; i < note.length(); i ++) {
			c = note.substring(i, i + 1);	//����ַ�
			rowBuf.append(c);				//��ӵ�������ʾ��
			rowBytes += (c.getBytes()[0] > 0 ? 1 : 2);	//�������ֽ���
			
			if (rowBytes > 18) {	//������ֽ�������18
				toolTip.append(rowBuf.toString());	//��ӹ�����ʾ�е�������ʾ
				toolTip.append(SPACE);				//��ӻ��пո�
				rowBuf = new StringBuffer();		//�ؽ�������ʾ�ж���
				rowBytes = 0;						//�ָ����ֽ���
			}
		}
		if (rowBytes == 0) {
			if (note.length() != 0) {
				int length = toolTip.length();
				toolTip.delete(length - SPACE.length(), length);	//�Ƴ������ӵĻ��пո�
			}
		} else {	//���һ�й�����ʾ��δ���
			toolTip.append(rowBuf.toString());	//��ӹ�����ʾ�е�������ʾ
		}
		
		toolTip.append("</html>"); //���HTML��ǩβ
		
		comp.setToolTipText(toolTip.toString());	//���ù�����ʾ
	}
}
