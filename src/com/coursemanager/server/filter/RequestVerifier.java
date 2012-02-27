package com.coursemanager.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.coursemanager.server.Settings;

/**
 * This class filters all requests hitting the server. It is capable of
 * checking for proper sessions and can also redirect unauthorized users
 * to the root or login page.
 *
 * @author Graham
 */
public class RequestVerifier implements Filter {

    @Override
    /**
     * Where the actual filtering takes place
     *
     * @param request The HTTP Request to inspect
     * @param response The response to write
     * @param chain The rest of the filter chain to pass to if we deem the request OK to continue
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.trace("Verifying request for resource");

        // Convert the requests into Http correspondent
        HttpServletRequest  httpRequest  = (HttpServletRequest)  request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getRequestURI().toLowerCase().matches(".*login.*")) {
            logger.trace("Login resource request");

            if (httpRequest.getRequestURI().equals("/login.do") && httpRequest.getMethod().equals("POST")) {
                String username = httpRequest.getParameter("username"),
                       password = httpRequest.getParameter("password");

                if (username == null || password == null) {
                    httpResponse.sendRedirect("/");
                    httpResponse.setStatus(HttpServletResponse.SC_OK);
                    httpResponse.getWriter().write("Both username and password are required to login");
                    return;
                }

                Cookie sessionCookie = Settings.authenticator.login(username, password);

                if (sessionCookie == null) {
                    httpResponse.sendRedirect("/login.html");
                    httpResponse.setStatus(HttpServletResponse.SC_OK);
                    httpResponse.getWriter().write("Failed to login with that username/password combination");
                    return;
                }

                httpResponse.addCookie(sessionCookie);
                httpResponse.sendRedirect("/dashboard.html");
                httpResponse.setContentLength(0);
                return;
            }
        }

        else if (isSecureRequest(httpRequest.getRequestURI()) && !containsCookie(httpRequest.getCookies())) {
            httpResponse.sendRedirect("/login.html");
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentLength(0);
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * Method to determine whether the request being made
     * is for a secure resource.
     *
     * @param requestURI The URI of the request
     * @return Whether the request is for a secure resource
     */
    private boolean isSecureRequest(String requestURI) {
        return !requestURI.matches("/(favicon.ico|image/).*$");
    }

    /**
     * Method to determine if the request has a valid session
     * cookie attached
     *
     * @param cookies All the cookies that came with the request
     * @return Whether there is a valid session cookie
     */
    private boolean containsCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies)
            if (cookie.getName().equals(Settings.cookieName) && 
                Settings.authenticator.hasSession(cookie.getValue()))
                    return true;

        return false;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    private static Logger logger = Logger.getLogger(RequestVerifier.class);
}
