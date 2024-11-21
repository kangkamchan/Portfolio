package net.haebup.payment;

import java.util.ArrayList;
import java.util.List;

import net.haebup.common.DBConnPool;

public class PaymentDAO extends DBConnPool{
	public PaymentDAO() {
		super();
	}
	
	//결제
	public boolean getPay(PaymentDTO dto) {
	    // endDate를 DATE_ADD로 계산하는 쿼리
	    String sqlInsert = "INSERT INTO tbl_payment (courseCode, memberId, price, viewStatus, startDate, endDate) VALUES (?, ?, ?, ?, NOW(), DATE_ADD(NOW(), INTERVAL ? DAY))";
	    String sqlCourseDuration = "SELECT courseDuration FROM tbl_course WHERE courseCode = ?";
	    
	    try {
	        // 1. courseCode에 해당하는 courseDuration 값을 가져오기
	        pstm = con.prepareStatement(sqlCourseDuration);
	        pstm.setString(1, dto.getCourseCode());
	        rs = pstm.executeQuery();

	        int courseDuration = 0;
	        if (rs.next()) {
	            courseDuration = rs.getInt("courseDuration");
	        }

	        // 2. 결제 정보 및 endDate 설정
	        if (courseDuration > 0) {
	            pstm = con.prepareStatement(sqlInsert);
	            pstm.setString(1, dto.getCourseCode());
	            pstm.setString(2, dto.getMemberId());
	            pstm.setInt(3, dto.getPrice());
	            pstm.setString(4, "N");
	            pstm.setInt(5, courseDuration);  // courseDuration을 INTERVAL로 사용하여 endDate 설정
	            
	            int rs = pstm.executeUpdate();
	            
	            if (rs > 0) {
	                return true;
	            }
	        }
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	        System.out.println("결제 오류");
	        e.printStackTrace();
	    }
	    return false;
	}


	//구매여부 확인
	public boolean getCoursePurchased(String memberId, String courseCode) {
		System.out.println(memberId);
		System.out.println(courseCode);
		String sql = "SELECT COUNT(courseCode) FROM tbl_payment WHERE memberId = ? AND courseCode = ? AND refund = ?";
		boolean result = false;
	    try {
	    	pstm = con.prepareStatement(sql);
	    	pstm.setString(1, memberId);
	    	pstm.setString(2, courseCode);
	    	pstm.setString(3, "N");
	    	rs = pstm.executeQuery();
	    	
	    	if(rs.next()) {
	    		result = rs.getInt(1) > 0;
	    	}
	    }catch(Exception e) {
	    	System.out.println(e.getMessage());
	        System.out.println("구매 안 함");
	        e.printStackTrace();
	    }
	    return result;
	}
	
