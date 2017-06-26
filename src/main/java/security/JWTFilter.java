package security;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import beans.jwt.MinimalJWTUser;
import io.jsonwebtoken.JwtException;
import services.security.jwt.JWTService;
 
/**
 * This class defines the filter applied to {@code /api/secure/*}.
 *
 */
@WebFilter(urlPatterns = { "/api/secure/*" })
public class JWTFilter implements Filter {
 
    @Autowired
    private JWTService jwtTokenService;
 
    @Value("${jwt.auth.header}")
    String authHeader;
 
    @Override public void init(FilterConfig filterConfig) throws ServletException  {}
    @Override public void destroy() {}
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final String authHeaderVal = httpRequest.getHeader(authHeader);
 
        if (authHeaderVal == null) {
        	
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
 
        try {
            MinimalJWTUser jwtUser = jwtTokenService.getUser(authHeaderVal);
            httpRequest.setAttribute("jwtUser", jwtUser);
        }
        catch(JwtException e) {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }
 
        chain.doFilter(httpRequest, httpResponse);
    }
}