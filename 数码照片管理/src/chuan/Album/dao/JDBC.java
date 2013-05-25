package chuan.Album.dao;

import java.io.File;
//与特定数据库的连接（会话）。在连接上下文中执行 SQL 语句并返回结果。
import java.sql.Connection;
//管理一组 JDBC 驱动程序的基本服务。
import java.sql.DriverManager;
//提供关于数据库访问错误或其他错误信息的异常。
import java.sql.SQLException;
//用于执行静态 SQL 语句并返回它所生成结果的对象。
import java.sql.Statement;
//JOptionPane 有助于方便地弹出要求用户提供值或向其发出通知的标准对话框。
import javax.swing.JOptionPane;

public class JDBC {
	
	private static final String DRIVERCLASS = "org.apache.derby.jdbc.EmbeddedDriver";	//数据库驱动
	private static final String URL = "jdbc:derby:db_album";	//数据库URL
	//该类提供了线程局部 (thread-local) 变量。
	//希望将状态与某一个线程（例如，用户 ID 或事务 ID）相关联
	private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();	//用来保存数据库连接
	private static Connection conn = null;	//数据库连接
	
	static {	//通过静态方法加载数据库驱动， 并且在数据库不存在的情况下自动创建数据库
		
		try {
			Class.forName(DRIVERCLASS);	//加载数据库驱动
			File db_album = new File("db_album");	//创建数据库文件对象
			if (! db_album.exists()) {	//数据库文件不存在
				createDatabase();	//创建数据库
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void createDatabase() throws Exception {
		String[] sqls = new String[2];	//定义创建数据库的SQL语句
		
		sqls[0] = "create table tb_album (id int not null,father_id int not null,name varchar(20) not null,primary key (id))";
        sqls[1] = "create table tb_photo (num varchar(28) not null,album_id int not null,date date not null,title varchar(20) not null,note varchar(200),primary key (num))";
        conn = DriverManager.getConnection(URL + ";create = true");
        
        threadLocal.set(conn);	//保存数据库连接
      //创建一个 Statement 对象来将 SQL 语句发送到数据库。
        Statement stmt = conn.createStatement();	//创建数据库连接连接状态对象
        for (int i = 0; i < sqls.length; i ++) {	//遍历SQL数组创建数据库
        	stmt.execute(sqls[i]);	//执行SQL语句
        }
        stmt.close();	//关闭数据库连接状态对象
	}
	
	protected static Connection getConnection() {	//创建数据库连接方法
		
		conn = (Connection) threadLocal.get();	//从线程中获得数据库连接
		
		if (conn == null) {	//没有可用的数据库连接
			try {
				conn = DriverManager.getConnection(URL);	//创建新的数据库连接
				threadLocal.set(conn);	//将数据库连接保存到线程中
			} catch (Exception e) {
				String[] infos = {"未能成功连接数据库！", "请确认本软件是否已经运行！" };
				JOptionPane.showMessageDialog(null, infos);	//弹出数据库连接失败提示
				
				System.exit(0);	//关闭系统
				
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	protected static boolean closeConnection() {	//关闭数据库连接的方法
		
		boolean isClosed = true;	//默认关闭成功
		
		conn = (Connection)threadLocal.get();	// 从线程中获得数据库连接
		threadLocal.set(null);	//清空线程中的数据库连接
		
		if (conn != null) {	//数据库连接可用
			
			try {
				conn.close();	//关闭数据库连接
			} catch (SQLException e) {
				isClosed = false;	//关闭失败
				e.printStackTrace();
			}
		}
		return isClosed;
	}
}
