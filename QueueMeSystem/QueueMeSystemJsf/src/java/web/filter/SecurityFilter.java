/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.filter;

import entity.AdminEntity;
import entity.BusinessEntity;
import entity.CustomerEntity;
import entity.FoodCourtEntity;
import entity.VendorEntity;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class SecurityFilter implements Filter {

    FilterConfig filterConfig;

    private static final String CONTEXT_ROOT = "/QueueMeSystemJsf";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();

        if (httpSession.getAttribute("isLogin") == null) {
            httpSession.setAttribute("isLogin", false);
        }

        Boolean isLogin = (Boolean) httpSession.getAttribute("isLogin");

        if (isLogin && requestServletPath.equals("/index.xhtml")) {
            System.err.println("true");
            httpServletResponse.sendRedirect(CONTEXT_ROOT + "/mainPage.xhtml");
        }

        if (!excludeLoginCheck(requestServletPath)) {
            if (isLogin) {
                BusinessEntity businessEntity = (BusinessEntity) httpSession.getAttribute("businessEntity");

                if (checkAccessRight(requestServletPath, businessEntity)) {
                    chain.doFilter(request, response);
                } else {
                    httpServletResponse.sendRedirect(CONTEXT_ROOT + "/mainPage.xhtml");
                }

            } else {
                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/index.xhtml");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public Boolean checkAccessRight(String path, BusinessEntity businessEntity) {
        if (businessEntity instanceof FoodCourtEntity) {

        } else if (businessEntity instanceof VendorEntity) {
            if (path.equals("/mainPage.xhtml")
                    || path.equals("/manageMenu.xhtml")) {
                return true;
            }
        } else if (businessEntity instanceof CustomerEntity) {

        } else if (businessEntity instanceof AdminEntity) {

        }

        return false;
    }

    @Override
    public void destroy() {
    }

    private Boolean excludeLoginCheck(String path) {
        if (path.equals("/index.xhtml")
                || path.equals("/forgetPassword.xhtml")
                || path.equals("/error404.xhtml")
                || path.startsWith("/images")
                || path.startsWith("/javax.faces.resource")) {
            return true;
        } else {
            return false;
        }
    }

}
