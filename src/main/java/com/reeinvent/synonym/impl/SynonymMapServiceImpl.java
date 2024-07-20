package com.reeinvent.synonym.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reeinvent.synonym.model.SynonymMap;
import com.reeinvent.synonym.service.SynonymMapService;
import com.reeinvent.synonym.repository.SynonymRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional 
public class SynonymMapServiceImpl implements SynonymMapService {
	
	@Autowired
	private SynonymRepository synonymRepository;
	
	@Override
	public SynonymMap createSynonym(SynonymMap map) {

		if(map.getSynonym1().toUpperCase().equals(map.getSynonym2().toUpperCase())) {
			return map;
		}
		for(SynonymMap syn: synonymRepository.findAll()) {
			if(map.getSynonym1().toUpperCase().equals(syn.getSynonym1().toUpperCase()) 
					&& map.getSynonym2().toUpperCase().equals(syn.getSynonym2().toUpperCase())
					|| map.getSynonym1().toUpperCase().equals(syn.getSynonym2().toUpperCase()) 
					&& map.getSynonym2().toUpperCase().equals(syn.getSynonym1().toUpperCase())) {
				return map;
			}
		}
		return synonymRepository.save(map);
	}

	@Override
	public List<SynonymMap> getAll() {
		return synonymRepository.findAll();
	}

	@Override
	public List<String> getSynonymByWord(String word) {
		List<String> result = synonymRepository.findByName(word); //call to DB for result
		
		if(!result.isEmpty()) { //in case of no result app will skip additional logic
			List<SynonymMap> map = synonymRepository.findAll(); //Map of All Synonyms
			map = map.stream().filter(i -> func(i, word)).collect(Collectors.toList()); //reduce map for already searched synonyms
			List<String> resultTemp = new ArrayList<String>(); // list for new results
			List<String> resultLoop = result; //list for looping through items
			
			while(resultLoop.size() > 0) {
				for(String res : resultLoop) {
					for(SynonymMap mapLoop : map) {
						if(!func(mapLoop, res)) {
							String newRes = mapLoop.getSynonym1()!=res?mapLoop.getSynonym1():mapLoop.getSynonym2();
							if(!result.contains(newRes)) {
								resultTemp.add(newRes);
							}
						}
					}
					map = map.stream().filter(i -> func(i, res)).collect(Collectors.toList()); //reduce map for already searched synonyms
				}
				resultLoop = resultTemp;
				result.addAll(resultTemp);
				resultTemp = new ArrayList<String>();
			}
		}
	
		return result;
	}
	
	private boolean func(SynonymMap map, String exclude) {
		if(exclude.equals(map.getSynonym1()) || exclude.equals(map.getSynonym2())) {
			return false;
		}
		return true;
	}
	
}
