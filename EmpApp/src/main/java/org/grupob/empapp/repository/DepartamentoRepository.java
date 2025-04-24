package org.grupob.empapp.repository;

import org.grupob.empapp.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, UUID> {
//    List<Departamento> findByDname(String dname);
//
//    List<Departamento> findByLocBetween(String locAfter, String locBefore);
//
//    @Query("SELECT d FROM Departamento d WHERE d.loc BETWEEN :locAfter AND :locBefore")
//    List<Departamento> devuelveLocaidadEntrePrametros(String locAfter, String locBefore);


    Optional<Departamento> findDepartamentoByNombre(String nombre);

}
