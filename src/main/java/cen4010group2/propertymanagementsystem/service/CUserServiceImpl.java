package cen4010group2.propertymanagementsystem.service;

import cen4010group2.propertymanagementsystem.model.CUser;
import cen4010group2.propertymanagementsystem.repository.CUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * @author Hunter B.
 */
@Service
@RequiredArgsConstructor
public class CUserServiceImpl implements CUserService
{
    private final CUserRepository cUserRepository;

    @Override
    public CUser save(CUser user)
    {
        return cUserRepository.save(user);
    }

    public Optional<CUser> findCUserByEmail(String email)
    {
        return cUserRepository.findCUserByEmail(email);
    }

    @Override
    public CUser getCUserByEmail(String email)
    {
        return cUserRepository.findCUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User could not be found by email."));
    }

    @Override
    public CUser getCUserByUsername(String username)
    {
        return cUserRepository.findCUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User could not be found by username."));
    }

    @Override
    public void delete(CUser user)
    {
        cUserRepository.delete(user);
    }
}
