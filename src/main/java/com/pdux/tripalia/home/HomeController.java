package com.pdux.tripalia.home;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

	@Autowired
	private CityRepository cityRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Principal principal) {
		City city = new City();
		city.setId(123L);
		city.setName("jordi");
		city.setBookingUrl("dasdsa");
		cityRepository.save(city);
		return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
	}
}
