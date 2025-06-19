import React from "react";
import { useNavigate } from "react-router-dom";
import "./HomePage.css";
import Image from "/projectX.jpg"; // Если файл лежит в `public/projectX.jpg`

const HomePage = () => {
  const navigate = useNavigate();

  return (
    <div className="home-wrapper">
      <div className="home-content">
        <div className="home-text">
          <h1>Добро пожаловать в Help Desk</h1>
          <p className="lead">
            Сервис для быстрой обработки заявок и технической поддержки внутри нашей компании. Упрощайте процессы, экономьте время.
          </p>
          <ul className="features">
            <li>✔ Оставляйте заявки онлайн</li>
            <li>✔ Отслеживайте статус обработки</li>
            <li>✔ Простой и понятный интерфейс</li>
            <li>✔ Удобство</li>
          </ul>
        </div>
        <div className="home-image">
          <img src={Image} alt="Support Illustration" />
        </div>
      </div>
    </div>
  );
};

export default HomePage;
