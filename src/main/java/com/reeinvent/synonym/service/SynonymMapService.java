package com.reeinvent.synonym.service;

import java.util.List;

import com.reeinvent.synonym.model.SynonymMap;
import com.reeinvent.synonym.model.Word;

public interface SynonymMapService {

	public SynonymMap createSynonym(SynonymMap map);
	public List<SynonymMap> getAll();
	public List<String> getSynonymByWord(String word);
}
