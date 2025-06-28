import { Link, useNavigate } from "react-router-dom";
import "./Navbar.css";

function Navbar() {
  const navigate = useNavigate();
  const isAuthenticated = localStorage.getItem('token'); // Проверяем наличие токена

  const handleLogout = () => {
    localStorage.removeItem('token'); // Очищаем токен
    navigate('/'); // Перенаправляем на главную
    window.location.reload(); // Обновляем страницу для применения изменений
  };

  return (
    <nav className="navbar">
      <h2 className="logo">Help Desk</h2>
      <div className="nav-links">
        <Link to="/">Главная</Link>
        
        {isAuthenticated ? (
          <button onClick={handleLogout} className="logout-button">
            Выйти
          </button>
        ) : (
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