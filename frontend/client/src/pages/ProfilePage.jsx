import React, { useState, useEffect } from "react";
import axios from "../axiosConfig"; // axios с токеном
import "./ProfilePage.css";

const ProfilePage = () => {
  const [user, setUser] = useState({
    first_name: "",
    second_name: "",
    last_name: "",
    password: "",
    email: "",
  });

  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);

  // Получение профиля и заявок
  useEffect(() => {
    const fetchData = async () => {
      try {
        const profileRes = await axios.get("profile");
        setUser(profileRes.data);

        const tasksRes = await axios.get("tasks");
        setTasks(tasksRes.data);

        setLoading(false);
      } catch (error) {
        console.log("Токен при входе в ProfilePage:", localStorage.getItem("token"));
        console.error("Ошибка при загрузке профиля или заявок:", error);
        alert("Не удалось загрузить данные профиля или заявок");
      }
    };

    fetchData();
  }, []);

  const handleEdit = async (e) => {
    e.preventDefault();
    try {
      const { first_name, second_name, last_name, password } = user;
  
      const payload = { first_name, second_name, last_name };
    if (password && password.trim()) {
      payload.password = password;
}

      const response = await axios.patch("/profile", payload);
      alert("Профиль успешно обновлён!");
      setUser((prev) => ({ ...prev, ...response.data, password: "" })); // очищаем пароль
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

  if (loading) return <p>Загрузка данных...</p>;

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

        <label>Новый пароль</label>
        <input type="password" name="password" value={user.password} onChange={handleChange} placeholder="Оставьте пустым, если не хотите менять" />

        <label>Email</label>
        <input name="email" value={user.email} onChange={handleChange} disabled />

        <button type="submit">Сохранить</button>
      </form>

      <h3>Мои заявки</h3>
      {tasks.length === 0 ? (
        <p>У вас нет заявок</p>
      ) : (
        <ul className="task-list">
          {tasks.map((task) => (
            <li key={task.id} className="task-item">
              <div className="task-text">{task.text || task.description}</div>
              <span className={`status ${task.status?.toLowerCase() || "unknown"}`}>
                {task.status || "Без статуса"}
              </span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default ProfilePage;
