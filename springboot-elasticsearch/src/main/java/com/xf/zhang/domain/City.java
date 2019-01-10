package com.xf.zhang.domain;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "province", type = "city")
/**
 * 1.类上注解：
 * @Document (相当于Hibernate实体的@Entity/@Table)(必写)，
 * 加上了@Document注解之后，默认情况下这个实体中所有的属性都会被建立索引、并且分词。
 */
@Setter//use lombok
@Getter
public class City {

	/**
	 * 属性注解 @Field (相当于Hibernate实体的@Column注解)
	 * @Field默认是可以不加的，默认所有属性都会添加到ES中。
	 * 加上@Field之后，@document默认把所有字段加上索引失效，只有家@Field 才会被索引(同时也看设置索引的属性是否为no)
	 */

	/**
	 * 城市编号
	 */
	private Long id;

	/**
	 * 城市名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 城市评分
	 */
	private Integer score;
}
