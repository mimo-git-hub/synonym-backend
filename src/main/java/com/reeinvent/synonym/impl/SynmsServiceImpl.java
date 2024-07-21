package com.reeinvent.synonym.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reeinvent.synonym.model.Synms;
import com.reeinvent.synonym.model.SynonymMap;
import com.reeinvent.synonym.model.Word;
import com.reeinvent.synonym.repository.SynmsRepository;
import com.reeinvent.synonym.repository.WordRepository;
import com.reeinvent.synonym.service.SynmsService;

import jakarta.transaction.Transactional;

@Service
@Transactional 
public class SynmsServiceImpl implements SynmsService {

	@Autowired
	private SynmsRepository synmsRepository;
	
	@Autowired
	private WordRepository wordRepository;
	
	@Override
	public List<Long> findByWordId(Long id) {
		return synmsRepository.findByWordId(id);
	}

	@Override
	public synchronized SynonymMap createSynms(SynonymMap map) {
		
		if(map.getSynonym1().toUpperCase().equals(map.getSynonym2().toUpperCase())) {
			return null;
		}
		
		Long syns1 = wordRepository.findByWordName(map.getSynonym1().toUpperCase());
		Long syns2 = wordRepository.findByWordName(map.getSynonym2().toUpperCase());
		
		if(syns1 != null && syns2 != null) {
			for(Synms syn : synmsRepository.findAll()) {
				if(syns1.equals(syn.getSyns1()) && syns2.equals(syn.getSyns2())
						|| syns1.equals(syn.getSyns2()) && syns2.equals(syn.getSyns1())) {
					return null;
				}
			}
		}
		
		if(syns1 == null) {
			syns1 = wordRepository.save(new Word(null, map.getSynonym1())).getId();
		}
		if(syns2 == null) {
			syns2 = wordRepository.save(new Word(null, map.getSynonym2())).getId();
		}
		
		Synms synm = synmsRepository.save(new Synms(null, syns1, syns2));
		map.setId(synm.getId());
		
		return map;
	}

	@Override
	public synchronized List<String> getSynonymByWord(String word) {
		Long id = wordRepository.findByWordName(word.toUpperCase()); //get id of word since synonyms are mapped with word id's for better db performance
		List<Long> result = findByWordId(id); //get direct synonyms for input word
		//Transitive rule implementation
		if(!result.isEmpty()) { //in case of no result app will skip additional logic
			List<Synms> map = synmsRepository.findAll();  
			map = map.stream().filter(i -> func(i, id)).collect(Collectors.toList()); //reduce map for already searched synonyms
			List<Long> resultTemp = new ArrayList<Long>(); // list for new results
			List<Long> resultLoop = result; //list for looping through items, this list prevents infinite loop
			
			while(resultLoop.size() > 0) { //search of Transitive sysnonyms
				for(Long res : resultLoop) {
					for(Synms mapLoop : map) {
						if(!func(mapLoop, res)) {
							Long newRes = mapLoop.getSyns1()!=res?mapLoop.getSyns1():mapLoop.getSyns2();
							if(!result.contains(newRes)) {
								resultTemp.add(newRes);
							}
						}
					}
					map = map.stream().filter(i -> func(i, res)).collect(Collectors.toList()); //reduce map for already searched synonyms
				}
				resultLoop = resultTemp;
				result.addAll(resultTemp);
				resultTemp = new ArrayList<Long>();
			}
		}
		
		Map<Long, String> wordsMap = wordRepository.findAll().stream().collect(Collectors.toMap(Word::getId, Word::getName));//fetch map of words and id's
				
		return result.stream().collect(Collectors.mapping(i -> wordsMap.get(i), Collectors.toList())); //return words as strings
		
	}
	
	private boolean func(Synms map, Long exclude) {
		if(exclude.equals(map.getSyns1()) || exclude.equals(map.getSyns2())) {
			return false;
		}
		return true;
	}

	@Override
	public synchronized List<SynonymMap> getAll() {
		
		List<Synms> synms = synmsRepository.findAll();
		Map<Long, String> wordsMap = wordRepository.findAll().stream().collect(Collectors.toMap(Word::getId, Word::getName));

		return synms.stream().collect(Collectors.mapping(i -> new SynonymMap(i.getId(), wordsMap.get(i.getSyns1()), wordsMap.get(i.getSyns2())), Collectors.toList()));
	}

}
