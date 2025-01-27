package com.javaweb.repository.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.javaweb.Utils.ConnectionJDBCUtil;
import com.javaweb.Utils.NumberUtil;
import com.javaweb.Utils.StringUtil;
import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

	public static void joinTable(BuildingSearchBuilder buildingSearchBuilder, StringBuilder sql) {
		Long staffId = buildingSearchBuilder.getStaffId();
		if (staffId != null) {
			sql.append(" join assignmentbuilding on b.id = assignmentbuilding.buildingid ");
		}

		List<String> typecode = buildingSearchBuilder.getTypeCode();
		if (typecode != null && typecode.size() != 0) {
			sql.append(" join buildingrenttype on b.id = buildingrenttype.buildingid ");
			sql.append(" join renttype on renttype.id = buildingrenttype.renttypeid ");
		}

	}

	// normal: field cua bang do
	// special: phai join vs bang khac moi tim duoc (<= and <=)
	public static void queryNormal(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {

		//Java reflection
		try {
			Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
			for(Field item : fields) {
				item.setAccessible(true);
				String fieldName = item.getName();
				if(fieldName.equals("typecode") && fieldName.startsWith("area")
						&& fieldName.startsWith("rentPrice")) {
					Object value = item.get(buildingSearchBuilder);
					if (value != null) {
						if (item.getType().getName().equals("java.lang.Long") || item.getType().getName().equals("java.lang.Integer")) {
							where.append(" and b." + fieldName + " = " + value);
						} else if (item.getType().getName().equals("java.lang.String")){
							where.append(" and b." + fieldName + " like '%" + value + "%' ");
						}
					}
				}

			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
//		for (Map.Entry<String, Object> it : params.entrySet()) {
//			if (!it.getKey().equals("staffId") && !it.getKey().equals("typecode") && !it.getKey().startsWith("area")
//					&& !it.getKey().startsWith("rentPrice")) {
//				String value = it.getValue().toString();
//				if (StringUtil.checkString(value)) {
//					if (NumberUtil.isNumber(value)) {
//						where.append(" and b." + it.getKey() + " = " + value);
//					} else {
//						where.append(" and b." + it.getKey() + " like '%" + value + "%' ");
//					}
//				}
//			}
//		}
	}

	public static void querySpecial(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
		Long staffId = buildingSearchBuilder.getStaffId();
		if (staffId != null) {
			where.append(" and assignmentbuilding.staffId = " + staffId);
		}
		Long rentAreaTo = buildingSearchBuilder.getAreaTo();
		Long rentAreaFrom = buildingSearchBuilder.getAreaFrom();
		if (rentAreaTo != null || rentAreaFrom != null) {
			where.append(" and exists (select * from rentarea r where b.id = r.buildingid ");
			if(rentAreaFrom != null) {
				where.append(" and r.value >= " + rentAreaFrom);
			}
			if(rentAreaTo != null) {
				where.append(" and r.value <= " + rentAreaTo);
			}
			where.append(") ");
		}

		Long rentPriceTo = buildingSearchBuilder.getRentPriceTo();
		Long rentPriceFrom = buildingSearchBuilder.getRentPriceFrom();
		if (rentAreaTo != null || rentAreaFrom != null) {
			if (rentAreaFrom != null) {
				where.append(" and b.rentprice <= " + rentPriceTo);
			}
			if (rentAreaTo != null) {
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

		List<String> typecode = buildingSearchBuilder.getTypeCode();

		//java 8 - stream
		if (typecode != null && typecode.size() != 0) {
			where.append(" and(");
			String sql = typecode.stream().map(it-> "renttype.code like" + "'%" + it + "%'").collect(Collectors.joining(" or "));
			where.append(sql);
			where.append(" ) ");
		}

	}

	@Override
	public List<BuildingEntity> findAll(  BuildingSearchBuilder buildingSearchBuilder ) {
		StringBuilder sql = new StringBuilder(
				"select b.id, b.name, b.districtid, b.street, b.ward, b.numberofbasement, b.floorarea, b.rentprice, b.managername, b.managerphonenumber, b.servicefee, b.brokeragefee from building b ");
		joinTable(buildingSearchBuilder , sql);
		StringBuilder where = new StringBuilder("WHERE 1 = 1 ");
		queryNormal(buildingSearchBuilder , where);
		querySpecial(buildingSearchBuilder , where);
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
