package cen4010group2.propertymanagementsystem.controller;

import cen4010group2.propertymanagementsystem.model.CUser;
import cen4010group2.propertymanagementsystem.model.Role;
import cen4010group2.propertymanagementsystem.repository.CUserRepository;
import cen4010group2.propertymanagementsystem.security.Register;
import cen4010group2.propertymanagementsystem.service.CUserServiceImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @author Hunter B.
 */

@RestController
public class CUserController
{
    private static final String TOKEN_PREFIX = "Bearer";

    private final String secret;
    private final CUserRepository cUserRepository;
    private final CUserServiceImpl cUserService;
    private final PasswordEncoder passwordEncoder;

    public CUserController(CUserRepository cUserRepository, CUserServiceImpl cUserService, PasswordEncoder passwordEncoder, @Value("${jwt_secret}") String secret)
    {
        this.cUserRepository = cUserRepository;
        this.cUserService = cUserService;
        this.passwordEncoder = passwordEncoder;
        this.secret = secret;

    }
    @GetMapping("admin/getAll")
    public List<CUser> getAllCUsers()
    {
        return cUserRepository.findAll();
    }

    @PostMapping("/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void register(@RequestBody Register registerCredentials)
    {
        CUser user = new CUser();
        user.setUsername(registerCredentials.getUsername());
        user.setEmail(registerCredentials.getEmail());
        user.setPassword(passwordEncoder.encode(registerCredentials.getPassword()));
        user.setRole(Set.of(Role.ROLE_USER));
        user.setEnabled(true);
        cUserService.save(user);
    }

    @GetMapping("cuser/getRole")
    public Set<Role> getUserRole(HttpServletRequest request)
    {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        CUser u = cUserService.getCUserByEmail(email);
        return u.getRole();
    }



}
