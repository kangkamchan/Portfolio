package net.tclass.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.tclass.common.DBConnPool;

public class MessageDAO extends DBConnPool {
	
	//보낸쪽지리스트조회
	public List<MessageDTO> getSendList(Map<String,String> map){
		System.out.println("==============================");
		System.out.println("MessageDAO > getSendList > start");
		try {
			List<MessageDTO> list = new ArrayList<MessageDTO>();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT idx, sendId, receiveId, title, content, "
					+ "tmb.name, "
					+ "fileName, orgFileName, fileExt, filePath, fileSize, "
					+ "sendDel, receiveDel, tms.regDate, receiveDate "
					+ "FROM tbl_message AS tms INNER JOIN tbl_member AS tmb ON tms.receiveId = tmb.memberId "
					+ "WHERE sendId=? AND sendDel=? ORDER BY idx DESC LIMIT 0, ?");
			System.out.println("sql : " + sql);
			pstm = con.prepareStatement(sql.toString());
			pstm.setString(1, map.get("sendId"));
			pstm.setString(2, "N");
			pstm.setInt(3, Integer.parseInt(map.get("printCnt")));
			System.out.println("pstm : " + pstm);
			rs = pstm.executeQuery();
			while(rs.next()) {
				MessageDTO dto = new MessageDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setSendId(rs.getString("sendId"));
				dto.setReceiveId(rs.getString("receiveId"));
				dto.setReceiveName(rs.getString("tmb.name"));
				dto.setTitle((rs.getString("title").length()>20)? rs.getString("title").substring(0,20)+"..." : rs.getString("title"));
				dto.setContent(rs.getString("content"));
				if(rs.getString("fileName")!=null) dto.setFileName(rs.getString("fileName"));
				if(rs.getString("orgFileName")!=null) dto.setOrgFileName(rs.getString("orgFileName"));
				if(rs.getString("fileExt")!=null) dto.setFileExt(rs.getString("fileExt"));
				if(rs.getString("filePath")!=null) dto.setFilePath(rs.getString("filePath"));
				dto.setFileSize(rs.getInt("fileSize"));
				dto.setSendDel(rs.getString("sendDel"));
				dto.setReceiveDel(rs.getString("receiveDel"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				if(rs.getTimestamp("receiveDate")!=null)dto.setReceiveDate(rs.getTimestamp("receiveDate").toLocalDateTime());
				list.add(dto);
			}
			return list;
		}catch(Exception e) {
			System.out.println("MessageDAO > getSendList > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MessageDAO > getSendList > end");
		System.out.println("==============================");
		return null;
	}
	//쪽지상세조회
	public MessageDTO getMessage(int idx) {
		System.out.println("==============================");
		System.out.println("MessageDAO > getSendMessage > start");
		String sql = "SELECT idx, sendId, receiveId, title, content, "
				+ "fileName, orgFileName, fileExt, filePath, fileSize, "
				+ "sendDel, receiveDel, regDate, receiveDate "
				+ "FROM tbl_message WHERE idx=?";
		System.out.println("sql : " + sql);
		try {
			pstm=con.prepareStatement(sql);
			pstm.setInt(1, idx);
			rs=pstm.executeQuery();
			if(rs.next()) {
				MessageDTO dto = new MessageDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setSendId(rs.getString("sendId"));
				dto.setReceiveId(rs.getString("receiveId"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content").replace("\r\n", "<br>"));
				if(rs.getString("fileName")!=null) dto.setFileName(rs.getString("fileName"));
				if(rs.getString("orgFileName")!=null) dto.setOrgFileName(rs.getString("orgFileName"));
				if(rs.getString("fileExt")!=null) dto.setFileExt(rs.getString("fileExt"));
				if(rs.getString("filePath")!=null) dto.setFilePath(rs.getString("filePath"));
				dto.setFileSize(rs.getInt("fileSize"));
				dto.setSendDel(rs.getString("sendDel"));
				dto.setReceiveDel(rs.getString("receiveDel"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				if(rs.getTimestamp("receiveDate")!=null)dto.setReceiveDate(rs.getTimestamp("receiveDate").toLocalDateTime());
				return dto;
			}
		}catch(Exception e) {
			System.out.println("MessageDAO > getSendMessage > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MessageDAO > getSendMessage > end");
		System.out.println("==============================");
		return null;
	}
	//받은쪽지 조회시 받은 날짜 등록
	public boolean updateRecieveDate(int idx) {
		System.out.println("==============================");
		System.out.println("MessageDAO > updateRecieveDate > start");
		String sql = "UPDATE tbl_message SET receiveDate=NOW() WHERE idx = ?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, idx);
			int result = pstm.executeUpdate();
			if(result>0) {
				return true;
			}
		}catch(Exception e) {
			System.out.println("MessageDAO > updateRecieveDate > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MessageDAO > updateRecieveDate > end");
		System.out.println("==============================");
		return false;
	}
	//받은쪽지리스트조회
	public List<MessageDTO> getReceiveList(Map<String,String> map){
		System.out.println("==============================");
		System.out.println("MessageDAO > getReceiveList > start");
		try {
			List<MessageDTO> list = new ArrayList<MessageDTO>();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT idx, sendId, receiveId, title, content, "
					+ "tmb.name, "
					+ "fileName, orgFileName, fileExt, filePath, fileSize, "
					+ "sendDel, receiveDel, tms.regDate, receiveDate "
					+ "FROM tbl_message AS tms INNER JOIN tbl_member AS tmb ON tms.sendId = tmb.memberId "
					+ "WHERE receiveId=? AND receiveDel=? ORDER BY idx DESC LIMIT 0, ?");
			System.out.println("sql : " + sql);
			pstm = con.prepareStatement(sql.toString());
			pstm.setString(1, map.get("receiveId"));
			pstm.setString(2, "N");
			pstm.setInt(3, Integer.parseInt(map.get("printCnt")));
			System.out.println("pstm : " + pstm);
			rs = pstm.executeQuery();
			while(rs.next()) {
				MessageDTO dto = new MessageDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setSendId(rs.getString("sendId"));
				dto.setReceiveId(rs.getString("receiveId"));
				dto.setSendName(rs.getString("tmb.name"));
				dto.setTitle((rs.getString("title").length()>20)? rs.getString("title").substring(0,20)+"..." : rs.getString("title"));
				dto.setContent(rs.getString("content").replace("\r\n", "<br>"));
				if(rs.getString("fileName")!=null) dto.setFileName(rs.getString("fileName"));
				if(rs.getString("orgFileName")!=null) dto.setOrgFileName(rs.getString("orgFileName"));
				if(rs.getString("fileExt")!=null) dto.setFileExt(rs.getString("fileExt"));
				if(rs.getString("filePath")!=null) dto.setFilePath(rs.getString("filePath"));
				dto.setFileSize(rs.getInt("fileSize"));
				dto.setSendDel(rs.getString("sendDel"));
				dto.setReceiveDel(rs.getString("receiveDel"));
				dto.setRegDate(rs.getTimestamp("regDate").toLocalDateTime());
				if(rs.getTimestamp("receiveDate")!=null)dto.setReceiveDate(rs.getTimestamp("receiveDate").toLocalDateTime());
				list.add(dto);
			}
			return list;
		}catch(Exception e) {
			System.out.println("MessageDAO > getReceiveList > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MessageDAO > getReceiveList > end");
		System.out.println("==============================");
		return null;
	}

	//등록
	public int regist(MessageDTO dto) {
		System.out.println("==============================");
		System.out.println("MessageDAO > regist > start");
		StringBuilder sql = new StringBuilder();

		sql.append("INSERT INTO tbl_message (");
		sql.append("sendId, receiveId, title, content, ");
		sql.append("fileName, orgFileName, fileExt, filePath, fileSize");
		sql.append(") VALUES (");
		sql.append("?,?,?,?,");
		sql.append("?,?,?,?,?");
		sql.append(")");
		System.out.println("sql : " + sql);
		try {
			pstm = con.prepareStatement(sql.toString());
			pstm.setString(1, dto.getSendId());
			pstm.setString(2, dto.getReceiveId());
			pstm.setString(3, dto.getTitle());
			pstm.setString(4, dto.getContent());
			pstm.setString(5, dto.getFileName());
			pstm.setString(6, dto.getOrgFileName());
			pstm.setString(7, dto.getFileExt());
			pstm.setString(8, dto.getFilePath());
			pstm.setInt(9, dto.getFileSize());
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("MessageDAO > regist > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MessageDAO > regist > end");
		System.out.println("==============================");
		return 0;
	}
	//보낸쪽지삭제
	public int sendDelete(int idx, String memberId) {
		System.out.println("==============================");
		System.out.println("MessageDAO > delete > start");
		String sql = "UPDATE tbl_message SET sendDel=? WHERE idx=? AND sendId=?";
		System.out.println("sql : " + sql);
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1,"Y");
			pstm.setInt(2, idx);
			pstm.setString(3, memberId);
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("MessageDAO > delete > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MessageDAO > delete > end");
		System.out.println("==============================");
		return 0;
	}

	//받은쪽지삭제
	public int receiveDelete(int idx, String memberId) {
		System.out.println("==============================");
		System.out.println("MessageDAO > delete > start");
		String sql = "UPDATE tbl_message SET receiveDel=? WHERE idx=? AND redeiveId=?";
		System.out.println("sql : " + sql);
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1,"Y");
			pstm.setInt(2, idx);
			pstm.setString(3, memberId);
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("MessageDAO > delete > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MessageDAO > delete > end");
		System.out.println("==============================");
		return 0;
	}
	//쪽지 한번에 삭제
	public int deleteMessageList(List<Integer> messageIdxList, String category) {
		System.out.println("==============================");
		System.out.println("MessageDAO > deleteMessageList > start");
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE tbl_message SET "+ category +"Del = 'Y' WHERE ");
		Iterator<Integer> it = messageIdxList.iterator();
		while(it.hasNext()) {
			it.next();
			sql.append("idx=?");
			if(!it.hasNext()){
				break;
			}
			sql.append(" OR ");
		}
		it=null;
		System.out.println("sql : " + sql);
		try {
			pstm = con.prepareStatement(sql.toString());
			int cnt = 1;
			it = messageIdxList.iterator();
			while(it.hasNext()) {
				int idx = it.next();
				pstm.setInt(cnt++, idx);
			}
			System.out.println("pstm : " + pstm.toString());
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("MessageDAO > deleteMessageList > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MessageDAO > deleteMessageList > end");
		System.out.println("==============================");
		return 0;
	}
	//쪽지 한번에 읽기
	public int readMessageList(List<Integer> messageIdxList) {
		System.out.println("==============================");
		System.out.println("MessageDAO > readMessageList > start");
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE tbl_message SET receiveDate=NOW() WHERE ");
		Iterator<Integer> it = messageIdxList.iterator();
		while(it.hasNext()) {
			it.next();
			sql.append("(idx=? AND receiveDate IS NULL)");
			if(!it.hasNext()){
				break;
			}
			sql.append(" OR ");
		}
		it=null;
		System.out.println("sql : " + sql);
		try {
			pstm = con.prepareStatement(sql.toString());
			int cnt = 1;
			it = messageIdxList.iterator();
			while(it.hasNext()) {
				int idx = it.next();
				pstm.setInt(cnt++, idx);
			}
			System.out.println("pstm : " + pstm.toString());
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("MessageDAO > readMessageList > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MessageDAO > readMessageList > end");
		System.out.println("==============================");
		return 0;
	}
	//받은쪽지 수 조회
	public int totalReceivedMessageCount(String receiveId) {
		System.out.println("==============================");
		System.out.println("MessageDAO > totalReceivedMessageCount > start");
		String sql = "SELECT COUNT(*) FROM tbl_message WHERE receiveId=? AND receiveDel=?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, receiveId);
			pstm.setString(2, "N");
			rs = pstm.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		}catch(Exception e) {
			System.out.println("MessageDAO > totalReceivedMessageCount > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MessageDAO > totalReceivedMessageCount > end");
		System.out.println("==============================");
		return 0;
	}
	//안읽은 받은쪽지 수 조회
	public int unreadReceivedMessageCount(String receiveId) {
		System.out.println("==============================");
		System.out.println("MessageDAO > unreadReceivedMessageCount > start");
		String sql = "SELECT COUNT(*) FROM tbl_message WHERE receiveId=? AND receiveDel=? AND receiveDate IS NULL";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, receiveId);
			pstm.setString(2, "N");
			rs = pstm.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		}catch(Exception e) {
			System.out.println("MessageDAO > unreadReceivedMessageCount > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MessageDAO > unreadReceivedMessageCount > end");
		System.out.println("==============================");
		return 0;
	}
	
	//보낸쪽지상세조회
	public MessageDTO getSendMessage(int idx, String memberId) {
		System.out.println("==============================");
		System.out.println("MessageDAO > getSendMessage > start");
		try {
			
		}catch(Exception e) {
			System.out.println("MessageDAO > getSendMessage > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MessageDAO > getSendMessage > end");
		System.out.println("==============================");
		return null;
	}
	//받은쪽지상세조회
	public MessageDTO getReceiveMessage(int idx, String memberId) {
		System.out.println("==============================");
		System.out.println("MessageDAO > getReceiveMessage > start");
		try {
			
		}catch(Exception e) {
			System.out.println("MessageDAO > getReceiveMessage > error");
			System.out.println(e.getMessage());
		}
		System.out.println("MessageDAO > getReceiveMessage > end");
		System.out.println("==============================");
		return null;
	}
}
