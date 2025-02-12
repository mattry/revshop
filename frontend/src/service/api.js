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

export const search = async (keyword) => {
    return await api.get(`/product/search?keyword=${keyword}`)
}

export const addToCart = async (requestBody) => {
    return await api.post("/cart/add", requestBody);
}

export const getCartDetails = async (buyerId) => {
    return api.get(`/cart/details/${buyerId}`)
        .then(response => response.data)
        .catch(error => { throw error.response?.data?.error || "Failed to fetch cart"; });
};

export const updateCartItem = async (buyerId, productId, quantity) => {
    return api.patch(`/cart/update`, null, {
        params: { buyerId, productId, quantity }
    })
    .then(response => response.data)
    .catch(error => { throw error.response?.data?.error || "Failed to update cart"; });
};

export const removeCartItem = async (buyerId, productId) => {
    return updateCartItem(buyerId, productId, 0); // Setting quantity to 0 removes the item
};