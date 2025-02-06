import React, { useEffect, useState } from "react";
import { getCartDetails, updateCartItem, removeCartItem } from "./api";

const Cart = ({ buyerId }) => {
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchCart();
    }, []);

    const fetchCart = async () => {
        try {
            setLoading(true);
            const data = await getCartDetails(buyerId);
            setCart(data);
        } catch (err) {
            setError(err);
        } finally {
            setLoading(false);
        }
    };

    const handleUpdateQuantity = async (productId, quantity) => {
        if (quantity < 0) return;

        try {
            await updateCartItem(buyerId, productId, quantity);
            fetchCart(); // Refresh cart after update
        } catch (err) {
            alert(err);
        }
    };

    const handleRemoveItem = (productId) => handleUpdateQuantity(productId, 0);

    if (loading) return <p>Loading cart...</p>;
    if (error) return <p className="text-red-500">{error}</p>;
    if (!cart || cart.items.length === 0) return <p>Your cart is empty.</p>;

    return (
        <div className="p-6 bg-white shadow-lg rounded-lg">
            <h2 className="text-2xl font-bold mb-4">Your Cart</h2>
            <table className="w-full border-collapse border border-gray-200">
                <thead>
                    <tr className="bg-gray-100">
                        <th className="p-2 border">Product</th>
                        <th className="p-2 border">Price</th>
                        <th className="p-2 border">Quantity</th>
                        <th className="p-2 border">Subtotal</th>
                        <th className="p-2 border">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {cart.items.map((item) => (
                        <tr key={item.productId} className="text-center">
                            <td className="p-2 border">{item.productName}</td>
                            <td className="p-2 border">${item.price.toFixed(2)}</td>
                            <td className="p-2 border">
                                <input
                                    type="number"
                                    min="1"
                                    value={item.quantity}
                                    onChange={(e) => handleUpdateQuantity(item.productId, parseInt(e.target.value))}
                                    className="w-12 text-center border rounded"
                                />
                            </td>
                            <td className="p-2 border">${item.subtotal.toFixed(2)}</td>
                            <td className="p-2 border">
                                <button
                                    onClick={() => handleRemoveItem(item.productId)}
                                    className="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600"
                                >
                                    Remove
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <div className="mt-4 flex justify-between items-center">
                <h3 className="text-xl font-bold">Total: ${cart.total.toFixed(2)}</h3>
                <button className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600">
                    Checkout
                </button>
            </div>
        </div>
    );
};

export default Cart;