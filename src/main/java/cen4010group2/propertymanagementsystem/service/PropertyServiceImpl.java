package cen4010group2.propertymanagementsystem.service;

import cen4010group2.propertymanagementsystem.model.CUser;
import cen4010group2.propertymanagementsystem.model.Property;
import cen4010group2.propertymanagementsystem.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Hunter B.
 */
@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService
{

    private final PropertyRepository propertyRepository;

    @Override
    public List<Property> getAllProperties()
    {
        return propertyRepository.findAll();
    }

    @Override
    public Property getPropertyById(Long id)
    {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Property could not be found by id."));
    }

    @Override
    public Property saveProperty(Property property)
    {
        return propertyRepository.save(property);
    }

    @Override
    public List<Property> getPropertiesByUser(CUser user)
    {
        Long ownerId = user.getId();
        List <Property> allP = getAllProperties();
        List <Property> userP = new ArrayList<Property>();

        Iterator<Property> iter = allP.iterator();
        while (iter.hasNext())
        {
            Property p = iter.next();
            if(p.getOwnerAccountID() == ownerId)
            {
                userP.add(p);
            }
        }

        return userP;
    }

    @Override
    public List<Property> getSharedPropertiesByUser(CUser user)
    {
        List<Property> propSharedWithUser = new ArrayList<>();
        List <Property> allP = getAllProperties();
        List<CUser> sharedUsers = user.getUsersSharedFrom();

        Iterator<Property> iterP = allP.iterator();
        while(iterP.hasNext())
        {
            Property p = iterP.next();
            Iterator<CUser> iterSU = sharedUsers.iterator();
            while(iterSU.hasNext())
            {
                CUser u = iterSU.next();
                if(u.getId() == p.getOwnerAccountID())
                {
                    propSharedWithUser.add(p);
                }
            }
        }
        return propSharedWithUser;
    }

    @Override
    public void deleteProperty(Property property)
    {
        propertyRepository.delete(property);
    }

    @Override
    public void deletePropertyById(Long id)
    {
        propertyRepository.deleteById(id);
    }
}
