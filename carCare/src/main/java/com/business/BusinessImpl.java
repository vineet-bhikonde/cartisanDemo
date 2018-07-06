package com.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.dbmanager.DBConnection;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataBodyPart;

public class BusinessImpl {

	private static final String ROOT_PATH = "/cartisan/webApp/carCare/imagesVechile/";

	public String process(RepairInfo repairInfo, List<FormDataBodyPart> bodyParts) {

		String result = "FAILED";
		try {
			DBConnection dbConnection = new DBConnection();
			long key = dbConnection.getMaxKey();
			int rowCount = dbConnection.saveData(key, repairInfo);
			if (rowCount > 0) {
				for (int i = 0; i < bodyParts.size(); i++) {
					BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
					String fileName = bodyParts.get(i).getContentDisposition().getFileName();
					File directory=new File(ROOT_PATH);
					if(!directory.exists()) {
						directory.mkdirs();
					}
					String filePath = ROOT_PATH + fileName;
					uploadImage(bodyPartEntity.getInputStream(), filePath);
					rowCount = dbConnection.saveImagePath(key, filePath);
				}
			}
			if (rowCount > 0) {
				result = "SUCCESS";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void uploadImage(InputStream inputStream, String filePath) {
		try {
			OutputStream out = new FileOutputStream(new File(filePath));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(filePath));
			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
