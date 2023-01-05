package com.example.projectpractice.services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectpractice.document.Doc;
import com.example.projectpractice.repository.DocRepository;
import com.example.projectpractice.repository.TableInputOutputRepository;

@Service
public class TableServices {
	
	ArrayList<String> tables;
	ArrayList<String> columns;
	@Autowired
	TableInputOutputRepository tableInputOutputRepository;
	
	@Autowired
	DocRepository docRepository;

	public ArrayList<String> getAllTables() {
		System.out.println("In get all tables");
		try {
			tables = this.tableInputOutputRepository.getTables();
		} catch (Exception e) {
			System.out.println(e);
		}
		return tables;
	}

	public ArrayList<String> getAllColumns(String TableName) {
		try {
			columns = this.tableInputOutputRepository.getColumns(TableName);
		} catch (Exception e) {
			System.out.println(e);
		}
		return columns;
	}
	
	public String saveData() throws ExecutionException,InterruptedException,IOException {

		System.out.println("Loading Data");

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/elastic_Search_db", "root", "root");

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from table_input_output");
			
			ArrayList<Doc> esdata=new ArrayList<>();
    
			while(rs.next()) {
				String tableName = rs.getString(1);
				String inputColumns = rs.getString(2);
				String outputColumns = rs.getString(3);
				List<String> inputCols = Arrays.asList(inputColumns.split(";"));
				List<String> outputCols = Arrays.asList(outputColumns.split(";"));
				Statement st1 = con.createStatement();
				String sql1 = "select * from " + tableName;
				ResultSet rs1 = st1.executeQuery(sql1);
				while (rs1.next()) {
					String input_cols_data = "";
					String output_cols_data = "";
					for (int i = 0; i < inputCols.size(); i++) {
						input_cols_data += String.valueOf(rs1.getObject(inputCols.get(i))) + " ; ";
					}
					for (int i = 0; i < outputCols.size(); i++) {
						output_cols_data += tableName + " _ " + outputCols.get(i) + " _ "
								+ String.valueOf(rs1.getObject(outputCols.get(i))) + " ; ";
					}
					System.out.println("input data = "+input_cols_data);
					System.out.println("output data = "+output_cols_data);
					Doc esdata2=new Doc(input_cols_data,output_cols_data);
					docRepository.save(esdata2);
				
			   }
			}
			con.close();
			rs.close();
			System.out.println("Loading Completed");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "data is loaded into elastic search";

	}

}
