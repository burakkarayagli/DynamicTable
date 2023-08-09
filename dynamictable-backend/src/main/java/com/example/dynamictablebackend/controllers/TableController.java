package com.example.dynamictablebackend.controllers;

import com.example.dynamictablebackend.TableInfo;
import com.example.dynamictablebackend.TableService;
import com.example.dynamictablebackend.models.TableEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class TableController {

    @Autowired
    private TableService tableService;

    @PostMapping(path = "/saveTable")
    public ResponseEntity<TableEntity> saveTable(@RequestBody String data) {
        TableEntity tableEntity = tableService.saveTable(data);

        if (tableEntity != null) {
            return ResponseEntity.ok(tableEntity);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(path = "/createTable")
    public ResponseEntity<String> createTable(@RequestBody String data) {
        TableInfo tableInfo = tableService.createTable(data);

        if (tableInfo != null) {
            return ResponseEntity.ok("Table created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating table");
        }
    }

    @GetMapping(path = "/getAllTables")
    public ResponseEntity<Iterable<TableEntity>> getAllTables() throws SQLException {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @GetMapping(path = "/getAllTableInfo")
    public ResponseEntity<List<TableInfo>> getAllTableInfo() throws SQLException {

        try {
            return ResponseEntity.ok(tableService.getAllTableInfo());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    //Delete table by id
    @DeleteMapping(path = "/deleteTableById/{id}")
    public ResponseEntity<String> deleteTableById(@PathVariable Long id) {
        try {
            boolean deleted = tableService.deleteById(id);

            if (deleted) {
                return ResponseEntity.ok("Table deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Table not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting table: " + e.getMessage());
        }
    }
}
