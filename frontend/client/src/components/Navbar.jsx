import { Link, useNavigate } from "react-router-dom";
import "./Navbar.css";

function Navbar() {
  const navigate = useNavigate();
  const isAuthenticated = !!localStorage.getItem("token");
  const role = localStorage.getItem("role");

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    navigate("/");
    window.location.reload();
  };

  return (
    <nav className="navbar">
      <Link to="/" className="logo">Help Desk</Link>
      <div className="nav-links">
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
