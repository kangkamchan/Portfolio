package net.haebup.myclass;

import java.util.ArrayList;
import java.util.List;

import net.haebup.common.DBConnPool;
import net.haebup.course.CourseDTO;

public class MyclassDAO extends DBConnPool {
	//강감찬
	//강의 목록 조회 : 회원별 과목별 강의리스트 조회하기
	public List<MyclassCourseListDTO> getMyclassCourseList(String memberId, String subjectCode, int offset, int pageSize) {
		System.out.println("MyclassDAO > getMyclassCourseList > start");
	    String sql = "SELECT p.courseCode, p.memberId, p.startDate, p.endDate, c.courseName, m.name AS 'teacherName' "
	    		+ "FROM tbl_payment AS p "
	    		+ "INNER JOIN tbl_course AS c ON p.courseCode = c.courseCode "
	    		+ "INNER JOIN tbl_member AS m ON c.teacherId = m.memberId "
	    		+ "WHERE p.memberId = ? AND p.refund = 'N' AND (NOW() BETWEEN p.startDate AND p.endDate) AND c.subjectCode = ? "
	    		+ "ORDER BY p.startDate DESC "
	    		+ "LIMIT ?, ? ";
	    
	    System.out.println("sql : " + sql);
	     // 강의 목록 페이징 처리
	    try {

	        pstm = con.prepareStatement(sql);
	        pstm.setString(1, memberId);
	        pstm.setString(2, subjectCode);
	        pstm.setInt(3,offset);
	        pstm.setInt(4, pageSize);
	        System.out.println(pstm);
	        rs = pstm.executeQuery();
		    List<MyclassCourseListDTO> list = new ArrayList<MyclassCourseListDTO>();
	        // 결과를 리스트에 추가
	        while (rs.next()) {
	        	System.out.println("MyclassDAO > getMyclassCourseList > while");
	        	MyclassCourseListDTO dto = new MyclassCourseListDTO();
	            dto.setCourseCode(rs.getString("courseCode"));
	            dto.setMemberId(rs.getString("memberId"));
	            dto.setStartDate(rs.getTimestamp("startDate").toLocalDateTime());
	            dto.setEndDate(rs.getTimestamp("endDate").toLocalDateTime());
	            dto.setCourseName(rs.getString("courseName"));
	            dto.setTeacherName(rs.getString("teacherName"));
	            list.add(dto);
	        }
	        System.out.println("MyclassDAO > getMyclassCourseList > end(list)");
	        return list;
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	        System.out.println("강의 목록 조회 오류");
	    }
	    System.out.println("MyclassDAO > getMyclassCourseList > end(null)");
	    return null;  // 강의 리스트 반환
	}
	//강의목록조회시 전체 게시글 수 확인
	public int getTotalCount(String memberId, String subjectCode) {
	    String sql = "SELECT COUNT(*) "
	    		+ "FROM tbl_payment AS p "
	    		+ "INNER JOIN tbl_course AS c ON p.courseCode = c.courseCode "
	    		+ "INNER JOIN tbl_member AS m ON c.teacherId = m.memberId "
	    		+ "WHERE p.memberId = ? AND p.refund = 'N' AND (NOW() BETWEEN p.startDate AND p.endDate) AND c.subjectCode = ? ";
	    try {
	        pstm = con.prepareStatement(sql);
	        pstm.setString(1, memberId);
	        pstm.setString(2, subjectCode);
	        rs = pstm.executeQuery();
	        // 결과를 리스트에 추가
	        if(rs.next()) {
	        	return rs.getInt(1);
	        }
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	        System.out.println("강의 목록 전체게시글수조회 오류");
	    }
	    return 0;  // 강의 리스트 반환
	}
	//강의목록조회시 전체 게시글 수 확인
	public int getTotalCount(String teacherId) {
	    String sql = "SELECT COUNT(*) "
	    		+ "FROM tbl_course"
	    		+ "WHERE teacherId = ?";
	    try {
	        pstm = con.prepareStatement(sql);
	        pstm.setString(1, teacherId);
	        rs = pstm.executeQuery();
	        // 결과를 리스트에 추가
	        if(rs.next()) {
	        	return rs.getInt(1);
	        }
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	        System.out.println("강의 목록 전체게시글수조회 오류");
	    }
	    return 0;  // 강의 리스트 반환
	}
	//강의 목록 조회 : 회원별 과목별 강의리스트 조회하기
	public List<MyclassCourseListDTO> getMyclassCourseList(String teacherId, int offset, int pageSize) {
		System.out.println("MyclassDAO > getMyclassCourseList > start");
	    String sql = "SELECT courseCode, courseName "
	    		+ "FROM tbl_course "
	    		+ "WHERE teacherId = ? "
	    		+ "ORDER BY regDate DESC "
	    		+ "LIMIT ?, ? ";
	    
	    System.out.println("sql : " + sql);
	     // 강의 목록 페이징 처리
	    try {
	        pstm = con.prepareStatement(sql);
	        pstm.setString(1, teacherId);
	        pstm.setInt(2,offset);
	        pstm.setInt(3, pageSize);
	        System.out.println(pstm);
	        rs = pstm.executeQuery();
		    List<MyclassCourseListDTO> list = new ArrayList<MyclassCourseListDTO>();
	        // 결과를 리스트에 추가
	        while (rs.next()) {
	        	System.out.println("MyclassDAO > getMyclassCourseList > while");
	        	MyclassCourseListDTO dto = new MyclassCourseListDTO();
	            dto.setCourseCode(rs.getString("courseCode"));
	            dto.setCourseName(rs.getString("courseName"));
	            list.add(dto);
	        }
	        System.out.println("MyclassDAO > getMyclassCourseList > end(list)");
	        return list;
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	        System.out.println("강의 목록 조회 오류");
	    }
	    System.out.println("MyclassDAO > getMyclassCourseList > end(null)");
	    return null;  // 강의 리스트 반환
	}
}
