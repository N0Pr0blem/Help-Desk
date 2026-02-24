import { useState, useEffect } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import "./Navbar.css";

function Navbar() {
  const navigate = useNavigate();
  const location = useLocation();
  const isAuthenticated = !!localStorage.getItem("token");
  const role = localStorage.getItem("role");
  const [menuOpen, setMenuOpen] = useState(false);

  useEffect(() => {
    setMenuOpen(false);
  }, [location.pathname]);

  useEffect(() => {
    if (menuOpen) {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "";
    }
    return () => {
      document.body.style.overflow = "";
    };
  }, [menuOpen]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    setMenuOpen(false);
    navigate("/");
    window.location.reload();
  };

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };

  const closeMenu = () => {
    setMenuOpen(false);
  };

  return (
    <nav className="navbar">
      <Link to="/" className="logo">Help Desk</Link>
      
      <button 
        className={`burger-menu ${menuOpen ? "active" : ""}`} 
        onClick={toggleMenu}
        aria-label="Меню"
      >
        <span></span>
        <span></span>
        <span></span>
      </button>

      <div 
        className={`nav-overlay ${menuOpen ? "active" : ""}`} 
        onClick={closeMenu}
      />

      <div className={`nav-links ${menuOpen ? "active" : ""}`}>
        {isAuthenticated && (
          <>
            <Link to="/profile">Личный кабинет</Link>

            {role === "ADMIN" && (
              <>
                <Link to="/admin">Панель администратора</Link>
                <Link to="/admin/tasks">Заявки (общие)</Link>
                <Link to="/sysadmin">Панель сисадмина</Link>
              </>
            )}

            {role === "SYSADMIN" && (
              <Link to="/sysadmin">Панель сисадмина</Link>
            )}

            <button onClick={handleLogout} className="logout-button">
              Выйти
            </button>
          </>
        )}

        {!isAuthenticated && (
          <>
            <Link to="/login">Вход</Link>
            <Link to="/register">Регистрация</Link>
          </>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
