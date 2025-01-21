package com.hanaro.endingcredits.endingcreditsapi.utils.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
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
                .withBasicAuth("elastic", "endingcredits") // Elasticsearch 인증 정보
                .build();
    }
}