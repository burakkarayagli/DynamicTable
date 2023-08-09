package com.example.dynamictablebackend;
import com.example.dynamictablebackend.models.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TableRepository extends JpaRepository<TableEntity, Long> {

    //Delete table by name
    void deleteByName(String name);

    Optional<TableEntity> findByName(String tableName);

}

