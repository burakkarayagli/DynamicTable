import Head from "next/head";
import Image from "next/image";
import { Inter } from "next/font/google";
import styles from "@/styles/Home.module.css";
import Button from "@mui/material/Button";
import { Add, BorderAll } from "@mui/icons-material";
import { useState } from "react";
import FormulaModal from "./components/FormulaModal";
import { Backdrop } from "@mui/material";

const inter = Inter({ subsets: ["latin"] });

function randomHexColor() {
    return "#" + Math.floor(Math.random() * 16777215).toString(16);
}

export default function Home() {
    const [isFormulaModalOpen, setIsFormulaModalOpen] = useState(false);

    function openFormulaModal() {
        setIsFormulaModalOpen(true);
    }
    
    function closeFormulaModal(e) {
        console.log(e.target.id)
        if (e.target.id === "formula-modal" || e.target.parentNode.id === "close-button") {
            setIsFormulaModalOpen(false);
        }
        
    }

    return (
        <>
            <Button className= "mt-2 ml-2 text-white bg-blue-700 hover:bg-blue-800 focus:outline-none" id="addButton" onClick={openFormulaModal}>ADD</Button>
            {isFormulaModalOpen && <FormulaModal closeModal={closeFormulaModal}/>}
            {isFormulaModalOpen && <Backdrop />}
        </>
    );
}
