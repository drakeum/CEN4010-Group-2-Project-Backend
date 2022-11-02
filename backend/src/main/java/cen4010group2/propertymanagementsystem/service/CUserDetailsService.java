package cen4010group2.propertymanagementsystem.service;

import cen4010group2.propertymanagementsystem.model.CUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Hunter B.
 */

@Service
@RequiredArgsConstructor
public class CUserDetailsService implements UserDetailsService
{
    private final CUserServiceImpl cUserServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        CUser user = cUserServiceImpl.getCUserByEmail(email);
        return new User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, user.getAuthorities());
    }
}
