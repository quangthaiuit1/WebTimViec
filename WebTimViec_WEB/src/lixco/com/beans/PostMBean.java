package lixco.com.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;

import lixco.com.entities.AllowAccess;
import lixco.com.entities.Job;
import lixco.com.entities.Location;
import lixco.com.entities.Post;
import lixco.com.entities.Post_Job;
import lixco.com.entities.Role_Permission;
import lixco.com.entities.User_Role;
import lixco.com.services.JobService;
import lixco.com.services.LocationService;
import lixco.com.services.PostService;
import lixco.com.services.Post_JobService;
import lixco.com.services.Role_PermissionService;
import lixco.com.services.User_RoleService;

@ManagedBean
@ViewScoped
public class PostMBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private static PostService postService; // POST_SERVICE

	@Inject
	private static LocationService locationService;

	@Inject
	private static JobService jobService;

	@Inject
	private static Role_PermissionService r_pService;

	@Inject 
	private static Post_JobService p_jService;
	
	private List<Post> posts = new ArrayList<>();
	private List<Location> locations;
	private List<Job> jobs;
	private Job job;
	
	//Tim kiem
	private Post post;
	private Location searchLocation = new Location();
	private Job searchJob = new Job();
	private String nameSearched = "";
	private Post deleted;
	private Post updated;
	private int updateLocationId;
	private List<Post> searchPosts;
	private int countPost = 0;
	private List<Role_Permission> test = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		job = new Job();
		posts = postService.findAllByFilter();
		countPost = posts.size();
		locations = locationService.findAll();
		jobs = jobService.findAll();
		//init variable search feature
		searchPosts = postService.findAllByFilter();
		searchLocation = new Location();
		searchJob = new Job();
		//test
		test = r_pService.findByRoleId(1);
	}

////	Lay ngay hien tai
//	public Date getCurrentDate() {
//		Date date = new Date();  
//	    System.out.println(date);
//	    Date abc = posts.get(0).getCreatedDate();
//	    System.out.println(abc);
//	    LocalDateTime ldt = LocalDateTime.now();
//	    System.out.println(ldt);
//	    
//	    java.util.Date utilDate = new java.util.Date();
//	    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
//	    System.out.println("utilDate:" + utilDate);
//	    System.out.println("sqlDate:" + sqlDate);
//	    return date;
//	}
//AUTOCOMPLETE
	//Chuc nang tim kiem
	public void showpost() {
		posts = new ArrayList<>();
		countPost = 0;
		if(searchJob == null) {
			searchJob = new Job();
		}
		if(searchLocation == null) {
			searchLocation = new Location();
		}
		if(StringUtils.isEmpty(nameSearched) && StringUtils.isEmpty(searchJob.getName()) && StringUtils.isEmpty(searchLocation.getName())) {
			posts = searchPosts;
			this.countPost = posts.size();
			return;
		}
		//Thanh search chi nhap o bai viet
//		if(  !nameSearched.equals("")  &&  searchJob.getName() == null && searchLocation.getName() == null) {
//			posts = postService.findByNamePost(nameSearched);
//			for (Post p : posts) {
//				this.countPost += 1;
//			}
//			return;
//		}
//		//Thanh search chi nhap vao o cong viec
//		if(nameSearched.equals("") && searchJob.getName() != null && searchLocation.getName() == null) {
//			List<Post_Job> temps = new ArrayList<>();
//			temps = p_jService.findByJob(searchJob.getName());
//			for(Post_Job post : temps) {
//				posts.add(post.getPost());
//			}
//			this.countPost = posts.size();
//			return;	
//		}
//		//Thanh search chi nhap vao o dia diem
//		if(nameSearched.equals("") && searchJob.getName() == null && searchLocation.getName() != null) {
//			List<Post_Job> temps = new ArrayList<>();
//			temps = p_jService.findByLocation(searchLocation.getName());
//			for(Post_Job post : temps) {
//				posts.add(post.getPost());
//			}
//			for (Post p : posts) {
//				this.countPost += 1;
//			}
//			searchLocation = new Location();
//			return;
//		}
//		//Thanh search nhap vao o bai viet va o cong viec
//		if(!nameSearched.equals("") && searchJob.getName() != null && searchLocation.getName() == null) {
//			List<Post_Job> temps = new ArrayList<>();
//			temps = p_jService.findByNamePostFilterJob(nameSearched, searchJob.getName());
//			for(Post_Job post : temps) {
//				posts.add(post.getPost());
//			}
//			for (Post p : posts) {
//				this.countPost += 1;
//			}
//			searchJob = new Job();
//			return;
//		}
//		//Thanh search chi nhap vao o cong viec va dia diem
//		if(nameSearched.equals("") &&  searchJob.getName() != null && searchLocation.getName() != null ) {
//			List<Post_Job> temps = new ArrayList<>();
//			temps = p_jService.findByJobAndLocation(searchJob.getName(), searchLocation.getName());
//			for(Post_Job post : temps) {
//				posts.add(post.getPost());
//			}
//			for (Post p : posts) {
//				this.countPost += 1;
//			}
//			searchJob = new Job();
//			searchLocation = new Location();
//			return;
//		}
//		//Thanh search chi nhap vao o bai viet va dia diem
//		if(!nameSearched.equals("") &&  searchJob.getName() == null &&  searchLocation.getName() != null ) {
//			List<Post_Job> temps = new ArrayList<>();
//			temps = p_jService.findByPostAndLocation(nameSearched, searchLocation.getName());
//			for(Post_Job post : temps) {
//				posts.add(post.getPost());
//			}
//			for (Post p : posts) {
//				this.countPost += 1;
//			}
//			searchLocation = new Location();
//			return;
//		}
		
		
		//Thanh search nhap vao ca 3 o.
//		if(!nameSearched.equals("") && searchJob.getName() != null && searchLocation.getName() != null) {
			List<Post_Job> temps = new ArrayList<>();
			temps = p_jService.find(nameSearched, searchJob.getName(), searchLocation.getName());
			for(Post_Job post : temps) {
				posts.add(post.getPost());
			}
			countPost = posts.size();
			return;
//		}
		
		
		}
		//Tim kiem theo ten bai post
		public List<String> searchPost(String input){
			List<String> result = new ArrayList<>();
			List<Post> search = new ArrayList<>();
			search = postService.findByNamePost(input);
			for (Post post : search) {
				result.add(post.getName());
			}
			for (Post p : posts) {
				this.countPost += 1;
			}
			return result;
		}
	
		//Tim kiem doi tuong Post
		public List<Post> completePost(String input) {
			String queryLowerCase = input.toLowerCase();
			return searchPosts.stream().filter(t -> t.getName().toLowerCase().startsWith(queryLowerCase))
					.collect(Collectors.toList());
		}
	
		public List<Job> completeJob(String input) {
			String queryLowerCase = input.toLowerCase();
			return jobs.stream().filter(t -> t.getName().toLowerCase().startsWith(queryLowerCase))
					.collect(Collectors.toList());
		}
	
		public List<Location> completeLocation(String input) {
	
			String queryLowerCase = input.toLowerCase();
			return locations.stream().filter(t -> t.getName().toLowerCase().startsWith(queryLowerCase))
					.collect(Collectors.toList());
		}
