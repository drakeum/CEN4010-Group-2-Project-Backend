package cen4010group2.propertymanagementsystem.controller;

import cen4010group2.propertymanagementsystem.model.CUser;
import cen4010group2.propertymanagementsystem.model.NewProperty;
import cen4010group2.propertymanagementsystem.model.Property;
import cen4010group2.propertymanagementsystem.service.CUserService;
import cen4010group2.propertymanagementsystem.service.PropertyServiceImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


}
