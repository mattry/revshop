import { Button, List, ListItem, ListItemText } from "@mui/material";
import { Link } from "react-router-dom";
import { useState } from "react";
import CheckOutForm from "./CheckOutForm";

const CartItemList = ({ cart, onDelete, onUpdate }) => {

    const [showCheckout, setShowCheckout] = useState(false);

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
                        <div>
                            <input
                                type="number"
                                min="1"
                                value={item.quantity}
                                onChange={(e) =>
                                    onUpdate(item.productId, parseInt(e.target.value))
                                }
                                style={{ width: "60px", marginRight: "10px" }}
                            />
                            <Button
                                variant="outlined"
                                color="error"
                                onClick={() => onDelete(item.productId)}
                            >
                                Remove from Cart
                            </Button>
                        </div>
                    </ListItem>
                ))}
            </List>
            <h3>Total: ${cart.total.toFixed(2)}</h3><br />
            <Button
                variant="contained"
                color="primary"
                onClick={() => setShowCheckout(!showCheckout)}
                style={{ marginTop: "10px" }}
            >
                {showCheckout ? "Cancel Checkout" : "Proceed to Checkout"}
            </Button><br /><br />

            {showCheckout && <CheckOutForm />}
        </>
    );
};

export default CartItemList