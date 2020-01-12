package lixco.com.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.PrimeFaces;

import lixco.com.entities.AllowAccess;
import lixco.com.entities.Permission;
import lixco.com.entities.Role;
import lixco.com.entities.Role_Permission;
import lixco.com.entities.User;
import lixco.com.entities.User_Role;
import lixco.com.services.PermissionService;
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
	private boolean roleValue = false;
	//Toan bo role
	private List<Role> roles;
	//Bien de sua quyen truy cap tu danh sach role
	private AllowAccess access = new AllowAccess();
	
	//Toan bo permission
	private List<Permission> permissions;
	private Role selectedRole;
	//Danh sach role duoc chon
	private List<Role> selectedRoles;
	//Quyen them xoa sua
	private boolean isCreate = false;
	private boolean isRead = false;
	private boolean isUpdate = false;
	private boolean isDelete = false;
	
	//Variable Cap quyen va phan quyen
	private List<User> selectedUsers;
	private User selectedUser;
	private List<User_Role> user_Roles;
	private List<Role_Permission> rolePermissionList;
	private Role deletedRole;
	private Role newRole;
	
	
	@Inject 
	private UserService userService;
	
	@Inject 
	private User_RoleService u_rService;
	
	@Inject 
	private RoleService roleService;
	
	@Inject
	private PermissionService permissionService;
	
	@Inject
	private Role_PermissionService r_pService;

	
	@PostConstruct
	public void init() {
		//Toan bo role
		roles = roleService.findAllByFilter();
		//Toan bo user
		allAccount = userService.findAllByFilter();
		permissions = permissionService.findAllByFilter();
		selectedRoles = new ArrayList<>();
		selectedRole = new Role();
		deletedRole = new Role();
		newRole = new Role();
		
	}
	public void xemQuyen() {
		selectedRoles = new ArrayList<>();
		user_Roles = u_rService.findByUserId(selectedUser.getId());
		for (User_Role user : user_Roles) {
			selectedRoles.add(user.getRole());
		}
	}

	//Xem duoc truy cap form nao
	public void xemQuyenTruyCapForm() {
		List<Role_Permission> temp = new ArrayList<>();
		rolePermissionList = new ArrayList<>();
		access = new AllowAccess();
		temp = r_pService.find(selectedRole.getId(), 0L, null, null);
		rolePermissionList = temp;
		newRole = new Role();
	}
	//Cap nhat quyen
	public void updateQuyen() {
		try {
			//Tim cac quyen cua user tren form nay (them,xoa,sua,doc)
			AllowAccess allowAccess= (AllowAccess)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("allowAccess");
			//Kiem tra quyen
			if (allowAccess.isUpdate() == true) {
				// selected User
				//selected Role
				List<User_Role> temp = u_rService.find(selectedUser.getId(), 0L);
				for (User_Role user_Role : temp) {
					u_rService.delete(user_Role);
					System.out.println("a");
				}
				for (Role role : selectedRoles) {
					u_rService.update(selectedUser, role);
					System.out.println("b");
				}
				allAccount = userService.findAllByFilter();
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Cập nhật thành công.");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
			else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Không có quyền sửa!");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		} catch (Exception e) {

		}
	}
	//Cap nhat quyen truy cap form
	public void updateQuyenTruyCap(){
		try {
			//Tim cac quyen cua user tren form nay (them,xoa,sua,doc)
			AllowAccess allowAccess= (AllowAccess)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("allowAccess");
			//Kiem tra quyen
			if (allowAccess.isUpdate() == true) {
				List<Role_Permission> temp = r_pService.find(selectedRole.getId(), 0L, null, null);
				for (Role_Permission role_Permission : temp) {
					r_pService.delete(role_Permission);
				}
				for (Role_Permission abc : rolePermissionList) {
					r_pService.update(abc);
				}
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Cập nhật thành công.");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
			else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Không có quyền sửa!");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		} catch (Exception e) {

		}		
	}
	//Xoa Quyen
	public void deleteRole() {
		//Tim cac quyen cua user tren form nay (them,xoa,sua,doc)
			AllowAccess allowAccess= (AllowAccess)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("allowAccess");
			//Kiem tra quyen
			if (allowAccess.isDelete() == true) {
				deletedRole.setDeleted(true);
				
				for (Permission p : permissions) {
					List<Role_Permission> temp = new ArrayList<>();
					temp = r_pService.find(deletedRole.getId(), p.getId(), null, null);
					r_pService.delete(temp.get(0));
					System.out.println(temp.get(0).getPermission().getName());
				}
				roleService.delete(deletedRole);
				deletedRole = new Role();
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Xóa thành công.");
				PrimeFaces.current().dialog().showMessageDynamic(message);
				roles = roleService.findAllByFilter();
			}
			else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Không có quyền xóa!");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
	}
	//Them Quyen
	public void createRole() {
		//Tim cac quyen cua user tren form nay (them,xoa,sua,doc)
		AllowAccess allowAccess= (AllowAccess)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("allowAccess");
		//Kiem tra quyen
		if (allowAccess.isCreate() == false && (!newRole.getName().equals("") || newRole.getName().equals(""))) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Không có quyền thêm!");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}
		if(allowAccess.isCreate() == true && newRole.getName().equals("")) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Vui lòng nhập dữ liệu!");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}
		if(allowAccess.isCreate() == true && !newRole.getName().equals("")){

			Date date = new Date();
			newRole.setCreatedDate(date);
			newRole = roleService.create(newRole);
			//Lap toan bo permission
			for (Permission p : permissions) {
				Role_Permission temp = new Role_Permission();
				temp.setPermission(p);
				temp.setRole(newRole);
				temp.setCreate(false);
				temp.setDelete(false);
				temp.setRead(false);
				temp.setUpdate(false);
				r_pService.create(temp);
			}
			roles = roleService.findAllByFilter();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Thêm mới thành công.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}
	}
	public void cancelDelete() {
		PrimeFaces.current().executeScript("PF('dialogDelete').hide()");
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
	public boolean isRoleValue() {
		return roleValue;
	}
	public void setRoleValue(boolean roleValue) {
		this.roleValue = roleValue;
	}
	public Role getSelectedRole() {
		return selectedRole;
	}
	public void setSelectedRole(Role selectedRole) {
		this.selectedRole = selectedRole;
	}

	public boolean isCreate() {
		return isCreate;
	}
	public void setCreate(boolean isCreate) {
		this.isCreate = isCreate;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public boolean isUpdate() {
		return isUpdate;
	}
	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
	public boolean isDelete() {
		return isDelete;
	}
	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	public AllowAccess getAccess() {
		return access;
	}
	public void setAccess(AllowAccess access) {
		this.access = access;
	}
	public List<Role_Permission> getRolePermissionList() {
		return rolePermissionList;
	}
	public void setRolePermissionList(List<Role_Permission> rolePermissionList) {
		this.rolePermissionList = rolePermissionList;
	}
	public Role getDeletedRole() {
		return deletedRole;
	}
	public void setDeletedRole(Role deletedRole) {
		this.deletedRole = deletedRole;
	}
	public Role getNewRole() {
		return newRole;
	}
	public void setNewRole(Role newRole) {
		this.newRole = newRole;
	}
	
	
}
