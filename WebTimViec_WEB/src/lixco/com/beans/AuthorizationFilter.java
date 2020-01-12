package lixco.com.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import lixco.com.entities.AllowAccess;
import lixco.com.entities.Role_Permission;
import lixco.com.entities.User_Role;
import lixco.com.services.Role_PermissionService;
import lixco.com.services.User_RoleService;

@WebFilter("/pages/admin/pagesadmin/*")
public class AuthorizationFilter implements Filter {
	
	private List<User_Role> userRoleList = new ArrayList<>();
	private List<Role_Permission> permissions = new ArrayList<>();
	
	@Inject private User_RoleService u_rService;
	
	@Inject private Role_PermissionService rolePermissionService;
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		AllowAccess allowAccess = new AllowAccess();
	
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession(false);
		
		String url = request.getRequestURL().toString();
		
		//Lay list roles tu database
		userRoleList = u_rService.findByUserId((long)session.getAttribute("userId"));
		List<Long> roles = new ArrayList<>();
		for (User_Role user_role : userRoleList) {
			roles.add(user_role.getRole().getId());
		}
		//Lay list permission tu danh sach role
		permissions = rolePermissionService.find(0L, 0L, roles, null);

		
		boolean isAllow = false;
		for (int i = 0; i < permissions.size(); i++) {
			if( url.contains(permissions.get(i).getPermission().getLink())) {
				String uri = request.getRequestURI();
				List<Role_Permission> temps = new ArrayList<>();
				//Lay tat ca cac quyen vao form
				temps = rolePermissionService.find(0L, 0L, roles, uri);
				for (Role_Permission r : temps) {
					if(r.isCreate() == true) {
						allowAccess.setCreate(true);
					}
					if(r.isDelete() == true) {
						allowAccess.setDelete(true);
					}
					if(r.isUpdate() == true) {
						allowAccess.setUpdate(true);
					}
					if(r.isRead() == true) {
						allowAccess.setRead(true);
						isAllow = true;
					}
				}
				session.setAttribute("allowAccess", allowAccess);
			}
		}
		if(isAllow == true) {
			isAllow = false;
			chain.doFilter(request, response);
		}
		else {
			String url1 = "http://" + req.getServerName() + ":" + req.getServerPort() + "/WebTimViec_WEB/pages/admin/error/notfound.jsf";
			response.sendRedirect(url1);
		}
	}

	@Override
	public void destroy() {
	}

}