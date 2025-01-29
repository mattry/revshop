import { Button, ToggleButtonGroup, ToggleButton, TextField } from '@mui/material'
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

  const navigate = useNavigate();
  const { updateUser } = useUser();
  
  const handleRoleChange = (event, newRole) => {
    if (newRole !== null) {
      setRole(newRole);
    }
  };

  const handleRegister = async(e) => {
    e.preventDefault();
    const userInfo = { firstName, lastName, email, username, password, role };

    try {
      const response = role === "BUYER" 
        ? await buyerRegister(userInfo) 
        : await sellerRegister(userInfo);

      updateUser(response.data);
      navigate("/");
  } catch (error) {
      console.error("Error registering:", error);
  }
}

  return (
    <>
    <h1>Register</h1>
      <form onSubmit={handleRegister}>
        <TextField label="First Name" id="standard-basic" margin="normal" required onChange={(e) => setFirstName(e.target.value)}></TextField><br/>
        <TextField label="Last Name" id="standard-basic" margin="normal" required onChange={(e) => setLastName(e.target.value)}></TextField><br/>
        <TextField label="Email" id="standard-basic" margin="normal" type="email" required onChange={(e) => setEmail(e.target.value)}></TextField><br/>
        <TextField label="Username" id="standard-basic" margin="normal" required onChange={(e) => setUsername(e.target.value)}></TextField><br/>
        <TextField label="Password" id="standard-basic" margin="normal" type="password" required onChange={(e) => setPassword(e.target.value)}></TextField><br/>
        <ToggleButtonGroup 
          value={role} 
          exclusive 
          onChange={handleRoleChange}
        >
          <ToggleButton value="BUYER">Buyer</ToggleButton>
          <ToggleButton value="SELLER">Seller</ToggleButton>
        </ToggleButtonGroup>
        <br/>
        <br/>
        <Button variant="contained" type="submit">Register</Button>
      </form>
    </>
  )
}

export default RegisterForm;