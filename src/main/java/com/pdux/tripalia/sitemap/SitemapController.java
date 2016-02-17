package com.pdux.tripalia.sitemap;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pdux.tripalia.acco.Acco;
import com.pdux.tripalia.acco.AccoRespository;

@Controller
public class SitemapController {
	private static final int PAGE_SIZE = 250;
	private static final String[] LANGS = new String[] { "es", "en", "fr", "de" };
	@Inject
	private AccoRespository accoRespository;

	@RequestMapping(value = "/robots.txt", method = RequestMethod.GET)
	public ModelAndView robots() {
		return new ModelAndView("sitemap/robots");
	}

	@RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView sitemap() {
		int accoPages = findAllFieldsUrl(new PageRequest(0, PAGE_SIZE))
				.getTotalPages();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("langs", LANGS);
		model.put("accoPages", accoPages);
		return new ModelAndView("sitemap/sitemap", model);
	}

	@RequestMapping(value = "/sitemap-home.xml.gz", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView sitemapHome() {
		return new ModelAndView("sitemap/sitemap-home", "langs", LANGS);
	}

	@RequestMapping(value = "/sitemap-accos-{lang:[a-zA-Z]{2}}-{page}.xml.gz", method = RequestMethod.GET,produces = "plain/xml; charset=utf-8")
	@ResponseBody
	public ModelAndView sitemap(@PathVariable String lang,
			@PathVariable Integer page) {
		Page<AccoUri> accoUris = findAllFieldsUrl(new PageRequest(page, 250))
				.map(new Converter<Acco, AccoUri>() {
					@Override
					public AccoUri convert(Acco source) {
						return new AccoUri(source.url);
					}
				});
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("lang", lang);
		model.put("acco_uris", accoUris);
		return new ModelAndView("sitemap/sitemap-accos", model);
	}

	@Cacheable("acco.findAllFieldsUrl")
	public Page<Acco> findAllFieldsUrl(Pageable page) {
		return accoRespository.findAllUrl(page);
	}

}
