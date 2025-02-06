import './App.css'
import LandingPage from './components/LandingPage';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import { UserProvider } from './components/UserContext';
import NavBar from './components/NavBar';
import InventoryPage from './components/inventory-components/InventoryPage';
import ProductPage from './components/product-components/ProductPage';
import ResultsPage from './components/product-components/ResultsPage';
import ViewOrders from './components/order-components/ViewOrders';
import CartPage from './components/cart-components/CartPage';
import { Elements } from '@stripe/react-stripe-js';
import { loadStripe } from "@stripe/stripe-js";

const stripePromise = loadStripe("pk_test_51QoXFWFYU01h0bhqRm9aoB7FgeGSoZBsSazZz7IwoHimOQh0q44fnwoCkkGBxUGcN6DvmO4DnoX7Ox1i95eYJLri00NXjsJhZw");

function App() {

  return (
    <>
    <Elements stripe={stripePromise}>
    <UserProvider>
      <Router>
        <NavBar />
        <Routes>
          <Route path ="/" element={<LandingPage />}/>
          <Route path="/inventory" element={<InventoryPage />}/>
          <Route path="/product/:id" element={<ProductPage />}/>
          <Route path="/results" element={<ResultsPage />} />
          <Route path="/cart" element={<CartPage />} />
          <Route path="/orders" element={<ViewOrders />} />
        </Routes>
      </Router>
      </UserProvider>
      </Elements>
    </>
  )
}

export default App
