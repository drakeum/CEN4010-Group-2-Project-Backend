package cen4010group2.propertymanagementsystem.controller;

import cen4010group2.propertymanagementsystem.model.Property;
import cen4010group2.propertymanagementsystem.repository.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Hunter B.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/property")
public class PropertyController
{
    private final PropertyRepository propertyRepository;

    @GetMapping("/getAll")
    public List<Property> getAllProperties()
    {
        return propertyRepository.findAll();
    }
}