	//구매 정보 가져옴
	public PaymentDTO getPurchasedInfo(String memberId, String courseCode) {
		String sql = "SELECT paymentCode, courseCode, memberId, price, paymentDate, refund, "
				+ "viewStatus, startDate, endDate FROM tbl_payment WHERE memberId = ? AND courseCode = ?";
		PaymentDTO dto = null;
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, memberId);
			pstm.setString(2, courseCode);
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				dto = new PaymentDTO();
				dto.setPaymentCode(rs.getInt("paymentCode"));
				dto.setCourseCode(rs.getString("courseCode"));
				dto.setMemberId(rs.getString("memberId"));
				dto.setPrice(rs.getInt("price"));
				if(rs.getTimestamp("paymentDate")!=null)dto.setPaymentDate(rs.getTimestamp("paymentDate").toLocalDateTime()); 
				dto.setRefund(rs.getString("refund"));
				dto.setViewStatus(rs.getString("viewStatus"));
				if(rs.getTimestamp("startDate")!=null)dto.setStartDate(rs.getTimestamp("startDate").toLocalDateTime());
				if(rs.getTimestamp("endDate")!=null)dto.setEndDate(rs.getTimestamp("endDate").toLocalDateTime());
				
			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
	        System.out.println("구매 정보 확인");
	        e.printStackTrace();
		}
		return dto;
	}
	
	//환불시 결제코드로 PaymentDTO 조회
	public PaymentDTO getPaymentInfo(int paymentCode) {
		String sql = "SELECT paymentCode, courseCode, memberId, price, paymentDate, refund, "
				+ "viewStatus, startDate, endDate FROM tbl_payment WHERE paymentCode = ?";
		PaymentDTO dto = null;
		try {
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, paymentCode);
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				dto = new PaymentDTO();
				dto.setPaymentCode(rs.getInt("paymentCode"));
				dto.setCourseCode(rs.getString("courseCode"));
				dto.setMemberId(rs.getString("memberId"));
				dto.setPrice(rs.getInt("price"));
				if(rs.getTimestamp("paymentDate")!=null)dto.setPaymentDate(rs.getTimestamp("paymentDate").toLocalDateTime()); 
				dto.setRefund(rs.getString("refund"));
				dto.setViewStatus(rs.getString("viewStatus"));
				if(rs.getTimestamp("startDate")!=null)dto.setStartDate(rs.getTimestamp("startDate").toLocalDateTime());
				if(rs.getTimestamp("endDate")!=null)dto.setEndDate(rs.getTimestamp("endDate").toLocalDateTime());
				
			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
	        System.out.println("구매 정보 확인");
	        e.printStackTrace();
		}
		return dto;
	}
	
	
	
	//아이디로 결제내역 리스트 조회
	public List<PaymentDTO> getList(String memberId, int offset, int pageSize){
		System.out.println("PaymentDAO > getList > start");
		String sql = "SELECT paymentCode, p.courseCode AS courseCode, c.courseName AS courseName, memberId, p.price AS price, paymentDate, "
				+ "refund, refundDate, startDate, viewStatus, endDate "
				+ "FROM tbl_payment AS p "
				+ "INNER JOIN tbl_course AS c ON p.courseCode = c.courseCode "
				+ "WHERE memberId=? "
				+ "ORDER BY paymentDate DESC "
				+ "LIMIT ?, ?";
		try {
			pstm=con.prepareStatement(sql);
			pstm.setString(1, memberId);
			pstm.setInt(2, offset);
			pstm.setInt(3, pageSize);
			rs=pstm.executeQuery();
			List<PaymentDTO> list = new ArrayList<PaymentDTO>();
			while(rs.next()) {
				PaymentDTO dto = new PaymentDTO();
				dto.setPaymentCode(rs.getInt("paymentCode"));
				dto.setCourseCode(rs.getString("courseCode"));
				dto.setCourseName(rs.getString("courseName"));
				dto.setMemberId(rs.getString("memberId"));
				dto.setPrice(rs.getInt("price"));
				dto.setPaymentDate(rs.getTimestamp("paymentDate").toLocalDateTime());
				dto.setRefund(rs.getString("refund"));
				if(rs.getTimestamp("refundDate")!=null)dto.setRefundDate(rs.getTimestamp("refundDate").toLocalDateTime());
				if(rs.getTimestamp("startDate")!=null)dto.setStartDate(rs.getTimestamp("startDate").toLocalDateTime());
				dto.setViewStatus(rs.getString("viewStatus"));
				if(rs.getTimestamp("endDate")!=null)dto.setEndDate(rs.getTimestamp("endDate").toLocalDateTime());
				list.add(dto);
			}
			System.out.println("PaymentDAO > getList > end");	
			return list;
		}catch(Exception e) {
			System.out.println("PaymentDAO > getList > error");
			System.out.println(e.getMessage());
		}
		System.out.println("PaymentDAO > getList > end");
		return null;
	}
	//아이디로 결제내역 조회시 totalcount 조회
	public int getTotalCountByMemberId(String memberId) {
		System.out.println("PaymentDAO > getTotalCountByMemberId > start");
		String sql = "SELECT COUNT(*) "
				+ "FROM tbl_payment "
				+ "WHERE memberId=? ";
		try {
			pstm=con.prepareStatement(sql);
			pstm.setString(1, memberId);
			rs=pstm.executeQuery();
			if(rs.next()) {
				System.out.println("PaymentDAO > getTotalCountByMemberId > end");	
				return rs.getInt(1);
			}
		}catch(Exception e) {
			System.out.println("PaymentDAO > getTotalCountByMemberId > error");
			System.out.println(e.getMessage());
		}
		System.out.println("PaymentDAO > getTotalCountByMemberId > end");
		return 0;
	}
	//환불 : refund 를 Y로 변경
	public int refund(int paymentCode) {
		System.out.println("PaymentDAO > refund > start");
		String sql = "UPDATE tbl_payment SET refund=?, endDate=NOW() WHERE paymentCode = ?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, "Y");
			pstm.setInt(2, paymentCode);
			System.out.println("PaymentDAO > refund > end");
			return pstm.executeUpdate();
		}catch(Exception e) {
			System.out.println("PaymentDAO > refund > error");
			System.out.println(e.getMessage());
		}
		System.out.println("PaymentDAO > refund > end");
		return 0;
	}
	
	public boolean modifyViewStatus(String courseCode, String memberId) {
		String sql = "UPDATE tbl_payment SET viewStatus = ? WHERE courseCode = ? AND memberId = ?";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, "Y");
			pstm.setString(2, courseCode);
			pstm.setString(3, memberId);
			int rs = pstm.executeUpdate();
			if(rs > 0 ) {
				return true;
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
	        System.out.println("강의 수강 viewStatus 변경");
	        e.printStackTrace();
		}
		return false;
	}
}
