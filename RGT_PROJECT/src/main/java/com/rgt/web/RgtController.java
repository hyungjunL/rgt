package com.rgt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rgt.dto.OrderDTO;
import com.rgt.service.RgtService;

@RestController
public class RgtController {

	@Autowired
	private RgtService rgtService;
	
	/**
	 * RestAPI 과제
	 * @param orderDTO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/rgt/test" }, method = RequestMethod.POST)
	public String rgtTest(@RequestBody OrderDTO orderDTO) throws Exception {
		
		return rgtService.rgtTest(orderDTO);
		
	}
	
	@RequestMapping(value = { "/async" }, method = RequestMethod.GET)
	public String async(@RequestParam(value="param") String param) throws Exception {
		
		return rgtService.async(param);
		
	}
	
	
}
