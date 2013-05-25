package chuan.Album.mwing;

import chuan.Album.UI.AlbumPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//���ڽ�������¼��ĳ����������ࡣ
import java.awt.event.ComponentAdapter;
//ָʾ������ƶ�����С�����Ļ�ɼ��Ա����ĵĵͼ����¼�����Ҳ������������¼��ĸ��ࣩ��
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import chuan.Album.dao.Dao;
import chuan.Album.frame.ShowDialog;
import chuan.Album.tool.ToolTip;

public class PhotoPreviewButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Dao dao = Dao.getInstance();
	private static final Vector<PhotoPreviewButton> selectedPhoto = new Vector<PhotoPreviewButton>();
	private static int keyCode;
	private String path;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public static Vector<PhotoPreviewButton> getSelectedPhoto() {
		return selectedPhoto;
	} 
	public PhotoPreviewButton(final File photoFile) {
		setHorizontalTextPosition(SwingConstants.CENTER);	//ˮƽ��������λ��ͼƬ����
		setVerticalTextPosition(SwingConstants.BOTTOM);		//��ֱ����ͼ��λ�������·�
		setMargin(new Insets(0, 0, 0, 0));	//û�м��
		setContentAreaFilled(false);	//�������������
		setBorderPainted(false);	//�����Ʊ߿�
		setFocusPainted(false);		//�����ƽ���״̬
		//Ϊ��ť���ñ�����Ƭ
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				ImageIcon imageIcon = new ImageIcon(photoFile.getPath());	// ����ImageIcon���͵�ͼƬ����
				Image image = createImage(130, 130);	//����ָ����С��Image���Ͷ���
				Graphics g = image.getGraphics();	//��ȡimage�Ļ�ͼ����
				g.drawImage(imageIcon.getImage(), 0, 0, 130, 130,
						PhotoPreviewButton.this);	//��ͼͼƬ��image
				image.flush();	//ˢ��image
				setIcon(new ImageIcon(image));	//����ͼƬ
				Vector<Object> photoV = dao.selectPhoto(photoFile.getName());	//���ͼƬ��Ϣ
				setText(photoV.get(3).toString());	//�����ı�
				ToolTip.set(PhotoPreviewButton.this, photoV);	//���ù�����ʾ
				setName(photoFile.getName());	//����ͼƬ����
				setPath(photoFile.getPath());	//����ͼƬ·��
				removeComponentListener(this);	//�Ƴ�ʱ�������
			}
		});
		//��������¼�
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {	//����
					MPanel panel = (MPanel)AlbumPanel.getPhotoPanel()
							.getComponent(0);	//��������ʽ������
					if (panel instanceof LanternSlidePanel) {	//�õ�Ƭ��ʽ���
						LanternSlidePanel lanternSlidePanel = (LanternSlidePanel)panel;
						MLabel photoLabel = lanternSlidePanel
								.getShowPhotoLabel();	//���ͼƬ��ǩ����
						photoLabel.setIcon(new ImageIcon(
								PhotoPreviewButton.this.getPath()));//�޸�ͼƬ
					}
					String photoPath = PhotoPreviewButton.this.getPath();	//�����Ƭ���·��
					photoPath = photoPath.replace('\\', '/');	//�滻�ַ�
					photoPath = photoPath.substring(photoPath.indexOf("/chuan/img/") + 11);
					String[] nodes = photoPath.split("/");	//�ָ����·��
					JTree albumTree = AlbumPanel.getAlbumTree();	//������������
					MTreeNode node = (MTreeNode)albumTree.getModel().getRoot();	//// ���������ĸ��ڵ����
					for (int i = 0; i < nodes.length - 1; i ++) {	//�������·��
						Enumeration<?> enu = node.children();	//����ӽڵ��ö�ٶ���
						while (enu.hasMoreElements()) {	//����ö�ٶ���
							node = (MTreeNode)enu.nextElement();	//��ýڵ����
							if (node.getUserObject().equals(nodes[i])) {// ����ڵ��ǩ�����������
								if (! node.isLoad()) {	//����ýڵ���δ����
									AlbumPanel.loadChildNode(node);	//���ظýڵ�
								}
								break;	//����ѭ��
							}
						}
					}
					TreePath treePath = new TreePath(node.getPath());	//��������·������
					albumTree.scrollPathToVisible(treePath);	//�����ýڵ����ɼ�
					TreeSelectionListener treeSelectionListener = albumTree
							.getTreeSelectionListeners()[0];	//�������ѡ���¼�������
					albumTree.removeTreeSelectionListener(treeSelectionListener);// �Ƴ�ѡ���¼�������
					albumTree.setSelectionPath(treePath);	//ѡ�и�·���ڵ�
					albumTree.addTreeSelectionListener(treeSelectionListener);// ���ѡ���¼�������
				} else {	//����
					new ShowDialog(photoFile).setVisible(true);	//ȫ����ʾ��Ƭ
				}
			}
		});
		//��������¼�����ð�����
		addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent e) {	//���̰���ʱ����
				keyCode = e.getKeyCode();	//��ñ����µİ����ļ�ֵ
			}
			
			public void keyReleased(KeyEvent e) {	//�����̰������ͷ�ʱ����
				keyCode = 0;	//�ָ�ΪĬ�ϼ�ֵ
			}
		});
		//�����°�ť�¼�
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				PhotoPreviewButton noncePhoto = PhotoPreviewButton.this;//��õ�ǰ��ť����
				switch (keyCode) {	//�жϵ�ǰ���µļ�
				case KeyEvent.VK_CONTROL:	//Ctrl�����£�׷��ѡ�е�ǰ��ť
					noncePhoto.setForeground(Color.RED);	//����ǰ��ɫ
					selectedPhoto.add(noncePhoto);	//��ӵ�������
					break;
				case KeyEvent.VK_SHIFT:	// Shift�������£�׷��ѡ���ϴΰ��µİ�ť�뵱ǰ��ť֮������а�ť
					if(selectedPhoto.isEmpty()) {	//��δѡ���κ���Ƭ
						noncePhoto.setForeground(Color.RED);	//����ǰ��ɫ
						selectedPhoto.add(noncePhoto);	//��ӵ�������
					} else {	//�Ѵ���ѡ����Ƭ
						JButton lastPhoto = selectedPhoto.lastElement();	//����ϴΰ��µİ�ť����
						cancelSelected();	//��ձ�ѡ�е�ͼƬ��ť
						JButton startPhoto = lastPhoto;	//��ʼ��ͼƬ��ť
						JButton endPhoto = noncePhoto;	//������ͼƬ��ť����
						Point lastLocation = lastPhoto.getLocation();	//����ϴΰ��°�ť����ʼ���Ƶ�����
						Point nonceLocation = noncePhoto.getLocation();	//��õ�ǰ��ť����ʼ���Ƶ�����
						//ȷ���ϴΰ��µİ�ť�кͱ��ΰ��µİ�ť�ľ���˳��
						if (nonceLocation.getY() == lastLocation.getY()) {	//��ͬ��һ��
							if (nonceLocation.getX() < lastLocation.getX()) {// ���ΰ��µİ�ť��ǰ
								startPhoto = noncePhoto;
								endPhoto = lastPhoto;
							}
						} else {	//����ͬһ��
							if (nonceLocation.getY() < lastLocation.getY()) {// ���ΰ��µİ�ť��ǰ

								startPhoto = noncePhoto;
								endPhoto = lastPhoto;
							}
						}
						//�������ͼƬ��ť����
						Component[] components = PhotoPreviewButton.this
								.getParent().getComponents();
						boolean isSelected = false;	//Ĭ��Ϊ��������һ����ѡ�е�ͼƬ��ť
						for (int i =0; i < components.length; i ++) {	//����ͼƬ��ť����
							PhotoPreviewButton button = (PhotoPreviewButton)components[i];	//���ͼƬ��ť����
							if (isSelected) {	//�Ѿ��������˵�һ����ѡ�е�ͼƬ��ť
								button.setForeground(Color.RED);// ����ǰ��ɫ
								selectedPhoto.add(button);// ��ӵ�������
								if (button.equals(endPhoto)) {// �Ѿ������������һ����ѡ�е�ͼƬ��ť
									break;// ֹͣ����������ѭ��
								}
							} else {// ��δ�������˵�һ����ѡ�е�ͼƬ��ť
								if (button.equals(startPhoto)) {// �Ѿ��������˵�һ����ѡ�е�ͼƬ��ť
									button.setForeground(Color.RED);// ����ǰ��ɫ
									selectedPhoto.add(button);// ��ӵ�������
									isSelected = true;// �Ѿ��������˵�һ����ѡ�е�ͼƬ��ť
								}
							}
						}
						selectedPhoto.add(noncePhoto);	// �����ΰ��µ�ͼƬ��ť�����ٴ���ӵ����������
					}
					break;
				default:	//δ�����κμ�
					cancelSelected();	//��ձ�ѡ�е�ͼƬ��ť
					noncePhoto.setForeground(Color.RED);
					selectedPhoto.add(noncePhoto);
				}
			}
			private void cancelSelected() {
				//������ѡ�е�ͼƬ��ť
				for (Iterator<PhotoPreviewButton> it = selectedPhoto.iterator();
						it.hasNext();) {
					it.next().setForeground(Color.BLACK);	//�ָ�ΪĬ��ǰ��ɫ
				}
				selectedPhoto.clear();	//�����ѡ�е�ͼƬ��ť
			}
		});
	}
}
