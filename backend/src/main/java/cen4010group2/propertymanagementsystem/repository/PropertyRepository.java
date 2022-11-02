package cen4010group2.propertymanagementsystem.repository;

import cen4010group2.propertymanagementsystem.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Hunter B.
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>
{

}
