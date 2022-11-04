package cen4010group2.propertymanagementsystem.service;

import cen4010group2.propertymanagementsystem.model.Property;

import java.util.List;

/**
 * @author Hunter B.
 */
public interface PropertyService
{
    public Property save(Property user);

    public List<Property> getAllProperties();

    public Property saveProperty(Property property);

}
