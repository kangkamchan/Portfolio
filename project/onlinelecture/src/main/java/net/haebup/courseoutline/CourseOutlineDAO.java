package net.haebup.courseoutline;

import net.haebup.common.DBConnPool;

public class CourseOutlineDAO extends DBConnPool {
	public int regist(CourseOutlineDTO dto) {
		System.out.println("CourseOutlineDAO > regist > start");
		String sql = "INSERT INTO tbl_course_outline (courseCode, outlineNo, content, time) "
				+ "VALUES(?, ?, ?, ?)";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, dto.getCourseCode());
			pstm.setInt(2, dto.getOutlineNo());
			pstm.setString(3, dto.getContent());
			pstm.setString(4, dto.getTime());
			System.out.println("CourseOutlineDAO > regist > end");
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("CourseOutlineDAO > regist > error");
			System.out.println(e.getMessage());
		}
		System.out.println("CourseOutlineDAO > regist > end");
		return 0;
	}
	public int getNextOutlineNo(String courseCode) {
		System.out.println("CourseOutlineDAO > getNextOutlineNo > start");
		String sql = "SELECT MAX(outlineNo)+1 FROM tbl_course_outline WHERE courseCode = ?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, courseCode);
			rs = pstm.executeQuery();
			if(rs.next()) {
				System.out.println("CourseOutlineDAO > getNextOutlineNo > end");
				return (rs.getInt(1)==0)? 1 : rs.getInt(1);
			}
		}catch(Exception e) {
			System.out.println("CourseOutlineDAO > getNextOutlineNo > error");
			System.out.println(e.getMessage());
		}
		System.out.println("CourseOutlineDAO > getNextOutlineNo > end");
		return 1;
	}
}
