import { createContext, useState, useContext, useEffect } from "react";

const UserContext = createContext();

export const UserProvider = ({children}) => {

    const storedUser = JSON.parse(localStorage.getItem("user"));

    const [user, setUser] = useState(storedUser || null);

    const updateUser = (newUser) => {
        setUser(newUser);  
        localStorage.setItem("user", JSON.stringify(newUser));
    }

    const clearUser = () => {
        setUser(null);
        localStorage.removeItem("user");
    }

    useEffect(() => {
        if (user) {
            localStorage.setItem("user", JSON.stringify(user));
        } else {
            localStorage.removeItem("user");
        }
    }, [user]);

    return (
        <UserContext.Provider value ={{ user, updateUser, clearUser }}>
            {children}
        </UserContext.Provider>
    );
};

export const useUser = () => {
    return useContext(UserContext);
};