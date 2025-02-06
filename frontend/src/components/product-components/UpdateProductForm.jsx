import { useState } from "react";
import api from "../../service/api";
import { Button, TextField, InputAdornment } from "@mui/material";

const UpdateProductForm = ({ product, onUpdateSuccess }) => {

    const [updatedProduct, setUpdatedProduct] = useState({
        name: product.name,
        description: product.description,
        price: product.price,
    });

    const onChange = (e) => {
        const { name, value} = e.target;
        setUpdatedProduct((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await api.patch(`/sellers/products/${product.productId}`, updatedProduct);
            onUpdateSuccess(response.data);
        } catch (error) {
            console.error("Error updating product", error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <TextField label="Product Name" name="name" id="standard-basic" margin="normal" required value={updatedProduct.name} onChange={onChange}></TextField><br/>
            <TextField label="Product Description" name="description" id="standard-basic" margin="normal" required value={updatedProduct.description} onChange={onChange}></TextField><br/>
            <TextField 
                label="Price" 
                type="number" 
                name="price" 
                id="standard-basic" 
                margin="normal" 
                required 
                value={updatedProduct.price} 
                onChange={onChange}
                slotProps={{
                    input: {
                      startAdornment: <InputAdornment position="start">$</InputAdornment>,
                    },
                  }}
            >
            </TextField><br/>
            <Button variant="contained" type="submit">Save Changes</Button>
            <Button variant="outlined" onClick={() => onUpdateSuccess(product)}>Cancel</Button>
        </form>
    )

}

export default UpdateProductForm;