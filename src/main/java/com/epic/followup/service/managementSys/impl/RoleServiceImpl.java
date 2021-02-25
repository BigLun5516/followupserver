package com.epic.followup.service.managementSys.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.RoleModel;
import com.epic.followup.repository.managementSys.RoleRepository;
import com.epic.followup.repository.managementSys.UserRepository;
import com.epic.followup.service.managementSys.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 查询全部角色
     * @param
     * @return JSONObject
     */
    @Override
    public JSONObject findAllRoles(){
        JSONObject res = new JSONObject();
        List<RoleModel> rolelist=roleRepository.findAll();
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        res.put("data",rolelist);
        return res;
    }

    /**
     * 删除角色
     * @param id
     * @return JSONObject
     */
    @Override
    public JSONObject deleteRole(Long id){
        JSONObject res = new JSONObject();

        try {
            roleRepository.deleteById(id);
            userRepository.upDateUserType(id);//角色删除后，相应的用户对应的角色也要删除
        }catch (EmptyResultDataAccessException e){
            res.put("errorCode", 500);
            res.put("errorMsg", "删除失败");
            return res;
        }

        res.put("errorCode", 200);
        res.put("errorMsg", "删除成功");
        return res;

    }


    /**
     * 新增角色
     * @param params
     * @return JSONObject
     */
    @Override
    public JSONObject insertRole(JSONObject params){

        JSONObject res = new JSONObject();

        RoleModel roleModel = new RoleModel();
        roleModel.setName(params.getString("name"));
        roleModel.setRemark(params.getString("remark"));
        roleModel.setLimit1(params.getString("limit1"));
        roleModel.setLimit2(params.getString("limit2"));

        roleRepository.save(roleModel);
        res.put("errorCode", 200);
        res.put("errorMsg", "插入成功");
        return res;
    }

    /**
     * 编辑角色
     * @param params
     * @return JSONObject
     */
    @Override
    public JSONObject editRole(JSONObject params){

        JSONObject res = new JSONObject();

        Long id=params.getLong("id");
        Optional<RoleModel> u = roleRepository.findById(id);
        if (!u.isPresent()){
            res.put("errorCode", 500);
            res.put("errorMsg", "编辑失败");
            return res;
        }
        RoleModel roleModel=u.get();
        roleModel.setName(params.getString("name"));
        roleModel.setRemark(params.getString("remark"));
        roleModel.setLimit1(params.getString("limit1"));
        roleModel.setLimit2(params.getString("limit2"));

        roleRepository.save(roleModel);
        res.put("errorCode", 200);
        res.put("errorMsg", "编辑成功");
        return res;
    }
}
