package com.coursemaster.server.filter;

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

import com.coursemaster.auth.AuthenticationManager;
import com.coursemaster.auth.Authenticator;

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

        String requestURI = httpRequest.getRequestURI();
        boolean authenticated = containsCookie(httpRequest, httpResponse);

        // Users who request secure content without
        // a session are redirected to the login page
        if (!isInsecureRequest(requestURI) && !authenticated) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.sendRedirect("/login.html?unauthorized=true");
            return;
        }

        // Send root requests to the dashboard,
        // as well as login page requests for users
        // who are already logged in.
        if ((requestURI.equals("/") || requestURI.equals("/login.html")) && authenticated) {
            httpResponse.sendRedirect("/service/dashboard");
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
        return requestURI.equals("/") ||
               requestURI.toLowerCase().contains("login") ||
               requestURI.equals("/favicon.ico");
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
            if (cookie.getName().equals(Authenticator.cookieName)) {

                // If the cookie matches the server cookie, look
                // for that cookie in the current list of sessions
                if(Authenticator.hasSession(cookie.getValue())) {
                    request.setAttribute("session", AuthenticationManager.authenticator.getSession(cookie.getValue()));
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
    // Not currently implemented
    public void init(FilterConfig arg0) throws ServletException { }

    @Override
    // Not currently implemented
    public void destroy() { }

    private static Logger logger = Logger.getLogger(RequestVerifier.class);
}
