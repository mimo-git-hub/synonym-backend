package com.reeinvent.synonym.service;

import java.util.List;

import com.reeinvent.synonym.model.Word;

public interface WordService {
	
	public List<Word> getAll();
	public Long findByWordName(String name);
	public Word createWord(String word);
}
