import api from '../../service/api';
import { useUser } from '../UserContext';
import { useState } from 'react';
import { Button, TextField, InputAdornment, Select, MenuItem, FormControl, InputLabel, Paper, Box, Typography } from "@mui/material";

const CreateProduct = ({ handleProductCreation }) => {

    const { user } = useUser();
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [price, setPrice] = useState("");
    const [stock, setStock] = useState("");
    const [category, setCategory] = useState("");
    const [pic, setPic] = useState(null);

    const handleFileChange = (e) => {
        setPic(e.target.files[0]); // Store the uploaded image
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Use FormData to handle file uploads
        const formData = new FormData();
        formData.append("name", name);
        formData.append("description", description);
        formData.append("price", price);
        formData.append("stock", stock);
        formData.append("category", category);
        if (pic) {
            formData.append("image", pic); // Attach the image file
        }

        try {
            const response = await api.post(`/sellers/${user.userId}/products`, formData, {
                headers: {
                    "Content-Type": "multipart/form-data", // Required for file uploads
                },
            });
            handleProductCreation();
            setName("");
            setDescription("");
            setPrice("");
            setStock("");
            setCategory("");
            setPic(null);
        } catch (error) {
            console.error("Error creating product:", error);
        }
    };

    return (
        <>
            <Box
                display="flex"
                flexDirection="column"
                justifyContent="center"
                alignItems="center"
            >
                <Typography variant="h4" gutterBottom align="center">
                    Add a New Product
                </Typography>
                <Paper elevation={3} sx={{ padding: 4, width: "100%", maxWidth: 400 }}>
                    <form onSubmit={handleSubmit}>
                        <TextField fullWidth label="Product Name" id="standard-basic" margin="normal" required value={name} onChange={(e) => setName(e.target.value)}></TextField><br />
                        <TextField fullWidth label="Product Description" id="standard-basic" margin="normal" required value={description} onChange={(e) => setDescription(e.target.value)}></TextField><br />
                        <FormControl fullWidth margin="normal" required>
                            <InputLabel>Category</InputLabel>
                            <Select
                                value={category}
                                onChange={(e) => setCategory(e.target.value)}
                                label="Category"
                            >
                                <MenuItem value="VIDEOGAMES">Video Games</MenuItem>
                                <MenuItem value="DVD_BLURAY">DVD/Blu-ray</MenuItem>
                                <MenuItem value="AUDIOBOOKS">Audiobooks</MenuItem>
                            </Select>
                        </FormControl>
                        <TextField
                            fullWidth
                            label="Price"
                            id="standard-basic"
                            margin="normal"
                            required
                            value={price}
                            onChange={(e) => setPrice(e.target.value)}
                            type="number"
                            slotProps={{
                                input: {
                                    startAdornment: <InputAdornment position="start">$</InputAdornment>,
                                },
                            }}
                        >
                        </TextField><br />
                        <TextField
                            fullWidth label="Stock"
                            id="standard-basic" margin="normal"
                            required value={stock}
                            onChange={(e) => setStock(e.target.value)}
                            type="number">
                        </TextField><br />

                        <TextField
                            fullWidth
                            type="file"
                            inputProps={{ accept: "image/*" }}
                            onChange={handleFileChange}
                            margin="normal"
                        /><br />

                        <Button fullWidth variant="contained" type="submit">List New Product</Button>
                    </form>
                </Paper>
            </Box>
        </>
    )

}

export default CreateProduct;