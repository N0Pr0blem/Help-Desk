import React, { useState, useEffect } from "react";
import axios from "../axiosConfig"; // наш axios с токеном
import "./ProfilePage.css";

const ProfilePage = () => {
  const [user, setUser] = useState({
    first_name: "",
    second_name: "",
    last_name: "",
    email: "",
  });

  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);

  // Получение профиля
  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axios.get("/profile");
        setUser(response.data);
        setLoading(false);
      } catch (error) {
        console.error("Ошибка при загрузке профиля:", error);
        alert("Не удалось загрузить профиль");
      }
    };

    fetchProfile();
  }, []);

  const handleEdit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.put("/profile", user);
      alert("Профиль успешно обновлён!");
      setUser(response.data); // обновляем на случай, если сервер что-то изменил
    } catch (error) {
      console.error("Ошибка при обновлении профиля:", error);
      alert("Не удалось обновить профиль");
    }
  };

  const handleChange = (e) => {
    setUser((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  if (loading) return <p>Загрузка профиля...</p>;

  return (
    <div className="profile-container">
      <h2>Личный кабинет</h2>

      <form className="user-form" onSubmit={handleEdit}>
        <label>Имя</label>
        <input name="first_name" value={user.first_name} onChange={handleChange} />
        <label>Фамилия</label>
        <input name="second_name" value={user.second_name} onChange={handleChange} />
        <label>Отчество</label>
        <input name="last_name" value={user.last_name} onChange={handleChange} />
        <label>Email</label>
        <input name="email" value={user.email} onChange={handleChange} disabled />

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
