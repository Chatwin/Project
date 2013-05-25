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
//用于接收组件事件的抽象适配器类。
import java.awt.event.ComponentAdapter;
//指示组件被移动、大小被更改或可见性被更改的低级别事件（它也是其他组件级事件的根类）。
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
		setHorizontalTextPosition(SwingConstants.CENTER);	//水平方向文字位于图片中央
		setVerticalTextPosition(SwingConstants.BOTTOM);		//垂直方向图像位于文字下方
		setMargin(new Insets(0, 0, 0, 0));	//没有间距
		setContentAreaFilled(false);	//不填充内容区域
		setBorderPainted(false);	//不绘制边框
		setFocusPainted(false);		//不绘制焦点状态
		//为按钮设置背景照片
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				ImageIcon imageIcon = new ImageIcon(photoFile.getPath());	// 创建ImageIcon类型的图片对象
				Image image = createImage(130, 130);	//创建指定大小的Image类型对象
				Graphics g = image.getGraphics();	//获取image的绘图对象
				g.drawImage(imageIcon.getImage(), 0, 0, 130, 130,
						PhotoPreviewButton.this);	//绘图图片到image
				image.flush();	//刷新image
				setIcon(new ImageIcon(image));	//设置图片
				Vector<Object> photoV = dao.selectPhoto(photoFile.getName());	//获得图片信息
				setText(photoV.get(3).toString());	//设置文本
				ToolTip.set(PhotoPreviewButton.this, photoV);	//设置工具提示
				setName(photoFile.getName());	//设置图片名称
				setPath(photoFile.getPath());	//设置图片路径
				removeComponentListener(this);	//移除时间监听器
			}
		});
		//捕获鼠标事件
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {	//单击
					MPanel panel = (MPanel)AlbumPanel.getPhotoPanel()
							.getComponent(0);	//获得浏览方式面板对象
					if (panel instanceof LanternSlidePanel) {	//幻灯片方式浏览
						LanternSlidePanel lanternSlidePanel = (LanternSlidePanel)panel;
						MLabel photoLabel = lanternSlidePanel
								.getShowPhotoLabel();	//获得图片标签对象
						photoLabel.setIcon(new ImageIcon(
								PhotoPreviewButton.this.getPath()));//修改图片
					}
					String photoPath = PhotoPreviewButton.this.getPath();	//获得照片存放路径
					photoPath = photoPath.replace('\\', '/');	//替换字符
					photoPath = photoPath.substring(photoPath.indexOf("/chuan/img/") + 11);
					String[] nodes = photoPath.split("/");	//分隔相册路径
					JTree albumTree = AlbumPanel.getAlbumTree();	//获得相册树对象
					MTreeNode node = (MTreeNode)albumTree.getModel().getRoot();	//// 获得相册树的根节点对象
					for (int i = 0; i < nodes.length - 1; i ++) {	//遍历相册路径
						Enumeration<?> enu = node.children();	//获得子节点的枚举对象
						while (enu.hasMoreElements()) {	//遍历枚举对象
							node = (MTreeNode)enu.nextElement();	//获得节点对象
							if (node.getUserObject().equals(nodes[i])) {// 如果节点标签等于相册名称
								if (! node.isLoad()) {	//如果该节点尚未加载
									AlbumPanel.loadChildNode(node);	//加载该节点
								}
								break;	//跳出循环
							}
						}
					}
					TreePath treePath = new TreePath(node.getPath());	//创建相册的路径对象
					albumTree.scrollPathToVisible(treePath);	//滚动该节点至可见
					TreeSelectionListener treeSelectionListener = albumTree
							.getTreeSelectionListeners()[0];	//获得树的选中事件监听器
					albumTree.removeTreeSelectionListener(treeSelectionListener);// 移除选中事件监听器
					albumTree.setSelectionPath(treePath);	//选中该路径节点
					albumTree.addTreeSelectionListener(treeSelectionListener);// 添加选中事件监听器
				} else {	//连击
					new ShowDialog(photoFile).setVisible(true);	//全屏显示照片
				}
			}
		});
		//捕获键盘事件，获得按键码
		addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent e) {	//键盘按下时触发
				keyCode = e.getKeyCode();	//获得被按下的按键的键值
			}
			
			public void keyReleased(KeyEvent e) {	//当键盘按键被释放时触发
				keyCode = 0;	//恢复为默认键值
			}
		});
		//捕获按下按钮事件
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				PhotoPreviewButton noncePhoto = PhotoPreviewButton.this;//获得当前按钮对象
				switch (keyCode) {	//判断当前按下的键
				case KeyEvent.VK_CONTROL:	//Ctrl被按下，追加选中当前按钮
					noncePhoto.setForeground(Color.RED);	//设置前景色
					selectedPhoto.add(noncePhoto);	//添加到向量中
					break;
				case KeyEvent.VK_SHIFT:	// Shift键被按下，追加选中上次按下的按钮与当前按钮之间的所有按钮
					if(selectedPhoto.isEmpty()) {	//尚未选中任何照片
						noncePhoto.setForeground(Color.RED);	//设置前景色
						selectedPhoto.add(noncePhoto);	//添加到向量中
					} else {	//已存在选中照片
						JButton lastPhoto = selectedPhoto.lastElement();	//获得上次按下的按钮对象
						cancelSelected();	//清空被选中的图片按钮
						JButton startPhoto = lastPhoto;	//开始的图片按钮
						JButton endPhoto = noncePhoto;	//结束的图片按钮对象
						Point lastLocation = lastPhoto.getLocation();	//获得上次按下按钮的起始绘制点坐标
						Point nonceLocation = noncePhoto.getLocation();	//获得当前按钮的起始绘制点坐标
						//确定上次按下的按钮盒和本次按下的按钮的具体顺序
						if (nonceLocation.getY() == lastLocation.getY()) {	//在同意一行
							if (nonceLocation.getX() < lastLocation.getX()) {// 本次按下的按钮在前
								startPhoto = noncePhoto;
								endPhoto = lastPhoto;
							}
						} else {	//不在同一行
							if (nonceLocation.getY() < lastLocation.getY()) {// 本次按下的按钮在前

								startPhoto = noncePhoto;
								endPhoto = lastPhoto;
							}
						}
						//获得所有图片按钮对象
						Component[] components = PhotoPreviewButton.this
								.getParent().getComponents();
						boolean isSelected = false;	//默认为遍历到第一个被选中的图片按钮
						for (int i =0; i < components.length; i ++) {	//遍历图片按钮数组
							PhotoPreviewButton button = (PhotoPreviewButton)components[i];	//获得图片按钮对象
							if (isSelected) {	//已经遍历到了第一个被选中的图片按钮
								button.setForeground(Color.RED);// 设置前景色
								selectedPhoto.add(button);// 添加到向量中
								if (button.equals(endPhoto)) {// 已经遍历到了最后一个被选中的图片按钮
									break;// 停止遍历，跳出循环
								}
							} else {// 尚未遍历到了第一个被选中的图片按钮
								if (button.equals(startPhoto)) {// 已经遍历到了第一个被选中的图片按钮
									button.setForeground(Color.RED);// 设置前景色
									selectedPhoto.add(button);// 添加到向量中
									isSelected = true;// 已经遍历到了第一个被选中的图片按钮
								}
							}
						}
						selectedPhoto.add(noncePhoto);	// 将本次按下的图片按钮对象再次添加到向量的最后
					}
					break;
				default:	//未按下任何键
					cancelSelected();	//清空被选中的图片按钮
					noncePhoto.setForeground(Color.RED);
					selectedPhoto.add(noncePhoto);
				}
			}
			private void cancelSelected() {
				//遍历被选中的图片按钮
				for (Iterator<PhotoPreviewButton> it = selectedPhoto.iterator();
						it.hasNext();) {
					it.next().setForeground(Color.BLACK);	//恢复为默认前景色
				}
				selectedPhoto.clear();	//清除被选中的图片按钮
			}
		});
	}
}
