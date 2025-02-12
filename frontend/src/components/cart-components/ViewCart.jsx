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

    const handleUpdateQuantity = async (productId, quantity) => {
        if (quantity < 1) {
            alert("Quantity cannot be less than 1.");
            return;
        }
    
        try {
            await api.patch(`/cart/update`, null, {
                params: {
                    buyerId: user.userId,
                    productId,
                    quantity,
                },
            });
    
            setCart((prev) => ({
                ...prev,
                items: prev.items.map((item) =>
                    item.productId === productId
                        ? { ...item, quantity, subtotal: item.price * quantity }
                        : item
                ),
                total: prev.items.reduce(
                    (acc, item) =>
                        item.productId === productId
                            ? acc + item.price * quantity
                            : acc + item.subtotal,
                    0
                ),
            }));
        } catch (error) {
            console.error("Error updating cart quantity", error);
        }
    };

    useEffect(() => {
        fetchCart();
    }, [user])

    return(
        <>
            {cart.items.length > 0 ? (
                <CartItemList cart={cart} onDelete={handleDelete} onUpdate={handleUpdateQuantity}/>
            ) : (
                <p>Your cart is empty.</p>
            )}
        </>
    )
}

export default ViewCart;