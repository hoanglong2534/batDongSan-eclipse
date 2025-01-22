package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.javaweb.Utils.ConnectionJDBCUtil;
import com.javaweb.Utils.NumberUtil;
import com.javaweb.Utils.StringUtil;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

	public static void joinTable(Map<String, Object> params, List<String> typecode, StringBuilder sql) {
		String staffId = (String) params.get("staffid");
		if (StringUtil.checkString(staffId)) {
			sql.append(" join assignmentbuilding on b.id = assignmentbuilding.buildingid ");
		}
		if (typecode != null && typecode.size() != 0) {
			sql.append(" join buildingrenttype on b.id = buildingrenttype.buildingid ");
			sql.append(" join renttype on renttype.id = buildingrenttype.renttypeid ");
		}

	}

	// normal: field cua bang do
	// special: phai join vs bang khac moi tim duoc (<= and <=)
	public static void queryNormal(Map<String, Object> params, StringBuilder where) {
		for (Map.Entry<String, Object> it : params.entrySet()) {
			if (!it.getKey().equals("staffId") && !it.getKey().equals("typecode") && !it.getKey().startsWith("area")
					&& !it.getKey().startsWith("rentPrice")) {
				String value = it.getValue().toString();
				if (StringUtil.checkString(value)) {
					if (NumberUtil.isNumber(value)) {
						where.append(" and b." + it.getKey() + " = " + value);
					} else {
						where.append(" and b." + it.getKey() + " like '%" + value + "%' ");
					}
				}
			}
		}
	}

	public static void querySpecial(Map<String, Object> params, List<String> typecode, StringBuilder where) {
		String staffId = (String) params.get("staffId");
		if (StringUtil.checkString(staffId)) {
			where.append(" and assignmentbuilding.staffId = " + staffId);
		}
		String rentAreaTo = (String) params.get("areaTo");
		String rentAreaFrom = (String) params.get("areaFrom");
		if (StringUtil.checkString(rentAreaFrom) || StringUtil.checkString(rentAreaTo)) {
			where.append(" and exists (select * from rentarea r where b.id = r.buildingid ");
			if(StringUtil.checkString(rentAreaFrom)) {
				where.append(" and r.value >= " + rentAreaFrom);
			}
			if(StringUtil.checkString(rentAreaTo)) {
				where.append(" and r.value <= " + rentAreaTo);
			}
			where.append(") ");
		}

		String rentPriceTo = (String) params.get("rentPriceTo");
		String rentPriceFrom = (String) params.get("rentPriceFrom");
		if (StringUtil.checkString(rentPriceTo) || StringUtil.checkString(rentPriceFrom)) {
			if (StringUtil.checkString(rentPriceTo)) {
				where.append(" and b.rentprice <= " + rentPriceTo);
			}
			if (StringUtil.checkString(rentPriceFrom)) {
				where.append(" and b.rentprice >= " + rentPriceFrom);
			}
		}

		// java 7

//		if (typecode != null && typecode.size() != 0) {
//			List<String> code = new ArrayList<>();
//			for (String item : typecode) {
//				code.add("'" + item + "'");
//			}
//			where.append(" and renttype.code in(" + String.join(",", code) + ")");
//		}

		//java 8 - stream
		if (typecode != null && typecode.size() != 0) {
			where.append(" and(");
			String sql = typecode.stream().map(it-> "renttype.code like" + "'%" + it + "%'").collect(Collectors.joining(" or "));
			where.append(sql);
			where.append(" ) ");
		}

	}

	@Override
	public List<BuildingEntity> findAll(Map<String, Object> params, List<String> typecode) {
		StringBuilder sql = new StringBuilder(
				"select b.id, b.name, b.districtid, b.street, b.ward, b.numberofbasement, b.floorarea, b.rentprice, b.managername, b.managerphonenumber, b.servicefee, b.brokeragefee from building b ");
		joinTable(params, typecode, sql);
		StringBuilder where = new StringBuilder("WHERE 1 = 1 ");
		queryNormal(params, where);
		querySpecial(params, typecode, where);
		where.append("group by b.id;");
		List<BuildingEntity> result = new ArrayList<>();
		sql.append(where);
		try (Connection conn = ConnectionJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql.toString());) {
			while (rs.next()) {
				BuildingEntity buildingEntity = new BuildingEntity();
				buildingEntity.setId(rs.getLong("b.id"));
				buildingEntity.setName(rs.getString("b.name"));
				buildingEntity.setWard(rs.getString("b.ward"));
				buildingEntity.setDistrictid(rs.getLong("b.districtid"));
				buildingEntity.setStreet(rs.getString("b.street"));
				buildingEntity.setFloorArea(rs.getLong("b.floorarea"));
				buildingEntity.setRentPrice(rs.getLong("b.rentprice"));
				buildingEntity.setServiceFee(rs.getString("b.servicefee"));
				buildingEntity.setBrokerageFee(rs.getLong("b.brokeragefee"));
				buildingEntity.setManagerName(rs.getString("b.managername"));
				buildingEntity.setManagerPhoneNumber(rs.getString("b.managerphonenumber"));
				buildingEntity.setNumberofbasement(rs.getLong("b.numberofbasement"));

				result.add(buildingEntity);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected Failed!");
		}

		return result;
	}

}
