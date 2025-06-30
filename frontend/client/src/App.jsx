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
import RoleProtectedRoute from "./components/RoleProtectedRoute";
import Footer from "./components/Footer";  

function App() {
  return (
    <div className="app-container">
      <Navbar />
      <div className="main-content">
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route
          path="/profile"
          element={
            <PrivateRoute>
              <ProfilePage />
            </PrivateRoute>
          }
        />
        <Route path="/create-task" element={<CreateTaskPage />} />
        <Route path="/verificate" element={<VerificationPage />} />

        {/* 🔒 Только для ADMIN */}
        <Route
          path="/admin"
          element={
            <RoleProtectedRoute allowedRoles={["ADMIN"]}>
              <AdminPanelPage />
            </RoleProtectedRoute>
          }
        />
        <Route
          path="/admin/create-user"
          element={
            <RoleProtectedRoute allowedRoles={["ADMIN"]}>
              <CreateUserPage />
            </RoleProtectedRoute>
          }
        />
        <Route
          path="/admin/edit-user/:id"
          element={
            <RoleProtectedRoute allowedRoles={["ADMIN"]}>
              <EditUserPage />
            </RoleProtectedRoute>
          }
        />

        {/* 🔒 ADMIN */}
        <Route
          path="/admin/tasks"
          element={
            <RoleProtectedRoute allowedRoles={["ADMIN"]}>
              <SysadminTasksPage />
            </RoleProtectedRoute>
          }
        />

        {/* 🔒 ADMIN и SYSADMIN */}
        <Route
          path="/sysadmin"
          element={
            <RoleProtectedRoute allowedRoles={["ADMIN", "SYSADMIN"]}>
              <SysadminSoloPage />
            </RoleProtectedRoute>
          }
        />
      </Routes>
      </div>
      <Footer /> 
    </div>
  );
}

export default App;
