package cen4010group2.propertymanagementsystem.controller;

import cen4010group2.propertymanagementsystem.model.CUser;
import cen4010group2.propertymanagementsystem.model.Role;
import cen4010group2.propertymanagementsystem.repository.CUserRepository;
import cen4010group2.propertymanagementsystem.security.Register;
import cen4010group2.propertymanagementsystem.service.CUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author Hunter B.
 */

@RestController
@AllArgsConstructor
public class CUserController
{
    private final CUserRepository cUserRepository;
    private final CUserServiceImpl cUserService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/getAll")
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
        user.setRole(Set.of(Role.ROLE_ADMIN));
        user.setEnabled(true);
        cUserService.save(user);
    }


    @GetMapping("/cuser")
    public String userEndpoint()
    {
        return "Hello, user";
    }

    @GetMapping("/admin")
    public String adminEndpoint()
    {
        return "Hello, admin";
    }
}
