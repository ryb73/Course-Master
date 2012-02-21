package com.coursemanager.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

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
     * @param chain The rest of the filter chain to
     *  pass to if we deem the request OK to continue
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.trace("Verifying request for resource");

        // Check request path/cookies

        chain.doFilter(request, response);
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
