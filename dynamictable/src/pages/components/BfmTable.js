import { useReactTable, flexRender } from "@tanstack/react-table";
import React, { useState, useRef, useEffect, useMemo, useCallback} from 'react';
import "ag-grid-community/styles/ag-grid.css"; // Core grid CSS, always needed
import "ag-grid-community/styles/ag-theme-alpine.css"; // Optional theme CSS
import { createRoot } from "react-dom/client";
import { AgGridReact } from "ag-grid-react"; // the AG Grid React Component


function BfmTable(props) {
    function createEmptyData(input_columns, output_columns) {
        var emptyData = [];
        var data = {};
        for(var i = 0; i < input_columns.length; i++) {
            data[input_columns[i]] = "";
        }
        for(var i = 0; i < output_columns.length; i++) {
            data[output_columns[i]] = "";
        }
        emptyData.push(data);
        return emptyData;
    }


    var bfm_name = props.bfm_name;
    var input_columns = props.input_columns;
    var output_columns = props.output_columns;

    const gridRef = useRef(); // Optional - for accessing Grid's API
    
    const [rowData, setRowData] = useState(createEmptyData(input_columns, output_columns)); // Set rowData to Array of Objects, one Object per Row

    var columns = [];
    

    for(var i = 0; i < input_columns.length; i++) {
        columns.push({field: input_columns[i], filter: true});
    }

    for(var i = 0; i < output_columns.length; i++) {
        columns.push({field: output_columns[i], filter: true});
    }

    // Each Column Definition results in one Column.
    const [columnDefs, setColumnDefs] = useState(
        columns
    );

    


    

    

    // DefaultColDef sets props common to all Columns
    const defaultColDef = useMemo(() => ({
        sortable: true,
        editable: true,
    }));

    // Example of consuming Grid Event
    const cellClickedListener = useCallback((event) => {
        console.log("cellClicked", event);
        console.log(columnDefs);
        console.log(createEmptyData(input_columns, output_columns))
        //console.log(columns);
    }, []);

    // Example load data from server
    // useEffect(() => {
    //     fetch("https://www.ag-grid.com/example-assets/row-data.json")
    //         .then((result) => result.json())
    //         .then((rowData) => setRowData(rowData));
    // }, []);

    // Example using Grid's API
    const buttonListener = useCallback((e) => {
        gridRef.current.api.deselectAll();
    }, []);

    return (
        <div>
            {/* Example using Grid's API */}
            <button onClick={buttonListener}>Push Me</button>

            {/* On div wrapping Grid a) specify theme CSS Class Class and b) sets Grid size */}
            <div
                className="ag-theme-alpine"
                style={{ width: 500, height: 500 }}
            >
                <AgGridReact
                    ref={gridRef} // Ref for accessing Grid's API
                    rowData={rowData} // Row Data for Rows
                    columnDefs={columnDefs} // Column Defs for Columns
                    defaultColDef={defaultColDef} // Default Column Properties
                    animateRows={true} // Optional - set to 'true' to have rows animate when sorted
                    rowSelection="single" // Options - allows click selection of rows
                    onCellClicked={cellClickedListener} // Optional - registering for Grid Event
                />
            </div>
        </div>
    );
}

export default BfmTable;
