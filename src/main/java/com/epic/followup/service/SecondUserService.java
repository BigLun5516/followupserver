package com.epic.followup.service;

import com.epic.followup.model.SecondUserModel;
import com.epic.followup.temporary.web.LoginRequest;

/**
 * @author : zx
 * @version V1.0
 */
public interface SecondUserService {

    SecondUserModel findByUserNameAndPassword(String username, String password);
    boolean findUserByName(String username);
    public SecondUserModel checkUser(LoginRequest user);
    <S extends SecondUserModel> boolean addUser(S u);
}
