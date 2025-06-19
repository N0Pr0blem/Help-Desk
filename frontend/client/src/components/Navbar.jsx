import { Link } from "react-router-dom";
import "./Navbar.css";

function Navbar() {
  return (
    <nav className="navbar">
      <h2 className="logo">Help Desk</h2>
      <div className="nav-links">
        <Link to="/">Главная</Link>
        <Link to="/login">Вход</Link>
        <Link to="/register">Регистрация</Link>
      </div>
    </nav>
  );
}

export default Navbar;
