import React, { useState } from "react";
import { Divider } from "@mui/material";
import ColumnCell from "./ColumnCell";
import EmptyAddRow from "./EmptyAddRow";
import DataRow from "./DataRow";

function BfmTable(props) {
    const { bfm_name, input_columns, output_columns } = props;

    const columns = [...input_columns, ...output_columns];

    const handleRowDataChange = (rowData) => {
        // Assuming that each row has a unique identifier (e.g., ID) that you can use to identify rows in a more sophisticated app
        // For this example, I'm using the index of the row in the `rows` state array as a makeshift ID
        setRows((prevRows) =>
            prevRows.map((prevRow, index) => {
                if (index === prevRows.length - 1) {
                    // If it's the last row (the row with the new changes), return the updated row data
                    return (
                        <DataRow
                            key={index}
                            cellCount={
                                input_columns.length + output_columns.length
                            }
                            onSaveRowData={handleRowDataChange}
                        />
                    );
                } else {
                    // If it's not the last row, return the previous row
                    return prevRow;
                }
            })
        );
        // You can also do any additional logic here to save the row data to a database or perform other actions.
        // For now, I'm just printing the updated data to the console as an example.
        console.log("Updated Row Data:", rowData);
    };

    const [rows, setRows] = useState([
        <DataRow
            key={0}
            cellCount={input_columns.length + output_columns.length}
            onSaveRowData={handleRowDataChange}
        />,
    ]);

    

    function addRow() {
        console.log("add row");
        setRows((prevRows) => [
            ...prevRows,
            <DataRow
                key={prevRows.length}
                cellCount={input_columns.length + output_columns.length}
            />,
        ]);

        console.log(rows);
    }
    
    function getRowData() {
        var rowData = [];
        var rowContainer = document.getElementById("row-container");
        var rows = rowContainer.getElementsByClassName("data-row");
        for (var i = 0; i < rows.length; i++) {
            var row = rows[i];
            var cells = row.getElementsByClassName("data-cell-input");
            var cellData = [];
            for (var j = 0; j < cells.length; j++) {
                var cell = cells[j];
                cellData.push(columns[j] + ": " + cell.value)
            }
            rowData.push(cellData);
        }
        
        console.log(rowData);
        return rowData;
    }



    return (
        <div>
            <h1 className="bfm-header">{bfm_name}</h1>
            <Divider variant="middle" style={{ margin: 10 }} />
            <div className="row-container" id="row-container">
                <div className="grid-row">
                    {input_columns.map((column, index) => (
                        <ColumnCell
                            key={index}
                            column={column}
                            isInput={true}
                        />
                    ))}
                    {output_columns.map((column, index) => (
                        <ColumnCell key={index} column={column} />
                    ))}
                </div>
                {rows}
                <div className="grid-row">
                    <EmptyAddRow addRow={addRow} />
                </div>
            </div>
            <button onClick={getRowData}>Get Row Data</button>
        </div>
    );
}

export default BfmTable;
