package net.fullstack7.mooc.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.fullstack7.mooc.service.AdminServiceIf;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.net.URLEncoder;

//@WebFilter(urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    private AdminServiceIf adminService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        adminService = (AdminServiceIf) WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext()).getBean("adminServiceImpl");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if(request.getRequestURI().equals("/admin/login")) {
            filterChain.doFilter(request, response);
        }

        else {
            String adminId = (String) request.getSession().getAttribute("loginAdminId");

            if (adminId == null || !adminService.existsAdmin(adminId)) {
                String originalURL = request.getRequestURI();
                if (request.getQueryString() != null) {
                    originalURL += "?" + request.getQueryString();
                }
                request.getSession().setAttribute("redirectAfterLogin", URLEncoder.encode(URLEncoder.encode(originalURL, "UTF-8"), "UTF-8"));
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<script>");
                response.getWriter().println("alert('관리자 로그인 이후 이용 가능합니다');");
                response.getWriter().println("location.href='/admin/login';");
                response.getWriter().println("</script>");
            }
            else filterChain.doFilter(request, response);
        }

    }
}
