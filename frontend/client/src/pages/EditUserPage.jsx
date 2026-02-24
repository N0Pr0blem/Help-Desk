import React, { useState, useEffect } from "react";
import axios from "../axiosConfig";
import { useParams, useNavigate } from "react-router-dom";
import "./EditUserPage.css";

function EditUserPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    email: "",
    first_name: "",
    second_name: "",
    last_name: "",
    role: "",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const res = await axios.get(`/users/${id}`);
        setForm(res.data);
      } catch (err) {
        setError("Ошибка при загрузке данных пользователя");
        console.error(err);
      }
    };

    fetchUser();
  }, [id]);

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
      await axios.patch(`/users/${id}`, {
        first_name: form.first_name,
        second_name: form.second_name,
        last_name: form.last_name,
        role: form.role,
      });
      setSuccess("Пользователь успешно обновлён!");
      setTimeout(() => {
        navigate("/admin");
      }, 1500);
    } catch (err) {
      setError("Ошибка при обновлении пользователя");
      console.error(err);
    }
  };

  return (
    <div className="edit-user-wrapper">
      <div className="edit-user-form">
        <h2>Редактирование пользователя</h2>
        <form onSubmit={handleSubmit}>
          <label>
            Email (нельзя изменить):
            <input type="email" value={form.email} disabled />
          </label>

          <label>
            Имя:
            <input
              type="text"
              name="first_name"
              value={form.first_name}
              onChange={handleChange}
            />
          </label>

          <label>
            Отчество:
            <input
              type="text"
              name="second_name"
              value={form.second_name}
              onChange={handleChange}
            />
          </label>

          <label>
            Фамилия:
            <input
              type="text"
              name="last_name"
              value={form.last_name}
              onChange={handleChange}
            />
          </label>

          <label>
            Роль:
            <select
              name="role"
              value={form.role}
              onChange={handleChange}
            >
              <option value="USER">Пользователь</option>
              <option value="SYSADMIN">Сисадмин</option>
              <option value="ADMIN">Администратор</option>
            </select>
          </label>

          <button type="submit">Сохранить</button>
        </form>

        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}
      </div>
    </div>
  );
}

export default EditUserPage;
