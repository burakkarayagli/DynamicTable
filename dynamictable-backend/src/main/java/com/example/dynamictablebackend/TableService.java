package com.example.dynamictablebackend;

import com.example.dynamictablebackend.models.TableEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TableService {
    private final TableRepository tableRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public TableEntity saveTable(String data) {
        //data format:
        //data: {"bfm_name":"tablo3","input_columns":["a"],"output_columns":["b"]}

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            String tableName = jsonNode.get("name").asText();
            JsonNode inputColumns = jsonNode.get("input_columns");
            JsonNode outputColumns = jsonNode.get("output_columns");
            //Convert JsonNode to String array
            String[] inputColumnsArray = objectMapper.convertValue(inputColumns, String[].class);
            String[] outputColumnsArray = objectMapper.convertValue(outputColumns, String[].class);
            //Concenate input and output columns


            //Create TableEntity object
            TableEntity tableEntity = new TableEntity(tableName, List.of(inputColumnsArray), List.of(outputColumnsArray));
            //Save tableEntity to database
            return tableRepository.save(tableEntity);


        }
        catch (Exception e) {
            System.out.println("TableService saveTable error: " + e.getMessage());
            return null;
        }




    }

    public TableInfo createTable(String data) {
        //data format:
        //data: {"bfm_name":"tablo3","input_columns":["a"],"output_columns":["b"]}

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            String tableName = jsonNode.get("name").asText();
            JsonNode inputColumns = jsonNode.get("input_columns");
            JsonNode outputColumns = jsonNode.get("output_columns");
            //Convert JsonNode to String array
            String[] inputColumnsArray = objectMapper.convertValue(inputColumns, String[].class);
            String[] outputColumnsArray = objectMapper.convertValue(outputColumns, String[].class);
            //Concenate input and output columns
            String[] columns = new String[inputColumnsArray.length + outputColumnsArray.length];

            for (int i = 0; i < inputColumnsArray.length; i++) {
                columns[i] = "IN_" + inputColumnsArray[i];
            }
            for (int i = 0; i < outputColumnsArray.length; i++) {
                columns[i + inputColumnsArray.length] = "OUT_" + outputColumnsArray[i];
            }

            //Create table in database
            String sql = "CREATE TABLE " + tableName + " (id SERIAL PRIMARY KEY, " + String.join(" VARCHAR(255), ", columns) + " VARCHAR(255))";
            jdbcTemplate.execute(sql);

            //Create TableInfo object
            return new TableInfo(tableName, List.of(columns));


        }
        catch (Exception e) {
            System.out.println("TableService createTable error: " + e.getMessage());
            return null;
        }
    }

    //Get all tables from database
    public Iterable<TableEntity> getAllTables() {
        return tableRepository.findAll();
    }

    public List<TableInfo> getAllTableInfo() throws SQLException {
        List<TableInfo> tableInfoList = new ArrayList<>();

        DatabaseMetaData databaseMetaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
        ResultSet tables = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});

        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            List<String> columnNames = new ArrayList<>();
            ResultSet columns = databaseMetaData.getColumns(null, null, tableName, null);
            while (columns.next()) {
                columnNames.add(columns.getString("COLUMN_NAME"));
            }
            tableInfoList.add(new TableInfo(tableName, columnNames));
        }

        return tableInfoList;
    }

    public boolean deleteByName(String tableName) {
        Optional<TableEntity> optionalTable = tableRepository.findByName(tableName);
        if (optionalTable.isPresent()) {
            tableRepository.delete(optionalTable.get());
            return true; // Deletion successful
        } else {
            return false; // Table not found
        }
    }

    public boolean deleteById(Long id) {
        Optional<TableEntity> optionalTable = tableRepository.findById(id);
        if (optionalTable.isPresent()) {
            tableRepository.delete(optionalTable.get());
            return true; // Deletion successful
        } else {
            return false; // Table not found
        }
    }
}

