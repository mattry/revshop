import { useEffect, useState } from "react";
import api from "../../service/api";
import { useParams } from "react-router-dom";
import { useUser } from "../UserContext";
import UpdateProductForm from "./UpdateProductForm";
import { Button, TextField } from "@mui/material";
import { addToCart } from "../../service/api";


const ProductPage = () =>{
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [isEditing, setIsEditing] = useState(false);
    const { user } = useUser();
    const [quantity, setQuantity] = useState(1);

    const fetchProductDetails = async () => {
        try{
            const response = await api.get(`/product/${id}`);
            setProduct(response.data);
        } catch (error) {
            console.error("Error fetching product details", error);
        }
    }
    
    const submitHandler = async () => {
        const request = {
            buyerId: user.userId,
            productId: id,
            quantity: quantity
        }

        try {
            const response = await addToCart(request);
            console.log(response.data);
        } catch (error) {
            console.error("Error adding product to cart:", error);
        }
    }

    useEffect(() => {
        fetchProductDetails();
    }, [id]);


    if (!product) {
        return <p>Loading...</p>;
    }

    const isUserSeller = user?.userId === product.sellerId;
    const isBuyer = user?.role === "BUYER";
    
    return(
        <>
            {!isEditing ? (
                <>
                    <h1>{product.name}</h1>
                    <h3>{product.description}</h3>
                    <h4>{product.category}</h4>
                    <h3>${product.price.toFixed(2)}</h3><br/>

                    {isUserSeller && (
                        <Button variant="outlined" onClick={() => setIsEditing(true)}>Edit Product</Button>
                    )}

                    {isBuyer && (
                        <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
                            <TextField
                                type="number"
                                label="Quantity"
                                value={quantity}
                                onChange={(e) => {
                                    const value = Math.max(1, parseInt(e.target.value) || 1);
                                    setQuantity(value);
                                }}
                                size="small"
                                style={{ width: "80px" }}
                            />
                        <Button variant="outlined" onClick={submitHandler}>
                            Add to Cart
                        </Button>
                    </div>
                    )}
                </>
            ) : (
                <UpdateProductForm 
                    product={product} 
                    onUpdateSuccess={(updatedProduct) => {
                        setProduct(updatedProduct);
                        setIsEditing(false);
                    }} 
                />
            )}
        </>
    )
}

export default ProductPage;