import { useEffect, useState } from "react";
import api from "../../service/api";
import { useParams } from "react-router-dom";


const ProductPage = () =>{
    const { id } = useParams();
    const [product, setProduct] = useState(null);

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
    
    return(
        <>
            <h1>{product.name}</h1>
            <h3>{product.description}</h3>
            <h3>{product.price}</h3>
        </>
    )
}

export default ProductPage;