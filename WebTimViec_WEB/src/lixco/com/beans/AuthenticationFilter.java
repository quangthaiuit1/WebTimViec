package lixco.com.beans;

import java.io.IOException;
import java.util.List;

import javax.faces.application.ResourceHandler;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lixco.com.entities.Role_Permission;
import lixco.com.entities.User_Role;
import lixco.com.services.Role_PermissionService;
import lixco.com.services.User_RoleService;

@WebFilter("/pages/admin/*")
public class AuthenticationFilter implements Filter {// check login thanhf conng
	
	@Inject User_RoleService u_rService;
	@Inject 
	private Role_PermissionService rolePermissionService;
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession(false);

		String loginURI = request.getContextPath() + "/pages/login/login.xhtml";
		String homeURI	= request.getContextPath() + "/pages/home/post.xhtml";
		
		
		boolean loggedIn = session != null && session.getAttribute("userId") != null;
		boolean loginRequest = request.getRequestURI().equals(loginURI);
		boolean resourceRequest = request.getRequestURI()
				.startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER);
		
		if(loggedIn || loginRequest || resourceRequest) {
			chain.doFilter(request, response);
		}
		else {
			response.sendRedirect(loginURI);
		}
		
		
	}

	@Override
	public void destroy() {
	}

}