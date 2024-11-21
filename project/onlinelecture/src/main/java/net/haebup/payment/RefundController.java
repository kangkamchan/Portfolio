package net.haebup.payment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet implementation class RefundController
 */
@WebServlet("/payment/refund.do")
public class RefundController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RefundController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("RefundController > doGet > start");
		int paymentCode = CommonUtils.ifNullInt(request.getParameter("paymentCode"), 0);
		System.out.println("paymentCode : "+paymentCode);
		if(paymentCode==0) {
			JSFunc.alertBack("잘못된 접근입니다.", response);
			return;
		}
		PaymentDAO dao = new PaymentDAO();
		PaymentDTO dto = dao.getPaymentInfo(paymentCode);
		System.out.println("dto : "+dto);
		if(dto==null) {
			dao.close();
			JSFunc.alertBack("잘못된 접근입니다.", response);
			return;
		}
		if(dto.getViewStatus().equals("Y")) {
			dao.close();
			JSFunc.alertBack("이미 시청한 강의는 환불이 불가합니다. 고객센터로 연락해주세요.", response);
			return;
		}
		if(dto.getRefund().equals("Y")) {
			dao.close();
			JSFunc.alertBack("이미 환불이 완료된 강의입니다.", response);
			return;
		}
		int result = dao.refund(paymentCode);
		dao.close();
		if(result<=0) {
			JSFunc.alertBack("환불시 오류가 발생했습니다.", response);
			return;
		}
		JSFunc.alertLocation("강의 환불 완료", "../mypage/view.do?action=paymentList", response);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
