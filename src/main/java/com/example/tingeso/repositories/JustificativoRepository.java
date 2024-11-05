package com.example.tingeso.repositories;

import com.example.tingeso.entities.JustificativoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JustificativoRepository extends JpaRepository<JustificativoEntity,Integer>{

    @Query(value = "select * from justificativo as e where e.rut = :rut and e.fecha =:fecha limit 1",
            nativeQuery = true)
    JustificativoEntity buscarJustificativo(@Param("rut") String rut, @Param("fecha") String fecha);
}
