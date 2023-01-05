package com.example.projectpractice.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Similarity;

@Document(indexName = "index30")
public class Doc {
	
	 @Id
	 private String table;
	 
	 @Field(type = FieldType.Text,analyzer = "standard",similarity = Similarity.BM25)
     private String input;
	 
	 @Field(type=FieldType.Text,similarity = Similarity.BM25)
	 private String output;
	 
     public Doc(String input,String output){
    	 this.input=input;
    	 this.output=output;
     }
     public void setInput(String input) {
    	 this.input=input;
     }
     public String getInput() {
    	 return input;
     }
     public void setOutput(String output) {
    	 this.output=output;
     }
     public String getOutput() {
    	 return output;
     }
}