import { useState, useEffect } from "react";
import { useUser } from "../UserContext";
import api from "../../service/api";

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
                <p>You have orders...</p>
            ) : (
                <p>You have no orders...</p>
            )}
        </>
    )

}

export default ViewOrders;