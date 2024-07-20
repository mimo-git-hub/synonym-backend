package com.reeinvent.synonym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reeinvent.synonym.model.SynonymMap;

@Repository
public interface SynonymRepository extends JpaRepository<SynonymMap, Long>{
	
	@Query(value ="SELECT DECODE(synonym_1, ?1, synonym_2, synonym_1) FROM synonyms WHERE synonym_1 = ?1 OR synonym_2 = ?1", nativeQuery = true)
	List<String> findByName(String name);
}
