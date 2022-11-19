package cen4010group2.propertymanagementsystem.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hunter B.
 */

@NoArgsConstructor
@Getter
@Setter
public class EditAccount
{
    String username;
    String oldPassword;
    String newPassword;
}
