package com.duckbill.cine_list.db.repository;

import com.duckbill.cine_list.db.entity.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface FilmeRepository extends JpaRepository<Filme, UUID> {

}