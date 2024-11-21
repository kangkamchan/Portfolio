package net.haebup.score;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.haebup.common.DBConnPool;

public class ScoreDAO extends DBConnPool {
	//리스트 조회
	public List<ScoreDTO> getScoreList(Map<String, String> map){
		int pageNo = Integer.parseInt(map.get("pageNo"));
		int pageSize = Integer.parseInt(map.get("pageSize"));
		int offset = (pageNo-1)*pageSize;
		String memberId = map.get("memberId");
		String sql = "SELECT s.idx, s.memberId, s.courseCode, s.courseNo, s.score, s.regDate, s.modifyDate, s.deleteDate, c.courseName "
				+ "FROM tbl_score AS s "
				+ "INNER JOIN tbl_course AS c ON s.courseCode = c.courseCode "
				+ "WHERE memberId=? "
				+ "ORDER BY regDate DESC "
				+ "LIMIT ?, ?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, memberId);
			pstm.setInt(2, offset);
			pstm.setInt(3, pageSize);
			rs = pstm.executeQuery();
			List<ScoreDTO> list = new ArrayList<ScoreDTO>();
			while(rs.next()) {
				ScoreDTO dto = new ScoreDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setMemberId(rs.getString("memberId"));
				dto.setCourseCode(rs.getString("courseCode"));
				dto.setCourseNo(rs.getInt("courseNo"));
				dto.setScore(rs.getInt("score"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				if(rs.getTimestamp("modifyDate")!=null) dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
				if(rs.getTimestamp("deleteDate")!=null) dto.setDeleteDate(rs.getTimestamp("deleteDate").toLocalDateTime());
				dto.setCourseName(rs.getString("courseName"));
				list.add(dto);
			}
			return list;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	//리스트 조회
	public List<ScoreDTO> getScoreList(String memberId, int pageNo, int pageSize){
		int offset = (pageNo-1)*pageSize;		
		String sql = "SELECT s.idx, s.memberId, s.courseCode, s.courseNo, s.score, s.regDate, s.modifyDate, s.deleteDate, c.courseName "
				+ "FROM tbl_score AS s "
				+ "INNER JOIN tbl_course AS c ON s.courseCode = c.courseCode "
				+ "WHERE memberId=? "
				+ "ORDER BY regDate DESC "
				+ "LIMIT ?, ?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, memberId);
			pstm.setInt(2, offset);
			pstm.setInt(3, pageSize);
			rs = pstm.executeQuery();
			List<ScoreDTO> list = new ArrayList<ScoreDTO>();
			while(rs.next()) {
				ScoreDTO dto = new ScoreDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setMemberId(rs.getString("memberId"));
				dto.setCourseCode(rs.getString("courseCode"));
				dto.setCourseNo(rs.getInt("courseNo"));
				dto.setScore(rs.getInt("score"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				if(rs.getTimestamp("modifyDate")!=null) dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
				if(rs.getTimestamp("deleteDate")!=null) dto.setDeleteDate(rs.getTimestamp("deleteDate").toLocalDateTime());
				dto.setCourseName(rs.getString("courseName"));
				list.add(dto);
			}
			return list;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	//total count 조회
	public int getTotalCount(String memberId) {
		
		String sql = "SELECT COUNT(*) "
				+ "FROM tbl_score AS s "
				+ "INNER JOIN tbl_course AS c ON s.courseCode = c.courseCode "
				+ "WHERE memberId=? ";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, memberId);
			rs = pstm.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}
	//상세조회
	public ScoreDTO getScore(int idx) {
		String sql = "SELECT s.idx, s.memberId, s.courseCode, s.courseNo, s.score, s.regDate, s.modifyDate, s.deleteDate, c.courseName "
				+ "FROM tbl_score AS s "
				+ "INNER JOIN tbl_course AS c ON s.courseCode = c.courseCode "
				+ "WHERE idx=?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setInt(1,idx);
			rs=pstm.executeQuery();
			if(rs.next()) {
				ScoreDTO dto = new ScoreDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setMemberId(rs.getString("memberId"));
				dto.setCourseCode(rs.getString("courseCode"));
				dto.setCourseNo(rs.getInt("courseNo"));
				dto.setScore(rs.getInt("score"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				if(rs.getTimestamp("modifyDate")!=null) dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
				if(rs.getTimestamp("deleteDate")!=null) dto.setDeleteDate(rs.getTimestamp("deleteDate").toLocalDateTime());
				dto.setCourseName(rs.getString("courseName"));
				return dto;
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
