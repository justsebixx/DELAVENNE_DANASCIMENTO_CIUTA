import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import ScrollToTop from './components/ScrollToTop';
import ProtectedRoute from './components/ProtectedRoute';
import ErrorBoundary from './components/ErrorBoundary';
import Home from './pages/Home';
import ManageBooks from './pages/ManageBooks';
import MyBorrows from './pages/MyBorrows';
import SearchBooks from './pages/SearchBooks';
import Statistics from './pages/Statistics';
import Notifications from './pages/Notifications';
import NotFound from './pages/NotFound';
import About from './pages/About';
import Contact from './pages/Contact';
import LegalNotice from './pages/LegalNotice';
import PrivacyPolicy from './pages/PrivacyPolicy';
import Login from './pages/Login';
import Profile from './pages/Profile';
import './App.css';
import Footer from './components/Footer';

function App() {
  return (
    <ErrorBoundary>
      <Router>
        <ScrollToTop />
        <Header />
        <main>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/search-books" element={<SearchBooks />} />
            <Route path="/my-borrows" element={<MyBorrows />} />
            <Route path="/notifications" element={<Notifications />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/manage-books" element={
              <ProtectedRoute allowedRoles={["ADMIN", "BIBLIOTHECAIRE"]}>
                <ManageBooks />
              </ProtectedRoute>
            } />
            <Route path="/statistics" element={
              <ProtectedRoute allowedRoles={["ADMIN", "BIBLIOTHECAIRE"]}>
                <Statistics />
              </ProtectedRoute>
            } />
            <Route path="/about" element={<About />} />
            <Route path="/contact" element={<Contact />} />
            <Route path="/login" element={<Login />} />
            <Route path="/legal" element={<LegalNotice />} />
            <Route path="/privacy" element={<PrivacyPolicy />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </main>
        <Footer />
      </Router>
    </ErrorBoundary>
  );
}

export default App;
