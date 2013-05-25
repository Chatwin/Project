package chuan.Album.dao;

import java.io.File;
//���ض����ݿ�����ӣ��Ự������������������ִ�� SQL ��䲢���ؽ����
import java.sql.Connection;
//����һ�� JDBC ��������Ļ�������
import java.sql.DriverManager;
//�ṩ�������ݿ���ʴ��������������Ϣ���쳣��
import java.sql.SQLException;
//����ִ�о�̬ SQL ��䲢�����������ɽ���Ķ���
import java.sql.Statement;
//JOptionPane �����ڷ���ص���Ҫ���û��ṩֵ�����䷢��֪ͨ�ı�׼�Ի���
import javax.swing.JOptionPane;

public class JDBC {
	
	private static final String DRIVERCLASS = "org.apache.derby.jdbc.EmbeddedDriver";	//���ݿ�����
	private static final String URL = "jdbc:derby:db_album";	//���ݿ�URL
	//�����ṩ���ֲ߳̾� (thread-local) ������
	//ϣ����״̬��ĳһ���̣߳����磬�û� ID ������ ID�������
	private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();	//�����������ݿ�����
	private static Connection conn = null;	//���ݿ�����
	
	static {	//ͨ����̬�����������ݿ������� ���������ݿⲻ���ڵ�������Զ��������ݿ�
		
		try {
			Class.forName(DRIVERCLASS);	//�������ݿ�����
			File db_album = new File("db_album");	//�������ݿ��ļ�����
			if (! db_album.exists()) {	//���ݿ��ļ�������
				createDatabase();	//�������ݿ�
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void createDatabase() throws Exception {
		String[] sqls = new String[2];	//���崴�����ݿ��SQL���
		
		sqls[0] = "create table tb_album (id int not null,father_id int not null,name varchar(20) not null,primary key (id))";
        sqls[1] = "create table tb_photo (num varchar(28) not null,album_id int not null,date date not null,title varchar(20) not null,note varchar(200),primary key (num))";
        conn = DriverManager.getConnection(URL + ";create = true");
        
        threadLocal.set(conn);	//�������ݿ�����
      //����һ�� Statement �������� SQL ��䷢�͵����ݿ⡣
        Statement stmt = conn.createStatement();	//�������ݿ���������״̬����
        for (int i = 0; i < sqls.length; i ++) {	//����SQL���鴴�����ݿ�
        	stmt.execute(sqls[i]);	//ִ��SQL���
        }
        stmt.close();	//�ر����ݿ�����״̬����
	}
	
	protected static Connection getConnection() {	//�������ݿ����ӷ���
		
		conn = (Connection) threadLocal.get();	//���߳��л�����ݿ�����
		
		if (conn == null) {	//û�п��õ����ݿ�����
			try {
				conn = DriverManager.getConnection(URL);	//�����µ����ݿ�����
				threadLocal.set(conn);	//�����ݿ����ӱ��浽�߳���
			} catch (Exception e) {
				String[] infos = {"δ�ܳɹ��������ݿ⣡", "��ȷ�ϱ�����Ƿ��Ѿ����У�" };
				JOptionPane.showMessageDialog(null, infos);	//�������ݿ�����ʧ����ʾ
				
				System.exit(0);	//�ر�ϵͳ
				
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	protected static boolean closeConnection() {	//�ر����ݿ����ӵķ���
		
		boolean isClosed = true;	//Ĭ�Ϲرճɹ�
		
		conn = (Connection)threadLocal.get();	// ���߳��л�����ݿ�����
		threadLocal.set(null);	//����߳��е����ݿ�����
		
		if (conn != null) {	//���ݿ����ӿ���
			
			try {
				conn.close();	//�ر����ݿ�����
			} catch (SQLException e) {
				isClosed = false;	//�ر�ʧ��
				e.printStackTrace();
			}
		}
		return isClosed;
	}
}
