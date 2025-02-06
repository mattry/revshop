import { Link, useNavigate } from "react-router-dom";
import { useUser } from "./UserContext";
import { search } from "../service/api";
import { AppBar, Toolbar, Typography, Button, Box, InputBase, IconButton } from "@mui/material";
import SearchIcon from '@mui/icons-material/Search'
import { useState } from "react";

const NavBar = () => {

    const { user, clearUser } = useUser();
    const navigate = useNavigate();
    const [searchTerm, setSearchTerm] = useState("");

    const handleLogout = () => {
        clearUser();
        navigate("/");
    }

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!searchTerm.trim()) return;

        try {
            const response = await search(searchTerm);
            navigate("/results", { state: {results: response.data } });
            setSearchTerm("");
        } catch (error) {
            console.error("Could not perform search:", error)
        }
    };

    return (
        <Box sx={{ display: "flex", m: 0, p: 0}}>
            <AppBar position="static">
                <Toolbar sx={{ px: 2}}>
                    <Typography variant="h6" sx={{ flexGrow: 1, ml: 0}}>
                        RevShop
                    </Typography>
                {user && (
                    <>
                        <form onSubmit={handleSearch} style={{ display: "flex", alignItems: "center" }}>
                            <Box
                            sx={{
                                display: "flex",
                                alignItems: "center",
                                backgroundColor: "rgba(255, 255, 255, 0.15)",
                                borderRadius: "4px",
                                padding: "4px 10px",
                                ml: 2,
                            }}
                            >
                            <InputBase
                                placeholder="Search..."
                                inputProps={{ "aria-label": "search" }}
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                sx={{ color: "inherit", width: "150px" }}
                            />
                            </Box>
                            <IconButton type="submit" color="inherit">
                            <SearchIcon />
                            </IconButton>
                        </form>
                        <Button color="inherit" component={Link} to="/">
                            Home
                        </Button>
                        {user.role === "SELLER" && (
                            <Button color="inherit" component={Link} to="/inventory">
                                Inventory
                            </Button>
                        )}
                        {user.role === "BUYER" && (
                            <Button color="inherit" component={Link} to="/cart">
                                Cart
                            </Button>
                        )}
                        <Button color="inherit" onClick={handleLogout}>
                            Logout
                        </Button>
                    </>
                )}
                </Toolbar>
            </AppBar>
      </Box>
    );

}

export default NavBar;