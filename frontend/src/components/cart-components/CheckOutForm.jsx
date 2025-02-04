import { CardElement, useStripe, useElements } from "@stripe/react-stripe-js";
import { useState } from "react";
import api from "../../service/api";
import { Button } from "@mui/material";

const CheckOutForm = () => {
    
    const stripe = useStripe();
    const elements = useElements();
    const [error, setError] = useState(null);
    const [processing, setProcessing] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setProcessing(true);
        setError(null);

        if (!stripe || !elements) return;

        const {error, paymentMethod } = await stripe.createPaymentMethod({
            type: "card",
            card: elements.getElement(CardElement),
        });

        if (error) {
            setError(error.message);
            setProcessing(false);
            return;
        }

        console.log("Payment Method ID: ", paymentMethod.id);

        try {
            const response = await api.post("/checkout", {
                paymentMethodId: paymentMethod.id
            });

    
            const data = response.data;
            setProcessing(false);
    
            if (data.error) {
                setError(data.error);
            } else {
                alert("Payment successful!");
            }
        } catch (error) {
            setProcessing(false);
            setError(error.response?.data?.error || "An error occurred");
        }
    };

    return (
        <>
        <br/><br/>
        <form onSubmit={handleSubmit}>
            <CardElement />
            <br/><br/>
            <Button variant="contained" type="submit" disabled={processing || !stripe}>
                {processing ? "Processing.." : "Pay Now"}
            </Button>
            {error && <p style={{ color: "red" }}>{error}</p>}
        </form>
        </>
    );

}

export default CheckOutForm;