package com.javaweb.converter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.javaweb.Utils.MapUtil;
import com.javaweb.builder.BuildingSearchBuilder;

@Component
public class BuildingSearchBuilderConverter {
    public BuildingSearchBuilder toBuildingSearchBuilder(Map<String, Object> mp, List<String> typeCode) {
        BuildingSearchBuilder buildingSearchBuilder = new BuildingSearchBuilder.Builder()
            .setName(MapUtil.getObject(mp, "name", String.class))
            .setFloorArea(MapUtil.getObject(mp, "floorArea", Long.class))
            .setWard(MapUtil.getObject(mp, "ward", String.class))
            .setStreet(MapUtil.getObject(mp, "street", String.class))
            .setDistrictId(MapUtil.getObject(mp, "districtId", Long.class))
            .setNumberOfBasement(MapUtil.getObject(mp, "numberOfBasement", Integer.class))
            .setTypeCode(typeCode)
            .setManagerName(MapUtil.getObject(mp, "managerName", String.class))
            .setManagerPhoneNumber(MapUtil.getObject(mp, "managerPhoneNumber", String.class))
            .setRentPriceFrom(MapUtil.getObject(mp, "rentPriceFrom", Long.class))
            .setRentPriceTo(MapUtil.getObject(mp, "rentPriceTo", Long.class))
            .setAreaFrom(MapUtil.getObject(mp, "areaFrom", Long.class))
            .setAreaTo(MapUtil.getObject(mp, "areaTo", Long.class))
            .setStaffId(MapUtil.getObject(mp, "staffId", Long.class))
            .build();

        return buildingSearchBuilder;
    }
}
