package com.example.smarthomeapp.model;

import java.util.HashSet;
import java.util.Set;

/**
 */
public class Room implements java.io.Serializable {

	// Fields

	private Integer roomId;
	private String roomName;
	private String roomPassword;
	private UserInfo userInfo;
	private Float roomSize;
	private Boolean isExistedAirCondition;
	private Boolean isExistedLamp;
	private Boolean isExistedWaterHeater;
	private Boolean isExistedSheSwitch;
	private Boolean isExistedCurtain;
	private Set<CircuitLine> circuitLines = new HashSet<CircuitLine>(0);
	private Set<Box> boxes = new HashSet<Box>(0);
	private Set<AirCondition> airConditon = new HashSet<AirCondition>(0);
	private Set<Curtain> curtain = new HashSet<Curtain>(0);
	private Set<Lamp> lamp = new HashSet<Lamp>(0);
	private Set<WaterHeater> waterHeater = new HashSet<WaterHeater>(0);
	// Constructors

	/** default constructor */
	public Room() {
	}

	/** minimal constructor */
	public Room(Float roomSize) {
		this.roomSize = roomSize;
	}

	/** full constructor */
	public Room(Integer roomId, String roomName, String roomPassword,
				UserInfo userInfo, Float roomSize, Boolean isExistedAirCondition,
				Boolean isExistedLamp, Boolean isExistedWaterHeater,
				Boolean isExistedSheSwitch, Boolean isExistedCurtain,
				Set<CircuitLine> circuitLines, Set<Box> boxes,
				Set<AirCondition> airConditon, Set<Curtain> curtain,
				Set<Lamp> lamp, Set<WaterHeater> waterHeater) {
		super();
		this.roomId = roomId;
		this.roomName = roomName;
		this.roomPassword = roomPassword;
		this.userInfo = userInfo;
		this.roomSize = roomSize;
		this.isExistedAirCondition = isExistedAirCondition;
		this.isExistedLamp = isExistedLamp;
		this.isExistedWaterHeater = isExistedWaterHeater;
		this.isExistedSheSwitch = isExistedSheSwitch;
		this.isExistedCurtain = isExistedCurtain;
		this.circuitLines = circuitLines;
		this.boxes = boxes;
		this.airConditon = airConditon;
		this.curtain = curtain;
		this.lamp = lamp;
		this.waterHeater = waterHeater;
	}





	public Integer getRoomId() {
		return this.roomId;
	}







	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}


	public Float getRoomSize() {
		return this.roomSize;
	}

	public void setRoomSize(Float roomSize) {
		this.roomSize = roomSize;
	}


	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getRoomPassword() {
		return roomPassword;
	}

	public void setRoomPassword(String roomPassword) {
		this.roomPassword = roomPassword;
	}



	public Set<CircuitLine> getCircuitLines() {
		return this.circuitLines;
	}

	public void setCircuitLines(Set<CircuitLine> circuitLines) {
		this.circuitLines = circuitLines;
	}

	public Set<Box> getBoxes() {
		return this.boxes;
	}

	public void setBoxes(Set<Box> boxes) {
		this.boxes = boxes;
	}




	public Set<AirCondition> getAirConditon() {
		return airConditon;
	}

	public void setAirConditon(Set<AirCondition> airConditon) {
		this.airConditon = airConditon;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public Set<Curtain> getCurtain() {
		return curtain;
	}

	public void setCurtain(Set<Curtain> curtain) {
		this.curtain = curtain;
	}
	public Set<Lamp> getLamp() {
		return lamp;
	}

	public void setLamp(Set<Lamp> lamp) {
		this.lamp = lamp;
	}


	public Set<WaterHeater> getWaterHeater() {
		return waterHeater;
	}

	public void setWaterHeater(Set<WaterHeater> waterHeater) {
		this.waterHeater = waterHeater;
	}


	public Boolean getIsExistedAirCondition() {
		return isExistedAirCondition;
	}

	public void setIsExistedAirCondition(Boolean isExistedAirCondition) {
		this.isExistedAirCondition = isExistedAirCondition;
	}

	public Boolean getIsExistedLamp() {
		return isExistedLamp;
	}

	public void setIsExistedLamp(Boolean isExistedLamp) {
		this.isExistedLamp = isExistedLamp;
	}
	public Boolean getIsExistedWaterHeater() {
		return isExistedWaterHeater;
	}

	public void setIsExistedWaterHeater(Boolean isExistedWaterHeater) {
		this.isExistedWaterHeater = isExistedWaterHeater;
	}
	public Boolean getIsExistedSheSwitch() {
		return isExistedSheSwitch;
	}

	public void setIsExistedSheSwitch(Boolean isExistedSheSwitch) {
		this.isExistedSheSwitch = isExistedSheSwitch;
	}
	public Boolean getIsExistedCurtain() {
		return isExistedCurtain;
	}

	public void setIsExistedCurtain(Boolean isExistedCurtain) {
		this.isExistedCurtain = isExistedCurtain;
	}
}
