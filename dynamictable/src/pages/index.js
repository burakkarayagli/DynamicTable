import Head from "next/head";
import Image from "next/image";
import { Inter } from "next/font/google";
import styles from "@/styles/Home.module.css";
import Button from "@mui/material/Button";
import { Add, BorderAll } from "@mui/icons-material";
import { useEffect, useState } from "react";
import FormulaModal from "./components/FormulaModal";
import BfmTable from "./components/BfmTable";
import axios from "axios";
import { Box, Tab, Tabs, Typography } from "@mui/material";
import AddIcon from '@mui/icons-material/Add';

const baseURL = "http://localhost:8080";
const inter = Inter({ subsets: ["latin"] });

export default function Home() {
    const [isFormulaModalOpen, setIsFormulaModalOpen] = useState(false);
    const [data, setData] = useState({});
    const [tables, setTables] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    // const [data, setData] = useState({});

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
                // Add the new bfm to the list of bfms
                console.log(response.data)
                setTables((prevTables) => [...prevTables, response.data]);
            })
            .catch((error) => {
                console.error("Failed to save row data:", error);
                // Handle the error here, display a message, or take appropriate action
            });

        setData(bfm_data);
        setIsFormulaModalOpen(false);
    }

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

    useEffect(() => {
        axios.get(baseURL + "/api/getAllTableInfo").then((response) => {
            console.log(response.data);
            setTables(response.data);
            setIsLoading(false);
        });
    }, []);

    return (
        <div>
            {/* <div>
                <Button
                    className="mt-2 ml-2 text-white bg-blue-700 hover:bg-blue-800 focus:outline-none"
                    id="addButton"
                    onClick={openFormulaModal}
                >
                    ADD
                </Button>
            </div> */}
            {isFormulaModalOpen && (
                <FormulaModal
                    closeModal={closeFormulaModal}
                    createButton={handleCreateFormula}
                />
            )}
            {isLoading ? (
                <div>Loading...</div>
            ) : (
                <div className="bfm-table-container">
                    <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
                        <Tabs
                            value={tabIndex}
                            onChange={handleTabChange}
                            className="tabs-container"
                        >
                            {tables.map((table, index) => (
                                <Tab label={table.tableName} />
                            ))}
                            <Tab icon={<AddIcon/>} onClick={openFormulaModal} className="addButton" id="addButton"/>
                        </Tabs>
                    </Box>
                    {tables.map((table, index) => (
                        console.log(table.tableName),
                        <BfmTable
                            index={index}
                            value={tabIndex}
                            className="bfm-table"
                            bfm_name={table.tableName}
                            input_columns={table.columnNames}
                            output_columns={table.columnNames}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}
