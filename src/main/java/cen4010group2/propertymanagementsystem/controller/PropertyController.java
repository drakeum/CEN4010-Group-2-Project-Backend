package cen4010group2.propertymanagementsystem.controller;

import cen4010group2.propertymanagementsystem.model.*;
import cen4010group2.propertymanagementsystem.service.CUserService;
import cen4010group2.propertymanagementsystem.service.PropertyServiceImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Hunter B.
 */
@RestController
public class PropertyController
{
    private static final String TOKEN_PREFIX = "Bearer";

    private final String secret;
    private final PropertyServiceImpl propertyService;

    private final CUserService cUserService;

    public PropertyController(PropertyServiceImpl propertyService, CUserService cUserService, @Value("${jwt_secret}") String secret)
    {
        this.propertyService = propertyService;
        this.cUserService = cUserService;
        this.secret = secret;
    }
    @GetMapping("/admin/getAllProperties")
    public List<Property> getAllProperties()
    {
        return propertyService.getAllProperties();
    }

    @PostMapping("/admin/addUserProperty")
    public void addUserProperty(@RequestBody AdminNewProperty adminNewProperty)
    {
        CUser u = cUserService.getCUserByEmail(adminNewProperty.getEmail());
        Property p = new Property();
        p.setName(adminNewProperty.getName());
        p.setItemValue(adminNewProperty.getValue());
        p.setCreationDate(new Timestamp(System.currentTimeMillis()));
        p.setOwnerAccountID(u.getId());
        propertyService.saveProperty(p);
    }

    @PutMapping("/admin/editUserProperty/{pid}")
    public void adminEditProperty(@PathVariable Long pid, @RequestBody NewProperty editProperty)
    {
        Property property = propertyService.getPropertyById(pid);
        //System.out.println("Old property name: " + property.getName());
        String newName = editProperty.getName();
        property.setName(newName);
        //System.out.println("New property name: " + newName);
        //System.out.println("Old property value: " + editProperty.getValue());
        double newValue = editProperty.getValue();
        property.setItemValue(newValue);
        //System.out.println("New property value: " + newValue);
        propertyService.saveProperty(property);
    }

    @DeleteMapping("/admin/removeUserProperty/{pid}")
    public void adminDeleteProperty(@PathVariable Long pid)
    {
        propertyService.deletePropertyById(pid);
    }

    @PostMapping("/cuser/addProperty")
    public void addProperty(@RequestBody NewProperty newProperty, HttpServletRequest request)
    {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("Token gathered, it is: " + token);
        String propertyName = newProperty.getName();
        double propertyValue = newProperty.getValue();
        Property p = new Property();
        p.setName(propertyName);
        p.setItemValue(propertyValue);
        p.setCreationDate(new Timestamp(System.currentTimeMillis()));
        String email = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        System.out.println("Email parsed, it is: " + email);
        CUser u = cUserService.getCUserByEmail(email);
        p.setOwnerAccountID(u.getId());
        propertyService.saveProperty(p);
    }

    @GetMapping("/cuser/getProperties")
    public List<Property> getProperties(HttpServletRequest request)
    {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        CUser u = cUserService.getCUserByEmail(email);
        return propertyService.getPropertiesByUser(u);
    }

    @DeleteMapping("/cuser/removeProperty")
    public void removeProperty(@RequestBody DeleteProperty deleteProperty, HttpServletRequest request)
    {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        CUser u = cUserService.getCUserByEmail(email);
        Long uid = u.getId();
        Long pid = deleteProperty.getId();
        Property property = propertyService.getPropertyById(pid);
        if(property.getOwnerAccountID() == uid)
        {
            propertyService.deleteProperty(property);
        }
        // TODO: Make return an unauthorized error if account id isn't property's owner id
    }

    @PutMapping("/cuser/editProperty/{pid}")
    public void editProperty(@PathVariable Long pid, @RequestBody NewProperty editProperty)
    {
        Property property = propertyService.getPropertyById(pid);
        System.out.println("Old property name: " + property.getName());
        String newName = editProperty.getName();
        property.setName(newName);
        System.out.println("New property name: " + newName);
        System.out.println("Old property value: " + editProperty.getValue());
        double newValue = editProperty.getValue();
        property.setItemValue(newValue);
        System.out.println("New property value: " + newValue);
        propertyService.saveProperty(property);
        // TODO: Make it so that this verifies the editing user owns the property
    }
}
