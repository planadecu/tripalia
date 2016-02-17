package com.pdux.tripalia.sitemap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.pdux.tripalia.acco.Acco;
import com.pdux.tripalia.acco.AccoRespository;

@Controller
public class SitemapController {
	@Inject
	private AccoRespository accoRespository;

	@RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET, produces = "application/xml")
	@ResponseBody
	public ModelAndView sitemap() {
		Collection<AccoUri> accoUris = Collections2.transform(
				findAllFieldsName(),
				new Function<Acco, AccoUri>() {
					@Override
					public AccoUri apply(Acco acco) {
						try {
							return new AccoUri(URLEncoder.encode(
									acco.name == null ? acco.id : acco.name
											.replaceAll(" ", "-"), "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
					}
				});
		return new ModelAndView("sitemap/sitemap", "acco_uris", accoUris);
	}

	@Cacheable("acco.findAllFieldsName")
	public List<Acco> findAllFieldsName() {
		return accoRespository.findAllFieldsName();
	}

	@RequestMapping(value = "/robots.txt", method = RequestMethod.GET)
	public ModelAndView robots() {
		return new ModelAndView("sitemap/robots");
	}
}
