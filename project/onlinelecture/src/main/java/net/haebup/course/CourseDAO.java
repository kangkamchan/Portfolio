package net.haebup.course;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.haebup.bbs.BbsDAO;
import net.haebup.bbs.BbsDTO;
import net.haebup.common.DBConnPool;
import net.haebup.courseoutline.CourseOutlineDTO;

public class CourseDAO extends DBConnPool{

	
	
	public CourseDAO() {
		super();
	}
	
	public int getTotalCourseCnt(Map<String, String> map) {
	    String searchKey = map.get("searchKey");
	    String searchValue = map.get("searchValue");
	    String subjectCode = map.get("subjectCode");

	    StringBuilder sql = new StringBuilder("SELECT COUNT(courseCode) FROM tbl_course");
	    boolean hasCondition = false; // WHERE 조건 여부 확인

	    // 과목 코드가 있을 경우
	    if (subjectCode != null && !subjectCode.isEmpty()) {
	        sql.append(" WHERE subjectCode = ?");
	        hasCondition = true;
	    }

	    // 검색어가 있을 경우
	    if (searchValue != null && !searchValue.isEmpty()) {
	        if (hasCondition) {
	            sql.append(" AND ");
	        } else {
	            sql.append(" WHERE ");
	        }
	        sql.append(searchKey).append(" LIKE ?");
	    }

	    int total = 0;
	    try {
	        pstm = con.prepareStatement(sql.toString());
	        int idx = 1;

	        if (subjectCode != null && !subjectCode.isEmpty()) {
	            pstm.setString(idx++, subjectCode);
	        }

	        if (searchValue != null && !searchValue.isEmpty()) {
	            pstm.setString(idx, "%" + searchValue + "%");
	        }

	        rs = pstm.executeQuery();
	        if (rs.next()) {
	            total = rs.getInt(1);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return total;
	}

	public List<CourseDTO> getCourseList(Map<String, String> map, int startIdx, int pageSize) {
	    String searchKey = map.get("searchKey");
	    String searchValue = map.get("searchValue");
	    String subjectCode = map.get("subjectCode");

	    StringBuilder sql = new StringBuilder("SELECT courseCode, subjectCode, courseName, teacherId, price, c.regDate, c.status, c.modifyDate, c.deleteDate, totalAttendee, courseDuration, m.name AS teacherName "
	    		+ "FROM tbl_course AS c "
	    		+ "INNER JOIN tbl_member AS m ON c.teacherId = m.memberId "
	    		);
	    boolean hasCondition = false;

	    // 과목 코드가 있을 경우
	    if (subjectCode != null && !subjectCode.isEmpty()) {
	        sql.append(" WHERE subjectCode = ?");
	        hasCondition = true;
	    }

	    // 검색어가 있을 경우
	    if (searchValue != null && !searchValue.isEmpty()) {
	        if (hasCondition) {
	            sql.append(" AND ");
	        } else {
	            sql.append(" WHERE ");
	        }
	        sql.append(searchKey).append(" LIKE ?");
	    }
	    
	    sql.append(" ORDER BY courseCode DESC LIMIT ?, ?");

	    List<CourseDTO> list = new ArrayList<>();
	    try {
	        pstm = con.prepareStatement(sql.toString());
	        int idx = 1;

	        if (subjectCode != null && !subjectCode.isEmpty()) {
	            pstm.setString(idx++, subjectCode);
	        }

	        if (searchValue != null && !searchValue.isEmpty()) {
	            pstm.setString(idx++, "%" + searchValue + "%");
	        }

	        pstm.setInt(idx++, startIdx);
	        pstm.setInt(idx, pageSize);

	        rs = pstm.executeQuery();

	        while (rs.next()) {
	            CourseDTO dto = new CourseDTO();
	            dto.setCourseCode(rs.getString("courseCode"));
	            dto.setCourseName(rs.getString("courseName"));
	            dto.setPrice(rs.getInt("price"));
	            dto.setTeacherId(rs.getString("teacherId"));
	            dto.setCourseDuration(rs.getInt("courseDuration"));
	            dto.setTotalAttendee(rs.getInt("totalAttendee"));
	            list.add(dto);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}

	
	//강의 상세보기
	//강좌 기본 정보 조회
	/*
	 * 모든 컬럼의 정보 가져오도록 수정함
	 */
	
	public CourseDTO getCourseInfo(String courseCode) {
		String sql = "SELECT courseCode, subjectCode, courseName, teacherId, price, "
				+ "c.regDate, c.status, c.modifyDate, c.deleteDate, totalAttendee, "
				+ "courseDuration, m.name AS teacherName , introduce "+
                "FROM tbl_course as c " +
                "JOIN tbl_member as m ON m.memberId = c.teacherId " +
                "WHERE c.courseCode = ?";
		//System.out.println("sql : " + sql);
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, courseCode);
			rs = pstm.executeQuery();
			System.out.println("pstm : " + pstm);
			if(rs.next()) {
				CourseDTO dto = new CourseDTO();
				dto.setCourseCode(rs.getString("courseCode"));
				dto.setSubjectCode(rs.getString("subjectCode"));
				dto.setCourseName(rs.getString("courseName"));
				dto.setTeacherId(rs.getString("teacherId"));
				dto.setTeacherName(rs.getString("teacherName"));
				dto.setPrice(rs.getInt("price"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				dto.setStatus(rs.getString("status"));
				if(rs.getTimestamp("modifyDate")!=null)dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
				if(rs.getTimestamp("deleteDate")!=null)dto.setDeleteDate(rs.getTimestamp("deleteDate").toLocalDateTime());
				dto.setTotalAttendee(rs.getInt("totalAttendee"));
				dto.setCourseDuration(rs.getInt("courseDuration"));
				dto.setIntroduce(rs.getString("introduce").replace("\r\n", "<br>"));
				return dto;
			}

		}catch(Exception e) {
			System.out.println(e.getMessage());
	        System.out.println("강의 기본 정보 조회 오류");
	        e.printStackTrace();
		}
		return null;
    }
	
	// 후기조회
//    public List<BbsDTO> getCourseReviews(String courseCode, String category) {
//        return bbsDao.getCourseReviews(courseCode, category);
//    }
	
	// 커리큘럼 조회 
    public List<CourseOutlineDTO> getCourseChapters(String courseCode) {
        String sql = "SELECT idx, courseCode, outlineNo, content, time FROM tbl_course_outline WHERE courseCode = ?";
        List<CourseOutlineDTO> list = new ArrayList<CourseOutlineDTO>();
        try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, courseCode);
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				CourseOutlineDTO dto = new CourseOutlineDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setCourseCode(rs.getString("courseCode"));
				dto.setOutlineNo(rs.getInt("outlineNo"));
				dto.setContent(rs.getString("content"));
				dto.setTime(rs.getString("time"));
				
				list.add(dto);
			}

		}catch(Exception e) {
			System.out.println(e.getMessage());
	        System.out.println("강의 목차 정보 조회 오류");
	        e.printStackTrace();
		}
        System.out.println(list);
        return list;
    }
        //영상 정보 가져오기
    public CourseOutlineDTO getCourseLearnInfo(String courseCode, int outlineNo) {
        String sql = "SELECT idx, courseCode, outlineNo, content, time FROM tbl_course_outline WHERE courseCode = ? AND outlineNo =?";
        CourseOutlineDTO dto = null;
        try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, courseCode);
			pstm.setInt(2, outlineNo);
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				dto = new CourseOutlineDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setCourseCode(rs.getString("courseCode"));
				dto.setOutlineNo(rs.getInt("outlineNo"));
				dto.setContent(rs.getString("content"));
				dto.setTime(rs.getString("time"));
				return dto;
			}

		}catch(Exception e) {
			System.out.println(e.getMessage());
	        System.out.println("강의 목차 정보 조회 오류");
	        e.printStackTrace();
		}
        return null;
    }

    //=================================================================================================================================================================================================
    //=================================================================================================================================================================================================
    //=================================================================================================================================================================================================
    //강감찬
    //해당과목 다음 등록될 강의코드 가져오기
    public String getNextCourseCode(String subjectCode) {
    	System.out.println("CourseDAO getNextCourseCode start");
    	String sql = "SELECT courseCode "
    			+ "FROM tbl_course "
    			+ "WHERE courseCode LIKE ? ORDER BY courseCode DESC LIMIT 1";
    	try {
    		pstm = con.prepareStatement(sql);
    		pstm.setString(1, "%"+subjectCode+"%");
    		rs = pstm.executeQuery();
    		if(rs.next()) {
    			StringBuilder nextCode = new StringBuilder(subjectCode);
    			int next = Integer.parseInt(rs.getString(1).replace(subjectCode, ""))+1;
    			for(int i = 3 ; i > next/10; i-- ) {
    				nextCode.append("0");
    			}
    	    	System.out.println("nextCode : " + nextCode);
    	    	System.out.println("CourseDAO getNextCourseCode end");
    			return nextCode.append(String.valueOf(next)).toString();
    		}
    	}catch(Exception e) {
        	System.out.println("CourseDAO getNextCourseCode error");
    		System.out.println(e.getMessage());
    	}
    	System.out.println("CourseDAO getNextCourseCode end");
    	return subjectCode+"0001";
    }

    //강의 등록
    public int regist(CourseDTO dto) {
    	System.out.println("CourseDAO regist start");
    	String sql = "INSERT INTO tbl_course("
    			+ "courseCode, subjectCode, courseName, "
    			+ "teacherId, price, courseDuration, introduce"
    			+ ")"
    			+ "VALUES ("
    			+ "?, ?, ?, "
    			+ "?, ?, ?, ?"
    			+ ")"
    			;
    	try {
    		pstm = con.prepareStatement(sql);
    		pstm.setString(1, dto.getCourseCode());
    		pstm.setString(2, dto.getSubjectCode());
    		pstm.setString(3, dto.getCourseName());
    		pstm.setString(4, dto.getTeacherId());
    		pstm.setInt(5,dto.getPrice());
    		pstm.setInt(6, dto.getCourseDuration());
    		pstm.setString(7, dto.getIntroduce());
    		System.out.println(pstm);
    		return pstm.executeUpdate();
    	}catch(Exception e) {
    		System.out.println("CourseDAO regist error");
    		System.out.println(e.getMessage());
    	}
    	System.out.println("CourseDAO regist end");
    	return 0;
    }
    //강의 수정
    public int modify(CourseDTO dto) {
    	System.out.println("CourseDAO modify start");
    	String sql = "UPDATE tbl_course "
    			+ "SET courseName=?, price=?, status=?, modifyDate=NOW(), totalAttendee=?, courseDuration=?, introduce=? "
    			+ "WHERE courseCode=?";
    	try {
    		pstm = con.prepareStatement(sql);
    		pstm.setString(1, dto.getCourseName());
    		pstm.setInt(2, dto.getPrice());
    		pstm.setString(3, dto.getStatus());
    		pstm.setInt(4, dto.getTotalAttendee());
    		pstm.setInt(5, dto.getCourseDuration());
    		pstm.setString(6, dto.getIntroduce());
    		pstm.setString(7, dto.getCourseCode());    		
    		return pstm.executeUpdate();
    	}catch(Exception e) {
    		System.out.println("CourseDAO modify error");
    		System.out.println(e.getMessage());
    	}
    	System.out.println("CourseDAO modify end");
    	return 0;
    }
    
	// 강의 목록 가져오기
	public List<CourseDTO> getCourseList(Map<String, String> map) {
	    String searchKey = map.get("searchKey");
	    String searchValue = map.get("searchValue");
	    int offset = Integer.parseInt(map.get("offset"));
	    int pageSize = Integer.parseInt(map.get("pageSize"));
	    StringBuilder sql = new StringBuilder("SELECT courseCode, subjectCode, courseName, teacherId, price, c.regDate, c.status, c.modifyDate, c.deleteDate, totalAttendee, courseDuration, m.name AS teacherName "
	    		+ "FROM tbl_course AS c "
	    		+ "INNER JOIN tbl_member AS m ON c.teacherId = m.memberId "
	    		);
	    
	    // 검색이 있을 때
	    if (searchValue != null && !searchValue.isEmpty()) {
	        sql.append(" WHERE " + searchKey + " LIKE ? ");
	    }
	    sql.append(" ORDER BY courseCode DESC LIMIT ?, ? "); // 강의 목록 페이징 처리

	    try {
	        int idx = 1;  // 인덱스 초기화
	        pstm = con.prepareStatement(sql.toString());
	        // 검색 조건이 있을 경우
	        if (searchValue != null && !searchValue.isEmpty()) {
	            pstm.setString(idx++, "%" + searchValue + "%"); // 검색 값 설정
	        }
	        pstm.setInt(idx++, offset);  // 시작 인덱스 설정
	        pstm.setInt(idx, pageSize);    // 페이지 크기 설정
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
	            dto.setTeacherName(rs.getString("teacherName"));
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
	//강의 목록 조회 : 선생님 아이디 해당하는거 모두 조회하기
	public List<CourseDTO> getCourseList(String teacherId) {
	    String sql = "SELECT courseCode, subjectCode, courseName, teacherId, price, regDate, status, modifyDate, deleteDate, totalAttendee, courseDuration FROM tbl_course WHERE teacherId=?";
	     // 강의 목록 페이징 처리

	    try {

	        pstm = con.prepareStatement(sql.toString());
	        // 검색 조건이 있을 경우

	        pstm.setString(1, teacherId);  // 시작 인덱스 설정
	        
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
    //=================================================================================================================================================================================================
    //=================================================================================================================================================================================================
    //=================================================================================================================================================================================================
	//선생님의 모든 정보 조회(선생님 페이지에서 사용)
	public CourseDTO getCourse(String teacherId) {
	    String sql = "SELECT courseCode, subjectCode, courseName, "
	    		+ "teacherId, price, c.regDate, "
	    		+ "c.status, c.modifyDate, c.deleteDate, "
	    		+ "totalAttendee, courseDuration, m.name AS teacherName "
	    		+ "FROM tbl_course AS c "
	    		+ "INNER JOIN tbl_member AS m ON c.teacherId = m.memberId "
	    		+ "WHERE teacherId=? "
	    		+ "LIMIT 1";
	    
	    try {
	        pstm = con.prepareStatement(sql);
	        pstm.setString(1, teacherId);

	        rs = pstm.executeQuery();
	        
	        // 단일 결과만 조회
	        if (rs.next()) {
	            CourseDTO dto = new CourseDTO();
	            dto.setCourseCode(rs.getString("courseCode"));
	            dto.setSubjectCode(rs.getString("subjectCode"));
	            dto.setCourseName(rs.getString("courseName"));
	            dto.setPrice(rs.getInt("price"));
	            dto.setTeacherId(rs.getString("teacherId"));
	            dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
	            dto.setStatus(rs.getString("status"));
	            dto.setTeacherName(rs.getString("teacherName"));
	            if (rs.getTimestamp("modifyDate") != null) {
	                dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
	            }
	            
	            if (rs.getTimestamp("deleteDate") != null) {
	                dto.setDeleteDate(rs.getTimestamp("deleteDate").toLocalDateTime());
	            }
	            
	            dto.setCourseDuration(rs.getInt("courseDuration"));
	            dto.setTotalAttendee(rs.getInt("totalAttendee"));
	            return dto;  // 단일 CourseDTO 객체 반환
	        }
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	        System.out.println("강의 조회 오류");
	    }
	    return null;  // 결과가 없을 경우 null 반환
	}


}
