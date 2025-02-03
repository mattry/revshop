import './App.css'
import LandingPage from './components/LandingPage';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import { UserProvider } from './components/UserContext';
import NavBar from './components/NavBar';
import InventoryPage from './components/inventory-components/InventoryPage';
import ProductPage from './components/product-components/ProductPage';
import ResultsPage from './components/product-components/ResultsPage';
import CartPage from './components/cart-components/CartPage';


function App() {

  return (
    <>
    <UserProvider>
      <Router>
        <NavBar />
        <Routes>
          <Route path ="/" element={<LandingPage />}/>
          <Route path="/inventory" element={<InventoryPage />}/>
          <Route path="/product/:id" element={<ProductPage />}/>
          <Route path="/results" element={<ResultsPage />} />
          <Route path="/cart" element={<CartPage />} />
        </Routes>
      </Router>
      </UserProvider>
    </>
  )
}

export default App
