package cen4010group2.propertymanagementsystem.service;

import cen4010group2.propertymanagementsystem.model.CUser;

/**
 * @author Hunter B.
 */
public interface CUserService
{
    public CUser save(CUser user);
    public CUser getCUserByEmail(String email);
    public CUser getCUserByUsername(String username);
    public void delete(CUser user);
}
