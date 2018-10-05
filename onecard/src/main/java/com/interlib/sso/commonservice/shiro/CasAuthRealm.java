package com.interlib.sso.commonservice.shiro;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.stereotype.Component;

import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderSession;
import com.interlib.sso.domain.Resources;
import com.interlib.sso.domain.Roles;
import com.interlib.sso.service.CompetsService;
import com.interlib.sso.service.ReaderService;
import com.interlib.sso.service.RolesService;

public class CasAuthRealm extends CasRealm {
	
	private static final Log log = LogFactory.getLog(CasAuthRealm.class);
	
	private ReaderService readerService;
	
	private RolesService rolesService;
	
	private CompetsService competsService;
	
	public void setReaderService(ReaderService readerService) {
		this.readerService = readerService;
	}
	
	public void setRolesService(RolesService rolesService) {
		this.rolesService = rolesService;
	}

	public void setCompetsService(CompetsService competsService) {
		this.competsService = competsService;
	}

	/** 
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		
		System.out.println("doGetAuthenticationInfo");
		
		CasToken casToken = (CasToken) authcToken;
		
        if (authcToken == null) {
            return null;
        }
        
        String ticket = (String)casToken.getCredentials();
        if (!StringUtils.hasText(ticket)) {
            return null;
        }
        TicketValidator ticketValidator = ensureTicketValidator();
		try {
	        // contact CAS server to validate service ticket
	        Assertion casAssertion = ticketValidator.validate(ticket, getCasService());
	        // get principal, user id and attributes
	        AttributePrincipal casPrincipal = casAssertion.getPrincipal();
	        String userId = casPrincipal.getName();
	        Map<String, Object> attributes = casPrincipal.getAttributes();
	        // refresh authentication token (user id + remember me)
	        casToken.setUserId(userId);
	        String rememberMeAttributeName = getRememberMeAttributeName();
	        String rememberMeStringValue = (String)attributes.get(rememberMeAttributeName);
	        boolean isRemembered = rememberMeStringValue != null && Boolean.parseBoolean(rememberMeStringValue);
	        if (isRemembered) {
	            casToken.setRememberMe(true);
	        }
	        // create simple authentication info
	        List<Object> principals = CollectionUtils.asList(userId, attributes);
	        PrincipalCollection principalCollection = new SimplePrincipalCollection(principals, getName());
	        
	        Reader reader = readerService.getReader(userId, (byte)1);
	        if(reader != null) {
	        	ReaderSession readerSession = new ReaderSession();
				
				List<Roles> roleList = readerService.getReaderRolesByRdId(reader.getRdId());
				
				List<Integer> compIdList = rolesService.getCompetsIdByRoles(roleList);
				
				List<Resources> resList = competsService.getResourcesByCompetIds(compIdList);
				
				readerSession.setReader(reader);
				
				readerSession.setResourceList(resList);
				
				SecurityUtils.getSubject().getSession().setAttribute("READER_SESSION", readerSession);
				
				return new SimpleAuthenticationInfo(principalCollection, ticket);
	        }
	        return null;
		} catch (TicketValidationException e) { 
            throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
        }
	}
	
	/** 
	 * 授权 
	 */   
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		String loginId = (String)principals.fromRealm(getName()).iterator().next();
		
		System.out.println("doGetAuthorizationInfo: 进行授权，登录id是：" + loginId);
		
		Reader reader = readerService.getReader(loginId, (byte)1);
		
		List<Roles> roleList = readerService.getReaderRolesByRdId(reader.getRdId());
		
		List<Integer> compIdList = rolesService.getCompetsIdByRoles(roleList);
		
		Collection<String> resourceList = competsService.getResourcesIdByCompetIds(compIdList);
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		
		info.addStringPermissions(resourceList);
		
		return info;

	}

}
