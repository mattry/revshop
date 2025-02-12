import { Button, ToggleButtonGroup, ToggleButton, TextField, Typography, CircularProgress } from '@mui/material'
import { useState } from 'react';
import { sellerRegister, buyerRegister } from '../../service/api';
import { useNavigate } from "react-router-dom";
import { useUser } from '../UserContext'

const RegisterForm = () => {
  
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("BUYER");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);

  const navigate = useNavigate();
  const { updateUser } = useUser();
  
  const handleRoleChange = (event, newRole) => {
    if (newRole !== null) {
      setRole(newRole);
    }
  };

  const handleRegister = async(e) => {
    e.preventDefault();
    setLoading(true);
    setError(false);
    const userInfo = { firstName, lastName, email, username, password, role };

    try {
      const response = role === "BUYER" 
        ? await buyerRegister(userInfo) 
        : await sellerRegister(userInfo);

      updateUser(response.data);
      navigate("/");
  } catch (error) {
      console.error("Error registering:", error);
      setError(true);
  } finally {
      setLoading(false);
  }

}

  return (
    <>
        <Typography variant="h4" gutterBottom align="center">
            Register
        </Typography>
        {error && (
                    <Typography color="error" align="center" sx={{ mt: 1 }}>
                        Invalid input, please try again.
                    </Typography>
                )}
      <form onSubmit={handleRegister}>
        <TextField fullWidth error={error} label="First Name" id="standard-basic" margin="normal" required onChange={(e) => setFirstName(e.target.value)}></TextField><br/>
        <TextField fullWidth error={error} label="Last Name" id="standard-basic" margin="normal" required onChange={(e) => setLastName(e.target.value)}></TextField><br/>
        <TextField fullWidth error={error} label="Email" id="standard-basic" margin="normal" type="email" required onChange={(e) => setEmail(e.target.value)}></TextField><br/>
        <TextField fullWidth error={error} label="Username" id="standard-basic" margin="normal" required onChange={(e) => setUsername(e.target.value)}></TextField><br/>
        <TextField fullWidth error={error} label="Password" id="standard-basic" margin="normal" type="password" required onChange={(e) => setPassword(e.target.value)}></TextField><br/>
        <ToggleButtonGroup 
          value={role} 
          exclusive 
          onChange={handleRoleChange}
          fullWidth 
        >
          <ToggleButton value="BUYER">Buyer</ToggleButton>
          <ToggleButton value="SELLER">Seller</ToggleButton>
        </ToggleButtonGroup>
        <br/>
        <br/>
        <Button fullWidth variant="contained" type="submit">
          {loading ? <CircularProgress size={24} color="inherit" /> : "Register"}
        </Button>
      </form>
    </>
  )
}

export default RegisterForm;