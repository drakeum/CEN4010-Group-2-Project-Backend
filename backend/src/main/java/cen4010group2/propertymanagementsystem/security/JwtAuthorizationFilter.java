package cen4010group2.propertymanagementsystem.security;

import cen4010group2.propertymanagementsystem.service.CUserDetailsService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Hunter B.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter
{
    private static final String TOKEN_PREFIX = "Bearer";
    private final CUserDetailsService cUserDetailsService;
    private final String secret;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, CUserDetailsService cUserDetailsService, @Value("${jwt_secret}") String secret)
    {
        super(authenticationManager);
        this.cUserDetailsService = cUserDetailsService;
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        UsernamePasswordAuthenticationToken auth = getAuthentication(request);
        if(auth == null)
        {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request)
    {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(token == null || !token.startsWith(TOKEN_PREFIX))
        {
            return null;
        }
        String email = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        if(email == null) return null;
        UserDetails userDetails = cUserDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }
}
