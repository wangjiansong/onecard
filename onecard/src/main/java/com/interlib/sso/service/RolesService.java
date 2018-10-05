package com.interlib.sso.service;

import java.util.List;

import com.interlib.sso.domain.Compets;
import com.interlib.sso.domain.Roles;
import com.interlib.sso.domain.URole_Compet;


public interface RolesService extends BaseService<Roles, Integer> {

	public List<Compets> getRoleCompetByRoleId(Integer roleId);
	
	public List<Integer> getRoleCompetIdByRolesId(Integer roleId);
	
	public void deleteRoleCompByRoleId(Integer roleId);
	
	public void saveRoleCompet(URole_Compet roleCom);
	
	public List<Compets> getOtherCompetByRoleId(Integer roleId);
	
	public List<Integer> getCompetsIdByRoles(List<Roles> rolesList);
}
