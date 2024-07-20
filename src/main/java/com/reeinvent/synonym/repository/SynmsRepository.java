package com.reeinvent.synonym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reeinvent.synonym.model.Synms;

@Repository
public interface SynmsRepository extends JpaRepository<Synms, Long>{

	@Query(value = "SELECT DECODE(syns1, ?1, syns2, syns1) FROM syn_map WHERE syns1 = ?1 OR syns2 = ?1", nativeQuery = true)
	 List<Long> findByWordId(Long id);
}
