import { useState } from "react";
import RegisterForm from "./RegisterForm";
import LoginForm from "./LoginForm";
import { Button, Box, Paper } from "@mui/material";
import { minHeight } from "@mui/system";

const AuthForm = () => {

    const [showLogin, setShowLogin] = useState(true);

    return(
        <Box
            display="flex"
            flexDirection="column"
            justifyContent="center"
            alignItems="center"
            minHeight="80vh"
        >
            <Paper elevation={3} sx={{ padding: 4, width: "100%", maxWidth: 400}}>
                {showLogin ? <LoginForm /> : <RegisterForm />}<br/>
                <Button fullWidth variant="outlined" onClick = {() => setShowLogin(!showLogin)}>
                    {showLogin ? "New user? Register" : "Existing user? Sign in"}
                </Button>
            </Paper>
        </Box>
    )
}

export default AuthForm;