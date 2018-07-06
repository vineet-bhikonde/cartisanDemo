package com.cartisan;

import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.business.BusinessImpl;
import com.business.RepairInfo;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

@Path("/carDetails")
public class RestController {

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postCarInfo(@FormDataParam("username") String username, @FormDataParam("email") String email,
			@FormDataParam("phone") String phone, @FormDataParam("model") String model,
			@FormDataParam("problem") String problem, @FormDataParam("file") List<FormDataBodyPart> bodyParts,
			@FormDataParam("file") FormDataContentDisposition formDataContentDisposition, @Context
			UriInfo uriInfo) {

		Boolean validate=validateData(username,email,phone,model,problem);
		
		if(!validate) {
			URI target=URI.create("http://localhost:8080/carCare/Error.html");
			return Response.temporaryRedirect(target).build();
		}
		RepairInfo repairInfo = new RepairInfo();
		repairInfo.setName(username);
		repairInfo.setEmail(email);
		repairInfo.setPhone(phone);
		repairInfo.setModel(model);
		repairInfo.setProblem(problem);

		BusinessImpl businessImpl = new BusinessImpl();
		String result = businessImpl.process(repairInfo, bodyParts);
		
		if(result.equals("SUCCESS")) {
			URI target=URI.create("http://localhost:8080/carCare/ThankYou.html");
			return Response.temporaryRedirect(target).build();
		}
		else {
			URI target=URI.create("http://localhost:8080/carCare/Error.html");
			return Response.temporaryRedirect(target).build();
		}
		//return new Viewable("/result", result);

	}
	
	public static Boolean validateData(String username,String email,String phone,String model,String problem) {			
			if(username.length()>0 && email.length()>0 && phone.length()>0 && model.length()>0 && problem.length()>0) {
				if(phone.length()>10) {
					return false;
				}else {
					Pattern p = Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b");
					Matcher m = p.matcher(email);
					if(!m.find()) {
						return false;
					}
				}
				return true;
			}
		return false;
	}

}