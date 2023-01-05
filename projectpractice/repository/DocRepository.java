package com.example.projectpractice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.example.projectpractice.document.Doc;

@EnableElasticsearchRepositories
public interface DocRepository extends ElasticsearchRepository<Doc, String> {
 void save(Doc doc);
	
	@Query("{\"match\":{\"input\":\"?0\"}}")
	Page<Doc> findByInput(String name, Pageable pageable);
	
	@Query("{\"bool\":{\r\n"
			+ "    \"must\":[{\r\n"
			+ "      \"multi_match\":{\r\n"
			+ "        \"fields\":  [ \"input\" ],\r\n"
			+ "      \"query\":     \"?0\"\r\n"
			+ "      }\r\n"
			+ "      },\r\n"
			+ "      {\r\n"
			+ "      \"multi_match\":{\r\n"
			+ "        \"fields\":[\"output\"],\r\n"
			+ "        \"query\":\"?1\"\r\n"
			+ "      }\r\n"
			+ "    }\r\n"
			+ "      ]\r\n"
			+ "  }}")
	Page<Doc> findByInputOutput(String input,String output,Pageable pageable);
   

}
