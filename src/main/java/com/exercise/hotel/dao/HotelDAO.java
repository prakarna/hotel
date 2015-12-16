package com.exercise.hotel.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.exercise.hotel.model.Hotel;

public class HotelDAO {
	private static HashMap<String, List<Hotel>> hotelMap = null;
	
	public HotelDAO() {
		if (hotelMap == null || hotelMap.isEmpty()) {
			init();
		}
	}
	
	public List<Hotel> search(String city) {
		return hotelMap.get(city);
	}
	
	private void init() {
		String csvFile = "../hoteldb.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {
			
			ArrayList<Hotel> listHotel = null;
			String key = null;
			hotelMap = new HashMap<String, List<Hotel>>();	
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			
			InputStream inputStream = classLoader.getResourceAsStream(csvFile);

			br = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = br.readLine()) != null) {
				String[] data = line.split(cvsSplitBy);
				key = data[0];
				
				if (key.equals("CITY")) continue;
				
				Hotel hotel = new Hotel(key, Integer.parseInt(data[1]), data[2], Double.parseDouble(data[3]));
				
				if (hotelMap.get(key) != null) {
					listHotel = (ArrayList<Hotel>) hotelMap.get(key);
				} else {
					listHotel = new ArrayList<Hotel>();
				}
				
				listHotel.add(hotel);
				
				hotelMap.put(key, listHotel);
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public  HashMap<String, List<Hotel>> getHotelMap() {
		return hotelMap;
	}
}
