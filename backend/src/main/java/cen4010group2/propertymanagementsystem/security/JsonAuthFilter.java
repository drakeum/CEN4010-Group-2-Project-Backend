package cen4010group2.propertymanagementsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author Hunter B.
 */
public class JsonAuthFilter extends UsernamePasswordAuthenticationFilter
{
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            Login authRequest = objectMapper.readValue(sb.toString(), Login.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(), authRequest.getPassword());
            setDetails(request, token);
            return this.getAuthenticationManager().authenticate(token);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
