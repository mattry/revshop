import { useState } from "react";
import RegisterForm from "./RegisterForm";
import LoginForm from "./LoginForm";
import { Button } from "@mui/material";

const AuthForm = () => {

    const [showLogin, setShowLogin] = useState(true);

    return(
        <>
            {showLogin ? <LoginForm /> : <RegisterForm />}<br/>
            <Button variant="outlined" onClick = {() => setShowLogin(!showLogin)}>
                {showLogin ? "New user? Register" : "Existing user? Sign in"}
            </Button>
        </>
    )
}

export default AuthForm;