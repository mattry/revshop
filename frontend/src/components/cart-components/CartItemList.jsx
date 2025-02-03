import { Button, List, ListItem, ListItemText } from "@mui/material";
import { Link } from "react-router-dom";

const CartItemList = ({ cart, onDelete }) => {
    return (
        <>
            <h2>Your Cart</h2>
            <List>
                {cart.items.map((item) => (
                    <ListItem
                    key={item.productId} 
                    divider 
                    style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}
                >
                    <ListItemText 
                        primary={<Link to={`/product/${item.productId}`}>{item.productName}</Link>} 
                        secondary={`$${item.price.toFixed(2)} x ${item.quantity} = $${item.subtotal.toFixed(2)}`}
                    />
                    <Button 
                        variant="outlined" 
                        color="error" 
                        onClick={() => onDelete(item.productId)}
                    >
                        Remove from Cart
                    </Button>
                </ListItem>
                ))}
            </List>
            <h3>Total: ${cart.total.toFixed(2)}</h3>
        </>
    );
};

export default CartItemList