package com.example.projectpractice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="table_input_output")
public class TableInputOutput {
	
	@Id
	@Column(name="table_name")
	private String tableName;
	@Column(name="input_columns")
	private String inputColumns;
	@Column(name="output_columns")
	private String outputColumns;
	
	
	public TableInputOutput() {
		super();
	}
	public TableInputOutput(String tableName, String inputColumns, String outputColumns) {
		super();
		this.tableName = tableName;
		this.inputColumns = inputColumns;
		this.outputColumns = outputColumns;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getInputColumns() {
		return inputColumns;
	}
	public void setInputColumns(String inputColumns) {
		this.inputColumns = inputColumns;
	}
	public String getOutputColumns() {
		return outputColumns;
	}
	public void setOutputColumns(String outputColumns) {
		this.outputColumns = outputColumns;
	}
	@Override
	public String toString() {
		return "TableInputOutput [tableName=" + tableName + ", inputColumns=" + inputColumns + ", outputColumns="
				+ outputColumns + "]";
	}
	
	

}
