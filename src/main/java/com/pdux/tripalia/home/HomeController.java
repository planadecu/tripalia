package com.pdux.tripalia.home;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mongodb.DBCollection;

@Controller
public class HomeController {

	@Autowired
	private MongoTemplate mongoTemplate;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Principal principal) {

		DBCollection collection = mongoTemplate.getCollection("city");
		System.out.println("db " + mongoTemplate.getDb() + " collection count "
				+ collection.getCount());
		return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
	}
}
