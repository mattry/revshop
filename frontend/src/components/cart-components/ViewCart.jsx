import { useUser } from "../UserContext";
import { useEffect, useState } from "react";
import api from "../../service/api";
import CartItemList from "./CartItemList";


const ViewCart = () => {

    const { user } = useUser();
    const [cart, setCart] = useState({ items: [], total: 0 });

    const fetchCart = async () => {
        try {
            const response = await api.get(`/cart/details/${user.userId}`);
            setCart(response.data);
        } catch (error) {
            console.error("Error fetching cart", error);
        }
    }

    const handleDelete = async (productId) => {
        if(window.confirm("Confirm delete")) {
            try {
                await api.delete(`/cart/remove/${productId}`);
                setCart((prev) => ({
                    ...prev,
                    items: prev.items.filter(item => item.productId !== productId),
                    total: prev.total - prev.items.find(item => item.productId === productId)?.subtotal || 0
                }));
            } catch (error) {
                console.error("Error removing from cart", error)
            }
        }
    };

    useEffect(() => {
        fetchCart();
    }, [user])

    return(
        <>
            {cart.items.length > 0 ? (
                <CartItemList cart={cart} onDelete={handleDelete} />
            ) : (
                <p>Your cart is empty.</p>
            )}
        </>
    )
}

export default ViewCart;