//END AUTOCOMPLETE

	public String returnIndex() {
		return "post";
	}
	
// GET URI
	public String getURI() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String uri = request.getRequestURI();
		System.out.println(uri);
		return uri;
	}
// 

//	START CRUD
	public void update() {
		List<Role_Permission> permissions = new ArrayList<>();
		//Tim cac quyen cua user tren form nay (them,xoa,sua,doc)
		AllowAccess allowAccess= (AllowAccess)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("allowAccess");
		//Kiem tra quyen
		if (allowAccess.isUpdate() == true) {
			Date date = new Date();
			updated.setModifiedDate(date);
			postService.update(updated);
			if (updateLocationId != 0) {
				updated.setLocation(locationService.findById(updateLocationId));
				updateLocationId = 0;
			}
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Update", "Sửa thành công .");
			PrimeFaces.current().executeScript("PF('dialogUpdate').hide()");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			posts = postService.findAllByFilter();
		}
		else {
			PrimeFaces.current().executeScript("PF('dialogUpdate').hide()");
			PrimeFaces.current().executeScript("PF('dialogUpdateError').show()");
		}
	}

	public void cancelUpdate() {
		PrimeFaces.current().executeScript("PF('dialogUpdate').hide()");
	}

	public void delete() {
		List<Role_Permission> permissions = new ArrayList<>();
		//Tim cac quyen cua user tren form nay (them,xoa,sua,doc)
		AllowAccess allowAccess= (AllowAccess)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("allowAccess");
		//Kiem tra quyen
		if (allowAccess.isDelete() == true) {
			deleted.setDeleted(true);
			postService.update(deleted);
			System.out.println("Đã xóa xong");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Delete", "Xóa thành công .");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			posts = postService.findAllByFilter();

		}
		else {
			PrimeFaces.current().executeScript("PF('dialogDelete').hide()");
			PrimeFaces.current().executeScript("PF('dialogDeleteError').show()");
		}
	}

	public void cancelDelete() {
		PrimeFaces.current().executeScript("PF('dialogDelete').hide()");
	}

//	END CRUD

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Post getDeleted() {
		return deleted;
	}

	public void setDeleted(Post deleted) {
		this.deleted = deleted;
	}

	public Post getUpdated() {
		return updated;
	}

	public void setUpdated(Post updated) {
		this.updated = updated;
	}

	public int getUpdateLocationId() {
		return updateLocationId;
	}

	public void setUpdateId(int updateLocationId) {
		this.updateLocationId = updateLocationId;
	}

	public List<Post> getSearchPosts() {
		return searchPosts;
	}

	public void setSearchPosts(List<Post> searchPosts) {
		this.searchPosts = searchPosts;
	}

	public Location getSearchLocation() {
		return searchLocation;
	}

	public void setSearchLocation(Location searchLocation) {
		this.searchLocation = searchLocation;
	}

	public Job getSearchJob() {
		return searchJob;
	}

	public void setSearchJob(Job searchJob) {
		this.searchJob = searchJob;
	}

	public String getNameSearched() {
		return nameSearched;
	}

	public void setNameSearched(String nameSearched) {
		this.nameSearched = nameSearched;
	}

	public int getCountPost() {
		return countPost;
	}

	public void setCountPost(int countPost) {
		this.countPost = countPost;
	}

	public List<Role_Permission> getTest() {
		return test;
	}

	public void setTest(List<Role_Permission> test) {
		this.test = test;
	}
	
	
}
