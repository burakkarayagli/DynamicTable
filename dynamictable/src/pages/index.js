import Head from "next/head";
import Image from "next/image";
import { Inter } from "next/font/google";
import styles from "@/styles/Home.module.css";
import Button from "@mui/material/Button";
import { Add, BorderAll } from "@mui/icons-material";
import { useState } from "react";
import FormulaModal from "./components/FormulaModal";
import BfmTable from "./components/BfmTable";

const inter = Inter({ subsets: ["latin"] });

export default function Home() {
    const [isFormulaModalOpen, setIsFormulaModalOpen] = useState(false);
    const [data, setData] = useState({
        bfm_name: "bfm1",
        input_columns: ["a", "b", "c"],
        output_columns: ["d", "e", "f"],
    });




    function openFormulaModal() {
        setIsFormulaModalOpen(true);
    }

    function closeFormulaModal(e) {
        console.log(e.target.id);
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
            bfm_name: bfmName,
            input_columns: columns[0],
            output_columns: columns[1],
        };

        

        setData(bfm_data);
        setIsFormulaModalOpen(false);
    }

    return (
        <>
            <div>
                <Button
                    className="mt-2 ml-2 text-white bg-blue-700 hover:bg-blue-800 focus:outline-none"
                    id="addButton"
                    onClick={openFormulaModal}
                >
                    ADD
                </Button>
            </div>
            {isFormulaModalOpen && (
                <FormulaModal
                    closeModal={closeFormulaModal}
                    createButton={handleCreateFormula}
                />
            )}
            <div className="bfm-table-container">
                {data.bfm_name && (
                    <BfmTable
                        className="bfm-table"
                        bfm_name={data.bfm_name}
                        input_columns={data.input_columns}
                        output_columns={data.output_columns}
                    />
                )}
            </div>
        </>
    );
}
