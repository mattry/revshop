import { Link } from "react-router-dom";
import { useUser } from "./UserContext";

const NavBar = () => {

    const { user } = useUser();

    return (
        <>
            {user ?
                <>
                    <Link to="/">Home</Link>
                    <Link to="inventory">Inventory</Link>
                </>
            : null }
        </>
    )

}

export default NavBar;