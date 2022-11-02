/*
package cen4010group2.propertymanagementsystem;

import cen4010group2.propertymanagementsystem.model.CUser;
import cen4010group2.propertymanagementsystem.model.Role;
import cen4010group2.propertymanagementsystem.service.CUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

*/
/**
 * @author Hunter B.
 *//*


@Component
@RequiredArgsConstructor
public class InitUsers implements CommandLineRunner
{
    private final CUserServiceImpl cUserService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception
    {
        CUser user = cUserService.save(CUser.builder()
                .username("Test")
                .email("test@outlook.com")
                .password(passwordEncoder.encode("1234"))
                .role(Set.of(Role.ROLE_USER))
                .build());
        user.setEnabled(true);
        cUserService.save(user);
    }
}
*/
