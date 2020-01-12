//package lixco.com.beans;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.Serializable;
//
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//
//import org.primefaces.event.FileUploadEvent;
//
//@ManagedBean
//@SessionScoped
//public class UploadImage implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//	// Thu muc chua image
//	private String destination = "D:\\WebTimViec\\WebTimViec_WEB\\WebContent\\resources\\images\\";
//	
//	
//	public void upload(FileUploadEvent event) {
//		try {
//			InputStream in = event.getFile().getInputstream();
//			// Ghi inputStream vao 1 OutputStream
//			// Tao outputstream -> new doi tuong xong truyen duong dan + fileName
//			OutputStream out = new FileOutputStream(new File(destination + "/" + event.getFile().getFileName()));
//			int read = 0;
//			byte[] bytes = new byte[1024];
//
//			while ((read = in.read(bytes)) != -1) {
//				out.write(bytes, 0, read);
//			}
//
//			in.close();
//			out.flush();
//			out.close();
//			System.err.println("New file created!");
//
////			Company temp = companyService.findById(customer.getCompany().getId());
////			temp.setLogo(event.getFile().getFileName());
////			companyService.update(temp);
////			PrimeFaces.current().executeScript("PF('dialogCreateSuccessLogo').show()");
//
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
////			PrimeFaces.current().executeScript("PF('dialogCreateErrorLogo').show()");
//		}
//	}
//
//
//	public String getDestination() {
//		return destination;
//	}
//
//
//	public void setDestination(String destination) {
//		this.destination = destination;
//	}
//	
//	
//
//}
