import { Button, TextField } from "@mui/material";
import { login } from "../../service/api";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useUser } from '../UserContext'

const LoginForm = () => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();
    const { updateUser } = useUser();


    const handleLogin = async(e) => {
        e.preventDefault();
        try {
            const response = await login({username, password})
            updateUser(response.data);
            navigate("/");
        } catch (error) {
            console.error("Error loggin in:", error);
        }
    }

    return (
        <>
        <h1>Login</h1>
        <form onSubmit={handleLogin}>
            <TextField label="Username" id="standard-basic" margin="normal" required value={username} onChange={(e) => setUsername(e.target.value)}></TextField><br/>
            <TextField label="Password" id="standard-basic" margin="normal" type="password" required value={password} onChange={(e) => setPassword(e.target.value)}></TextField><br/>
            <br/>
            <Button variant="contained" type="submit">Login</Button>
        </form>
    </>
    )

}

export default LoginForm;