import React, { useState } from "react";
import "./ProfilePage.css";

const ProfilePage = () => {
  // Заглушка для примера
  const [user, setUser] = useState({
    name: "Иван",
    second_name: "Петров",
    last_name: "Сергеевич",
    email: "ivan@example.com",
  });

  const [tasks] = useState([
    { id: 1, text: "Не работает принтер", status: "Ожидание" },
    { id: 2, text: "Нет интернета", status: "Обработано" },
  ]);

  const handleEdit = (e) => {
    e.preventDefault();
    console.log("Изменения:", user);
    // Тут будет PUT запрос
  };

  const handleChange = (e) => {
    setUser((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  return (
    <div className="profile-container">
      <h2>Личный кабинет</h2>

      <form className="user-form" onSubmit={handleEdit}>
        <label>Имя</label>
        <input name="name" value={user.name} onChange={handleChange} />
        <label>Фамилия</label>
        <input name="second_name" value={user.second_name} onChange={handleChange} />
        <label>Отчество</label>
        <input name="last_name" value={user.last_name} onChange={handleChange} />
        <label>Email</label>
        <input name="email" value={user.email} onChange={handleChange} />

        <button type="submit">Сохранить</button>
      </form>

      <h3>Мои заявки</h3>
      <ul className="task-list">
        {tasks.map((task) => (
          <li key={task.id}>
            <strong>{task.text}</strong>
            <span className={`status ${task.status.toLowerCase()}`}>
              {task.status}
            </span>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ProfilePage;
