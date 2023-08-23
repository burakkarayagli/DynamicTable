import { Inter } from "next/font/google";
import { useEffect, useState } from "react";
import FormulaModal from "./components/FormulaModal";
import Table from "./components/Table";
import axios from "axios";
import { Box, Tab, Tabs} from "@mui/material";
import AddIcon from '@mui/icons-material/Add';

const baseURL = "http://localhost:8080";
const notificationTime = 3000;
const inter = Inter({ subsets: ["latin"] });

export default function Home() {

    useEffect(() => {
        getAllTables();
    },[]);

    //Boolean to check if the formula modal is open
    const [isFormulaModalOpen, setIsFormulaModalOpen] = useState(false);
    //Array of tables
    const [tables, setTables] = useState([]);
    //Boolean to check if the page is loading
    const [isLoading, setIsLoading] = useState(true);

    const [notifications, setNotifications] = useState({
        message: "",
        type: "",
    }); // [ {message: "hello", type: "success"}, {message: "hello", type: "error"}

    function showNotification(message, type) {
        setNotifications({ message, type });
        setTimeout(() => {
            setNotifications({ message: "", type: "" });
        }, notificationTime);
    }

    function hideNotification() {
        setNotifications({ message: "", type: "" });
    }
    



    function openFormulaModal() {
        setIsFormulaModalOpen(true);
    }

    function closeFormulaModal(e) {
        if (
            e.target.id === "formula-modal" ||
            e.target.parentNode.id === "close-button" ||
            e.target.id === "cancel-button"
        ) {
            setIsFormulaModalOpen(false);
        }
    }

    function handleCreateFormula(e) {
        function readColumns() {
            const inputColumns = document.getElementById("input-columns");
            const outputColumns = document.getElementById("output-columns");
            const inputColumnsArray = [];
            const outputColumnsArray = [];

            for (let i = 0; i < inputColumns.children.length - 1; i++) {
                inputColumnsArray.push(
                    inputColumns.children[i].children[0].value
                );
            }

            for (let i = 0; i < outputColumns.children.length - 1; i++) {
                outputColumnsArray.push(
                    outputColumns.children[i].children[0].value
                );
            }

            return [inputColumnsArray, outputColumnsArray];
        }

        const columns = readColumns();
        const bfmName = document.getElementById("bfm-name").value;
        const bfm_data = {
            name: bfmName,
            input_columns: columns[0],
            output_columns: columns[1],
        };

        axios
            .post(baseURL + "/api/createTable", bfm_data)
            .then((response) => {
                console.log("This is the response: ", response.data);
                
                setTables((prevTables) => [...prevTables, response.data]);
            })
            .catch((error) => {
                console.error("Failed to save row data:", error);
                // Handle the error here, display a message, or take appropriate action
            });

        setIsFormulaModalOpen(false);
    }

    const handleTableDelete = (deletedTableName) => {
        setTables((prevTables) =>
            prevTables.filter((table) => table.tableName !== deletedTableName)
        );
    };

    //Tabs
    const [tabIndex, setTabIndex] = useState(0);
    function handleTabChange(e, newValue) {
        if (tables.length === 0) {
            // If there are no tables, directly open the formula modal
            openFormulaModal();
        } else if (newValue === tables.length) {
            // If the "ADD" tab is clicked and there are tables, open formula modal
            openFormulaModal();
        } else {
            // Switch to the selected tab
            setTabIndex(newValue);
        }
    }

    function getAllTables() {
        axios.get(baseURL + "/api/getAllTableInfo").then((response) => {
            setTables(response.data);
            console.log("useeffect data", response.data);
            setIsLoading(false);
        });
    }


    

    return (
        <div>
            {isFormulaModalOpen && (
                <FormulaModal
                    closeModal={closeFormulaModal}
                    createButton={handleCreateFormula}
                />
            )}
            {isLoading ? (
                <div>Loading...</div>
            ) : (
                <div className="table-container">
                    <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
                        <Tabs
                            value={tabIndex}
                            onChange={handleTabChange}
                            className="tabs-container"
                        >
                            {tables.map((table, index) => (
                                <Tab key={table.tableName} label={table.tableName} />
                            ))}
                            <Tab icon={<AddIcon/>} onClick={openFormulaModal} className="addButton" id="addButton"/>
                        </Tabs>
                    </Box>
                    {tables.map((table, index) => (
                        // console.log(table.tableName),
                        <Table
                            key = {table.tableName}
                            index={index}
                            value={tabIndex}
                            className="table"
                            id = {table.tableName}
                            name={table.tableName}
                            columns={table.columnNames}
                            deleteTableCallback={handleTableDelete}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}
