package lixco.com.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.PrimeFaces;

import lixco.com.entities.Role;
import lixco.com.entities.User;
import lixco.com.entities.User_Role;
import lixco.com.services.RoleService;
import lixco.com.services.Role_PermissionService;
import lixco.com.services.UserService;
import lixco.com.services.User_RoleService;

@ManagedBean
@ViewScoped
public class Capquyen implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//Toan bo user
	private List<User> allAccount;
	
	//Toan bo role
	private List<Role> roles;
	//Danh sach role duoc chon
	private List<Role> selectedRoles;
	
	//Variable Cap quyen va phan quyen
	private List<User> selectedUsers;
	private User selectedUser;
	private List<User_Role> user_Roles;
	@Inject private Role_PermissionService rolePermissionService;
	
	@Inject private UserService userService;
	
	@Inject private User_RoleService u_rService;
	
	@Inject private RoleService roleService;

	
	@PostConstruct
	public void init() {
		roles = new ArrayList<>();
		//Toan bo role
		roles = roleService.findAllByFilter();
		allAccount = new ArrayList<>();
		//Toan bo user
		allAccount = userService.findAllByFilter();
		
	}
	public void viewRole() {
		user_Roles = u_rService.findByUserId(selectedUser.getId());
		selectedRoles = new ArrayList<>();
		for (User_Role user : user_Roles) {
			selectedRoles.add(user.getRole());
		}
		PrimeFaces.current().executeScript("PF('carDialog').show()");
	}
	//Cap quyen
	public void capQuyenChoAccount() {
			allAccount = new ArrayList<>();
			allAccount = userService.findAllByFilter();
	}
	public List<User> getAllAccount() {
		return allAccount;
	}
	public void setAllAccount(List<User> allAccount) {
		this.allAccount = allAccount;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public List<Role> getSelectedRoles() {
		return selectedRoles;
	}
	public void setSelectedRoles(List<Role> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}
	public List<User> getSelectedUsers() {
		return selectedUsers;
	}
	public void setSelectedUsers(List<User> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}
	public User getSelectedUser() {
		return selectedUser;
	}
	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}
	public List<User_Role> getUser_Roles() {
		return user_Roles;
	}
	public void setUser_Roles(List<User_Role> user_Roles) {
		this.user_Roles = user_Roles;
	}
	
	
}
