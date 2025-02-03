import { useEffect, useState } from "react";
import api from "../../service/api";
import { useParams } from "react-router-dom";
import { useUser } from "../UserContext";
import UpdateProductForm from "./UpdateProductForm";
import { Button } from "@mui/material";


const ProductPage = () =>{
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [isEditing, setIsEditing] = useState(false);
    const { user } = useUser();

    const fetchProductDetails = async () => {
        try{
            const response = await api.get(`/product/${id}`);
            setProduct(response.data);
        } catch (error) {
            console.error("Error fetching product details", error);
        }
    }

    useEffect(() => {
        fetchProductDetails();
    }, [id]);


    if (!product) {
        return <p>Loading...</p>;
    }

    const isUserSeller = user?.userId === product.sellerId;
    
    return(
        <>
            {!isEditing ? (
                <>
                    <h1>{product.name}</h1>
                    <h3>{product.description}</h3>
                    <h3>${product.price.toFixed(2)}</h3><br/>

                    {isUserSeller && (
                        <Button variant="outlined" onClick={() => setIsEditing(true)}>Edit Product</Button>
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