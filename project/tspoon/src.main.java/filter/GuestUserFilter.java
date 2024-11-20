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
import net.tclass.member.MemberDTO;
import net.tclass.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet Filter implementation class GuestUserFilter
 */
@WebFilter(filterName="GuestUserFilter", urlPatterns= {"/login/login.do","/login/findId.do","/login/findPwd.do","/member/regist.do"})
public class GuestUserFilter extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;
	HttpServletRequest req;
	HttpServletResponse res;
    /**
     * @see HttpFilter#HttpFilter()
     */
    public GuestUserFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		System.out.println("filter destroy");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		this.req = (HttpServletRequest)request;
		this.res = (HttpServletResponse)response;
		MemberDTO loginDto = (MemberDTO)req.getSession().getAttribute("loginDto");
		
		if(loginDto!=null) {
			JSFunc.alertBack("정상적인 접근이 아닙니다", res);
			return;
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("filter init");
	}

}
