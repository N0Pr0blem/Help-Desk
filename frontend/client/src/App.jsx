import { Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import Navbar from "./components/Navbar";
import ProfilePage from "./pages/ProfilePage";
import CreateTaskPage from "./pages/CreateTaskPage";
import VerificationPage from "./pages/VerificationPage";
import PrivateRoute from "./components/PrivateRoute";
import AdminPanelPage from "./pages/AdminPanelPage";
import CreateUserPage from "./pages/CreateUserPage";
import EditUserPage from "./pages/EditUserPage";
import SysadminTasksPage from "./pages/SysadminTasksPage";
import SysadminSoloPage from "./pages/SysadminSoloPage";

function App() {
  return (
    <div className="app-container">
      <Navbar />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/profile" element={<PrivateRoute><ProfilePage /></PrivateRoute>}/>
        <Route path="/create-task" element={<CreateTaskPage />} />
        <Route path="/verificate" element={<VerificationPage />} />
        <Route path="/admin" element={<AdminPanelPage />} />
        <Route path="/admin/create-user" element={<CreateUserPage />} />
        <Route path="/admin/edit-user/:id" element={<EditUserPage />} />
        <Route path="/admin/sysadmin" element={<SysadminTasksPage />} />
        <Route path="/sysadmin" element={<SysadminSoloPage />} />
      </Routes>
    </div>
  );
}

export default App;
