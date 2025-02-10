import { useState, useEffect } from "react";
import { useUser } from "../UserContext";
import api from "../../service/api";
import Order from "./Order";
import { Typography } from "@mui/material";

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
                <>
                <Typography variant="h4" gutterBottom align="center">
                    Orders:
                </Typography>
                {orders.map((order) => (
                    <Order key={order.orderId} order={order} /> 
                ))}
                </>
            ) : (
                <Typography variant="h6" align="center">
                    You have no orders...
                </Typography>
            )}
        </>
    )

}

export default ViewOrders;