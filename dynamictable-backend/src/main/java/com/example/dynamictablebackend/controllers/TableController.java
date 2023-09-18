package com.example.dynamictablebackend.controllers;

import com.example.dynamictablebackend.responses.TableInfo;
import com.example.dynamictablebackend.services.TableService;
import com.example.dynamictablebackend.requests.SaveTableDataRequest;
import com.example.dynamictablebackend.responses.TableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.dynamictablebackend.constants.EndpointConstants;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(path = EndpointConstants.API)
public class TableController {

    @Autowired
    private TableService tableService;

    @PostMapping(path = EndpointConstants.SAVE_TABLE)
    public void saveTable(@RequestBody SaveTableDataRequest request) {

        //System.out.println("saveTable request: " + request.getTableName() + " " + request.getTableData());
        tableService.saveTableData(request);
    }

    @PostMapping(path = EndpointConstants.CREATE_TABLE)
    public ResponseEntity<TableInfo> createTable(@RequestBody String data) {
        TableInfo tableInfo = tableService.createTable(data);

        if (tableInfo != null) {
            return ResponseEntity.ok(tableInfo);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = EndpointConstants.GET_ALL_TABLES)
    public ResponseEntity<List<TableInfo>> getAllTableInfo() throws SQLException {

        try {
            return ResponseEntity.ok(tableService.getAllTables());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @GetMapping(path = EndpointConstants.GET_TABLE_BY_NAME + "/{name}")
    public ResponseEntity<TableResponse> getTableByName(@PathVariable String name) {
        try {
            TableResponse tableData = tableService.getTableByName(name);

            if (tableData != null) {
                return ResponseEntity.ok(tableData);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping(path = EndpointConstants.DELETE_TABLE_BY_NAME + "/{name}")
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

    @PostMapping(path = EndpointConstants.IMPORT_FROM_EXCEL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TableInfo> importFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(tableService.importFromExcel(file));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = EndpointConstants.EXPORT_TO_EXCEL + "/{name}")
    public ResponseEntity<Resource> exportToExcel(@PathVariable String name) {
        System.out.println("exportToExcel request: " + name);
        try {
            ByteArrayInputStream byteArrayInputStream = tableService.exportToExcel(name);
            InputStreamResource file = new InputStreamResource(byteArrayInputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name + ".xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(file);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
