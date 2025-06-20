import { Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import Navbar from "./components/Navbar";
import ProfilePage from "./pages/ProfilePage";
import CreateTaskPage from "./pages/CreateTaskPage";
import VerificationPage from "./pages/VerificationPage";

function App() {
  return (
    <div className="app-container">
      <Navbar />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/create-task" element={<CreateTaskPage />} />
        <Route path="/verificate" element={<VerificationPage />} />
      </Routes>
    </div>
  );
}

export default App;
