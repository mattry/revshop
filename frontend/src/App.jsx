import './App.css'
import LandingPage from './components/LandingPage';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import { UserProvider } from './components/UserContext';


function App() {

  return (
    <>
    <UserProvider>
      <Router>
        <Routes>
          <Route path ="/" element={<LandingPage />}/>
        </Routes>
      </Router>
      </UserProvider>
    </>
  )
}

export default App
