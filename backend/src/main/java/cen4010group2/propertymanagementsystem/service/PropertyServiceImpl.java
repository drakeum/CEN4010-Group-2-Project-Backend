package cen4010group2.propertymanagementsystem.service;

import cen4010group2.propertymanagementsystem.model.Property;
import cen4010group2.propertymanagementsystem.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Property save(Property property)
    {
        return propertyRepository.save(property);
    }
    @Override
    public List<Property> getAllProperties()
    {
        return propertyRepository.findAll();
    }

    @Override
    public Property saveProperty(Property property)
    {
        return propertyRepository.save(property);
    }
}
