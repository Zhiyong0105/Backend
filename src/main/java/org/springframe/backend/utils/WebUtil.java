package org.springframe.backend.utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

public class WebUtil {
    public static String renderString(HttpServletResponse response, String str) {
        try{
            response.setStatus(200);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print(str);
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
