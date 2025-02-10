import { Button, TextField, CircularProgress, Typography } from "@mui/material";
import { login } from "../../service/api";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useUser } from '../UserContext'

const LoginForm = () => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(false);
    const navigate = useNavigate();
    const { updateUser } = useUser();


    const handleLogin = async(e) => {
        e.preventDefault();
        setLoading(true);
        setError(false);

        try {
            const response = await login({username, password})
            updateUser(response.data);
            navigate("/");
        } catch (error) {
            console.error("Error logging in:", error);
            setError(true);
        } finally {
            setLoading(false);
        }
    }

    return (
        <>
        <Typography variant="h4" gutterBottom align="center">
            Login
        </Typography>
        {error && (
                    <Typography color="error" align="center" sx={{ mt: 1 }}>
                        Invalid username or password, please try again.
                    </Typography>
                )}
        <form onSubmit={handleLogin}>
            <TextField fullWidth label="Username" id="standard-basic" margin="normal" required value={username} error={error} onChange={(e) => setUsername(e.target.value)}></TextField><br/>
            <TextField fullWidth label="Password" id="standard-basic" margin="normal" type="password" required value={password} error={error} onChange={(e) => setPassword(e.target.value)}></TextField><br/>
            <br/>
            <Button fullWidth variant="contained" type="submit">
                {loading ? <CircularProgress size={24} color="inherit" /> : "Login"}
            </Button>
        </form>
    </>
    )

}

export default LoginForm;