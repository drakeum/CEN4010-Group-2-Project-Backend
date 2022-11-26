package cen4010group2.propertymanagementsystem.controller;

import cen4010group2.propertymanagementsystem.model.*;
import cen4010group2.propertymanagementsystem.repository.CUserRepository;
import cen4010group2.propertymanagementsystem.security.EditAccount;
import cen4010group2.propertymanagementsystem.security.Register;
import cen4010group2.propertymanagementsystem.service.CUserServiceImpl;
import cen4010group2.propertymanagementsystem.service.PropertyServiceImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Hunter B
 */

@RestController
public class CUserController
{
    private static final String TOKEN_PREFIX = "Bearer";

    private final String secret;
    private final CUserRepository cUserRepository;
    private final CUserServiceImpl cUserService;
    private final PasswordEncoder passwordEncoder;
    private final PropertyServiceImpl propertyService;

    public CUserController(CUserRepository cUserRepository, CUserServiceImpl cUserService, PasswordEncoder passwordEncoder, @Value("${jwt_secret}") String secret, PropertyServiceImpl propertyService)
    {
        this.cUserRepository = cUserRepository;
        this.cUserService = cUserService;
        this.passwordEncoder = passwordEncoder;
        this.secret = secret;
        this.propertyService = propertyService;
    }
    @GetMapping("/admin/getAllUsers")
    public List<CUser> getAllCUsers()
    {
        return cUserRepository.findAll();
    }

    @DeleteMapping("/admin/deleteUserAccount")
    public void deleteUserAccount(@RequestBody AdminDeleteUser adminDeleteUser)
    {
        CUser u = cUserService.getCUserByEmail(adminDeleteUser.getEmail());
        cUserService.delete(u);
        List<Property> userProperties = propertyService.getPropertiesByUser(u);
        Iterator<Property> iter = userProperties.iterator();
        while (iter.hasNext())
        {
            Property p = iter.next();
            propertyService.deleteProperty(p);
        }
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

    @GetMapping("/cuser/getRole")
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

    @DeleteMapping("/cuser/deleteAccount")
    public void deleteAccount(HttpServletRequest request)
    {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        CUser u = cUserService.getCUserByEmail(email);
        cUserService.delete(u);
        List<Property> userProperties = propertyService.getPropertiesByUser(u);
        Iterator<Property> iter = userProperties.iterator();
        while (iter.hasNext())
        {
            Property p = iter.next();
            propertyService.deleteProperty(p);
        }
        // TODO: Currently does not "log the user out" i.e. the JWT is still valid after account is deleted
    }

    @PutMapping("/cuser/editAccount")
    public void editAccount(@RequestBody EditAccount editAccount, HttpServletRequest request)
    {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        CUser u = cUserService.getCUserByEmail(email);
        String oldPass = editAccount.getOldPassword();
        if(passwordEncoder.matches(oldPass, u.getPassword()))
        {
            System.out.println("Old password matches");
            u.setUsername(editAccount.getUsername());
            u.setPassword(passwordEncoder.encode(editAccount.getNewPassword()));
            cUserService.save(u);
            System.out.println("New password saved");
        }
        else
        {
            System.out.println("Old password did not match");
            // TODO: Make return an error if old password isn't correct
        }

    }

    @PostMapping("/cuser/share")
    public void shareProperties(@RequestBody Share share, HttpServletRequest request)
    {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        CUser u1 = cUserService.getCUserByEmail(email);
        CUser u2 = cUserService.getCUserByEmail(share.getEmail());
        List<CUser> currentSharedWith = u1.getUsersSharedWith();
        currentSharedWith.add(u2);
        u1.setUsersSharedWith(currentSharedWith);
        cUserService.save(u1);

        List<CUser> currentSharedFrom = u2.getUsersSharedFrom();
        currentSharedFrom.add(u1);
        u2.setUsersSharedFrom(currentSharedFrom);
        cUserService.save(u2);
    }

    @PostMapping("/cuser/unshare")
    public void unShareProperties(@RequestBody Share share, HttpServletRequest request)
    {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        CUser u1 = cUserService.getCUserByEmail(email);
        CUser u2 = cUserService.getCUserByEmail(share.getEmail());
        List<CUser> currentSharedWith = u1.getUsersSharedWith();
        if(currentSharedWith.contains(u2))
        {
            currentSharedWith.remove(u2);
            u1.setUsersSharedWith(currentSharedWith);
            cUserService.save(u1);

            List<CUser> currentSharedFrom = u2.getUsersSharedFrom();
            currentSharedFrom.remove(u1);
            u2.setUsersSharedFrom(currentSharedFrom);
            cUserService.save(u2);
        }
    }

    @GetMapping("/cuser/me")
    public CUser getMe(HttpServletRequest request)
    {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        CUser u = cUserService.getCUserByEmail(email);
        return u;
    }

}
