package net.haebup.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBConnPool implements AutoCloseable {
	public Connection con;
	public Statement stmt;
	public PreparedStatement pstm;
	public ResultSet rs;
	
	public DBConnPool() {
		try {
			Context initCtx = new InitialContext();
			Context ctx = (Context)initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource)ctx.lookup("jdbc_haebup");
			con = ds.getConnection();
			
			System.out.println("===================================");
			System.out.println("DB 커넥션풀 접속 성공(커넥션풀)");
			System.out.println("con1 String : "+ con);
			System.out.println("===================================");
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("===================================");
			System.out.println("DB 커넥션풀 접속 연결 실패(커넥션풀)");
			System.out.println(e.getMessage());
			System.out.println("===================================");
		}
	}
	
	public DBConnPool(String globalName) {
		try {
			Context initCtx = new InitialContext();
			Context ctx = (Context)initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource)ctx.lookup(globalName);
			con = ds.getConnection();
			
			System.out.println("===================================");
			System.out.println("DB 커넥션풀 접속 성공(커넥션풀)");
			System.out.println("con1 String : "+ con);
			System.out.println("===================================");
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("===================================");
			System.out.println("DB 커넥션풀 접속 연결 실패(커넥션풀)");
			System.out.println(e.getMessage());
			System.out.println("===================================");
		}
	}
	
	public void close() {
		try {
			if(con != null) {
				con.close();
			}
			if(stmt != null) {
				stmt.close();
			}
			if(pstm != null) {
				pstm.close();
			}
			if(rs != null) {
				rs.close();
			}
			System.out.println("=========================");
			System.out.println("JDBC 해제");
			System.out.println("=========================");
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
