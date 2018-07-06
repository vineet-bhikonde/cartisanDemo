package com.dbmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.business.RepairInfo;

public class DBConnection {

	private static Connection conn = null;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "root123");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int saveData(long key, RepairInfo repairInfo) {
		int result = 0;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("insert into car_repair values(?,?,?,?,?,?)");
			pstmt.setLong(1, key);
			pstmt.setString(2, repairInfo.getName());
			pstmt.setString(3, repairInfo.getEmail());
			pstmt.setString(4, repairInfo.getPhone());
			pstmt.setString(5, repairInfo.getModel());
			pstmt.setString(6, repairInfo.getProblem());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public long getMaxKey() throws Exception {
		PreparedStatement pstmt = conn.prepareStatement("select max(id) as max from car_repair");
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			return rs.getInt("max") + 1;
		}
		return 0;
	}

	public int saveImagePath(long key, String imagePath) throws Exception {
		PreparedStatement pstmt = conn.prepareStatement("insert into car_image values(?,?)");
		pstmt.setLong(1, key);
		pstmt.setString(2, imagePath);
		return pstmt.executeUpdate();
	}
}