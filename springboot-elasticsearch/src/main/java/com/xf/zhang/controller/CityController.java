package com.xf.zhang.controller;

import com.xf.zhang.domain.City;
import com.xf.zhang.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CityController {

	@Autowired
	private CityService cityService;

	/**
	 * 插入 ES 新城市
	 *
	 * @param city
	 * @return
	 */
	@PostMapping(value = "/api/city")
	public Long createCity(/*@RequestBody*/ City city) {
		return cityService.saveCity(city);
	}

	/**
	 * 搜索返回分页结果
	 *
	 * @param pageNumber 当前页码
	 * @param pageSize 每页大小
	 * @param searchContent 搜索内容
	 * @return
	 */
	@GetMapping(value = "/api/city/search")
	public List<City> searchCity(@RequestParam(value = "pageNumber") Integer pageNumber,
	                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
	                             @RequestParam(value = "searchContent") String searchContent) {
		return cityService.searchCity(pageNumber, pageSize,searchContent);
	}
}
