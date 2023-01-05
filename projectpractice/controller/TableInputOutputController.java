package com.example.projectpractice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectpractice.document.Doc;
import com.example.projectpractice.entities.TableInputOutput;
import com.example.projectpractice.exception.ResourceNotFoundException;
import com.example.projectpractice.repository.DocRepository;
import com.example.projectpractice.repository.TableInputOutputRepository;
import com.example.projectpractice.services.TableServices;

@CrossOrigin
@RestController
@RequestMapping("/ESapi")
public class TableInputOutputController {
	 
	
	@Autowired
    TableInputOutputRepository tableInputOutputRepository;
	
	@Autowired
	TableServices tableServices;
	
	@Autowired
	DocRepository docRepository;
	
	//getting list of all configurations rest api
	@GetMapping("/list")
	public ResponseEntity<List<TableInputOutput>> getAllTables(){
		
		List<TableInputOutput> list = tableInputOutputRepository.findAll();
		if(list.size()<=0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.of(Optional.of(list));
		
	}
	//create configuration rest api
		@PostMapping(value = "/list",consumes = {"application/json"})
		public TableInputOutput createConfiguration(@RequestBody TableInputOutput configuration) {
			return tableInputOutputRepository.save(configuration);
		}
		
		//API for getting columns based on table name
		@GetMapping("/list/{tableName}")
		public ResponseEntity<TableInputOutput>  getConfigurationById(@PathVariable String tableName) {
			TableInputOutput configuration = tableInputOutputRepository.findById(tableName).orElseThrow(()-> new ResourceNotFoundException("Table with name :"+tableName+" does not exist"));
			return ResponseEntity.ok(configuration);
		}
		
		//API for update into configuration page
		@PutMapping("/list/{tableName}")
		public ResponseEntity<TableInputOutput> updateConfiguration(@PathVariable String tableName,@RequestBody TableInputOutput configuration){
		      TableInputOutput c = tableInputOutputRepository.findById(tableName).orElseThrow(()-> new ResourceNotFoundException("Table with name :"+tableName+" does not exist"));
		      c.setTableName(configuration.getTableName());
		      c.setInputColumns(configuration.getInputColumns());
		      c.setOutputColumns(configuration.getOutputColumns());
		      tableInputOutputRepository.save(c);
		      return ResponseEntity.ok(c);
		}
		
		//API for delete the table in configuration page
		@DeleteMapping("/list/{tableName}")
		public ResponseEntity<Map<String,Boolean>> deleteConfiguration(@PathVariable String tableName){
			TableInputOutput c = tableInputOutputRepository.findById(tableName).orElseThrow(()-> new ResourceNotFoundException("Table with name :"+tableName+" does not exist"));
			tableInputOutputRepository.delete(c);
			HashMap<String,Boolean> response = new HashMap<>();
			response.put("delete", Boolean.TRUE);
			return ResponseEntity.ok(response);
		}
		
		//API for retrieve all tables from database
		@GetMapping("/tables")
		public ResponseEntity<ArrayList<String>> getTable() {
			ArrayList<String> list = tableServices.getAllTables();
			return ResponseEntity.of(Optional.of(list));
		}
		
		//API for getting columns of a particular table
		@RequestMapping(value="/tables/{tableName}", method = RequestMethod.GET)
		public ArrayList<String> getColumns(@PathVariable String tableName){
			ArrayList<String> list = this.tableServices.getAllColumns(tableName);
			return list;			
		} 
		
		//Loading all the data into elasticsearch
		@GetMapping("/ESData")
		@ResponseBody
		public void submitData() throws Exception {
			 tableServices.saveData();
		}
		
		//API for searching 
		@GetMapping("/search/{inputsearch}")
		@ResponseBody
		public Page<Doc> findByInput(@PathVariable String inputsearch){
			Page<Doc> outputData;
			 List<String> al=Arrays.asList(inputsearch.split(" "));
		     System.out.println("list = "+al);
		     if(al.contains("of")){
		            int index = al.indexOf("of");
		            int length = 0;
		            for(int i=0;i<index;i++){
		                length+=al.get(i).length();
		            }
		            length+=index;
		            String output = inputsearch.substring(0,length);
		            String input = inputsearch.substring(length+2);
		            System.out.println("left side data output cols = "+output);
		            System.out.println("right side input data = "+input);
		            outputData = docRepository.findByInputOutput(input, output, PageRequest.of(0, 10));
		    }
			else {
			outputData = docRepository.findByInput(inputsearch,PageRequest.of(0, 10));
			}
			return outputData;
		}
		
		@Autowired
		ElasticsearchTemplate template;
		
		//API for deletion of an index in elasticsearch
		@GetMapping("/delete")
		public boolean deleteIndex() {
			return template.indexOps(Doc.class).delete();
		}
		
}
