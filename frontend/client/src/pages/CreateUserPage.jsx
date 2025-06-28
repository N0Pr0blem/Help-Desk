import React, { useState } from "react";
import axios from "../axiosConfig";
import "./CreateUserPage.css";
import { useNavigate } from "react-router-dom";

function CreateUserPage() {
  const [form, setForm] = useState({
    email: "",
    password: "",
    first_name: "",
    second_name: "",
    last_name: "",
    role: "USER",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    try {
      await axios.post("/users", form);
      setSuccess("Пользователь успешно создан!");
      setTimeout(() => {
        navigate("/admin");
      }, 1500);
    } catch (err) {
      setError("Ошибка при создании пользователя");
      console.error(err);
    }
  };

  return (
    <div className="create-user-wrapper">
      <div className="create-user-form">
        <h2>Создание пользователя</h2>
        <form onSubmit={handleSubmit}>
          <label>
            Email:
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              required
            />
          </label>

          <label>
            Пароль:
            <input
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              required
            />
          </label>

          <label>
            Имя (first_name):
            <input
              type="text"
              name="first_name"
              value={form.first_name}
              onChange={handleChange}
              required
            />
          </label>

          <label>
            Отчество (second_name):
            <input
              type="text"
              name="second_name"
              value={form.second_name}
              onChange={handleChange}
            />
          </label>

          <label>
            Фамилия (last_name):
            <input
              type="text"
              name="last_name"
              value={form.last_name}
              onChange={handleChange}
            />
          </label>

          <label>
            Роль:
            <select name="role" value={form.role} onChange={handleChange}>
              <option value="USER">USER</option>
              <option value="ADMIN">ADMIN</option>
              <option value="SYSADMIN">SYSADMIN</option>
            </select>
          </label>

          <button type="submit">Создать</button>
        </form>

        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}
      </div>
    </div>
  );
}

export default CreateUserPage;
