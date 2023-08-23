package com.example.dynamictablebackend.controllers;

import com.example.dynamictablebackend.TableInfo;
import com.example.dynamictablebackend.TableService;
import com.example.dynamictablebackend.models.TableEntity;
import com.example.dynamictablebackend.requests.SaveTableDataRequest;
import com.example.dynamictablebackend.responses.TableResponse;
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
    public void saveTable(@RequestBody SaveTableDataRequest request) {

        System.out.println("saveTable request: " + request.getTableName() + " " + request.getTableData());
        tableService.saveTableData(request);
    }

    @PostMapping(path = "/createTable")
    public ResponseEntity<TableInfo> createTable(@RequestBody String data) {
        TableInfo tableInfo = tableService.createTable(data);

        if (tableInfo != null) {
            return ResponseEntity.ok(tableInfo);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping(path = "/getAllTableInfo")
    public ResponseEntity<List<TableInfo>> getAllTableInfo() throws SQLException {

        try {
            return ResponseEntity.ok(tableService.getTables());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @GetMapping(path = "/test")
    public String testEndpoint() {
        return "Test is success";
    }

    @GetMapping(path = "/getTableByName/{name}")
    public ResponseEntity<TableResponse> getTableByName(@PathVariable String name) {
        try {
            TableResponse tableData = tableService.getRowData(name);

            if (tableData != null) {
                return ResponseEntity.ok(tableData);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping(path = "/deleteTableByName/{name}")
    public ResponseEntity<String> deleteTableByName(@PathVariable String name) {
        try {
            boolean deleted = tableService.deleteByName(name);

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
