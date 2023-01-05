package com.example.projectpractice.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.projectpractice.entities.TableInputOutput;

@Repository
public interface TableInputOutputRepository extends JpaRepository<TableInputOutput, String>{
	
	@Query(value="Show Tables",nativeQuery=true)
	public ArrayList<String> getTables();
	
	@Query(value="select Column_name from Information_schema.columns where Table_name=:n",nativeQuery = true)
	public ArrayList<String> getColumns(@Param("n") String TableName);
	

}