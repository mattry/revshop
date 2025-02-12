import { Paper, Box, Typography } from "@mui/material";
import { DataGrid } from '@mui/x-data-grid';

const Order = ({ order }) => {
    const items = order.items;

    const columns = [
        { field: "productName", headerName: "Product Name", width: 200 },
        { 
            field: "price", 
            headerName: "Price per Unit", 
            type: "number", 
            width: 150
        },
        { field: "quantity", headerName: "Quantity", type: "number", width: 120 },
        { 
            field: "subTotal", 
            headerName: "Subtotal", 
            type: "number", 
            width: 150
        }
    ];

    const rows = order.items.map((item, index) => ({
        id: `${order.orderId}-${index}`,
        productName: item.productName,
        price: "$" + item.price.toFixed(2),
        quantity: item.quantity,
        subTotal: "$" + item.subTotal.toFixed(2)
    }));

    console.log("Mapped rows:", rows);


    return(
        <Box display="flex" flexDirection="column" alignItems="center">
            <Paper elevation={3} sx={{ padding: 4, width: "100%", maxWidth: 700 }}>
                <Typography variant="h6" gutterBottom>
                    Order No: {order.orderId}
                </Typography>
                <Box sx={{ height: 300, width: "100%" }}>
                    <DataGrid
                        rows={rows}
                        columns={columns}
                        pageSizeOptions={[5, 10]}
                    />
                </Box>
                <Typography variant="h6" align="right" sx={{ mt: 2 }}>
                    Total: ${order.total.toFixed(2)}
                </Typography>
            </Paper>
        </Box>
    );
}

export default Order;