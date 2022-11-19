package cen4010group2.propertymanagementsystem.service;

import cen4010group2.propertymanagementsystem.model.CUser;
import cen4010group2.propertymanagementsystem.model.Property;

import java.util.List;

/**
 * @author Hunter B.
 */
public interface PropertyService
{
    public List<Property> getAllProperties();

    public Property getPropertyById(Long id);
    public Property saveProperty(Property property);

    public List<Property> getPropertiesByUser(CUser user);
    public void deleteProperty(Property property);

    public void deletePropertyById(Long id);
}
