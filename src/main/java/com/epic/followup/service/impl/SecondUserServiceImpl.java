package com.epic.followup.service.impl;

import com.epic.followup.model.SecondUserModel;
import com.epic.followup.repository.SecondUserRepository;
import com.epic.followup.service.SecondUserService;
import com.epic.followup.temporary.web.LoginRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class SecondUserServiceImpl implements SecondUserService {
    private final
    SecondUserRepository userRepository;

    public SecondUserServiceImpl(SecondUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SecondUserModel findByUserNameAndPassword(String username, String password) {
        return userRepository.findByUserNameAndPassword(username, password);
    }

    @Override
    public boolean findUserByName(String username) {
        SecondUserModel u =null;
        Optional<SecondUserModel> ou = userRepository.findByUserName(username);
        return ou.isPresent();
    }

    @Override
    public  <S extends SecondUserModel> boolean addUser(S u) {
        return (userRepository.save(u).getUserName() != null);
    }

    @Override
    public SecondUserModel checkUser(LoginRequest user) {
        SecondUserModel u =null;
        // 去数据库中通过username查找用户信息
        Optional<SecondUserModel> ou = userRepository.findByUserName(user.getUsername());
        if(ou.isPresent()){
            u = ou.get();
            if(u.getPassword().equals(user.getPassword())){
                return u;
            }
        }
        return null;
    }
}
