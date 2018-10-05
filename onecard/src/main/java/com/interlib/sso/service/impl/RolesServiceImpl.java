package com.interlib.sso.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.RolesDAO;
import com.interlib.sso.domain.Compets;
import com.interlib.sso.domain.Roles;
import com.interlib.sso.domain.URole_Compet;
import com.interlib.sso.service.RolesService;

@Service
public class RolesServiceImpl 
			extends AbstractBaseServiceImpl<Roles, Integer> implements RolesService{

	public RolesDAO rolesDAO;
	
	@Autowired
	public void setRolesDAO(RolesDAO rolesDAO) {
		this.rolesDAO = rolesDAO;
	}

	@Autowired
	public void setBaseDAO(RolesDAO rolesDAO) {
		super.setBaseDAO(rolesDAO);
	}

	@Override
	public List<Compets> getRoleCompetByRoleId(Integer roleId) {
		
		return rolesDAO.getRoleCompetByRoleId(roleId);
	}

	public List<Integer> getRoleCompetIdByRolesId(Integer roleId) {
		return rolesDAO.getRoleCompetIdByRolesId(roleId);
	}
	
	@Override
	public void deleteRoleCompByRoleId(Integer roleId) {
		rolesDAO.deleteRoleCompByRoleId(roleId);
	}

	@Override
	public void saveRoleCompet(URole_Compet roleCom) {
		rolesDAO.saveRoleCompet(roleCom);
	}

	@Override
	public List<Compets> getOtherCompetByRoleId(Integer roleId) {
		
		return rolesDAO.getOtherCompetByRoleId(roleId);
	}

	@Override
	public List<Integer> getCompetsIdByRoles(List<Roles> rolesList) {
		List<Integer> retList = new ArrayList<Integer>();
		for(Roles roles : rolesList) {
			List<Integer> compList = new ArrayList<Integer>();
			List<Integer> compIdList = getRoleCompetIdByRolesId(roles.getRoleId());
			retList.addAll(compIdList);
		}
		HashSet h = new HashSet(retList);
		retList.clear();
		retList.addAll(h);
		return retList;
	}
	
}
