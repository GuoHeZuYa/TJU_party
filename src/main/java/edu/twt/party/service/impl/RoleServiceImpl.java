package edu.twt.party.service.impl;

import edu.twt.party.dao.RoleMapper;
import edu.twt.party.pojo.Role;
import edu.twt.party.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(rollbackFor = Throwable.class)
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public Role findById(int rid) {
        return roleMapper.findById(rid);
    }
}
