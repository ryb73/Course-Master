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

        if (!containsCookie(httpRequest.getCookies())) {
            httpResponse.addCookie(Settings.authenticator.login("Junk", "More Junk"));
        }

        chain.doFilter(request, response);
    }

    private boolean containsCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies)
            if (cookie.getName().equals(Settings.cookieName))
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
