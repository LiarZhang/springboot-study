package com.xf.zhang.service.impl;


import com.xf.zhang.domain.City;
import com.xf.zhang.repository.CityRepository;
import com.xf.zhang.service.CityService;
import lombok.NonNull;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CityServiceImpl implements CityService {


	private static final Logger LOGGER = LoggerFactory.getLogger(CityServiceImpl.class);

	/* 分页参数 */
	Integer PAGE_SIZE = 12;          // 每页数量
	Integer DEFAULT_PAGE_NUMBER = 0; // 默认当前页码

	/* 搜索模式 */
	String SCORE_MODE_SUM = "sum"; // 权重分求和模式
	Float  MIN_SCORE = 10.0F;      // 由于无相关性的分值默认为 1 ，设置权重分最小值为 10

	@Autowired
	CityRepository cityRepository; // ES 操作类

	public Long saveCity(@NonNull City city) {
		City cityResult = cityRepository.save(city);
		return cityResult.getId();
	}

	@Override
	public List<City> searchCity(Integer pageNumber, Integer pageSize, String searchContent) {

		// 校验分页参数
		if (pageSize == null || pageSize <= 0) {
			pageSize = PAGE_SIZE;
		}
		if (pageNumber == null || pageNumber < DEFAULT_PAGE_NUMBER) {
			pageNumber = DEFAULT_PAGE_NUMBER;
		}
		// 构建搜索查询
		SearchQuery searchQuery = getCitySearchQuery(pageNumber,pageSize,searchContent);
		LOGGER.info("\n searchCity: searchContent [" + searchContent + "] \n DSL  = \n " + searchQuery.getQuery().toString());
		Page<City> cityPage = cityRepository.search(searchQuery);
		LOGGER.info(":"+cityPage.getContent());
		return cityPage.getContent();
	}

	/**
	 * 根据搜索词构造搜索查询语句
	 *
	 * 代码流程：
	 *      - 权重分查询
	 *      - 短语匹配
	 *      - 设置权重分最小值
	 *      - 设置分页参数
	 * @param pageNumber 当前页码
	 * @param pageSize 每页大小
	 * @param searchContent 搜索内容
	 * @return
	 */
	// 基于ES2.4.6版本 查询
	private SearchQuery getCitySearchQuery(Integer pageNumber, Integer pageSize, String searchContent) {
		// 短语匹配到的搜索词，求和模式累加权重分
		// 权重分查询 https://www.elastic.co/guide/cn/elasticsearch/guide/current/function-score-query.html
		//   - 短语匹配 https://www.elastic.co/guide/cn/elasticsearch/guide/current/phrase-matching.html
		//   - 字段对应权重分设置，可以优化成 enum
		//   - 由于无相关性的分值默认为 1 ，设置权重分最小值为 10

		FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
//				.add(QueryBuilders.matchPhraseQuery("name", searchContent),//模糊查询
//						ScoreFunctionBuilders.weightFactorFunction(1000))
//				.add(QueryBuilders.matchPhraseQuery("description", searchContent),//模糊查询
//						ScoreFunctionBuilders.weightFactorFunction(500))
				//termsQuery按字段分词后，精确查询
				// （汉字只能查询一个字）-->需解决中文分词问题
//				.add(QueryBuilders.termsQuery("description",searchContent),
//						ScoreFunctionBuilders.weightFactorFunction(1000))
				//数字范围查询,上、下线开闭区间
//				.add(QueryBuilders.rangeQuery("score").from(80).to(90).includeLower(true).includeUpper(false),
//						ScoreFunctionBuilders.weightFactorFunction(1000))
				//gt大于、gte大于等于、lt小于、lte小于等于
//				.add(QueryBuilders.rangeQuery("score").gt(85).includeLower(true).lt(90).includeUpper(true),
//						ScoreFunctionBuilders.weightFactorFunction(1000))
//				.add(QueryBuilders.prefixQuery("description",searchContent),//前缀匹配-->汉字匹配一个
//						ScoreFunctionBuilders.weightFactorFunction(1000))
				.add(QueryBuilders.wildcardQuery("description",searchContent+"*"),//通配符查询,汉字同样
						ScoreFunctionBuilders.weightFactorFunction(1000))
				.scoreMode(SCORE_MODE_SUM).setMinScore(MIN_SCORE);
		// 分页参数
		Pageable pageable = new PageRequest(pageNumber, pageSize);
		return new NativeSearchQueryBuilder()
				.withPageable(pageable)
				.withQuery(functionScoreQueryBuilder).build();
	}
}
