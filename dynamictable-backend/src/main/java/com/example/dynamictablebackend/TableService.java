package com.example.dynamictablebackend;

import com.example.dynamictablebackend.models.TableEntity;
import com.example.dynamictablebackend.requests.SaveTableDataRequest;
import com.example.dynamictablebackend.responses.TableResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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


        } catch (Exception e) {
            System.out.println("TableService saveTable error: " + e.getMessage());
            return null;
        }


    }

    public TableInfo createTable(String data) {
        System.out.println("TableService createTable data: " + data);

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

            //convert columns to List<String>
            List<String> columnList = new ArrayList<>();
            Collections.addAll(columnList, columns);

            //Create TableInfo object
            TableInfo tableInfo = new TableInfo(tableName, columnList);
            return tableInfo;


        } catch (Exception e) {
            System.out.println("TableService createTable error: " + e);
            return null;
        }
    }

    public  Connection createConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/dynamictable";
        String user = "postgres";
        String password = "1234";
        return DriverManager.getConnection(url, user, password);
    }


    public List<TableInfo> getTables() throws SQLException {
        Connection connection = createConnection();


        List<TableInfo> tableInfoList = new ArrayList<>();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tablesResultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
        while(tablesResultSet.next()){
            List<String> columnNames = new ArrayList<>();
            String tableName = tablesResultSet.getString("TABLE_NAME");
            ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, null);

            while (columnsResultSet.next()) {
                String columnName = columnsResultSet.getString("COLUMN_NAME");
                if (!columnName.equalsIgnoreCase("id")) {
                    columnNames.add(columnName);
                }
            }

            tableInfoList.add(new TableInfo(tableName, columnNames));

            columnsResultSet.close();
        }
        tablesResultSet.close();

        return tableInfoList;
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

            //Removing first column (id)
            columnNames.remove(0);
            tableInfoList.add(new TableInfo(tableName, columnNames));
        }

        return tableInfoList;
    }

    public boolean deleteByName(String tableName) {
        //Delete table from database if exists then return true if not return false
        try {
            String sql = "DROP TABLE " + tableName;
            jdbcTemplate.execute(sql);
            return true; // Deletion successful
        } catch (Exception e) {
            System.out.println("TableService deleteByName error: " + e.getMessage());
            return false; // Table not found
        }

    }

    public void saveTableData(SaveTableDataRequest request) {
        String tableName = request.getTableName();
        List<List<String>> tableData = request.getTableData();

        String deleteSql = "DELETE FROM " + tableName + ";";
        jdbcTemplate.execute(deleteSql);

        for (List<String> row : tableData) {
            HashMap<String, String> columnValueMap = new HashMap<>();
            for (String columnValue : row) {
                String[] split = columnValue.split(":");
                if (split.length == 2) { // Ensure the split has two parts
                    columnValueMap.put(split[0].trim(), split[1].trim());
                }
            }

            if (!columnValueMap.isEmpty()) {
                String columnNames = String.join(", ", columnValueMap.keySet());
                String columnValues = columnValueMap.values().stream()
                        .map(value -> "'" + value + "'")
                        .collect(Collectors.joining(", "));

                String sql = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + columnValues + ")";
                System.out.println("TableService saveTableData sql: " + sql);
                jdbcTemplate.execute(sql);
            }
        }
    }


    public TableResponse getTableByName(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        //Get table data from database
        List<List<String>> tableData = jdbcTemplate.query(sql, new ResultSetExtractor<List<List<String>>>() {
            @Override
            public List<List<String>> extractData(ResultSet rs) throws SQLException {
                List<List<String>> tableData = new ArrayList<>();
                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        row.add(rs.getString(i));
                    }
                    tableData.add(row);
                }
                return tableData;
            }
        });
        //Get column names from database
        List<String> columnNames = jdbcTemplate.query(sql, new ResultSetExtractor<List<String>>() {
            @Override
            public List<String> extractData(ResultSet rs) throws SQLException {
                List<String> columnNames = new ArrayList<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    columnNames.add(rs.getMetaData().getColumnName(i));
                }
                return columnNames;
            }
        });
        //Create TableResponse object
        return new TableResponse(tableName, columnNames, tableData);
    }

    public TableResponse getRowData(String tableName) {
        List<List<String>> tableData = new ArrayList<>();

        try {
            Connection connection = createConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            while (resultSet.next()) {
                List<String> row = new ArrayList<>();

                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    if (!columnName.equalsIgnoreCase("id")) {
                        Object columnValue = resultSet.getObject(i);
                        row.add(columnValue.toString());
                    }
                }

                tableData.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return new TableResponse(tableName, new ArrayList<>(), tableData);
    }
}



