package cen4010group2.propertymanagementsystem.repository;

import cen4010group2.propertymanagementsystem.model.CUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Hunter B.
 */
@Repository
public interface CUserRepository extends JpaRepository<CUser, Long>
{
    Optional<CUser> findCUserByUsername(String username);
    Optional<CUser> findCUserByEmail(String email);
}
