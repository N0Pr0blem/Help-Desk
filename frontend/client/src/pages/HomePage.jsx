import React from "react";
import { useNavigate } from "react-router-dom";
import "./HomePage.css";
import Image from "/projectX.jpg";

const HomePage = () => {
  const navigate = useNavigate();

  const handleCreateTaskClick = () => {
    // 1. Проверяем наличие токена в localStorage
    const token = localStorage.getItem("token");
    console.log("Текущий токен:", token); // Для отладки
    
    if (!token || token === "null" || token === "undefined") {
      // 2. Если токена нет - показываем alert и перенаправляем на логин
      alert("Для создания заявки необходимо войти в систему");
      navigate("/login");
      return; // Важно: прекращаем выполнение функции
    }
    
    // 3. Только если токен есть - перенаправляем на создание заявки
    navigate("/create-task");
  };

  return (
    <div className="home-wrapper">
      <div className="home-content">
        <div className="home-text">
          <h1>Добро пожаловать в Help Desk</h1>
          <p className="lead">
            Сервис для быстрой обработки заявок и технической поддержки.
          </p>
          <ul className="features">
            <li> Оставляйте заявки онлайн</li>
            <li> Отслеживайте статус обработки</li>
            <li> Простой и понятный интерфейс</li>
          </ul>
          <button 
            className="button-style"
            onClick={handleCreateTaskClick}
          >
            Оставить заявку
          </button>
        </div>
        <div className="home-image">
          <img src={Image} alt="Support Illustration" />
        </div>
      </div>
    </div>
  );
};

export default HomePage;