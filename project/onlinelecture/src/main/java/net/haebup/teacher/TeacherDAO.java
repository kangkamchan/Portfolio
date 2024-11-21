package net.haebup.teacher;

import java.util.ArrayList;
import java.util.List;

import net.haebup.bbs.BbsDTO;
import net.haebup.common.DBConnPool;
import net.haebup.course.CourseDTO;

public class TeacherDAO extends DBConnPool{
	public TeacherDAO() {}
	
	//과목 별 선생님 조회
	public List<CourseDTO> getTeacherList(String subjectCode) {
	    String sql = "SELECT courseCode, subjectCode, courseName, teacherId, price, regDate, status, modifyDate, deleteDate, totalAttendee, courseDuration FROM tbl_course ";
	    if(!subjectCode.isEmpty()) {
	    		sql += "WHERE subjectCode=?";
	    }
	    sql+= "GROUP BY teacherId";
	     // 강의 목록 페이징 처리
	    try {
	    	pstm = con.prepareStatement(sql);
	        // 검색 조건이 있을 경우
	    	if(!subjectCode.isEmpty()) {
		        pstm.setString(1, subjectCode);  // 시작 인덱스 설정
	    	}
	        rs = pstm.executeQuery();
		    List<CourseDTO> list = new ArrayList<CourseDTO>();
	        // 결과를 리스트에 추가
	        while (rs.next()) {
	            CourseDTO dto = new CourseDTO();
	            dto.setCourseCode(rs.getString("courseCode")); // 강의코드
	            dto.setSubjectCode(rs.getString("subjectCode"));
	            dto.setCourseName(rs.getString("courseName")); // 강의 이름
	            dto.setPrice(rs.getInt("price"));              // 가격
	            dto.setTeacherId(rs.getString("teacherId"));   // 선생님 ID
	            dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
	            dto.setStatus(rs.getString("status"));
	            if(rs.getTimestamp("modifyDate")!=null)dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
	            if(rs.getTimestamp("deleteDate")!=null)dto.setModifyDate(rs.getTimestamp("deleteDate").toLocalDateTime());
	            dto.setCourseDuration(rs.getInt("courseDuration")); // 강의 기간
	            dto.setTotalAttendee(rs.getInt("totalAttendee"));   // 수강 인원
	            list.add(dto);  // 리스트에 추가
	        }
	        return list;
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	        System.out.println("강의 목록 조회 오류");
	    }
	    return null;  // 강의 리스트 반환
	}
	
	//모든 선생님 조회
	public List<CourseDTO> getAllTeachers(){
		List<CourseDTO> list = new ArrayList<CourseDTO>();
		String sql = "SELECT courseCode, subjectCode, courseName, teacherId, price, regDate, status, modifyDate, deleteDate, totalAttendee, courseDuration FROM tbl_course";
		try {
	        pstm = con.prepareStatement(sql);
	        rs = pstm.executeQuery();
	        
	        while (rs.next()) {
	            CourseDTO dto = new CourseDTO();
	            dto.setCourseCode(rs.getString("courseCode"));
	            dto.setSubjectCode(rs.getString("subjectCode"));
	            dto.setCourseName(rs.getString("courseName"));
	            dto.setPrice(rs.getInt("price"));
	            dto.setTeacherId(rs.getString("teacherId"));
	            dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
	            dto.setStatus(rs.getString("status"));
	            if (rs.getTimestamp("modifyDate") != null) dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
	            if (rs.getTimestamp("deleteDate") != null) dto.setModifyDate(rs.getTimestamp("deleteDate").toLocalDateTime());
	            dto.setCourseDuration(rs.getInt("courseDuration"));
	            dto.setTotalAttendee(rs.getInt("totalAttendee"));
	            list.add(dto);
	        }
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	        System.out.println("모든 강사 조회 오류");
	    }
		return list;
		
	}
	
	//선생님 모든 후기 조회
	public List<BbsDTO> getAllReviews(String teacherId){
		String sql = "SELECT bbs.idx, bbs.courseCode, bbs.memberId, bbs.content, bbs.rating, bbs.regDate FROM tbl_course AS course "
						+ "JOIN tbl_bbs AS bbs ON course.courseCode = bbs.courseCode "
						+ "WHERE course.teacherId = ? AND bbs.category = 'R'";
		List<BbsDTO> reviews = new ArrayList<>();
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, teacherId);
			rs = pstm.executeQuery();
			while(rs.next()) {
				BbsDTO review = new BbsDTO();
                review.setIdx(rs.getInt("idx"));
                review.setCourseCode(rs.getString("courseCode"));
                review.setMemberId(rs.getString("memberId"));
                review.setContent(rs.getString("content"));
                review.setRating(rs.getInt("rating"));
                review.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
                reviews.add(review);
			}

		}catch(Exception e) {
			System.out.println(e.getMessage());
	        System.out.println("선생님의 모든 후기 조회 오류");
		}
		return reviews;	
	}
	
	//선생님 모든 후기 조회
	public List<BbsDTO> getAllReviews(String teacherId, int offset, int pageSize){
		String sql = "SELECT bbs.idx, bbs.courseCode, bbs.memberId, bbs.content, bbs.rating, bbs.regDate FROM tbl_course AS course "
						+ "JOIN tbl_bbs AS bbs ON course.courseCode = bbs.courseCode "
						+ "WHERE course.teacherId = ? AND bbs.category = 'R' ORDER BY bbs.regDate DESC LIMIT ?,?";
		List<BbsDTO> reviews = new ArrayList<>();
		System.out.println("offset : " + offset);
		System.out.println("pageSize : " + pageSize);
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, teacherId);
			pstm.setInt(2, offset);
			pstm.setInt(3, pageSize);
			rs = pstm.executeQuery();
			while(rs.next()) {
				BbsDTO review = new BbsDTO();
                review.setIdx(rs.getInt("idx"));
                review.setCourseCode(rs.getString("courseCode"));
                review.setMemberId(rs.getString("memberId"));
                review.setContent(rs.getString("content"));
                review.setRating(rs.getInt("rating"));
                review.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
                reviews.add(review);
			}

		}catch(Exception e) {
			System.out.println(e.getMessage());
	        System.out.println("선생님의 모든 후기 조회 오류");
		}
		return reviews;	
	}
	
	//후기조회시 totalCount 조회
	public int getTotalCount(String teacherId) {
		String sql = "SELECT COUNT(*) FROM tbl_course AS course "
				+ "JOIN tbl_bbs AS bbs ON course.courseCode = bbs.courseCode "
				+ "WHERE course.teacherId = ? AND bbs.category = 'R'";
	try {
		pstm = con.prepareStatement(sql);
		pstm.setString(1, teacherId);
		rs = pstm.executeQuery();
		if(rs.next()) {
			return rs.getInt(1);
		}
	
	}catch(Exception e) {
		System.out.println(e.getMessage());
	    System.out.println("선생님의 후기 토탈카운트 조회 오류");
	}
	return 0;	
		}
	}
