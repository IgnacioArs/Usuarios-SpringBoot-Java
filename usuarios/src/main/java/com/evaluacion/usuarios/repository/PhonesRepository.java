package com.evaluacion.usuarios.repository;

import com.evaluacion.usuarios.model.Phone;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

@Repository
public interface PhonesRepository extends JpaRepository<Phone, Long>{
}