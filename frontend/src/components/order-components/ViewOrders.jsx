import { useState, useEffect } from "react";
import { useUser } from "../UserContext";
import api from "../../service/api";
import Order from "./Order";

const ViewOrders = () => {

    const [orders, setOrders] = useState([]);

    const fetchOrders = async () => {
        try {
            const response = await api.get("/orders");
            setOrders(response.data);
            console.log(response.data);
        } catch (error) {
            console.error("Error fetching orders", error);
        }
    }

    useEffect(() => {
        fetchOrders();
    }, [])

    return(
        <>
            {orders.length > 0 ? (
                orders.map((order) => (
                    <Order key={order.orderId} order={order} /> 
                ))
            ) : (
                <p>You have no orders...</p>
            )}
        </>
    )

}

export default ViewOrders;