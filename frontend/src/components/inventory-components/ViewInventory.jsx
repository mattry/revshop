import api from "../../service/api";
import { useUser } from "../UserContext";
import { useEffect, useState } from "react";
import CreateProduct from "./CreateProduct";
import InventoryTable from "./InventoryTable";

const ViewInventory = () => {

    const { user } = useUser();
    const [inventory, setInventory] = useState([]);

    const fetchInventory = async () => {
        try {
            const response = await api.get(`/inventory/${user.userId}`);
            setInventory(response.data);
        } catch (error) {
            console.error("Error fetching inventory", error);
        }
    }

    useEffect(() => {
        fetchInventory();
    }, [user])

    const handleProductCreation = async () => {
        await fetchInventory();
    };

    const handleDelete = async (productId) => {

        if(window.confirm("Confirm delete")) {
            try {
                await api.delete(`/sellers/products/${productId}`);
                setInventory((prev) => prev.filter(item => item.id !== productId))
            } catch (error) {
                console.error("Error deleting product", error)
            }
        }
    };


    if(!inventory.length) {
        return(
            <>
                <h1>No items in inventory, create a new item to start selling</h1>
                <CreateProduct handleProductCreation={handleProductCreation} />
            </>
        )
    }

    return(
        <>
            <h1>Your Inventory</h1>
            <InventoryTable inventory={inventory} handleDelete={handleDelete} />
            <h1>Add a New Product</h1>
            <CreateProduct handleProductCreation={handleProductCreation} />
        </>
    )
}

export default ViewInventory;