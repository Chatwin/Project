package chuan.Album.frame;

import chuan.Album.UI.*;
import chuan.Album.calendar.CalendarPanel;
import chuan.Album.dao.*;
import chuan.Album.mwing.*;
import chuan.Album.tool.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public class FindPhotoInfoDialog extends JDialog{
	private chuan.Album.calendar.CalendarPanel appointCalendarPanel;
	private JPanel appointPanel;
	private JRadioButton appointRadioButton;
	private JComboBox<?> comparisonComboBox;
	private ButtonGroup dateButtonGroup;
	private JLabel dateLabel;
	private chuan.Album.calendar.CalendarPanel endCalendarPanel;
	private JButton exitButton;
	private JPanel passagePanel;
	private JRadioButton passageRadioButton;
	private JLabel remarkLabel;
	private JScrollPane remarkScrollPane;
	private JTextArea remarkTextArea;
	private chuan.Album.calendar.CalendarPanel startCalendarPanel;
	private JButton submitButton;
	private JLabel titleLabel;
	private JTextField titleTextField;
	private JLabel toLabel;
	//End of variables declaration
	
	private static final Dao dao = Dao.getInstance();
    private final JTextField appointTextField;
    private final JTextField startTextField;
    private final JTextField endTextField;
    private final int albumId;
    public FindPhotoInfoDialog(java.awt.Frame parent, boolean modal, int albumId) {
        super(parent, modal);
        initComponents();
        this.albumId = albumId;
        appointTextField = appointCalendarPanel.getTextField();
        startTextField = startCalendarPanel.getTextField();
        endTextField = endCalendarPanel.getTextField();
        dateRadioButtonActionPerformed(null);
        ScreenSize.centered(this);
    }

    private void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initComponents() {
		dateButtonGroup = new ButtonGroup();
		titleLabel = new JLabel();
		titleTextField = new JTextField();
		dateLabel = new JLabel();
		appointRadioButton = new JRadioButton();
		appointPanel = new JPanel();
		appointCalendarPanel = new chuan.Album.calendar.CalendarPanel();
		comparisonComboBox = new JComboBox<Object>();
		passageRadioButton = new JRadioButton();
		passagePanel = new JPanel();
		startCalendarPanel = new chuan.Album.calendar.CalendarPanel();
		toLabel = new JLabel();
		endCalendarPanel = new chuan.Album.calendar.CalendarPanel();
		remarkLabel = new JLabel();
		remarkScrollPane = new JScrollPane();
		remarkTextArea = new JTextArea();
		submitButton = new JButton();
		exitButton = new JButton();
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("查找照片");
		titleLabel.setText("图片标题：");
		titleTextField.setColumns(20);
		dateLabel.setText("拍摄日期：");
		dateButtonGroup.add(appointRadioButton);
		appointRadioButton.setSelected(true);
		appointRadioButton.setText("指定日期");
		appointRadioButton.setFocusPainted(false);
		appointRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dateRadioButtonActionPerformed(e);
			}	
		});
		comparisonComboBox.setModel(
				 new javax.swing.DefaultComboBoxModel(new String[] {"等于","大于","小于"}));
		GroupLayout appointPanelLayout = new GroupLayout(appointPanel);
		appointPanel.setLayout(appointPanelLayout);
		appointPanelLayout.setHorizontalGroup(
				appointPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(appointPanelLayout.createSequentialGroup()
	                .addComponent(appointCalendarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addGap(33, 33, 33)
	                .addComponent(comparisonComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
		);
		appointPanelLayout.setVerticalGroup(
	            appointPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addComponent(appointCalendarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	            .addComponent(comparisonComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	    );
		dateButtonGroup.add(passageRadioButton);
		passageRadioButton.setText("一段时间");
		passageRadioButton.setFocusPainted(false);
		passageRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dateRadioButtonActionPerformed(e);
			}
		});
		toLabel.setText("---");
		javax.swing.GroupLayout passagePanelLayout = new javax.swing.GroupLayout(passagePanel);
        passagePanel.setLayout(passagePanelLayout);
        passagePanelLayout.setHorizontalGroup(
            passagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(passagePanelLayout.createSequentialGroup()
                .addComponent(startCalendarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(endCalendarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        passagePanelLayout.setVerticalGroup(
            passagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(passagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(endCalendarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(toLabel)
                .addComponent(startCalendarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        remarkLabel.setText("图片描述：");
        remarkTextArea.setColumns(40);
        remarkTextArea.setRows(5);
        remarkScrollPane.setViewportView(remarkTextArea);
        submitButton.setText("确定");
        submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				submitButtonActionPerformed(e);	
			}
        });
        exitButton.setText("退出");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(267, Short.MAX_VALUE)
                .addComponent(submitButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(exitButton)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(passageRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(passagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(remarkLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(remarkScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(appointRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(appointPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(titleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleLabel)
                    .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(dateLabel)
                        .addComponent(appointRadioButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(appointPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(passageRadioButton)
                    .addComponent(passagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(remarkLabel)
                    .addComponent(remarkScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitButton)
                    .addComponent(submitButton))
                .addContainerGap())
        );
        pack();
	}
	private void dateRadioButtonActionPerformed(ActionEvent e) {
		if (appointRadioButton.isSelected()) {
			appointCalendarPanel.setEnabled(true);
			comparisonComboBox.setEnabled(true);
			startCalendarPanel.setEnabled(false);
			toLabel.setEnabled(false);
			endCalendarPanel.setEnabled(false);
		} else {
			appointCalendarPanel.setEnabled(false);
	        comparisonComboBox.setEnabled(false);
	        startCalendarPanel.setEnabled(true);
	        toLabel.setEnabled(true);
	        endCalendarPanel.setEnabled(true);
		}
	}
	private void submitButtonActionPerformed( ActionEvent e) {
		String title = titleTextField.getText().trim();	//获得标题关键字
		String appointDate = appointTextField.getText();//
		String spaceStartDate = startTextField.getText();
		String spaceEndDate = endTextField.getText();
		String remark = remarkTextArea.getText().trim();
		if (title.length() == 0 && remark.length() == 0) {
			if (appointRadioButton.isSelected()) {
				if (appointDate.length() == 0) {
					showMessage("友情提示", "至少填写一项查询条件");
					return;
				}
			} else {
				if (spaceStartDate.length() == 0 || spaceEndDate.length() == 0) {
					if (spaceStartDate.length() == 0 && spaceEndDate.length() == 0) {
						showMessage("友情提示", "至少填写一项查询条件");
					} else {
						showMessage("友情提示", "请将起止日期填写完整");
					}
					return;
				}
			}
		}
		final Vector<Vector> photos = new Vector<Vector>();
		if (appointRadioButton.isSelected()) {
			char compare;
			switch (comparisonComboBox.getSelectedIndex()) {
				case 1:
					compare = '>';
					break;
				case 2:
					compare = '<';
					break;
				default :
					compare = '=';
			}
			photos.addAll(dao.selectPhoto(albumId, title, appointDate, compare, remark));
		} else {
			DateFormat dateFormat = DateFormat.getDateInstance();
			Date startDate = null;
			Date endDate = null;
			try {
				startDate = dateFormat.parse(spaceStartDate);
				endDate = dateFormat.parse(spaceEndDate);
			} catch(ParseException e1) {
				showMessage("友情提示", "请将起止日期填写完整");
				return;
			}
			if (endDate.before(startDate)) {
				showMessage("友情提示", "终止日期不能小于起始日期！");
				return;
			} else {
				photos.addAll(dao.selectPhoto(albumId, title, spaceStartDate, spaceEndDate, remark));
			}
		}
		//获得浏览方式面板
		final MPanel panel = (MPanel)AlbumPanel.getPhotoPanel().getComponent(0);
		final JPanel photoBoxPanel = panel.getPhotoBoxPanel();
		photoBoxPanel.removeAll();
		panel.validate();
		this.dispose();	//销毁查找图片对话框
		if (photos.size() == 0) {
			showMessage("友情提示", "没有符合要求的照片！");
		} else {
			new Thread() {
				public void run() {
					String filePath = null;
					try {
						filePath = FindPhotoInfoDialog.class.getResource("/chuan/img").toURI().getPath();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					for (Vector<?> photo : photos) {
						String albumPath = photo.get(0).toString();
						int albumId = (Integer) photo.get(1);
						while (true) {
							Vector<?> album = dao.selectAlbum(albumId);
							albumId = (Integer)album.get(1);
							albumPath = album.get(2) + "/" + albumPath;
							if (albumId == 0) {
								break;
							}
						}
						photoBoxPanel.add(new PhotoPreviewButton(new File(filePath + "/" + albumPath)));
						panel.validate();
						try {
							Thread.sleep(600);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			showMessage("查询完毕", "共有 " + photos.size() + " 张符合条件的照片！");
		}
	}
	private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
		// TODO add your handling code here:
		    this.dispose();
		} 
}
