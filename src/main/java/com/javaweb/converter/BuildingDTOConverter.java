package com.javaweb.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentareaEntity;

@Component
public class BuildingDTOConverter {
	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private RentAreaRepository rentAreaRepository;

	@Autowired
	private ModelMapper modelMapper;

	public BuildingDTO toBuildingDTO(BuildingEntity item) {
		BuildingDTO building = modelMapper.map(item, BuildingDTO.class);

		DistrictEntity districtEntity = districtRepository.findNameById(item.getDistrictid());
		building.setAddress(item.getStreet() + ", " + item.getWard() + ", " + districtEntity.getName());

//		building.setName(item.getName());
//		building.setManagerName(item.getManagerName());
//		building.setManagerPhoneNumber(item.getManagerPhoneNumber());
//		building.setNumberofbasement(item.getNumberofbasement());
//		building.setBrokerageFee(item.getBrokerageFee());
//		building.setFloorArea(item.getFloorArea());
//		building.setServiceFee(item.getServiceFee());
//		building.setRentPrice(item.getRentPrice());

		List<RentareaEntity> rentAreas = rentAreaRepository.findByRentArea(item.getId());
		String areaResult = rentAreas.stream().map(it -> it.getValue().toString()).collect(Collectors.joining(","));
		building.setRentArea(areaResult);


		return building;

	}

}
