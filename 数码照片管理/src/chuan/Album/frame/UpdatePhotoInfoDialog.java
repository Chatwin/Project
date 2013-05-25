package chuan.Album.frame;

import chuan.Album.dao.*;
import chuan.Album.tool.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.CaretEvent;
public class UpdatePhotoInfoDialog  extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private chuan.Album.calendar.CalendarPanel calendarPanel;
	private JLabel dateLabel;
	private JButton exitButton;
	private JLabel remarkLabel;
	private JScrollPane remarkScrollPane;
	private JTextArea remarkTextArea;
	private JButton submitButton;
	private JLabel titleLabel;
	private JTextField titleTextField;
	
	private static Dao dao = Dao.getInstance();
	private JTextField dateTextField;
	private JButton selectedPhoto;
	public UpdatePhotoInfoDialog(java.awt.Frame parent, boolean modal, JButton selectedPhoto) {
		super(parent, modal);
		initComponents();
		dateTextField = calendarPanel.getTextField();
		Vector photoV = Dao.getInstance().selectPhoto(selectedPhoto.getName());
		titleTextField.setText(photoV.get(3).toString());
		dateTextField.setText(photoV.get(2).toString());
		remarkTextArea.setText(photoV.get(4).toString());
		this.selectedPhoto = selectedPhoto;
		ScreenSize.centered(this);
	}
	private void initComponents() {
		titleLabel = new JLabel();
		dateLabel = new JLabel();
		remarkLabel = new JLabel();
		titleTextField = new JTextField();
		calendarPanel = new chuan.Album.calendar.CalendarPanel();
		remarkScrollPane = new JScrollPane();
		remarkTextArea = new JTextArea();
		exitButton = new JButton();
		submitButton = new JButton();
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("修改信息");
		titleLabel.setText("图片标题：");
		dateLabel.setText("拍摄日期：");
		remarkLabel.setText("图片描述：");
		titleTextField.setColumns(30);
		titleTextField.addCaretListener(new javax.swing.event.CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				// TODO Auto-generated method stub
				titleTextFieldCaretUpdate(e);
			}
		});
		titleTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				titleTextFieldKeyTyped(e);
			}
		});
		remarkTextArea.setColumns(40);
		remarkTextArea.setLineWrap(true);
		remarkTextArea.setRows(5);
		remarkScrollPane.setViewportView(remarkTextArea);
		exitButton.setText("退出");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitButtonActionPerformed(e);
			}
		});
		submitButton.setText("确定");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(titleLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(dateLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(calendarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(remarkLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(remarkScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(200, Short.MAX_VALUE)
                    .addComponent(submitButton)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(exitButton)
                    .addContainerGap())
            );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(titleLabel)
                        .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(calendarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dateLabel))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(remarkLabel)
                        .addComponent(remarkScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exitButton)
                        .addComponent(submitButton))
                    .addContainerGap())
            );

            pack();
	}
	private void titleTextFieldCaretUpdate(CaretEvent e) {
		if (titleTextField.getText().trim().length() == 0) {
			submitButton.setEnabled(false);
		} else {
			if (! submitButton.isEnabled()) {
				submitButton.setEnabled(true);
			}
		}
	}
	private void titleTextFieldKeyTyped(KeyEvent e) {
		if (titleTextField.getText().getBytes().length == 20) {
			e.consume();
		}
	}
	private void exitButtonActionPerformed(ActionEvent e) {
		this.dispose();
	}
	private void submitButtonActionPerformed(ActionEvent e) {
		this.dispose();
		selectedPhoto.setText(titleTextField.getText());
		Vector<Object> photoV = dao.selectPhoto(selectedPhoto.getName());
		photoV.set(2, dateTextField.getText());
		photoV.set(3, titleTextField.getText());
		photoV.set(4, remarkTextArea.getText());
		ToolTip.set(selectedPhoto, photoV);
		dao.updatePhoto(selectedPhoto.getName(), titleTextField.getText(),
				dateTextField.getText(), remarkTextArea.getText());
	}
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				UpdatePhotoInfoDialog dialog = new UpdatePhotoInfoDialog(new JFrame(), true, null);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}
}
