import api from '../../service/api';
import { useUser } from '../UserContext';
import { useState } from 'react';
import { Button, TextField } from "@mui/material";

const CreateProduct = ({ handleProductCreation }) => {

    const { user } = useUser();
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [price, setPrice] = useState("");
    const [stock, setStock] = useState("");

    const handleSubmit = async(e) => {
        e.preventDefault();

        const request = { name, description, price, stock}

        try {
            const response = await api.post(`/sellers/${user.userId}/products`, request)
            handleProductCreation();
            setName("");
            setDescription("");
            setPrice("");
            setStock("");
        } catch (error) {
            console.error("Error creating product:", error);
        }
    }

    return (
        <>
        <form onSubmit={handleSubmit}>
            <TextField label="Product Name" id="standard-basic" margin="normal" required value={name} onChange={(e) => setName(e.target.value)}></TextField><br/>
            <TextField label="Product Description" id="standard-basic" margin="normal" required value={description} onChange={(e) => setDescription(e.target.value)}></TextField><br/>
            <TextField label="Price" id="standard-basic" margin="normal" required value={price} onChange={(e) => setPrice(e.target.value)} type="number"></TextField><br/>
            <TextField label="Stock" id="standard-basic" margin="normal" required value={stock} onChange={(e) => setStock(e.target.value)} type="number"></TextField><br/>
            <Button variant="contained" type="submit">List New Product</Button>
        </form>
        </>
    )

}

export default CreateProduct;