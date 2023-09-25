package com.example.dynamictablebackend.services;

import com.example.dynamictablebackend.kafka.Publisher;
import com.example.dynamictablebackend.requests.SaveTableDataRequest;
import com.example.dynamictablebackend.kafka.KafkaMessageDTO;
import com.example.dynamictablebackend.responses.TableInfo;
import com.example.dynamictablebackend.responses.TableResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private KafkaTemplate<String, KafkaMessageDTO> kafkaTemplate;
    private final Publisher publisher;

    public TableService(Publisher publisher) {
        this.publisher = publisher;
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

//            KafkaMessage kafkaMessage = new KafkaMessage("Table created successfully", "success");
//            publisher.publish(kafkaMessage);

            return tableInfo;


        } catch (Exception e) {
            System.out.println("TableService createTable error: " + e);
            return null;
        }
    }

    public Connection createConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/dynamictable";
        String user = "postgres";
        String password = "1234";
        return DriverManager.getConnection(url, user, password);
    }


    public List<TableInfo> getAllTables() throws SQLException {
        Connection connection = createConnection();


        List<TableInfo> tableInfoList = new ArrayList<>();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tablesResultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
        while (tablesResultSet.next()) {
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
                    System.out.println(split[0].trim());
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

        KafkaMessageDTO kafkaMessageDTO = new KafkaMessageDTO("Table data saved successfully", "success");
        kafkaTemplate.send("test", kafkaMessageDTO);
    }

    public TableResponse getTableByName(String tableName) {
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

    public TableInfo importFromExcel(MultipartFile file) throws IOException, SQLException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        //Name of file
        String tableName = file.getOriginalFilename().split("\\.")[0];
        //Columns array
        List<String> columns = new ArrayList<>();
        //Read first row of excel file
        Row firstRow = sheet.getRow(0);
        for (int i = 0; i < firstRow.getPhysicalNumberOfCells(); i++) {
            Cell cell = firstRow.getCell(i);
            String cellValue = dataFormatter.formatCellValue(cell);
            columns.add(cellValue);
        }

        List<List<String>> tableData = new ArrayList<>();
        //Starting from index 1 because first row is column names
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            List<String> rowData = new ArrayList<>();
            for (int j = 0; j < columns.size(); j++) {
                String cellValue = dataFormatter.formatCellValue(row.getCell(j));
                if (cellValue != null) {
                    rowData.add(cellValue);
                } else {
                    rowData.add("");
                }


            }
            tableData.add(rowData);
        }
        System.out.println("TableService importFromExcel tableData: " + tableData);

        //Create table in database with tableName, columns and tableData
        Connection connection = createConnection();
        Statement statement = connection.createStatement();
        String create_sql = "CREATE TABLE " + tableName + " (id SERIAL PRIMARY KEY, " + String.join(" VARCHAR(255), ", columns) + " VARCHAR(255))";
        statement.execute(create_sql);
        //Insert data into table
        for (List<String> row : tableData) {
            HashMap<String, String> columnValueMap = new HashMap<>();
            for (String columnValue : row) {
                columnValueMap.put(columns.get(row.indexOf(columnValue)), columnValue);
            }

            if (!columnValueMap.isEmpty()) {
                String columnNames = String.join(", ", columnValueMap.keySet());
                String columnValues = columnValueMap.values().stream()
                        .map(value -> "'" + value + "'")
                        .collect(Collectors.joining(", "));

                String insert_sql = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + columnValues + ")";
                System.out.println("TableService saveTableData sql: " + insert_sql);
                statement.execute(insert_sql);
            }
        }

        return new TableInfo(tableName, columns);
    }


    public ByteArrayInputStream exportToExcel(String tableName) {
        try {
            Connection connection = createConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(tableName);


            //Write column names
            Row firstRow = sheet.createRow(0);
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                String columnName = resultSet.getMetaData().getColumnName(i);
                if (!columnName.equalsIgnoreCase("id")) {
                    System.out.println("TableService exportToExcel columnName: " + columnName);
                    Cell cell = firstRow.createCell(i - 2);
                    cell.setCellValue(columnName);
                }
            }

            //Write table data
            int rowIndex = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowIndex);
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    if (!columnName.equalsIgnoreCase("id")) {
                        Object columnValue = resultSet.getObject(i);
                        Cell cell = row.createCell(i - 2);
                        if (columnValue != null) {
                            cell.setCellValue(columnValue.toString());
                        } else {
                            cell.setCellValue(""); // Or handle null case as needed
                        }
                    }
                }
                rowIndex++;
            }

            //Write workbook to file and return it
            workbook.write(outputStream);
            //Save workbook to file
            FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/" + tableName + ".xlsx");
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            workbook.close();

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}



