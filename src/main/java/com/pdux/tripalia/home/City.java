package com.pdux.tripalia.home;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@SuppressWarnings("serial")
public class City implements java.io.Serializable {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String bookingUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBookingUrl() {
		return bookingUrl;
	}

	public void setBookingUrl(String bookingUrl) {
		this.bookingUrl = bookingUrl;
	}

}
