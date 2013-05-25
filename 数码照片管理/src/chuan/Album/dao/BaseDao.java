package chuan.Album.dao;

//与特定数据库的连接（会话）。在连接上下文中执行 SQL 语句并返回结果。
import java.sql.Connection;
//提供关于数据库访问错误或其他错误信息的异常。
import java.sql.SQLException;
//表示数据库结果集的数据表，通常通过执行查询数据库的语句生成。
import java.sql.ResultSet;
//用于执行静态 SQL 语句并返回它所生成结果的对象。
import java.sql.Statement;
import java.util.Vector;;

public class BaseDao {
	//查询多个记录
	public Vector<Vector<Object>> selectSomeNote(String sql) {
		Vector<Vector<Object>> rsV = new Vector<Vector<Object>>();	//创建结果集向量
		Connection conn = JDBC.getConnection();	//获得数据库连接
		
		try {
			Statement stmt = conn.createStatement();	//创建连接状态对象
			//executeQuery(sql) 执行给定的 SQL 语句，该语句返回单个 ResultSet 对象。
			ResultSet rs = stmt.executeQuery(sql);	//执行SQL语句获得查询结果
			//getMetaData()获取此 ResultSet 对象的列的编号、类型和属性。
			int columnCount = rs.getMetaData().getColumnCount();	//获得查询数据表列数
			
			while (rs.next()) {		//遍历结果集
				Vector<Object> rowV = new Vector<Object>();	//创建行向量
				for (int column = 1; column <= columnCount; column ++) {
					rowV.add(rs.getObject(column));		//添加列值
				}
				rsV.add(rowV);
			}
			rs.close();	//关闭结果集对象
			stmt.close();	//关闭连接状态对象
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rsV;	//返回结果集向量
	}
	
	//查询单个记录
	public Vector<Object> selectOnlyNote(String sql) {
		Vector<Object> rowV = null;
		Connection conn = JDBC.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int columnCount = rs.getMetaData().getColumnCount();	//获得查询数据表列数
			while (rs.next()) {		//遍历结果集
				rowV = new Vector<Object>();	//创建行向量
				for (int column = 1; column <= columnCount; column ++) {
					rowV.add(rs.getObject(column));		//添加列值
				}
			}
			rs.close();	//关闭结果集对象
			stmt.close();	//关闭连接状态对象
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rowV;
	}
	
	//查询多个值
	public Vector<Object> selectSomeValue(String sql) {
		Vector<Object> valueV = new Vector<Object>();
		Connection conn = JDBC.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				valueV.add(rs.getObject(1));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return valueV;
	}
	
	//查询单个值
	public Object selectOnlyValue(String sql) {
		Object value = null;
		Connection conn = JDBC.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs  = stmt.executeQuery(sql);
			while (rs.next()) {
				value = rs.getObject(1);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	//插入、修改、删除记录
	public boolean longHaul(String sql) {
		boolean isLongHaul = true;		//默认持久化成功
		Connection conn = JDBC.getConnection();	//获得数据库连接
		try {
			//将此连接的自动提交模式设置为给定状态。
			conn.setAutoCommit(false);	//设置为手动提交
			Statement stmt = conn.createStatement();	//创建连接状态对象
			stmt.executeUpdate(sql);	//执行SQL语句
			stmt.close();	//关闭连接状态对象
			conn.commit();	//提交持久化
		} catch (SQLException e) {
			isLongHaul = false;	//持久化失败
			try {
				conn.rollback();	//回滚
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return isLongHaul;	//返回持久化结果
	}
}
