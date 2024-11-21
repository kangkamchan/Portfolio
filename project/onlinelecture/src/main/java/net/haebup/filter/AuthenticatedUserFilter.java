package net.haebup.filter;

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
import net.haebup.member.MemberDTO;
import net.haebup.utils.CommonUtils;
import net.haebup.utils.JSFunc;

import java.io.IOException;

/**
 * Servlet Filter implementation class AuthenticatedUserFilter
 */
@WebFilter(filterName="AuthenticatedUserFilter", urlPatterns= {
		"/bbs/regist.do","/bbs/download.do","/course/learn.do","/course/purchased.do","/material/*","/member/modify.do"
		,"/member/delete.do","/myclass/*","/mypage/*","/payment/*","/review/regist.do"
		})
public class AuthenticatedUserFilter extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;
	HttpServletRequest req;
	HttpServletResponse res;
    /**
     * @see HttpFilter#HttpFilter()
     */
    public AuthenticatedUserFilter() {
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
		this.req = (HttpServletRequest)request;
		this.res = (HttpServletResponse)response;
		MemberDTO loginDto = (MemberDTO)req.getSession().getAttribute("loginDto");
//		System.out.println("referer : " + req.getHeader("referer"));
//		System.out.println("queryString : " + req.getQueryString());
//		System.out.println("refererURI : " + CommonUtils.getRefererUri(req.getHeader("referer")));
//		String refererUri = CommonUtils.getRefererUri(req.getHeader("referer"));
		if(loginDto==null) {
			JSFunc.alertLocation("로그인하세요","../login/login.do?", res);
			return;
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
