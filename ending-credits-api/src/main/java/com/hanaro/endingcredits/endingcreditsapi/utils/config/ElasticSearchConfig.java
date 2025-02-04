package com.hanaro.endingcredits.endingcreditsapi.utils.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.client.ClientConfiguration;

@Configuration
@EnableElasticsearchRepositories(
        basePackages = "com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.elasticsearch"
)
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .withBasicAuth("elastic", "endingcredits")
                .build();
    }

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClient restClient = RestClient.builder(HttpHost.create("http://localhost:9200")).build();
        RestClientTransport transport = new RestClientTransport(restClient, new co.elastic.clients.json.jackson.JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    @Bean
    public ElasticsearchOperations elasticsearchOperations() {
        return new ElasticsearchTemplate(elasticsearchClient());
    }
}
