package net.fullstack7.mooc.util;

import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JSFunc {
    public static void alertLocation(String msg, String url, jakarta.servlet.http.HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            String code = "<script>"
                    + "alert('" + msg + "');"
                    + "location.href='" + url + "';"
                    + "</script>";
            writer.print(code);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void back(jakarta.servlet.http.HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            String code = "<script>"
                    + "history.back();"
                    + "</script>";
            writer.print(code);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void alertBack(String msg, jakarta.servlet.http.HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            String code = "<script>"
                    + "alert('" + msg + "');"
                    + "history.back();"
                    + "</script>";
            writer.print(code);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void alert(String msg, jakarta.servlet.http.HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            String code = "<script>"
                    + "alert('" + msg + "');"
                    + "</script>";
            writer.print(code);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
