import { useState } from "react";
import "./RegisterPage.css";

function RegisterPage() {
  const [formData, setFormData] = useState({
    name: "",
    second_name: "",
    last_name: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.name.trim()) newErrors.name = "Имя обязательно";
    if (!formData.second_name.trim()) newErrors.second_name = "Фамилия обязательно";
    if (!formData.last_name.trim()) newErrors.last_name = "Отчество обязательно";
    if (!formData.email.trim()) newErrors.email = "Email обязателен";
    else if (!/\S+@\S+\.\S+/.test(formData.email)) newErrors.email = "Некорректный email";
    if (!formData.password) newErrors.password = "Пароль обязателен";
    else if (formData.password.length < 6) newErrors.password = "Минимум 6 символов";
    if (formData.confirmPassword !== formData.password)
      newErrors.confirmPassword = "Пароли не совпадают";
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
   
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;

    console.log("Регистрация:", formData);
    // тут запросы на бэкенд
  };

  return (
    <div className="register-container">
      <form className="register-form" onSubmit={handleSubmit}>
      <h2 class="icon-registration">Регистрация</h2>

        <label>Имя</label>
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleChange}
        />
        {errors.name && <p className="error">{errors.name}</p>}

        <label>Фамилия</label>
        <input
          type="text"
          name="second_name"
          value={formData.second_name}
          onChange={handleChange}
        />
        {errors.second_name && <p className="error">{errors.second_name}</p>}

        <label>Отчество</label>
        <input
          type="text"
          name="last_name"
          value={formData.last_name}
          onChange={handleChange}
        />
        {errors.last_name && <p className="error">{errors.last_name}</p>}

        <label>Email</label>
        <input
          type="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
        />
        {errors.email && <p className="error">{errors.email}</p>}

        <label>Пароль</label>
        <input
          type="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
        />
        {errors.password && <p className="error">{errors.password}</p>}

        <label>Повторите пароль</label>
        <input
          type="password"
          name="confirmPassword"
          value={formData.confirmPassword}
          onChange={handleChange}
        />
        {errors.confirmPassword && (
          <p className="error">{errors.confirmPassword}</p>
        )}

        <button type="submit">Зарегистрироваться</button>
      </form>
    </div>
  );
}

export default RegisterPage;
