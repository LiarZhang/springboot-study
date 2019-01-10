package com.xf.zhang.controller;


import com.alibaba.fastjson.JSONObject;
import com.xf.zhang.domain.City;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class CityController {

    private static final String INDEX="city";//索引

    private static final String TYPE="city";//相当于DB中的table

    @Autowired
    private TransportClient transportClient;

    /**
     * 创建索引
     * @param index
     * @return
     */
    @RequestMapping("/create")
    public String crateIndex(String index){
        IndicesExistsResponse inExistsResponse = transportClient.admin().indices().exists(new IndicesExistsRequest(index)).actionGet();
        if(inExistsResponse.isExists()){
            return index+ "is exist";
        }
        CreateIndexResponse indexresponse = transportClient.admin().indices().prepareCreate(index).execute().actionGet();
        Boolean bool=indexresponse.isAcknowledged();
        return bool==true ? "success": "failer";
    }

    /**
     * 添加/修改数据
     * @param city
     * @return
     */
    @RequestMapping("/save")
    public String saveByIndex(City city){
        Map<String, Object> source = new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(city.getId())){
            source.put("id", city.getId());
        }
        if(StringUtils.isNotEmpty(city.getName())){
            source.put("name", city.getName());
        }
        if(StringUtils.isNotEmpty(city.getDescription())){
            source.put("description", city.getDescription());
        }
        if(StringUtils.isNotEmpty(city.getScore()+"")){
            source.put("score", city.getScore());
        }
        IndexResponse response = transportClient.prepareIndex(INDEX, TYPE, city.getId()).setSource(source).get();
        return response.getIndex();
    }

    /**
     * 根据id查询
     */
    @RequestMapping("/getById")
    public Map<String, Object> getById( String id){
        GetResponse response = transportClient.prepareGet(INDEX, TYPE, id).get();
        return response.getSource();
    }


    /**
     * 复杂查询
     * @return
     */
    @RequestMapping("/search")
    public JSONObject search(){
        /**
         * must ::      多个查询条件的完全匹配，相当于 and。
         * must_not ::  多个查询条件的相反匹配，相当于 not。
         * should ::    至少有一个查询条件匹配, 相当于 or。
         */
        JSONObject jsonObject=new JSONObject();
        SearchResponse response = transportClient.prepareSearch(INDEX).setTypes(TYPE)
                .setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.rangeQuery("score").from(80).to(95))
                        .must(QueryBuilders.matchQuery("description", "city"))
                        .must(QueryBuilders.prefixQuery("name", "beijing")))//匹配前缀
                .get();
        SearchHits hits = response.getHits();

        List<Map<String, Object>> list = new ArrayList();
        hits.forEach(action->{
            list.add(action.getSourceAsMap());
        });
        jsonObject.put("data",list);
        jsonObject.put("total",list.size());
        return jsonObject;
    }

    /**
     * 查询全部（0-50条数据）
     * @return
     */
    @RequestMapping("/getAll")
    public JSONObject getAll(){
        JSONObject jsonObject=new JSONObject();
        SearchResponse response = transportClient.prepareSearch(INDEX).setTypes(TYPE)
                .setFrom(0).setSize(50)//如果不设置默认查10条数据
                .setQuery(QueryBuilders.matchAllQuery())
               // .addSort("id", SortOrder.DESC)  // Set fielddata=true on [id],这里会报错
                .get();
        SearchHits hits = response.getHits();
        List<Map<String, Object>> list = new ArrayList();

        hits.forEach(action->{
            list.add(action.getSourceAsMap());
        });
        jsonObject.put("data",list);
        jsonObject.put("total",list.size());
        System.out.println("list size:"+list.size());
        return jsonObject;
    }

    @RequestMapping("/get")
    public JSONObject get(String content){
        JSONObject jsonObject=new JSONObject();

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red;font-weight:bold'>");//设置前缀
        highlightBuilder.postTags("</span>");//设置后缀
        highlightBuilder.field("name");// 设置高亮字段

        SearchResponse response = transportClient.prepareSearch(INDEX).setTypes(TYPE)
                //ES为了避免深分页，不允许使用分页(from&size)查询10000条以后的数据
                .setFrom(0).setSize(50)//如果不设置默认查10条数据
                .highlighter(highlightBuilder)  //设置高亮
                .setQuery(QueryBuilders.boolQuery()
                        // .must(QueryBuilders.rangeQuery("score").from(80).to(95))
                        // .must(QueryBuilders.matchQuery("description", content))
                        .must(QueryBuilders.matchPhraseQuery("name", content)))
                .get();

        SearchHits hits = response.getHits();
        List<Map<String, Object>> list = new ArrayList();
        //遍历数据 -->高亮处理数据
        hits.forEach(action->{
            Map<String, HighlightField> hightlightFields = action.getHighlightFields();
            HighlightField titleField = hightlightFields.get("name");
            //获取到原有内容中 每个高亮显示 集中位置fragment就是高亮片段
            Text[] fragments = titleField.fragments();
            String str=StringUtils.join(fragments,"-");//数组转字符串
            action.getSourceAsMap().put("name",str);
            list.add(action.getSourceAsMap());
        });
        jsonObject.put("data",list);
        jsonObject.put("total",list.size());
        return jsonObject;
    }

    /**
     * 分页查询
     * ES为了避免深分页，不允许使用分页(from&size)查询10000条以后的数据
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping("/getByScroll")
    public JSONObject getByScroll(int pageNumber,int pageSize){
        JSONObject jsonObject=new JSONObject();

        SearchResponse response = transportClient.prepareSearch(INDEX).setTypes(TYPE)
                .setScroll(new TimeValue(60000))
                .setSize(pageSize)//设置分页数量
                .get();
        SearchHits hits = response.getHits();
        List<Map<String, Object>> list = new ArrayList();
        if(pageNumber==1){//如果查询第一页直接返回数据
            //遍历数据
            hits.forEach(action->{
                list.add(action.getSourceAsMap());
            });
            jsonObject.put("data",list);
            jsonObject.put("total",list.size());
        }else {
            int time=1;
            while(true) {
                SearchResponse response2 = transportClient.prepareSearchScroll(response.getScrollId())
                        .setScroll(new TimeValue(60000))
                        .execute()
                        .actionGet();
                SearchHits hits2 = response2.getHits();
                if(++time==pageNumber){//判断页数

                    hits2.forEach(action->{
                        list.add(action.getSourceAsMap());
                    });
                    jsonObject.put("data",list);
                    jsonObject.put("total",list.size());
                    break;
                }
                //判断结束条件
                if (response2.getHits().getHits().length == 0) {
                    jsonObject.put("data",list);
                    jsonObject.put("total",list.size());
                    break;
                }
            }
        }
        return jsonObject;
    }
}
