package com.exercise.hotel.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.exercise.hotel.dao.HotelDAO;
import com.exercise.hotel.model.Hotel;
import com.exercise.hotel.model.HotelResult;
import com.exercise.hotel.model.SortType;

@Path("/services")
public class HotelServiceImpl implements HotelService {

	private static final long serialVersionUID = -8713481922053509097L;
	
	@Override
	public Response searchByCity(String city, String sort) {
		
		if (city == null || city.trim().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Input not provided").build(); 
		}
		
		HotelDAO hotelDAO = new HotelDAO();
		List<Hotel> hotels = hotelDAO.search(city);
		HotelResult result = new HotelResult();
		if (hotels != null) {
			if (sort != null && !sort.isEmpty() && SortType.DESC.name().equalsIgnoreCase(sort)) {
				Collections.sort(hotels, new Comparator<Hotel>() {
					@Override
					public int compare(Hotel h1, Hotel h2) {
						return (h2.getPrice() > h1.getPrice())?1:-1;
					}
				});
			} else {
				Collections.sort(hotels);
			}
			
			/*for (String k: hotelDAO.getHotelMap().keySet()) {
				List<Hotel> listHotel = (ArrayList<Hotel>) hotelDAO.getHotelMap().get(k);
				for (int i=0; i<listHotel.size(); i++) {
					System.out.println("City :" + listHotel.get(i).getCity() + " Hotel ID: " + listHotel.get(i).getHotelId() + " price : " + listHotel.get(i).getPrice());
				}
			}*/
			
			result.setList(hotels);
		}
		return Response.status(200).entity(result).build(); 
	}
	
}
