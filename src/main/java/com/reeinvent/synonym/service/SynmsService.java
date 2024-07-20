package com.reeinvent.synonym.service;

import java.util.List;

import com.reeinvent.synonym.model.Synms;
import com.reeinvent.synonym.model.SynonymMap;

public interface SynmsService {

	public List<Long> findByWordId(Long id);
	public SynonymMap createSynms(SynonymMap map);
	public List<SynonymMap> getAll();
	public List<String> getSynonymByWord(String word);
}
