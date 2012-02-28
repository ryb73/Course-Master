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

        // Users who request secure content without
        // a session are redirected to the login page
        if (!isInsecureRequest(httpRequest.getRequestURI()) && !containsCookie(httpRequest, httpResponse)) {
            httpResponse.sendRedirect("/login.html");
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Bump it down the chain if it's ok
        chain.doFilter(request, response);
    }

    /**
     * Method to determine whether the request being made
     * is for a secure resource.
     *
     * @param requestURI The URI of the request
     * @return Whether the request is for a secure resource
     */
    private boolean isInsecureRequest(String requestURI) {
        return (requestURI.toLowerCase().contains("login") ||
                requestURI.equals("/favicon.ico") ||
                requestURI.startsWith("/image/"));
    }

    /**
     * Method to determine if the request has a valid session
     * cookie attached
     *
     * @param cookies All the cookies that came with the request
     * @return Whether there is a valid session cookie
     */
    private boolean containsCookie(HttpServletRequest request, HttpServletResponse response) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(Settings.cookieName)) {

                // If the cookie matches the server cookie, look
                // for that cookie in the current list of sessions
                if(Settings.authenticator.hasSession(cookie.getValue())) {
                    request.setAttribute("session", Settings.authenticator.getSession(cookie.getValue()));
                    return true;
                }
            
                // If the user has a cookie that matches this servers cookie, but the
                // server doesn't recognize the cookie, revoke the cookie
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

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
