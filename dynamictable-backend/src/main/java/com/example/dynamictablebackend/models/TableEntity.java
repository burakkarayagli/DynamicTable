package com.example.dynamictablebackend.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@jakarta.persistence.Table(name = "tables")
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    //Columns array
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "input_columns", joinColumns = @JoinColumn(name = "table_id"))
    private List<String> input_columns;
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "output_columns", joinColumns = @JoinColumn(name = "table_id"))
    private List<String> output_columns;

    public TableEntity() {
    }

    public TableEntity(String name, List<String> input_columns, List<String> output_columns) {
        this.name = name;
        this.input_columns = input_columns;
        this.output_columns = output_columns;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getInput_columns() {
        return input_columns;
    }

    public void setInput_columns(List<String> input_columns) {
        this.input_columns = input_columns;
    }

    public List<String> getOutput_columns() {
        return output_columns;
    }

    public void setOutput_columns(List<String> output_columns) {
        this.output_columns = output_columns;
    }

    @Override
    public String toString() {
        return "TableEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", input_columns=" + input_columns +
                ", output_columns=" + output_columns +
                '}';
    }
}
