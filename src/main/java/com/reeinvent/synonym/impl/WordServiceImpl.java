package com.reeinvent.synonym.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reeinvent.synonym.model.Word;
import com.reeinvent.synonym.repository.WordRepository;
import com.reeinvent.synonym.service.WordService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WordServiceImpl implements WordService {

	@Autowired
	private WordRepository wordRepository;

	@Override
	public List<Word> getAll() {
		return this.wordRepository.findAll();
	}

	@Override
	public synchronized Long findByWordName(String name) {
		return wordRepository.findByWordName(name);
	}

	@Override
	public synchronized Word createWord(String word) {
		return wordRepository.save(new Word(null, word));
	}

}
