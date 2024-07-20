package com.reeinvent.synonym.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.reeinvent.synonym.model.SynonymMap;
import com.reeinvent.synonym.service.SynmsService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class SynonymRest {

	@Autowired
	public SynmsService synmsService;
	
	@PostMapping("/api")
	public SynonymMap createSynm(@RequestBody SynonymMap map) {	
		return synmsService.createSynms(map);
	}
	
	@GetMapping("/api/{name}")
	public ResponseEntity<List<String>> getSynonymsForWord(@PathVariable String name) {		
		return ResponseEntity.ok().body(synmsService.getSynonymByWord(name));
	}
	
	@GetMapping("/api")
	public List<SynonymMap>  getAllSyms() {		
		return synmsService.getAll();
	}
}
