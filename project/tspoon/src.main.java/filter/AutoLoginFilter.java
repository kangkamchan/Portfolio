package net.tclass.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tclass.member.MemberDAO;
import net.tclass.member.MemberDTO;
import net.tclass.utils.CookieManager;

import java.io.IOException;

/**
 * Servlet Filter implementation class AutoLoginFilter
 */
@WebFilter("/*")
public class AutoLoginFilter extends HttpFilter implements Filter {
     HttpServletRequest req;
     HttpServletResponse res;
    /**
     * @see HttpFilter#HttpFilter()
     */
    public AutoLoginFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		this.req=(HttpServletRequest)request;
		this.res=(HttpServletResponse)response;
		
		if(req.getSession().getAttribute("loginDto")==null) {
			String savedId = CookieManager.cookieValue("savedId", req);
			if(!savedId.isEmpty()) {
				MemberDAO dao = new MemberDAO();
				MemberDTO dto = dao.getMemberById(savedId);
				dao.close();
				if(dto!=null) {
					req.getSession().setAttribute("loginDto", dto);
				}
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
