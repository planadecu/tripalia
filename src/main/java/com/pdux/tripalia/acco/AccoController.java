package com.pdux.tripalia.acco;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccoController {
	@Inject
	private AccoRespository accoRespository;

	@RequestMapping(path = "/{lang}/h/{name}", method = RequestMethod.GET)
	public ModelAndView index(@PathVariable String lang,
			@PathVariable String name) throws InterruptedException,
			ExecutionException, UnsupportedEncodingException {
		Acco acco = accoRespository.findOneByName(
				URLDecoder.decode(name, "UTF-8").replaceAll("-", " ")).get();
		Map<String, Object> map = new HashMap<>();
		map.put("acco", acco);
		map.put("lang", lang);
		if (acco == null)
			return new ModelAndView("redirect:/" + lang + "/h/");
		else
			return new ModelAndView("acco/acco", map);
	}

	@RequestMapping(path = "/{lang}/h/", method = RequestMethod.GET)
	public ModelAndView index(@PathVariable String lang)
			throws InterruptedException, ExecutionException {
		Page<Acco> accos = accoRespository.findAllByPage(new PageRequest(0,
				100, new Sort(Sort.Direction.DESC, "name")));
		Map<String, Object> map = new HashMap<>();
		map.put("accos", accos);
		map.put("lang", lang);
		return new ModelAndView("acco/list", map);
	}
}
