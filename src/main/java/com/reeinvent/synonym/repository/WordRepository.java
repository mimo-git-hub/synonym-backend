package com.reeinvent.synonym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reeinvent.synonym.model.Word;


@Repository
public interface WordRepository extends JpaRepository<Word, Long>{
	
	@Query(value = "SELECT id FROM words WHERE UPPER(name) = ?1", nativeQuery = true)
	Long findByWordName(String name);
}