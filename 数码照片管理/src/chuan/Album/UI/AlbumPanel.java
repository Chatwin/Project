package chuan.Album.UI;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.*;
import javax.swing.tree.*;

import chuan.Album.mwing.BreviaryPhotoPanel;

import chuan.Album.mwing.LanternSlidePanel;
import chuan.Album.mwing.MLabel;
import chuan.Album.mwing.MPanel;
import chuan.Album.mwing.MTreeNode;
import chuan.Album.mwing.PhotoPreviewButton;
import chuan.Album.dao.*;
import chuan.Album.frame.FindPhotoInfoDialog;
import chuan.Album.frame.PlayDialog;
import chuan.Album.frame.UpdatePhotoInfoDialog;
public class AlbumPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//定义变量
	private JButton addAlbumButton;
	private JButton addPhotoButton;
	private JPanel albumPanel;
	private JScrollPane albumScrollPane;
	private static JTree albumTree;	//树状控件
	private JButton cancelSelectedButton;
	private JButton delAlbumButton;
	private JButton delPhotoButton;
	private JButton findPhotoButton;
	private JToolBar.Separator jSeparator1;	//工具栏上的分隔符
	private JToolBar.Separator jSeparator2;
	private static JPanel photoPanel;
	private JButton saveButton;
	private JComboBox<?> seeModeComboBox;		//下拉列表框
	private JSplitPane splitPane;			//分隔两个控件
	private JToolBar toolBar;				//工具栏
	private JButton updAlbumButton;
	private JButton updPhotoButton;
	private static final Dao dao = Dao.getInstance();
	private DefaultTreeModel treeModel;
	private Object primaryItem;
	private Thread loadPhotoThread;
	//定义变量结束
	Object[] photoInfo = { "", 0, "", "〈标题〉", "〈描述〉" };
	private ShowModeComboBoxIL showModeComboBoxIL = new ShowModeComboBoxIL();
	private class ShowModeComboBoxIL implements ItemListener {
		private Object primaryItem;
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if (e.getStateChange() == ItemEvent.SELECTED) {
				int selectedIndex = seeModeComboBox.getSelectedIndex();
				switch (selectedIndex) {
				case 0:
					photoPanel.remove(0);
					photoPanel.add(new BreviaryPhotoPanel());
					photoPanel.validate();
					break;
				case 1:
					photoPanel.remove(0);
					LanternSlidePanel panel = new LanternSlidePanel();
					if (panel.getPhotoBoxPanel().getComponentCount() > 0) {
						if (PhotoPreviewButton.getSelectedPhoto().size() > 0) {
							panel.getShowPhotoLabel().setIcon(
									new ImageIcon(PhotoPreviewButton
											.getSelectedPhoto().firstElement()
											.getPath()));
						} else {
							panel.getShowPhotoLabel()
									.setIcon(
											new ImageIcon(
													((PhotoPreviewButton) panel
															.getPhotoBoxPanel()
															.getComponent(0))
															.getPath()));
						}
					}
					photoPanel.add(panel);
					photoPanel.validate();
					break;
				default:
					MPanel showPanel = (MPanel) photoPanel.getComponent(0);
					Component[] photos = showPanel.getPhotoBoxPanel()
							.getComponents();
					if (photos.length > 0) {
						new PlayDialog(null, true, photos).setVisible(true);
					}
					seeModeComboBox.removeItemListener(showModeComboBoxIL);
					seeModeComboBox.setSelectedItem(primaryItem);
					seeModeComboBox.addItemListener(showModeComboBoxIL);
				}
			} else {
				primaryItem = e.getItem();
			}
		}
	}
	public static JTree getAlbumTree() {
		return albumTree;
	}
	public static JPanel getPhotoPanel() {
		return photoPanel;
	}
	private String getSelectedPath() throws UnsupportedEncodingException {
		String upLoadPath = "/chuan/img";
		try {
			upLoadPath = AlbumFrame.class.getResource(upLoadPath).toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		TreePath selectionPath = albumTree.getSelectionPath();
		if (selectionPath == null) {
			return null;
		} else {
			Object[] treePath = selectionPath.getPath();
			for (int i = 1; i < treePath.length; i ++) {
				upLoadPath += "/" + treePath[i];
			}
			return upLoadPath;
		}
	}
	public static void loadChildNode(MTreeNode node) {
		Vector<?> albums = dao.selectAlbums(node.getId());
		for (Iterator<?> it = albums.iterator(); it.hasNext();) {
			Vector<?> album = (Vector<?>) it.next();
			node.add(new MTreeNode((Integer) album.get(0), album.get(2)));
		}
		node.setLoad(true);
	}
	private String getAlbumName(int fatherId, String title, String message) {
		String name = null;	//相册名称
		while (name == null) {
			name = JOptionPane.showInputDialog(this, message, title, 
					JOptionPane.INFORMATION_MESSAGE);	//获得相册名称
			if (name == null) {	//取消
				break;
			} else {	//确定
				name = name.trim();	//去除首尾空格
				if (name.length() == 0) {	//未输入
					JOptionPane.showMessageDialog(this, "请输入相册名称！", "友情提示",
							JOptionPane.INFORMATION_MESSAGE);// 弹出提示输入框

					name = null;
				} else {	//已输入
					Object node = dao
							.selectOnlyValue("select id from tb_album where father_id="
									+ fatherId + " and name='" + name + "'");// 在所属相册中查询同名相册)
					if (node != null) {//已经存在
						JOptionPane.showMessageDialog(this, "该相册名称已经存在！",
								"友情提示", JOptionPane.INFORMATION_MESSAGE);// 弹出存在同名相册提示

						name = null;
					}
				}
			}
		}
		return name;
	}
	private javax.swing.filechooser.FileFilter swingFileFilter = new javax.swing.filechooser.FileFilter() {
		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				String name = file.getName();
				String type = name.substring(name.lastIndexOf("."))
						.toUpperCase();
				if (type.equals(".JPG") || type.endsWith(".JPEG")) {
					return true;
				}
				return false;
			}
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "图像文件（.JPG;.JPEG）";
		}
	};
	private java.io.FileFilter ioFileFilter = new java.io.FileFilter() {
		@Override
		public boolean accept(File file) {
			// TODO Auto-generated method stub
			if (file.isDirectory()) {
				return false;
			} else {
				String name = file.getName();
				String type = name.substring(name.lastIndexOf('.'))
						.toUpperCase();
				if (type.equals(".JPG") || type.equals(".JPEG")) {
					return true;
				} else {
					return false;
				}
			}
		}
	};
	private void initAlbumTree() {
		final MTreeNode root = new MTreeNode("album");
		loadChildNode(root);
		treeModel = new DefaultTreeModel(root, true);
		albumTree.setModel(treeModel);
	}
	private void initComponents() {
		toolBar = new JToolBar();
		addAlbumButton = new JButton();
		addAlbumButton.setText("添加");
		//注册要在工具提示中显示的文本。光标处于该组件上时显示该文本
		addAlbumButton.setToolTipText("添加相册");
		addAlbumButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/addAlbum.png")));
		updAlbumButton = new JButton();
		updAlbumButton.setText("修改");
		updAlbumButton.setToolTipText("修改相册");
		updAlbumButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/updAlbum.png")));
		delAlbumButton = new JButton();
		delAlbumButton.setText("删除");
		delAlbumButton.setToolTipText("删除相册");
		delAlbumButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/delAlbum.png")));
		cancelSelectedButton = new JButton();
		cancelSelectedButton.setText("取消");
		cancelSelectedButton.setToolTipText("取消相册的选中状态");
		cancelSelectedButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/cancelAlbum.png")));
		jSeparator1 = new JToolBar.Separator();
		addPhotoButton = new JButton();
		addPhotoButton.setText("添加");
		addPhotoButton.setToolTipText("添加照片");
		addPhotoButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/addPhoto.png")));
		updPhotoButton = new JButton();
		updPhotoButton.setText("修改");
		updPhotoButton.setToolTipText("修改照片信息");
		updPhotoButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/updPhoto.png")));
		delPhotoButton = new JButton();
		delPhotoButton.setText("删除");
		delPhotoButton.setToolTipText("删除照片");
		delPhotoButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/delAlbum.png")));
		findPhotoButton = new JButton();
		findPhotoButton.setText("搜索");
		findPhotoButton.setToolTipText("搜索照片");
		findPhotoButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/findPhoto.png")));
		saveButton = new JButton();
		saveButton.setText("保存");
		saveButton.setToolTipText("保存照片到制定位置");
		saveButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/savePhoto.png")));
		jSeparator2 = new JToolBar.Separator();
		seeModeComboBox = new JComboBox();
		splitPane = new JSplitPane();
		albumPanel = new JPanel();
		albumScrollPane = new JScrollPane();
		albumTree = new JTree();
		photoPanel = new JPanel();
		
		setLayout(new BorderLayout());
		
		//设置float属性，若为true则可移动，若为false则工具栏不可移动
		toolBar.setFloatable(false); 
		//设置此工具栏的 rollover 状态。如果 rollover 状态为 true，
		//则仅当鼠标指针悬停在工具栏按钮上时，才绘制该工具栏按钮的边框。此属性的默认值为 false。
		toolBar.setRollover(true);
		
		addAlbumButton.setFocusable(false); //设置默认焦点状态
		//设置文本相对于图标的水平位置
		addAlbumButton.setHorizontalTextPosition(SwingConstants.CENTER);
		//设置文本相对于图标的垂直位置
		addAlbumButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		//为addAlbumButton添加事件监听器
		addAlbumButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				addAlbumButtonActionPerformed(e);
			}
		});
		toolBar.add(addAlbumButton);	//将按钮添加到工具栏
		
		updAlbumButton.setFocusable(false);
		updAlbumButton.setHorizontalTextPosition(SwingConstants.CENTER);
		updAlbumButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		updAlbumButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					updAlbumButtonActionPerformed(e);
				} catch (UnsupportedEncodingException ev) {
					ev.printStackTrace();
				}
			}	
		});
		toolBar.add(updAlbumButton);
		
		delAlbumButton.setFocusable(false);
		delAlbumButton.setHorizontalTextPosition(SwingConstants.CENTER);
		delAlbumButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		delAlbumButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				delAlbumButtonActionPerformed(e);
			}
		});
		toolBar.add(delAlbumButton);
		
		cancelSelectedButton.setFocusable(false);
		cancelSelectedButton.setHorizontalTextPosition(SwingConstants.CENTER);
		cancelSelectedButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		cancelSelectedButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cancelSelectedButtonActionPerformed(e);
			}
		});
		toolBar.add(cancelSelectedButton);
		toolBar.add(jSeparator1);
		
		addPhotoButton.setFocusable(false);
		addPhotoButton.setHorizontalTextPosition(SwingConstants.CENTER);
		addPhotoButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		addPhotoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					addPhotoButtonActionPerformed(e);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		toolBar.add(addPhotoButton);
		
		updPhotoButton.setFocusable(false);
		updPhotoButton.setHorizontalTextPosition(SwingConstants.CENTER);
		updPhotoButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		updPhotoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updPhotoButtonActionPerformed(e);
			}
		});
		toolBar.add(updPhotoButton);
		
		delPhotoButton.setFocusable(false);
		delPhotoButton.setHorizontalTextPosition(SwingConstants.CENTER);
		delPhotoButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		delPhotoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delPhotoButtonActionPerformed(e);
			}
		});
		toolBar.add(delPhotoButton);
		
		findPhotoButton.setFocusable(false);
		findPhotoButton.setHorizontalTextPosition(SwingConstants.CENTER);
		findPhotoButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		findPhotoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				findPhotoButtonActionPerformed(e);
			}
		});
		toolBar.add(findPhotoButton);

		saveButton.setFocusable(false);
		saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
		saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButtonActionPerformed(e);
			}
		});
		toolBar.add(saveButton);
		toolBar.add(jSeparator2);
		
		seeModeComboBox.setModel(new DefaultComboBoxModel(
				new String[] {"缩略图", "幻灯片", "播放器" }));
		//Dimension 类封装单个对象中组件的宽度和高度（精确到整数）。
		seeModeComboBox.setMaximumSize(new Dimension(70, 30));
		seeModeComboBox.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				seeModeComboBoxItemStateChanged(e);	
			}
		});
		toolBar.add(seeModeComboBox);
		
		add(toolBar, BorderLayout.PAGE_START);
		
		//设置分隔条的位置。
		splitPane.setDividerLocation(190);
		
		//设置背景色
		albumPanel.setBackground(new Color(255, 255, 255));
		
		//设置组件边框
		albumScrollPane.setBorder(null);
		//确定水平滚动条何时显示在滚动窗格上。
		albumScrollPane
			.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//设置树状控件的根节点是否可见
		albumTree.setRootVisible(false);
		//当展开或折叠（“负展开”）树节点时，将要通知的 TreeExpansionListener
		albumTree.addTreeExpansionListener(new TreeExpansionListener() {
			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				// TODO Auto-generated method stub
				albumTreeTreeExpanded(event);
			}
			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				// TODO Auto-generated method stub
			//	albumTreeTreeExpanded(event);	
			}
		});
		// 当选中或取消选中（“负选择”）节点时，将要通知的 TreeSelectionListener
		albumTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				try {
					albumTreeValueChanged(e);
				} catch (UnsupportedEncodingException evt) {
					evt.printStackTrace();
				}
			}
		});
		//创建一个视口（如果有必要）并设置其视图。
		albumScrollPane.setViewportView(albumTree);
		
		//设置分隔符左边控件
		splitPane.setLeftComponent(albumPanel);
		
		GroupLayout albumPanelLayout = new GroupLayout(albumPanel);
		//设置沿水平轴确定组件位置和大小的 Group
		albumPanelLayout.setHorizontalGroup(albumPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
						albumPanelLayout.createSequentialGroup()
						.addContainerGap().addComponent(
								albumScrollPane,
								GroupLayout.DEFAULT_SIZE, 22,
								Short.MAX_VALUE).addContainerGap()));
		//设置沿垂直轴确定组件位置和大小的 Group
		albumPanelLayout.setVerticalGroup(albumPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
						albumPanelLayout.createSequentialGroup().addContainerGap()
						.addComponent(albumScrollPane, GroupLayout.DEFAULT_SIZE, 
								7, Short.MAX_VALUE).addContainerGap()));
		albumPanel.setLayout(albumPanelLayout);
		
		photoPanel.setLayout(new BorderLayout());
		splitPane.setRightComponent(photoPanel);
		
		add(splitPane, BorderLayout.CENTER);
	}
	private void addAlbumButtonActionPerformed(ActionEvent e) {
		MTreeNode lastSelectedNode = (MTreeNode)albumTree
				.getLastSelectedPathComponent();	//获得被选中的相册节点
		int fatherId = (lastSelectedNode == null ? 0 : lastSelectedNode.getId());	//定义所属相册编号
		String albumName = getAlbumName(fatherId, "添加相册", "请输入相册名称：");//获得相册名称
		if (albumName != null) {	//输入了相册名称
			int id = dao.insertAlbum(fatherId, albumName);	//保存到数据库
			if (lastSelectedNode == null) {	//添加顶级相册
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
						.getRoot();// 获得相册树的根节点
				root.add(new MTreeNode(id, albumName));//添加相册节点
				treeModel.reload();	//刷新树模型
			}else {// 添加子相册
				TreePath selectionPath = albumTree.getSelectionPath();// 获得选中节点路径对象
				if (albumTree.isExpanded(selectionPath)) {// 已经展开
					lastSelectedNode.add(new MTreeNode(id, albumName));// 添加相册节点
					treeModel.reload(lastSelectedNode);// 刷新指定节点
				} else {// 尚未展开
					albumTree.expandPath(selectionPath);// 则展开该节点
				}
			}
		}
	}
	private void updAlbumButtonActionPerformed(ActionEvent e) throws UnsupportedEncodingException{
		MTreeNode lastSelectedNode = (MTreeNode) albumTree
				.getLastSelectedPathComponent();//获得被选中的相册节点
		if (lastSelectedNode == null) {//未选中任何相册
			JOptionPane.showMessageDialog(this, "请选择要修改的相册", "友情提示",
					JOptionPane.INFORMATION_MESSAGE);//提示选中要修改的相册
		} else {//存在选中的相册
			String albumName = getAlbumName(lastSelectedNode.getId(), "修改相册",
					"请输入相册“" + lastSelectedNode.getUserObject() + "”的新名称：");//获得相册名称
			if (albumName != null) {//输入了相册名称
				System.out.println(getSelectedPath());
				dao.updateAlbum(lastSelectedNode.getId(), albumName);//将修改同步到数据库
				File album = new File(getSelectedPath());
				album.renameTo(new File(getSelectedPath().replace(
						lastSelectedNode.getUserObject().toString(),
						albumName)));
				lastSelectedNode.setUserObject(albumName);	//修改树节点
				treeModel.reload(lastSelectedNode);//刷新树节点
			}
		}
	}
	private void delAlbumButtonActionPerformed(ActionEvent e) {
		MTreeNode lastSelectedNode = 
				(MTreeNode)albumTree.getLastSelectedPathComponent();	//获得被选中的相册节点
		if (lastSelectedNode == null) {	//未选中任何相册
			JOptionPane.showMessageDialog(this, "请选择要删除的相册！", "友情提示",
					JOptionPane.INFORMATION_MESSAGE);//提示选中要删除的相册
		} else {
			int i = JOptionPane.showConfirmDialog(this, "确定要删除相册“" + 
					lastSelectedNode.getUserObject() + "”？", "友情提示", JOptionPane.YES_NO_OPTION);
			//询问是否真的删除
			if (i == 0) {	//确定要删除
				dao.deleteAlbum(lastSelectedNode.getId());	//从数据库中删除
				//获得父节点
				DefaultMutableTreeNode parentNode = 
						(DefaultMutableTreeNode)lastSelectedNode.getParent();
				parentNode.remove(lastSelectedNode);	//移除该节点
				treeModel.reload(parentNode);
			}
		}
	}
	private void cancelSelectedButtonActionPerformed(ActionEvent e) {
		albumTree.clearSelection();
	}
	private void saveButtonActionPerformed(ActionEvent e) {
		Vector<PhotoPreviewButton> photos = PhotoPreviewButton.getSelectedPhoto();//获得被选中的照片
		int amount = photos.size();	//获得选中照片数量
		if (amount == 0) {
			JOptionPane.showMessageDialog(null, "请先选择要保存的照片！", "友情提示", 
					JOptionPane.INFORMATION_MESSAGE);
		} else {	//存在被选中的照片
			JFileChooser pathChooser = new JFileChooser();	//创建文件选择框对象
			//只允许选择文件夹
			pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int optionSign = pathChooser.showSaveDialog(this);	//弹出对话框并获得操作标记值
			if (optionSign == JFileChooser.APPROVE_OPTION) {	//如果选择了文件夹
				//获得选择的文件夹对象
				File selectedPath = pathChooser.getSelectedFile();
				PhotoPreviewButton lastPhoto = photos.lastElement();	//获得最后一个照片对象
				if (lastPhoto == photos.get(0)) {	//和第一个照片重复
					photos.remove(amount - 1);	//移除最后一个照片
				} else {
					if (lastPhoto == photos.get(amount - 2)) {	//和倒数第二个照片重复
						photos.remove(amount - 1);	//移除最后一个照片
					}
				}
				int num = 1;	//照片编号
				for (PhotoPreviewButton photo : photos) {	//遍历照片
					try {
						//创建文件输入流对象
						InputStream inStream = new FileInputStream(photo.getPath());
						String name = (num ++) + "、" + photo.getText() + photo.getName()
								.substring(23);	//定义照片名称
						OutputStream outStream = new FileOutputStream(
								selectedPath.getPath() + "/" + name); //创建文件输出流对象
						int readyBytes = 0;	//读取字节数
						byte[] buffer = new byte[10240];	//定义缓存数组
						//从输入流读取数据到缓存数组中
						while ((readyBytes = inStream.read(buffer, 0, 10240)) != -1) {
							outStream.write(buffer, 0, readyBytes);	//输出缓存数组中的数据到输出流
						}
						outStream.close();
						inStream.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
	private void addPhotoButtonActionPerformed(ActionEvent e) throws UnsupportedEncodingException {
		MTreeNode selectedNode = 
				(MTreeNode)albumTree.getLastSelectedPathComponent();	//获得选中的节点
		if (selectedNode == null) {	//未选中任何相册
			JOptionPane.showMessageDialog(this, "请选择要添加照片的相册！", "友情提示", 
					JOptionPane.INFORMATION_MESSAGE);	//弹出选择相册的提示
		} else {	//存在选中相册
			JFileChooser fileChooser = new JFileChooser();	//创建文件选择器对象
			//设置选择模式
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.setFileFilter(swingFileFilter);	//设置文件过滤器
			int sign = fileChooser.showOpenDialog(this);
			if (sign == JFileChooser.APPROVE_OPTION) {	//选择了文件
				photoInfo[1] = selectedNode.getId();
				final String upLoadPath = getSelectedPath();
				File selectedFile = fileChooser.getSelectedFile();	//获得选择的文件对象
				if (selectedFile.isDirectory()) {	//如果选择的是文件夹
					//获得满足条件的图片文件
					final File[] files = selectedFile.listFiles(ioFileFilter);
					new Thread() {					//创建并开启一个线程
						public void run() {
							for (int i = 0; i < files.length; i ++) {	//遍历图片文件数组
								addPhoto(files[i], upLoadPath);			//添加照片
								try {
									Thread.sleep(300);					//休眠300毫秒
								} catch (InterruptedException ev) {
									ev.printStackTrace();
								}
							}
						}
					}.start();
				} else {
					addPhoto(selectedFile, upLoadPath);
				}
			}
		}
	}
	private void addPhoto(File selectedFile, String upLoadPath) {
		//上传图片并获得上传后的名称
		String photoName = upLoadPhoto(selectedFile, upLoadPath);
		photoInfo[0] = photoName;							//设置图片名称
		photoInfo[2] = photoName.substring(0, 10);			//设置拍摄日期
		dao.insertPhoto(photoInfo);							//保存信息到数据库
		MPanel panel = (MPanel)photoPanel.getComponent(0);	//获得浏览方式面板
		JPanel photoBox = panel.getPhotoBoxPanel();			//获得照片箱面板
		//添加缩略图
		photoBox.add(new PhotoPreviewButton(new File(upLoadPath + "/" + photoName)));
		panel.validate();									//刷新浏览方式面板
	}
	private String upLoadPhoto(File selectedPhoto, String upLoadPath) {
		//获得图片名称
		String selectedPhotoName = selectedPhoto.getName().toLowerCase();
		//获得照片格式
		String photoType = selectedPhotoName.substring(selectedPhotoName.lastIndexOf("."));
		String upLoadPhotoName = getPhotoName() + photoType;	//定义上传后的图片名称
		upLoadPath += "/" + upLoadPhotoName;	//定义上传路径
		File upLoadPhoto = new File(upLoadPath);//创建文件对象
		if (!upLoadPhoto.getParentFile().exists()) {	//如果上传路径不存在
			upLoadPhoto.getParentFile().mkdirs();		//创建该路径
		}
		try {
			upLoadPhoto.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			//创建文件输入流对象
			InputStream inStream = new FileInputStream(selectedPhoto);
			//创建文件输出流对象
			OutputStream outStream = new FileOutputStream(upLoadPhoto);
			int readBytes = 0;		//读取字节数
			byte[] buffer = new byte[10240];	//定义缓存数组
			//从输入流读取数组到缓存数组中
			while ((readBytes = inStream.read(buffer, 0, 10240)) != -1) {
				outStream.write(buffer, 0, readBytes);	//输出缓存数组中的数据到输入流
			}
			outStream.close();
			inStream.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return upLoadPhotoName;
	}
	private String getPhotoName() {
		Date currentDate = new Date();	//创建日期对象
		String date = String.format("%tF", currentDate);
		String time = String.format("%tT", currentDate);
		String milliSecond = String.format("%tL", currentDate);
		return date + " " + time.replace(':', '-') + " " + milliSecond;
	}
	private void updPhotoButtonActionPerformed(ActionEvent e) {
		//获得选中的照片
		Vector<PhotoPreviewButton> selectedPhoto = PhotoPreviewButton.getSelectedPhoto();
		if (selectedPhoto.size() != 1) {
			String message = selectedPhoto.isEmpty() ? 
					"请选择要修改的图片！" : "每次只能修改一张图片！";	//定义提示信息
			JOptionPane.showMessageDialog(this, message, "友情提示", 
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			new UpdatePhotoInfoDialog(null, true, selectedPhoto.firstElement())
				.setVisible(true);
		}
	}
	private void delPhotoButtonActionPerformed(ActionEvent e) {
		Vector<PhotoPreviewButton> selectedPhoto = 
				PhotoPreviewButton.getSelectedPhoto();	//获得被选中的照片
		if (selectedPhoto.isEmpty()) {
			JOptionPane.showMessageDialog(this, "请选择要删除的照片！", "友情提示",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			int i = JOptionPane.showConfirmDialog(this, "确定要删除这些照片？", "友情提示",
					JOptionPane.YES_NO_OPTION);
			if (i == 0) {
				MPanel panel = (MPanel)photoPanel.getComponent(0);
				JPanel photoBox = panel.getPhotoBoxPanel();
				for (int j = 0; j < selectedPhoto.size(); j ++) {
					PhotoPreviewButton button = selectedPhoto.get(j);
					photoBox.remove(button);
					dao.deletePhoto(button.getName());
					new File(button.getPath()).delete();
				}
				photoPanel.validate();
				selectedPhoto.clear();
			}
		}
	}
	private void findPhotoButtonActionPerformed (ActionEvent e) {
		MTreeNode selectedNode = (MTreeNode)albumTree.getLastSelectedPathComponent();
		new FindPhotoInfoDialog(null, true, (selectedNode == null ? 0 
				: selectedNode.getId())
		).setVisible(true);
	}
	private void seeModeComboBoxItemStateChanged(java.awt.event.ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			int selectedIndex = seeModeComboBox.getSelectedIndex();
			switch (selectedIndex) {
			case 0:
				photoPanel.remove(0);
				photoPanel.add(new BreviaryPhotoPanel());
				photoPanel.validate();
				break;
			case 1:
				photoPanel.remove(0);
				LanternSlidePanel panel = new LanternSlidePanel();
				if (panel.getPhotoBoxPanel().getComponentCount() > 0) {
					PhotoPreviewButton lanternSlide = null;
					if (PhotoPreviewButton.getSelectedPhoto().size() > 0) {
						lanternSlide = PhotoPreviewButton.getSelectedPhoto()
								.lastElement();
					} else {
						lanternSlide = (PhotoPreviewButton)panel.getPhotoBoxPanel()
								.getComponent(0);
					}
					panel.getShowPhotoLabel().setIcon(
							new ImageIcon(lanternSlide.getPath()));
				}
				photoPanel.add(panel);
				photoPanel.validate();
				break;
			default:
				MPanel showPanel = (MPanel)photoPanel.getComponent(0);
				Component[] photos = showPanel.getPhotoBoxPanel().getComponents();
				if (photos.length > 0) {
					new PlayDialog(null, true, photos).setVisible(true);
				}
				seeModeComboBox.removeItemListener(showModeComboBoxIL);
				seeModeComboBox.setSelectedItem(primaryItem);
				seeModeComboBox.addItemListener(showModeComboBoxIL);
			}
		} else {
			primaryItem = e.getItem();
		}
	}
	private void albumTreeTreeExpanded(TreeExpansionEvent evt) {
		TreePath selectedPath = evt.getPath();	//获得选中节点的路径对象
		MTreeNode lastNode = (MTreeNode)selectedPath.getLastPathComponent(); //获得选中节点对象
		if (!lastNode.isLoad()) {	//如果该节点尚未加载
			loadChildNode(lastNode);	//加载该节点
			treeModel.reload(lastNode);	//刷新树模型
		}
	}
	private void albumTreeValueChanged(TreeSelectionEvent evt) throws UnsupportedEncodingException {
		if (loadPhotoThread != null && loadPhotoThread.isAlive()) {	//存在加载图片的线程
			synchronized (loadPhotoThread) {	//将其加入到同步块中
				loadPhotoThread.interrupt();	//中断线程
			}
		}
		final MPanel showPanel = (MPanel)photoPanel.getComponent(0);	//获得浏览方式面板
		final MLabel photoLabel = showPanel.getShowPhotoLabel();	//获得幻灯片标签
		if (photoLabel.getIcon() != null) {	//幻灯片方式
			photoLabel.setIcon(null);	//	清空幻灯片
		}
		final JPanel photoBoxPanel = showPanel.getPhotoBoxPanel();	//获得图片箱面板
		if (photoBoxPanel.getComponentCount() > 0) {
			photoBoxPanel.removeAll();	//清空图片箱
		}
		String selectedPath = getSelectedPath();	//获得选中相册的路径
		if (selectedPath == null) {	//如果路径为空
			showPanel.validate();	//刷新面板
		} else {	//如果路径不为空
			final File[] photos = new File(selectedPath)
					.listFiles(ioFileFilter);	//获得所有照片对象
			if (photos != null && photos.length > 0) {	//如果有照片
				photoBoxPanel.add(new PhotoPreviewButton(photos[0]));	//添加一个照片到图片箱
				if (photoLabel != null) {
					photoLabel.setIcon(new ImageIcon(photos[0].getPath()));	//为幻灯片设置照片
				}
				showPanel.validate();
				loadPhotoThread = new Thread() {	//创建一个用来加载照片到图片箱的线程
					public void run() {
						for (int i = 1; i < photos.length; i ++) {	//遍历照片数组
							try {
								Thread.sleep(600);
							} catch (InterruptedException e) {
								break;
							}
							photoBoxPanel
									.add(new PhotoPreviewButton(photos[i]));	//添加指定照片到图片箱
							showPanel.validate();
						}
					}
				};
				loadPhotoThread.start();
			} else {	//如果没有照片
				showPanel.validate();
			}
		}
	}
	public AlbumPanel() {
		initComponents();
		initAlbumTree();
		photoPanel.add(new BreviaryPhotoPanel());
	}

}
