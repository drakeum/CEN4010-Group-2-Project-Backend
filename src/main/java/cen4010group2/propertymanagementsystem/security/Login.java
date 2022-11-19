package cen4010group2.propertymanagementsystem.security;

import lombok.*;

/**
 * @author Hunter B.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Login
{
    private String email;
    private String password;
}
