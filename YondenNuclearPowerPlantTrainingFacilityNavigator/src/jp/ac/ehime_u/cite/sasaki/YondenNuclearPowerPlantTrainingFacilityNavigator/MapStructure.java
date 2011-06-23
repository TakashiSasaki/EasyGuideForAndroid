package jp.ac.ehime_u.cite.sasaki.YondenNuclearPowerPlantTrainingFacilityNavigator;

import java.util.ArrayList;

public class MapStructure {
	private ArrayList<MapPoint> mapPoints;

	private ArrayList<MapPoint> buildings;
	private ArrayList<ArrayList<MapPoint>> rooms;
	private ArrayList<ArrayList<ArrayList<MapPoint>>> equipments;

	MapStructure(String facility_name, String media_root){
		
	}
	//テスト用
	MapStructure(){
		
	}
}
