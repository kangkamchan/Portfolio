package net.tclass.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBConnPool implements AutoCloseable{
	public Connection con;
	public Statement stmt;
	public PreparedStatement pstm;
	public ResultSet rs;
	
	public DBConnPool() {
		try {
		Context initCtx = new InitialContext();
		Context ctx = (Context)initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)ctx.lookup("jdbc_tspoon");
		con = ds.getConnection();
		System.out.println("===================================================");
		System.out.println("DB커넥션풀접속 성공");
		System.out.println("con1 String : " + con);
		System.out.println("===================================================");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("===================================================");
			System.out.println("DB커넥션풀접속연결실패");
			System.out.println("에러메세지 " + e.getMessage());
			System.out.println("===================================================");
		}
	}
	public DBConnPool(String globalName) {
		try {
		Context initCtx = new InitialContext();
		Context ctx = (Context)initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)ctx.lookup(globalName);
		con = ds.getConnection();
		System.out.println("===================================================");
		System.out.println("DB커넥션풀접속 성공");
		System.out.println("con1 String : " + con);
		System.out.println("===================================================");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("===================================================");
			System.out.println("DB커넥션풀접속연결실패");
			System.out.println("에러메세지 " + e.getMessage());
			System.out.println("===================================================");
		}
	}
	
	//연결 해제(리소스 자원 반납)
	@Override
	public void close() {
		try {
			if(rs != null) rs.close();
			if(pstm != null) pstm.close();
			if(stmt != null) stmt.close();
			if(con != null) con.close();
			System.out.println("===================================================");
			System.out.println("JDBC자원해제");
			System.out.println("===================================================");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("===================================================");
			System.out.println("에러메세지 " + e.getMessage());
			System.out.println("===================================================");
		}
	}
}

