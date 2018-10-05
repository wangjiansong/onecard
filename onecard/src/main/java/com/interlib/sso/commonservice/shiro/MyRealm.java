package com.interlib.sso.commonservice.shiro;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderSession;
import com.interlib.sso.domain.Resources;
import com.interlib.sso.domain.Roles;
import com.interlib.sso.exception.UnAcceptableLicenseException;
import com.interlib.sso.license.ValidSystem;
import com.interlib.sso.service.CompetsService;
import com.interlib.sso.service.ReaderService;
import com.interlib.sso.service.RolesService;

public class MyRealm extends AuthorizingRealm {
	
	@Autowired
	private ReaderService readerService;
	
	@Autowired
	private RolesService rolesService;
	
	@Autowired
	private CompetsService competsService;
	

	/** 
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用. 
	 */   
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		Reader reader = (Reader) getAvailablePrincipal(principals);
		
		if(reader != null) {
			List<Roles> roleList = readerService.getReaderRolesByRdId(reader.getRdId());
			
			List<Integer> compIdList = rolesService.getCompetsIdByRoles(roleList);
			
			Collection<String> resourceList = competsService.getResourcesIdByCompetIds(compIdList);
			
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			
			if(reader.getLibUser() == 1) {
				info.addRole("admin");
			} else {
				info.addRole("reader");
			}
			info.addStringPermissions(resourceList);
			
			return info;
		}
		return null;
		
	}

	/** 
	 * 认证回调函数,登录时调用. 
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		
		Reader reader = new Reader();
		reader.setRdId(token.getUsername());
		reader.setRdPasswd(new String(token.getPassword()));
		System.out.println(reader.getRdId());
		System.out.println(reader.getRdPasswd());
		//modify by huang at 20140304
		reader = readerService.readerLogin(reader, false);
//		cas
//		reader = readerService.getReader(reader.getRdId(), (byte)2);
		ValidSystem vl = new ValidSystem();
		if(!vl.isCanUse()) {
			throw new UnAcceptableLicenseException(vl.getErrorMsgToAdmin().toString());
		} else {
			if(reader != null) {
				//TODO 这里取得权限，设置session的做法似乎不妥，是否应该都交给shiro处理呢？？
				ReaderSession readerSession = new ReaderSession();
				
				List<Roles> roleList = readerService.getReaderRolesByRdId(reader.getRdId());
				
				List<Integer> compIdList = rolesService.getCompetsIdByRoles(roleList);
				
				List<Resources> resList = competsService.getResourcesByCompetIds(compIdList);
				
				readerSession.setReader(reader);
				
				readerSession.setResourceList(resList);
				
				SecurityUtils.getSubject().getSession().setAttribute("READER_SESSION", readerSession);
				
				return new SimpleAuthenticationInfo(reader,
						new String(token.getPassword()), token.getUsername());
			} else {
				throw new UnknownAccountException(
	                    "Username or password is unacceptable.");
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(4%4);
	}
}
