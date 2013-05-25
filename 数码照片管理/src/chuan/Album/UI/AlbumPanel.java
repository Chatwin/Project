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
	//�������
	private JButton addAlbumButton;
	private JButton addPhotoButton;
	private JPanel albumPanel;
	private JScrollPane albumScrollPane;
	private static JTree albumTree;	//��״�ؼ�
	private JButton cancelSelectedButton;
	private JButton delAlbumButton;
	private JButton delPhotoButton;
	private JButton findPhotoButton;
	private JToolBar.Separator jSeparator1;	//�������ϵķָ���
	private JToolBar.Separator jSeparator2;
	private static JPanel photoPanel;
	private JButton saveButton;
	private JComboBox<?> seeModeComboBox;		//�����б��
	private JSplitPane splitPane;			//�ָ������ؼ�
	private JToolBar toolBar;				//������
	private JButton updAlbumButton;
	private JButton updPhotoButton;
	private static final Dao dao = Dao.getInstance();
	private DefaultTreeModel treeModel;
	private Object primaryItem;
	private Thread loadPhotoThread;
	//�����������
	Object[] photoInfo = { "", 0, "", "�����⡵", "��������" };
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
		String name = null;	//�������
		while (name == null) {
			name = JOptionPane.showInputDialog(this, message, title, 
					JOptionPane.INFORMATION_MESSAGE);	//����������
			if (name == null) {	//ȡ��
				break;
			} else {	//ȷ��
				name = name.trim();	//ȥ����β�ո�
				if (name.length() == 0) {	//δ����
					JOptionPane.showMessageDialog(this, "������������ƣ�", "������ʾ",
							JOptionPane.INFORMATION_MESSAGE);// ������ʾ�����

					name = null;
				} else {	//������
					Object node = dao
							.selectOnlyValue("select id from tb_album where father_id="
									+ fatherId + " and name='" + name + "'");// ����������в�ѯͬ�����)
					if (node != null) {//�Ѿ�����
						JOptionPane.showMessageDialog(this, "����������Ѿ����ڣ�",
								"������ʾ", JOptionPane.INFORMATION_MESSAGE);// ��������ͬ�������ʾ

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
			return "ͼ���ļ���.JPG;.JPEG��";
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
		addAlbumButton.setText("���");
		//ע��Ҫ�ڹ�����ʾ����ʾ���ı�����괦�ڸ������ʱ��ʾ���ı�
		addAlbumButton.setToolTipText("������");
		addAlbumButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/addAlbum.png")));
		updAlbumButton = new JButton();
		updAlbumButton.setText("�޸�");
		updAlbumButton.setToolTipText("�޸����");
		updAlbumButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/updAlbum.png")));
		delAlbumButton = new JButton();
		delAlbumButton.setText("ɾ��");
		delAlbumButton.setToolTipText("ɾ�����");
		delAlbumButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/delAlbum.png")));
		cancelSelectedButton = new JButton();
		cancelSelectedButton.setText("ȡ��");
		cancelSelectedButton.setToolTipText("ȡ������ѡ��״̬");
		cancelSelectedButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/cancelAlbum.png")));
		jSeparator1 = new JToolBar.Separator();
		addPhotoButton = new JButton();
		addPhotoButton.setText("���");
		addPhotoButton.setToolTipText("�����Ƭ");
		addPhotoButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/addPhoto.png")));
		updPhotoButton = new JButton();
		updPhotoButton.setText("�޸�");
		updPhotoButton.setToolTipText("�޸���Ƭ��Ϣ");
		updPhotoButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/updPhoto.png")));
		delPhotoButton = new JButton();
		delPhotoButton.setText("ɾ��");
		delPhotoButton.setToolTipText("ɾ����Ƭ");
		delPhotoButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/delAlbum.png")));
		findPhotoButton = new JButton();
		findPhotoButton.setText("����");
		findPhotoButton.setToolTipText("������Ƭ");
		findPhotoButton.setIcon(new ImageIcon(
				AlbumPanel.class.getResource("/chuan/img/findPhoto.png")));
		saveButton = new JButton();
		saveButton.setText("����");
		saveButton.setToolTipText("������Ƭ���ƶ�λ��");
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
		
		//����float���ԣ���Ϊtrue����ƶ�����Ϊfalse�򹤾��������ƶ�
		toolBar.setFloatable(false); 
		//���ô˹������� rollover ״̬����� rollover ״̬Ϊ true��
		//��������ָ����ͣ�ڹ�������ť��ʱ���Ż��Ƹù�������ť�ı߿򡣴����Ե�Ĭ��ֵΪ false��
		toolBar.setRollover(true);
		
		addAlbumButton.setFocusable(false); //����Ĭ�Ͻ���״̬
		//�����ı������ͼ���ˮƽλ��
		addAlbumButton.setHorizontalTextPosition(SwingConstants.CENTER);
		//�����ı������ͼ��Ĵ�ֱλ��
		addAlbumButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		//ΪaddAlbumButton����¼�������
		addAlbumButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				addAlbumButtonActionPerformed(e);
			}
		});
		toolBar.add(addAlbumButton);	//����ť��ӵ�������
		
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
				new String[] {"����ͼ", "�õ�Ƭ", "������" }));
		//Dimension ���װ��������������Ŀ�Ⱥ͸߶ȣ���ȷ����������
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
		
		//���÷ָ�����λ�á�
		splitPane.setDividerLocation(190);
		
		//���ñ���ɫ
		albumPanel.setBackground(new Color(255, 255, 255));
		
		//��������߿�
		albumScrollPane.setBorder(null);
		//ȷ��ˮƽ��������ʱ��ʾ�ڹ��������ϡ�
		albumScrollPane
			.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//������״�ؼ��ĸ��ڵ��Ƿ�ɼ�
		albumTree.setRootVisible(false);
		//��չ�����۵�������չ���������ڵ�ʱ����Ҫ֪ͨ�� TreeExpansionListener
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
		// ��ѡ�л�ȡ��ѡ�У�����ѡ�񡱣��ڵ�ʱ����Ҫ֪ͨ�� TreeSelectionListener
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
		//����һ���ӿڣ�����б�Ҫ������������ͼ��
		albumScrollPane.setViewportView(albumTree);
		
		//���÷ָ�����߿ؼ�
		splitPane.setLeftComponent(albumPanel);
		
		GroupLayout albumPanelLayout = new GroupLayout(albumPanel);
		//������ˮƽ��ȷ�����λ�úʹ�С�� Group
		albumPanelLayout.setHorizontalGroup(albumPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
						albumPanelLayout.createSequentialGroup()
						.addContainerGap().addComponent(
								albumScrollPane,
								GroupLayout.DEFAULT_SIZE, 22,
								Short.MAX_VALUE).addContainerGap()));
		//�����ش�ֱ��ȷ�����λ�úʹ�С�� Group
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
				.getLastSelectedPathComponent();	//��ñ�ѡ�е����ڵ�
		int fatherId = (lastSelectedNode == null ? 0 : lastSelectedNode.getId());	//�������������
		String albumName = getAlbumName(fatherId, "������", "������������ƣ�");//����������
		if (albumName != null) {	//�������������
			int id = dao.insertAlbum(fatherId, albumName);	//���浽���ݿ�
			if (lastSelectedNode == null) {	//��Ӷ������
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
						.getRoot();// ���������ĸ��ڵ�
				root.add(new MTreeNode(id, albumName));//������ڵ�
				treeModel.reload();	//ˢ����ģ��
			}else {// ��������
				TreePath selectionPath = albumTree.getSelectionPath();// ���ѡ�нڵ�·������
				if (albumTree.isExpanded(selectionPath)) {// �Ѿ�չ��
					lastSelectedNode.add(new MTreeNode(id, albumName));// ������ڵ�
					treeModel.reload(lastSelectedNode);// ˢ��ָ���ڵ�
				} else {// ��δչ��
					albumTree.expandPath(selectionPath);// ��չ���ýڵ�
				}
			}
		}
	}
	private void updAlbumButtonActionPerformed(ActionEvent e) throws UnsupportedEncodingException{
		MTreeNode lastSelectedNode = (MTreeNode) albumTree
				.getLastSelectedPathComponent();//��ñ�ѡ�е����ڵ�
		if (lastSelectedNode == null) {//δѡ���κ����
			JOptionPane.showMessageDialog(this, "��ѡ��Ҫ�޸ĵ����", "������ʾ",
					JOptionPane.INFORMATION_MESSAGE);//��ʾѡ��Ҫ�޸ĵ����
		} else {//����ѡ�е����
			String albumName = getAlbumName(lastSelectedNode.getId(), "�޸����",
					"��������ᡰ" + lastSelectedNode.getUserObject() + "���������ƣ�");//����������
			if (albumName != null) {//�������������
				System.out.println(getSelectedPath());
				dao.updateAlbum(lastSelectedNode.getId(), albumName);//���޸�ͬ�������ݿ�
				File album = new File(getSelectedPath());
				album.renameTo(new File(getSelectedPath().replace(
						lastSelectedNode.getUserObject().toString(),
						albumName)));
				lastSelectedNode.setUserObject(albumName);	//�޸����ڵ�
				treeModel.reload(lastSelectedNode);//ˢ�����ڵ�
			}
		}
	}
	private void delAlbumButtonActionPerformed(ActionEvent e) {
		MTreeNode lastSelectedNode = 
				(MTreeNode)albumTree.getLastSelectedPathComponent();	//��ñ�ѡ�е����ڵ�
		if (lastSelectedNode == null) {	//δѡ���κ����
			JOptionPane.showMessageDialog(this, "��ѡ��Ҫɾ������ᣡ", "������ʾ",
					JOptionPane.INFORMATION_MESSAGE);//��ʾѡ��Ҫɾ�������
		} else {
			int i = JOptionPane.showConfirmDialog(this, "ȷ��Ҫɾ����ᡰ" + 
					lastSelectedNode.getUserObject() + "����", "������ʾ", JOptionPane.YES_NO_OPTION);
			//ѯ���Ƿ����ɾ��
			if (i == 0) {	//ȷ��Ҫɾ��
				dao.deleteAlbum(lastSelectedNode.getId());	//�����ݿ���ɾ��
				//��ø��ڵ�
				DefaultMutableTreeNode parentNode = 
						(DefaultMutableTreeNode)lastSelectedNode.getParent();
				parentNode.remove(lastSelectedNode);	//�Ƴ��ýڵ�
				treeModel.reload(parentNode);
			}
		}
	}
	private void cancelSelectedButtonActionPerformed(ActionEvent e) {
		albumTree.clearSelection();
	}
	private void saveButtonActionPerformed(ActionEvent e) {
		Vector<PhotoPreviewButton> photos = PhotoPreviewButton.getSelectedPhoto();//��ñ�ѡ�е���Ƭ
		int amount = photos.size();	//���ѡ����Ƭ����
		if (amount == 0) {
			JOptionPane.showMessageDialog(null, "����ѡ��Ҫ�������Ƭ��", "������ʾ", 
					JOptionPane.INFORMATION_MESSAGE);
		} else {	//���ڱ�ѡ�е���Ƭ
			JFileChooser pathChooser = new JFileChooser();	//�����ļ�ѡ������
			//ֻ����ѡ���ļ���
			pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int optionSign = pathChooser.showSaveDialog(this);	//�����Ի��򲢻�ò������ֵ
			if (optionSign == JFileChooser.APPROVE_OPTION) {	//���ѡ�����ļ���
				//���ѡ����ļ��ж���
				File selectedPath = pathChooser.getSelectedFile();
				PhotoPreviewButton lastPhoto = photos.lastElement();	//������һ����Ƭ����
				if (lastPhoto == photos.get(0)) {	//�͵�һ����Ƭ�ظ�
					photos.remove(amount - 1);	//�Ƴ����һ����Ƭ
				} else {
					if (lastPhoto == photos.get(amount - 2)) {	//�͵����ڶ�����Ƭ�ظ�
						photos.remove(amount - 1);	//�Ƴ����һ����Ƭ
					}
				}
				int num = 1;	//��Ƭ���
				for (PhotoPreviewButton photo : photos) {	//������Ƭ
					try {
						//�����ļ�����������
						InputStream inStream = new FileInputStream(photo.getPath());
						String name = (num ++) + "��" + photo.getText() + photo.getName()
								.substring(23);	//������Ƭ����
						OutputStream outStream = new FileOutputStream(
								selectedPath.getPath() + "/" + name); //�����ļ����������
						int readyBytes = 0;	//��ȡ�ֽ���
						byte[] buffer = new byte[10240];	//���建������
						//����������ȡ���ݵ�����������
						while ((readyBytes = inStream.read(buffer, 0, 10240)) != -1) {
							outStream.write(buffer, 0, readyBytes);	//������������е����ݵ������
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
				(MTreeNode)albumTree.getLastSelectedPathComponent();	//���ѡ�еĽڵ�
		if (selectedNode == null) {	//δѡ���κ����
			JOptionPane.showMessageDialog(this, "��ѡ��Ҫ�����Ƭ����ᣡ", "������ʾ", 
					JOptionPane.INFORMATION_MESSAGE);	//����ѡ��������ʾ
		} else {	//����ѡ�����
			JFileChooser fileChooser = new JFileChooser();	//�����ļ�ѡ��������
			//����ѡ��ģʽ
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.setFileFilter(swingFileFilter);	//�����ļ�������
			int sign = fileChooser.showOpenDialog(this);
			if (sign == JFileChooser.APPROVE_OPTION) {	//ѡ�����ļ�
				photoInfo[1] = selectedNode.getId();
				final String upLoadPath = getSelectedPath();
				File selectedFile = fileChooser.getSelectedFile();	//���ѡ����ļ�����
				if (selectedFile.isDirectory()) {	//���ѡ������ļ���
					//�������������ͼƬ�ļ�
					final File[] files = selectedFile.listFiles(ioFileFilter);
					new Thread() {					//����������һ���߳�
						public void run() {
							for (int i = 0; i < files.length; i ++) {	//����ͼƬ�ļ�����
								addPhoto(files[i], upLoadPath);			//�����Ƭ
								try {
									Thread.sleep(300);					//����300����
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
		//�ϴ�ͼƬ������ϴ��������
		String photoName = upLoadPhoto(selectedFile, upLoadPath);
		photoInfo[0] = photoName;							//����ͼƬ����
		photoInfo[2] = photoName.substring(0, 10);			//������������
		dao.insertPhoto(photoInfo);							//������Ϣ�����ݿ�
		MPanel panel = (MPanel)photoPanel.getComponent(0);	//��������ʽ���
		JPanel photoBox = panel.getPhotoBoxPanel();			//�����Ƭ�����
		//�������ͼ
		photoBox.add(new PhotoPreviewButton(new File(upLoadPath + "/" + photoName)));
		panel.validate();									//ˢ�������ʽ���
	}
	private String upLoadPhoto(File selectedPhoto, String upLoadPath) {
		//���ͼƬ����
		String selectedPhotoName = selectedPhoto.getName().toLowerCase();
		//�����Ƭ��ʽ
		String photoType = selectedPhotoName.substring(selectedPhotoName.lastIndexOf("."));
		String upLoadPhotoName = getPhotoName() + photoType;	//�����ϴ����ͼƬ����
		upLoadPath += "/" + upLoadPhotoName;	//�����ϴ�·��
		File upLoadPhoto = new File(upLoadPath);//�����ļ�����
		if (!upLoadPhoto.getParentFile().exists()) {	//����ϴ�·��������
			upLoadPhoto.getParentFile().mkdirs();		//������·��
		}
		try {
			upLoadPhoto.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			//�����ļ�����������
			InputStream inStream = new FileInputStream(selectedPhoto);
			//�����ļ����������
			OutputStream outStream = new FileOutputStream(upLoadPhoto);
			int readBytes = 0;		//��ȡ�ֽ���
			byte[] buffer = new byte[10240];	//���建������
			//����������ȡ���鵽����������
			while ((readBytes = inStream.read(buffer, 0, 10240)) != -1) {
				outStream.write(buffer, 0, readBytes);	//������������е����ݵ�������
			}
			outStream.close();
			inStream.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return upLoadPhotoName;
	}
	private String getPhotoName() {
		Date currentDate = new Date();	//�������ڶ���
		String date = String.format("%tF", currentDate);
		String time = String.format("%tT", currentDate);
		String milliSecond = String.format("%tL", currentDate);
		return date + " " + time.replace(':', '-') + " " + milliSecond;
	}
	private void updPhotoButtonActionPerformed(ActionEvent e) {
		//���ѡ�е���Ƭ
		Vector<PhotoPreviewButton> selectedPhoto = PhotoPreviewButton.getSelectedPhoto();
		if (selectedPhoto.size() != 1) {
			String message = selectedPhoto.isEmpty() ? 
					"��ѡ��Ҫ�޸ĵ�ͼƬ��" : "ÿ��ֻ���޸�һ��ͼƬ��";	//������ʾ��Ϣ
			JOptionPane.showMessageDialog(this, message, "������ʾ", 
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			new UpdatePhotoInfoDialog(null, true, selectedPhoto.firstElement())
				.setVisible(true);
		}
	}
	private void delPhotoButtonActionPerformed(ActionEvent e) {
		Vector<PhotoPreviewButton> selectedPhoto = 
				PhotoPreviewButton.getSelectedPhoto();	//��ñ�ѡ�е���Ƭ
		if (selectedPhoto.isEmpty()) {
			JOptionPane.showMessageDialog(this, "��ѡ��Ҫɾ������Ƭ��", "������ʾ",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			int i = JOptionPane.showConfirmDialog(this, "ȷ��Ҫɾ����Щ��Ƭ��", "������ʾ",
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
		TreePath selectedPath = evt.getPath();	//���ѡ�нڵ��·������
		MTreeNode lastNode = (MTreeNode)selectedPath.getLastPathComponent(); //���ѡ�нڵ����
		if (!lastNode.isLoad()) {	//����ýڵ���δ����
			loadChildNode(lastNode);	//���ظýڵ�
			treeModel.reload(lastNode);	//ˢ����ģ��
		}
	}
	private void albumTreeValueChanged(TreeSelectionEvent evt) throws UnsupportedEncodingException {
		if (loadPhotoThread != null && loadPhotoThread.isAlive()) {	//���ڼ���ͼƬ���߳�
			synchronized (loadPhotoThread) {	//������뵽ͬ������
				loadPhotoThread.interrupt();	//�ж��߳�
			}
		}
		final MPanel showPanel = (MPanel)photoPanel.getComponent(0);	//��������ʽ���
		final MLabel photoLabel = showPanel.getShowPhotoLabel();	//��ûõ�Ƭ��ǩ
		if (photoLabel.getIcon() != null) {	//�õ�Ƭ��ʽ
			photoLabel.setIcon(null);	//	��ջõ�Ƭ
		}
		final JPanel photoBoxPanel = showPanel.getPhotoBoxPanel();	//���ͼƬ�����
		if (photoBoxPanel.getComponentCount() > 0) {
			photoBoxPanel.removeAll();	//���ͼƬ��
		}
		String selectedPath = getSelectedPath();	//���ѡ������·��
		if (selectedPath == null) {	//���·��Ϊ��
			showPanel.validate();	//ˢ�����
		} else {	//���·����Ϊ��
			final File[] photos = new File(selectedPath)
					.listFiles(ioFileFilter);	//���������Ƭ����
			if (photos != null && photos.length > 0) {	//�������Ƭ
				photoBoxPanel.add(new PhotoPreviewButton(photos[0]));	//���һ����Ƭ��ͼƬ��
				if (photoLabel != null) {
					photoLabel.setIcon(new ImageIcon(photos[0].getPath()));	//Ϊ�õ�Ƭ������Ƭ
				}
				showPanel.validate();
				loadPhotoThread = new Thread() {	//����һ������������Ƭ��ͼƬ����߳�
					public void run() {
						for (int i = 1; i < photos.length; i ++) {	//������Ƭ����
							try {
								Thread.sleep(600);
							} catch (InterruptedException e) {
								break;
							}
							photoBoxPanel
									.add(new PhotoPreviewButton(photos[i]));	//���ָ����Ƭ��ͼƬ��
							showPanel.validate();
						}
					}
				};
				loadPhotoThread.start();
			} else {	//���û����Ƭ
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
