package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import com.javaweb.Utils.ConnectionJDBCUtil;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.entity.DistrictEntity;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository {


	@Override
	public DistrictEntity findNameById(Long id) {
		String sql = "select d.name from district d where d.id = " + id + ";";
		DistrictEntity districtEntity = new DistrictEntity();
		try (Connection conn = ConnectionJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {

				districtEntity.setName(rs.getString("name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected Failed!");
		}
		return districtEntity;
	}

}
