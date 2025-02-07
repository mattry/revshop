const Order = ({ order }) => {
    const items = order.items;

    return(
        <>
            <h4>Order No. {order.orderId}</h4>
            <h5>Order Details:</h5>
            <table>
                <thead>
                    <tr>
                        <th>Product Name</th>
                        <th>Price per Unit</th>
                        <th>Quantity</th>
                        <th>Subtotal</th>
                    </tr>
                </thead>
                <tbody>
                    {items.map((item) => (
                        <tr key={item.productId}>
                            <td>{item.productName}</td>
                            <td>${item.price.toFixed(2)}</td>
                            <td>{item.quantity}</td>
                            <td>${item.subTotal.toFixed(2)}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <h6>Total: ${order.total.toFixed(2)}</h6>
        </>
    )
}

export default Order;