package lixco.com.beans;

import java.io.IOException;
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
import lixco.com.services.JobService;
import lixco.com.services.LocationService;
import lixco.com.services.PostService;
import lixco.com.services.Post_JobService;

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
	private static Post_JobService p_jService;

	private List<Post> posts = new ArrayList<>();
	private List<Location> locations;
	private List<Job> jobs;
	private Job job;

	// Tim kiem
	private Post post;
	private Location searchLocation = new Location();
	private Job searchJob = new Job();
	private String nameSearched = "";
	private Post deleted;
	private Post updated;
	private int updateLocationId;
	private List<Post> searchPosts;
	private Post detailPost;
	
	//Phan trang 
	private int offset = 0;
	private int limit = 4;
	private int totalPages;
	private List<Integer> listPages;

	@PostConstruct
	public void init() {
		// init variable search feature
		searchPosts = postService.findAllByFilter();
		//Tinh tong so page va dua vao list de hien ben view
		try {
			listPages = new ArrayList<>();
			totalPages = (searchPosts.size() / limit) + 1;
			for(int i = 1; i <= totalPages; i++) {
				listPages.add(i); 
			}

		} catch (Exception e) {
		}
		//get offset
		try {
			offset = this.getParamPost();
		} catch (Exception e) {
			
		}
		job = new Job();
		//Phan trang
		//Neu trang chu hoac trang 1
		if(offset == 0 || offset == 1) {
			posts = postService.findByPagination(limit, 0);
			System.out.println("ok");
		}
		
		if(1 < offset && offset <= totalPages) {
			int temp = offset;
			offset = (offset * limit) - limit;
			posts = postService.findByPagination(limit,offset);
			offset = temp;
			
		}
		if(offset > totalPages) {
			offset = (totalPages * limit) - limit;
			posts = postService.findByPagination(limit,offset);
			offset = totalPages;
		}
		//End phan trang
		locations = locationService.findAllByFilter();
		jobs = jobService.findAll();

		searchLocation = new Location();
		searchJob = new Job();
		try {
			getParam();
		} catch (Exception e) {

		}
		//init variable pagination
		
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
	
//Chuc nang phan trang

//End phan trang
	// Chuc nang tim kiem
	public void showpost() {
		posts = new ArrayList<>();
		if (searchJob == null) {
			searchJob = new Job();
		}
		if (searchLocation == null) {
			searchLocation = new Location();
		}
		if (StringUtils.isEmpty(nameSearched) && StringUtils.isEmpty(searchJob.getName())
				&& StringUtils.isEmpty(searchLocation.getName())) {
			posts = searchPosts;
		} else {
			List<Post_Job> temps = new ArrayList<>();
			temps = p_jService.find(nameSearched, searchJob.getName(), searchLocation.getName());
			for (Post_Job post : temps) {
				posts.add(post.getPost());
			}
		}
	}

	// Tim kiem theo ten bai post
	public List<String> searchPost(String input) {
		List<String> result = new ArrayList<>();
		List<Post> search = new ArrayList<>();
		search = postService.find(input, 0L);
		for (Post post : search) {
			result.add(post.getName());
		}
		return result;
	}

	// Tim kiem doi tuong Post
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

//Sua bai post

	// get param
	public void getParam() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String postId = request.getParameter("postid");
		long num = Long.parseLong(postId);
		detailPost = postService.findById(num);
//		specifyTags = post_keywordService.findByPostId(detailPost.getId());
	}
	// get param -> show post
	public int getParamPost() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String temp = request.getParameter("page");
		return Integer.parseInt(temp);
	}

	public String returnIndex() {
		return "post";
	}

// GET URI
	public String getURI() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String uri = request.getRequestURI();
		System.out.println(uri);
		return uri;
	}
// 

//	START CRUD
	public void update() {
		// Tim cac quyen cua user tren form nay (them,xoa,sua,doc)
		AllowAccess allowAccess = (AllowAccess) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("allowAccess");
		// Kiem tra quyen
		if (allowAccess.isUpdate() == true) {
			Date date = new Date();
			detailPost.setModifiedDate(date);
			postService.update(detailPost);
			if (updateLocationId != 0) {
				detailPost.setLocation(locationService.findById(updateLocationId));
				updateLocationId = 0;
			}
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Sửa thành công .");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			posts = postService.findAllByFilter();
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Không có quyền.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}

	public void cancelUpdate() {
		String url="http://192.168.0.132:8380/WebTimViec_WEB/pages/admin/pagesadmin/post.jsf";
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		// Tim cac quyen cua user tren form nay (them,xoa,sua,doc)
		AllowAccess allowAccess = (AllowAccess) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("allowAccess");
		// Kiem tra quyen
		if (allowAccess.isDelete() == true) {
			deleted.setDeleted(true);
			postService.update(deleted);
			System.out.println("Đã xóa xong");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Delete", "Xóa thành công .");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			posts = postService.findAllByFilter();

		} else {
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

	public Post getDetailPost() {
		return detailPost;
	}

	public void setDetailPost(Post detailPost) {
		this.detailPost = detailPost;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<Integer> getListPages() {
		return listPages;
	}

	public void setListPages(List<Integer> listPages) {
		this.listPages = listPages;
	}

	public void setUpdateLocationId(int updateLocationId) {
		this.updateLocationId = updateLocationId;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
}
