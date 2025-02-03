import { Link } from "react-router-dom";
import { useUser } from "./UserContext";
import { AppBar, Toolbar, Typography, Button, Box } from "@mui/material";

const NavBar = () => {

    const { user } = useUser();

    return (
        <Box sx={{ m: 0, p: 0}}>
            <AppBar position="static">
                <Toolbar sx={{ px: 2}}>
                    <Typography variant="h6" sx={{ flexGrow: 1, ml: 0}}>
                        RevShop
                    </Typography>
                {user && (
                    <>
                        <Button color="inherit" component={Link} to="/">
                            Home
                        </Button>
                        <Button color="inherit" component={Link} to="/inventory">
                            Inventory
                        </Button>
                    </>
                )}
                </Toolbar>
            </AppBar>
      </Box>
    );

}

export default NavBar;