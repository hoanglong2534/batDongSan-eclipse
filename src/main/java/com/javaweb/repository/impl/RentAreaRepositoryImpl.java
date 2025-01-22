package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.Utils.ConnectionJDBCUtil;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.RentareaEntity;

@Repository
public class RentAreaRepositoryImpl implements RentAreaRepository {


	@Override
	public List<RentareaEntity> findByRentArea(Long value) {
		String sql = "select r.value from rentarea r where r.buildingid = " + value + ";";
		List<RentareaEntity> rentAreas = new ArrayList<>();
		try (Connection conn = ConnectionJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				RentareaEntity  rentareaEntity = new RentareaEntity();
				rentareaEntity.setValue(rs.getString("value"));
				rentAreas.add(rentareaEntity);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected Failed!");
		}
		return rentAreas;
	}

}