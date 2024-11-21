package net.tclass.qna;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.tclass.common.DBConnPool;

public class QnaDAO extends DBConnPool{
	public QnaDAO() {}
	//등록
	public int regist(QnaDTO dto) {
		System.out.println("--------------------------");
		System.out.println("QnaDAO > regist > start");
		try {
			String sql = "INSERT INTO tbl_qna("
					+ "memberId, title, content, "
					+ "fileName, orgFileName, fileExt, filePath, fileSize, "
					+ "category1, category2"
					+ ") "
					+ "VALUES("
					+ "?, ?, ?, "
					+ "?, ?, ?, ?, ?, "
					+ "?, ?"
					+ ")";
			pstm=con.prepareStatement(sql);
			pstm.setString(1, dto.getMemberId());
			pstm.setString(2, dto.getTitle());
			pstm.setString(3, dto.getContent());
			pstm.setString(4, dto.getFileName());
			pstm.setString(5, dto.getOrgFileName());
			pstm.setString(6, dto.getFileExt());
			pstm.setString(7, dto.getFilePath());
			pstm.setInt(8, dto.getFileSize());
			pstm.setString(9,dto.getCategory1());
			pstm.setString(10, dto.getCategory2());
			System.out.println("QnaDAO > regist > end");
			System.out.println("--------------------------");
			return pstm.executeUpdate();			
		}catch(Exception e) {
			System.out.println("QnaDAO > regist > error");
			System.out.println(e.getMessage());
		}
		System.out.println("QnaDAO > regist > end");
		System.out.println("--------------------------");
		return 0;
	}
	//리스트조회
	public List<QnaDTO> getList(Map<String,String> map){
		System.out.println("--------------------------");
		System.out.println("QnaDAO > getList > start");
		String sql = "SELECT idx, memberId, title, content, regDate, "
				+ "answerTitle, answerContent, answerDate, fileName, orgFileName, fileExt, "
				+ "filePath, fileSize , category1, category2 "
				+ "FROM tbl_qna WHERE memberId = ? "
				+ "ORDER BY idx DESC LIMIT 0, ?";
		System.out.println("sql : " + sql);
		try {
			pstm=con.prepareStatement(sql);
			pstm.setString(1, map.get("memberId"));
			pstm.setInt(2, Integer.parseInt(map.get("printCnt")));
			System.out.println("pstm : " + pstm);
			rs = pstm.executeQuery();
			List<QnaDTO> list = new ArrayList<QnaDTO>();
			while(rs.next()) {
				System.out.println(rs.getInt("idx"));
				QnaDTO dto = new QnaDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setMemberId(rs.getString("memberId"));
				dto.setTitle((rs.getString("title").length()>20)? rs.getString("title").substring(0,20)+"..." : rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				if(rs.getString("answerTitle")!=null)dto.setAnswerTitle(rs.getString("answerTitle"));
				if(rs.getString("answerContent")!=null)dto.setAnswerContent(rs.getString("answerContent"));
				if(rs.getTimestamp("answerDate")!=null)dto.setAnswerDate(rs.getTimestamp("answerDate").toLocalDateTime());
				if(rs.getString("fileName")!=null) dto.setFileName(rs.getString("fileName"));
				if(rs.getString("orgFileName")!=null) dto.setOrgFileName(rs.getString("orgFileName"));
				if(rs.getString("fileExt")!=null) dto.setFileExt(rs.getString("fileExt"));
				if(rs.getString("filePath")!=null) dto.setFilePath(rs.getString("filePath"));
				dto.setFileSize(rs.getInt("fileSize"));
				dto.setCategory1(rs.getString("category1"));
				dto.setCategory2(rs.getString("category2"));
				list.add(dto);
			}
			System.out.println("QnaDAO > getList > end");
			System.out.println("--------------------------");
			return list;
		}catch(Exception e) {
			System.out.println("QnaDAO > getList > error");
			System.out.println(e.getMessage());
		}
		System.out.println("QnaDAO > getList > end");
		System.out.println("--------------------------");
		return null;
	}
	//개별조회
	public QnaDTO getQnaByIdx(int idx) {
		System.out.println("--------------------------");
		System.out.println("QnaDAO > getQnaByIdx > start");
		String sql = "SELECT "
				+ "idx, memberId, title, content, regDate, "
				+ "answerTitle, answerContent, answerDate, fileName, orgFileName, fileExt, "
				+ "filePath, fileSize, category1, category2 "
				+ "FROM tbl_qna WHERE idx = ?";
		try {
			pstm=con.prepareStatement(sql);
			pstm.setInt(1, idx);
			rs = pstm.executeQuery();
		
			if(rs.next()) {
				QnaDTO dto = new QnaDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setMemberId(rs.getString("memberId"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				if(rs.getString("answerTitle")!=null)dto.setAnswerTitle(rs.getString("answerTitle"));
				if(rs.getString("answerContent")!=null)dto.setAnswerContent(rs.getString("answerContent"));
				if(rs.getTimestamp("answerDate")!=null)dto.setAnswerDate(rs.getTimestamp("answerDate").toLocalDateTime());
				if(rs.getString("fileName")!=null) dto.setFileName(rs.getString("fileName"));
				if(rs.getString("orgFileName")!=null) dto.setOrgFileName(rs.getString("orgFileName"));
				if(rs.getString("fileExt")!=null) dto.setFileExt(rs.getString("fileExt"));
				if(rs.getString("filePath")!=null) dto.setFilePath(rs.getString("filePath"));
				dto.setFileSize(rs.getInt("fileSize"));
				dto.setCategory1(rs.getString("category1"));
				dto.setCategory2(rs.getString("category2"));
				System.out.println("QnaDAO > getQnaByIdx > end");
				System.out.println("--------------------------");
				return dto;
			}
		}catch(Exception e) {
			System.out.println("QnaDAO > getQnaByIdx > error");
			System.out.println(e.getMessage());
		}
		System.out.println("QnaDAO > getQnaByIdx > end");
		System.out.println("--------------------------");
		return null;
	}
	//수정
	public int modify(QnaDTO dto) {
		System.out.println("--------------------------");
		System.out.println("QnaDAO > modify > start");
		String sql = "UPDATE tbl_qna SET "
				+ "title=?, content=?, fileName=?, orgFileName=?, fileExt=?, filePath=?, fileSize=? "
				+ "WHERE idx=?";
		try {
			pstm=con.prepareStatement(sql);
			pstm.setString(1, dto.getTitle());
			pstm.setString(2, dto.getContent());
			pstm.setString(3, dto.getFileName());
			pstm.setString(4, dto.getOrgFileName());
			pstm.setString(5, dto.getFileExt());
			pstm.setString(6, dto.getFilePath());
			pstm.setInt(7, dto.getFileSize());
			pstm.setInt(8, dto.getIdx());
			System.out.println("QnaDAO > modify > end");
			System.out.println("--------------------------");
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("QnaDAO > modify > error");
			System.out.println(e.getMessage());
		}
		System.out.println("QnaDAO > modify > end");
		System.out.println("--------------------------");
		return 0;
	}
	//삭제
	public int delete(int idx) {
		System.out.println("--------------------------");
		System.out.println("QnaDAO > delete > start");
		String sql = "DELETE FROM tbl_qna WHERE idx = ?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, idx);
			System.out.println("QnaDAO > delete > end");
			System.out.println("--------------------------");
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("QnaDAO > delete > error");
			System.out.println(e.getMessage());
		}
		System.out.println("QnaDAO > delete > end");
		System.out.println("--------------------------");
		return 0;
	}
}
