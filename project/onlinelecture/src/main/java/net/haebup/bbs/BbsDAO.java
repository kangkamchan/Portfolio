package net.haebup.bbs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.haebup.common.DBConnPool;

public class BbsDAO extends DBConnPool {
	//개별조회
	public BbsDTO getPostByIdx(int idx) {
		System.out.println("=============================");
		System.out.println("BbsDAO > getPostByIdx > start");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT idx, refIdx, levelIdx, sortOrder, category, "
				+ "memberId, title, content, regDate, modifyDate, "
				+ "readCnt, rating, courseCode, filePath, fileName, orgFileName, fileExt, fileSize "
				+ "FROM tbl_bbs "
				+ "WHERE idx=?");
		try {
			pstm = con.prepareStatement(sql.toString());
			pstm.setInt(1, idx);
			
			rs = pstm.executeQuery();

			if(rs.next()) {
				BbsDTO dto = new BbsDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setRefIdx(rs.getInt("refIdx"));
				dto.setLevelIdx(rs.getInt("levelIdx"));
				dto.setSortOrder(rs.getInt("sortOrder"));
				dto.setCategory(rs.getString("category"));
				dto.setMemberId(rs.getString("memberId"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				if(rs.getTimestamp("modifyDate")!=null) dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
				dto.setReadCnt(rs.getInt("readCnt"));
				dto.setRating(rs.getInt("rating"));
				dto.setCourseCode(rs.getString("courseCode"));
				if(rs.getString("fileName")!=null) dto.setFileName(rs.getString("fileName"));
				if(rs.getString("orgFileName")!=null) dto.setOrgFileName(rs.getString("orgFileName"));
				if(rs.getString("fileExt")!=null) dto.setFileExt(rs.getString("fileExt"));
				if(rs.getString("filePath")!=null) dto.setFilePath(rs.getString("filePath"));
				dto.setFileSize(rs.getInt("fileSize"));
				System.out.println("BbsDAO > getPostByIdx > end");
				System.out.println("=============================");
				return dto;
			}
		}catch(Exception e) {
			System.out.println("BbsDAO > getPostByIdx > error");
			System.out.println(e.getMessage());
		}
		System.out.println("BbsDAO > getPostByIdx > end");
		System.out.println("=============================");
		return null;
	}
	//조건에 맞는 전체 게시글 수 조회 -> 페이징블록에서 사용
	public int getTotalCount(Map<String,String> map) {
		System.out.println("=============================");
		System.out.println("BbsDAO > getTotalCount > start");
		String category = map.get("category");
		String levelIdx = map.get("levelIdx");
		String searchWord = map.get("searchWord");
		String searchCategory = map.get("searchCategory");
		String whereWord = map.get("whereWord");
		String whereValue = map.get("whereValue");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) FROM tbl_bbs "
				+ "WHERE CATEGORY = ? AND levelIdx = ? ");
		if(map.get("whereWord") !=null&&!map.get("whereWord").equals("")) {
			sql.append("AND "+whereWord+" = ? ");
		}
		if(map.get("searchWord") != null&&!map.get("searchWord").equals("")) {
			sql.append("AND ");
			sql.append(searchCategory);
			sql.append("LIKE '%");
			sql.append(searchWord);
			sql.append("%' ");
		}
		System.out.println(sql);
		try {
			
			pstm = con.prepareStatement(sql.toString());
			pstm.setString(1, category);
			pstm.setString(2, levelIdx);
			if(map.get("whereWord") !=null&&!map.get("whereWord").equals("")) {
				pstm.setString(3, whereValue);
			}
			System.out.println(pstm);
			rs = pstm.executeQuery();
			int result = 0;
			if(rs.next()) {
				result = rs.getInt(1);
			}
			System.out.println("BbsDAO > getTotalCount > end");
			System.out.println("=============================");
			return result;
		}catch(Exception e) {
			System.out.println("BbsDAO > getTotalCount > error");
			System.out.println(e.getMessage());
		}
		System.out.println("BbsDAO > getTotalCount > end");
		System.out.println("=============================");
		return 0;
	}
	//게시글리스트조회
	/*
	 * map 에 들어가야할 값
	 * 필수
	 * key : category, 값 : 게시글 종류 코드 F자유, R후기, M자료실, N공지
	 * key : pageNo, 값 : 출력할 페이지 번호
	 * key : pageSize, 값 : 페이지에 출력할 게시글 수
	 * key : levelIdx, 값 : 댓글이면 1 게시글이면 0
	 * 선택
	 * key : searchWord, 값 : 검색어
	 * key : searchCategory, 값 : 검색대상
	 * key : whereWord, 값 : where 조건절에 조건으로 들어갈 컬럼명
	 * key : whereValue, 값 : 강의후기, where 조건절에 조건으로 들어갈 값
	 * 
	 * 예)
	 * 마이페이지의 내가쓴 글 목록 : category - F, whereWord - memberId, whereValue - 아이디값
	 * 강의페이지의 후기목록 : category - R, whereWord - courseCode, whereValue - 강의코드값
	 */
	public List<BbsDTO> getPostList(Map<String,String> map){
		System.out.println("=============================");
		System.out.println("BbsDAO > getPostList > start");
		String category = map.get("category");
		int pageNo = Integer.parseInt(map.get("pageNo"));
		int pageSize = Integer.parseInt(map.get("pageSize"));
		int offset = (pageNo-1)*pageSize;
		String levelIdx = map.get("levelIdx");
		String searchWord = map.get("searchWord");
		String searchCategory = map.get("searchCategory");
		String whereWord = map.get("whereWord");
		String whereValue = map.get("whereValue");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT idx, refIdx, levelIdx, sortOrder, category, "
				+ "memberId, title, content, regDate, modifyDate, "
				+ "readCnt, rating, courseCode, filePath, fileName, orgFileName, fileExt, fileSize "
				+ "FROM tbl_bbs "
				+ "WHERE CATEGORY = ? AND levelIdx = ? ");
		if(map.get("whereWord") !=null&&!map.get("whereWord").equals("")) {
			sql.append("AND " + whereWord +" = ? ");
		}
		if(map.get("searchWord") != null&&!map.get("searchWord").equals("")) {
			sql.append("AND ");
			sql.append(searchCategory);
			sql.append("LIKE '%");
			sql.append(searchWord);
			sql.append("%' ");
		}
		sql.append("ORDER BY idx DESC LIMIT ?, ?");
		System.out.println(sql);
		try {
			pstm = con.prepareStatement(sql.toString());
			pstm.setString(1, category);
			pstm.setString(2, levelIdx);
			if(map.get("whereWord") !=null&&!map.get("whereWord").equals("")) {
				pstm.setString(3, whereValue);
				pstm.setInt(4,offset);
				pstm.setInt(5, pageSize);
			}else {
				pstm.setInt(3,offset);
				pstm.setInt(4, pageSize);
			}
			System.out.println(pstm);
			rs = pstm.executeQuery();
			List<BbsDTO> list = new ArrayList<BbsDTO>();
			while(rs.next()) {
				BbsDTO dto = new BbsDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setRefIdx(rs.getInt("refIdx"));
				dto.setLevelIdx(rs.getInt("levelIdx"));
				dto.setSortOrder(rs.getInt("sortOrder"));
				dto.setCategory(rs.getString("category"));
				dto.setMemberId(rs.getString("memberId"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				dto.setReadCnt(rs.getInt("readCnt"));
				dto.setRating(rs.getInt("rating"));
				dto.setCourseCode(rs.getString("courseCode"));
				if(rs.getTimestamp("modifyDate")!=null) dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
				if(rs.getString("fileName")!=null) dto.setFileName(rs.getString("fileName"));
				if(rs.getString("orgFileName")!=null) dto.setOrgFileName(rs.getString("orgFileName"));
				if(rs.getString("fileExt")!=null) dto.setFileExt(rs.getString("fileExt"));
				if(rs.getString("filePath")!=null) dto.setFilePath(rs.getString("filePath"));
				dto.setFileSize(rs.getInt("fileSize"));
				list.add(dto);
			}
			System.out.println("BbsDAO > getPostList > end");
			System.out.println("=============================");
			return list;
		}catch(Exception e) {
			System.out.println("BbsDAO > getPostList > error");
			System.out.println(e.getMessage());
		}
		System.out.println("BbsDAO > getPostList > end");
		System.out.println("=============================");
		return null;
	}	
	
	public boolean getRegistReview(BbsDTO dto) {
		String sql = "INSERT INTO tbl_bbs (refIdx, levelIdx, sortOrder, category, "
		           + "memberId, title, content, "
		           + "readCnt, rating, courseCode, filePath, fileName, orgFileName, fileExt, fileSize) "
		           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, dto.getRefIdx() != 0 ? dto.getRefIdx() : 0);
			pstm.setInt(2, dto.getLevelIdx() != 0 ? dto.getLevelIdx() : 0);
			pstm.setInt(3, dto.getSortOrder() != 0 ? dto.getSortOrder() : 0);
			pstm.setString(4, dto.getCategory());
			pstm.setString(5, dto.getMemberId());
			pstm.setString(6, dto.getTitle());
			pstm.setString(7, dto.getContent());
			pstm.setInt(8, dto.getReadCnt() != 0 ?  dto.getReadCnt() : 0);
			pstm.setInt(9, dto.getRating());
			pstm.setString(10, dto.getCourseCode());
			pstm.setString(11, dto.getFilePath() != null ?  dto.getFilePath() : null);
			pstm.setString(12, dto.getFileName() != null ?  dto.getFileName() : null);
			pstm.setString(13, dto.getOrgFileName() != null ?  dto.getOrgFileName() : null);
			pstm.setString(14, dto.getFileExt() != null ?  dto.getFileExt() : null);
			pstm.setInt(15, dto.getFileSize() != 0 ?  dto.getFileSize() : 0 );
			
			int rs = pstm.executeUpdate();
			if(rs > 0) {
				return true;
			}
		}catch(Exception e) {
			
		}
		return false;
	}
	//등록
	public boolean registPost(BbsDTO dto) {
		
		String sql = "INSERT INTO tbl_bbs (refIdx, levelIdx, sortOrder, category, "
		           + "memberId, title, content, "
		           + "readCnt, rating, courseCode, filePath, fileName, orgFileName, fileExt, fileSize) "
		           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, dto.getRefIdx() != 0 ? dto.getRefIdx() : 0);
			pstm.setInt(2, dto.getLevelIdx() != 0 ? dto.getLevelIdx() : 0);
			pstm.setInt(3, dto.getSortOrder() != 0 ? dto.getSortOrder() : 0);
			pstm.setString(4, dto.getCategory());
			pstm.setString(5, dto.getMemberId());
			pstm.setString(6, dto.getTitle());
			pstm.setString(7, dto.getContent());
			pstm.setInt(8, dto.getReadCnt() != 0 ?  dto.getReadCnt() : 0);
			pstm.setInt(9, dto.getRating());
			pstm.setString(10, dto.getCourseCode());
			pstm.setString(11, dto.getFilePath() != null ?  dto.getFilePath() : null);
			pstm.setString(12, dto.getFileName() != null ?  dto.getFileName() : null);
			pstm.setString(13, dto.getOrgFileName() != null ?  dto.getOrgFileName() : null);
			pstm.setString(14, dto.getFileExt() != null ?  dto.getFileExt() : null);
			pstm.setInt(15, dto.getFileSize() != 0 ?  dto.getFileSize() : 0 );
			
			int rs = pstm.executeUpdate();
			if(rs > 0) {
				return true;
			}
		}catch(Exception e) {
			
		}
		return false;
	}
		public boolean regist(BbsDTO dto) {
		String sql = "INSERT INTO tbl_bbs (refIdx, levelIdx, sortOrder, category, "
		           + "memberId, title, content, "
		           + "readCnt, rating, courseCode, filePath, fileName, orgFileName, fileExt, fileSize) "
		           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, dto.getRefIdx() != 0 ? dto.getRefIdx() : 0);
			pstm.setInt(2, dto.getLevelIdx() != 0 ? dto.getLevelIdx() : 0);
			pstm.setInt(3, dto.getSortOrder() != 0 ? dto.getSortOrder() : 0);
			pstm.setString(4, dto.getCategory());
			pstm.setString(5, dto.getMemberId());
			pstm.setString(6, dto.getTitle());
			pstm.setString(7, dto.getContent().replace("\r\n","<br>"));
			pstm.setInt(8, dto.getReadCnt() != 0 ?  dto.getReadCnt() : 0);
			pstm.setInt(9, dto.getRating());
			pstm.setString(10, dto.getCourseCode());
			pstm.setString(11, dto.getFilePath() != null ?  dto.getFilePath() : null);
			pstm.setString(12, dto.getFileName() != null ?  dto.getFileName() : null);
			pstm.setString(13, dto.getOrgFileName() != null ?  dto.getOrgFileName() : null);
			pstm.setString(14, dto.getFileExt() != null ?  dto.getFileExt() : null);
			pstm.setInt(15, dto.getFileSize() != 0 ?  dto.getFileSize() : 0 );
			
			int rs = pstm.executeUpdate();
			if(rs > 0) {
				return true;
			}
		}catch(Exception e) {
			
		}
		return false;
	}
	//수정
	public int modifyPost(BbsDTO dto) {
		System.out.println("BbsDAO > modifyPost > start");
		String sql = "UPDATE tbl_bbs SET refIdx=?, levelIdx=?, sortOrder=?, category=?, "
						           + "memberId=?, title=?, content=?, "
						           + "readCnt=?, rating=?, courseCode=?, "
						           + "filePath=?, fileName=?, orgFileName=?, "
						           + "fileExt=?, fileSize=? "
						           + "WHERE idx=?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, dto.getRefIdx());
			pstm.setInt(2, dto.getLevelIdx());
			pstm.setInt(3, dto.getSortOrder());
			pstm.setString(4, dto.getCategory());
			pstm.setString(5, dto.getMemberId());
			pstm.setString(6, dto.getTitle());
			pstm.setString(7, dto.getContent().replace("\r\n", "<br>"));
			pstm.setInt(8, dto.getReadCnt());
			pstm.setInt(9, dto.getRating());
			pstm.setString(10, dto.getCourseCode());
			pstm.setString(11, dto.getFilePath());
			pstm.setString(12, dto.getFileName());
			pstm.setString(13,dto.getOrgFileName());
			pstm.setString(14, dto.getFileExt());
			pstm.setInt(15, dto.getFileSize());
			pstm.setInt(16, dto.getIdx());
			System.out.println("BbsDAO > modifyPost > end");
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("BbsDAO > modifyPost > error");
			System.out.println(e.getMessage());
		}
		System.out.println("BbsDAO > modifyPost > end");
		return 0;
	}
	//삭제
	public int deletePost(BbsDTO dto) {
		System.out.println("BbsDAO > deletePost > start");
		String sql = "DELETE FROM tbl_bbs WHERE idx=?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, dto.getIdx());
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("BbsDAO > deletePost > error");
			System.out.println(e.getMessage());
		}
		System.out.println("BbsDAO > deletePost > end");
		return 0;
	}
	//여러개삭제
	public int deletePostList(List<BbsDTO> list) {
		return 0;
	}

}
