import Head from "next/head";
import Image from "next/image";
import { Inter } from "next/font/google";
import styles from "@/styles/Home.module.css";
import Button from "@mui/material/Button";
import { Add, BorderAll } from "@mui/icons-material";

const inter = Inter({ subsets: ["latin"] });

function randomHexColor() {
    return "#" + Math.floor(Math.random() * 16777215).toString(16);
}

export default function Home() {
    return (
        <>
            <Button
                endIcon={<Add style={
                    {
                        //make bolder
                        fontWeight: "bold",
                        //make bigger
                        fontSize: "200%",
                        //make color random
                        color: randomHexColor(),
                    }
                }/>}
                style={{
                    border: "1px solid",
                    borderRadius: "5px",
                    borderColor: "#5390d9",
                    backgroundColor: "#80ffdb",
                    marginTop: "20px",
                    marginLeft: "20px",
                    fontSize: "150%",
                }}
                size="large"
            >
                Add
            </Button>
        </>
    );
}
