package lixco.com.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.primefaces.PrimeFaces;
import org.primefaces.model.UploadedFile;

import lixco.com.entities.Company;
import lixco.com.entities.Role;
import lixco.com.entities.User;
import lixco.com.services.RoleService;
import lixco.com.services.UserService;
import lixco.com.services.User_RoleService;

@ManagedBean
@SessionScoped
public class RegistrationMBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private User newUser;
	private Company newCompany;
	private UploadedFile image;

	@Inject
	private UserService userService;

	@Inject
	private User_RoleService u_rService;

	@Inject
	private RoleService roleService;

	@PostConstruct
	public void init() {
		newUser = new User();
		newCompany = new Company();

	}

	public void Registration() {
		if (newUser.getUsername().equals("") && newUser.getPassword().equals("")) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Vui lòng nhập dữ liệu!");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		} else {
			User test = userService.login(newUser.getUsername(), null);
			if(test != null) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Tên đăng nhập đã tồn tại!");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
			else {
				// Tao User de insert vao bang User_role
				User userTemp = userService.registration(newUser, newCompany);
				// Khoi tao lai doi tuong newUser
				newUser = new User();
				//Gan mac dinh la Customer (khach hang)
				Role roleTemp = roleService.findById(4);
				u_rService.create(userTemp, roleTemp);
				PrimeFaces.current().executeScript("PF('dialogCreateSuccessRegist').show()");
			}
		}
	}

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}	

	public Company getNewCompany() {
		return newCompany;
	}

	public void setNewCompany(Company newCompany) {
		this.newCompany = newCompany;
	}

	public UploadedFile getImage() {
		return image;
	}

	public void setImage(UploadedFile image) {
		this.image = image;
	}

}
