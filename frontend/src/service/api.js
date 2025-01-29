import axios from "axios";

axios.defaults.withCredentials = true;

const api = axios.create({
    baseURL: 'http://localhost:8080'
});

export default api;

export const login = async (credentials) => {
    return await api.post("/auth/login", credentials);
}

export const buyerRegister = async (userInfo) => {
    return await api.post("/auth/buyerRegister", userInfo)
}

export const sellerRegister = async (userInfo) => {
    return await api.post("/auth/sellerRegister", userInfo)
}