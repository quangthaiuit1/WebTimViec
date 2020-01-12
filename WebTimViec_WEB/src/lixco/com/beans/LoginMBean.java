package lixco.com.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lixco.com.entities.User;
import lixco.com.entities.User_Role;
import lixco.com.services.UserService;
import lixco.com.services.User_RoleService;

@ManagedBean
@SessionScoped
public class LoginMBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private User userLogin;
	private String username;
	private String password;
	private String message;
	private boolean isAdmin;
	
	private List<User_Role> userRoleList = new ArrayList<>();	

	
	@Inject
	private UserService userService;
	
	@Inject 
	private User_RoleService u_rService;
	
	@PostConstruct
	public void init(){
		
	}
	
	
	public void login() throws IOException{
		FacesContext context = FacesContext.getCurrentInstance();
		userLogin = userService.login(username, password);
		if(userLogin == null) {
			 message = "Đăng nhập không thành công.";
			 FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsf");
			}
		else {
			userRoleList = u_rService.findByUserId(userLogin.getId());
			List<Long> roles = new ArrayList<>();
			for (User_Role user_role : userRoleList) {
				roles.add(user_role.getRole().getId());
			}
			
			context.getExternalContext().getSessionMap().put("userId", userLogin.getId());
			context.getExternalContext().getSessionMap().put("nameUser", userLogin.getName());
			
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			for (User_Role u_r : userRoleList) {
				if(u_r.getRole().getName().equals("Customer")) {
					response.sendRedirect(request.getContextPath() + "/pages/web/index.jsf");
					return;
				}
			}
			response.sendRedirect(request.getContextPath() + "/pages/admin/index.jsf");
		}
	}
//Dang xuat
	public void logOut() throws Throwable {
		// Invalidate session of a sessionscoped managed bean
		this.userLogin = null;
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		FacesContext fContext = FacesContext.getCurrentInstance();
		ExternalContext extContext = fContext.getExternalContext();
		extContext.redirect("http://192.168.0.132:8380/WebTimViec_WEB/pages/web/index.jsf");

	}
	
	public User getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(User userLogin) {
		this.userLogin = userLogin;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }


	public boolean isAdmin() {
		return isAdmin;
	}


	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}


	public List<User_Role> getUserRoleList() {
		return userRoleList;
	}


	public void setUserRoleList(List<User_Role> userRoleList) {
		this.userRoleList = userRoleList;
	}
	
	
}
