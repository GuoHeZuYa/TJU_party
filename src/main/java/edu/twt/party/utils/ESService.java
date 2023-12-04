package edu.twt.party.utils;

/**
 * @ClassName: ESService
 * @Description:
 * @Author: 过河卒
 * @Date: 2023/2/13 17:06
 * @Version: 1.0
 */

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.cat.IndicesResponse;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexState;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ESService<T> {



    @Resource(name="clientByPasswd")
    private ElasticsearchClient elasticsearchClient;

    public void addIndex(String name) throws IOException {
        elasticsearchClient.indices().create(c -> c.index(name));
    }

    public boolean indexExists(String name) throws IOException {
        return elasticsearchClient.indices().exists(b -> b.index(name)).value();
    }

    public void delIndex(String name) throws IOException {
        elasticsearchClient.indices().delete(c -> c.index(name));
    }


    /**
     * 查看索引信息
     *
     * @param indexName
     * @return
     * @throws Exception
     */
    public Map<String, IndexState> getIndexMsg(String indexName) throws Exception {
        GetIndexResponse getIndexResponse = elasticsearchClient
                .indices()
                .get(getIndex -> getIndex
                        .index(indexName)
                );
        Map<String, IndexState> result = getIndexResponse.result();
        return result;
    }

    /**
     * 查看所有索引信息
     *
     * @return
     * @throws Exception
     */
    public List<IndicesRecord> getAllIndex() throws Exception {
        IndicesResponse indicesResponse = elasticsearchClient
                .cat()
                .indices();
        return indicesResponse.valueBody();
    }

    /**
     * 添加文档信息
     *
     * @param indexName
     * @param obj
     * @throws IOException
     */
    public long createDocument(String indexName, Object obj) throws Exception {
        
        IndexResponse indexResponse = elasticsearchClient
                .index(x -> x
                        .index(indexName)
                        .document(obj)
                );
        long version = indexResponse.version();

        return version;
    }

    /**
     * 添加文档信息 指定id
     *
     * @param indexName
     * @param obj
     * @throws IOException
     */
    public long createDocument(String indexName, String id, Object obj) throws Exception {
        
        IndexResponse indexResponse = elasticsearchClient
                .index(x -> x
                        .index(indexName)
                        .id(id)
                        .document(obj)
                );
        long version = indexResponse.version();

        return version;
    }

    /**
     * 修改文档自定义属性
     *
     * @param indexName
     * @param id
     * @param obj
     * @return version
     * @throws Exception
     */
    public long updateDocument(String indexName, String id, Object obj) throws Exception {

        UpdateResponse<Object> userUpdateResponse = elasticsearchClient
                .update(x -> x
                        .index(indexName)
                        .id(id)
                        .doc(obj), Object.class);
        long version = userUpdateResponse.version();


        return version;
    }

    public  <T> List<T> exactSearch(String indexName,String fieldName,String searchContent) throws IOException {

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(indexName)
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m.term(
                                        t -> t.field(fieldName).value(FieldValue.of(searchContent))))
                        )
                ));

        SearchResponse<T> response = (SearchResponse<T>) elasticsearchClient.search(searchRequest, getClass().getComponentType());
        return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }


    public <T> List<T> fuzzySearch(String indexName,String fieldName,String searchContent) throws IOException {

        MatchQuery query = QueryBuilders.match().field(fieldName).query(searchContent).build();

        SearchRequest searchRequest = SearchRequest.of(s -> s
                        .index(indexName)
                        .query(query._toQuery())
                        );
        SearchResponse<T> response = (SearchResponse<T>) elasticsearchClient.search(searchRequest,getClass().getComponentType());
        return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }


}
