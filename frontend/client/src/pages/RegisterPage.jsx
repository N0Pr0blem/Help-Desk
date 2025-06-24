import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "../axiosConfig";
import "./RegisterPage.css";
import Image from "/projectX.jpg"; 

function RegisterPage() {
  const [formData, setFormData] = useState({
    first_name: "",
    second_name: "",
    last_name: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

  const navigate = useNavigate();
  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.first_name.trim()) newErrors.first_name = "Имя обязательно";
    if (!formData.second_name.trim()) newErrors.second_name = "Фамилия обязательна";
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

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    try {
      const response = await axios.post("/auth/register", {
        first_name: formData.first_name,
        second_name: formData.second_name,
        last_name: formData.last_name,
        email: formData.email,
        password: formData.password,
      });

      navigate("/verificate", {
        state: {
          successMessage: "Регистрация прошла успешно! Проверьте почту для активации.",
        },
      });
    } catch (error) {
      let message = "Неизвестная ошибка";
      if (error.response?.data) {
        if (error.response.data.USER_EXIST_EXCEPTION) {
          message = error.response.data.USER_EXIST_EXCEPTION;
        } else if (error.response.data.message) {
          message = error.response.data.message;
        } else {
          message = JSON.stringify(error.response.data);
        }
      } else if (error.message) {
        message = error.message;
      }
      alert("Ошибка регистрации: " + message);
    }
  };

  return (
    <div className="register-wrapper">
      <div className="register-content">
        <div className="register-left">
          <form className="register-form" onSubmit={handleSubmit}>
            <h2 className="icon-registration">Регистрация</h2>

            <label>Имя</label>
            <input type="text" name="first_name" value={formData.first_name} onChange={handleChange} />
            {errors.first_name && <p className="error">{errors.first_name}</p>}

            <label>Фамилия</label>
            <input type="text" name="second_name" value={formData.second_name} onChange={handleChange} />
            {errors.second_name && <p className="error">{errors.second_name}</p>}

            <label>Отчество</label>
            <input type="text" name="last_name" value={formData.last_name} onChange={handleChange} />
            {errors.last_name && <p className="error">{errors.last_name}</p>}

            <label>Email</label>
            <input type="email" name="email" value={formData.email} onChange={handleChange} />
            {errors.email && <p className="error">{errors.email}</p>}

            <label>Пароль</label>
            <input type="password" name="password" value={formData.password} onChange={handleChange} />
            {errors.password && <p className="error">{errors.password}</p>}

            <label>Повторите пароль</label>
            <input
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
            />
            {errors.confirmPassword && <p className="error">{errors.confirmPassword}</p>}

            <button type="submit">Зарегистрироваться</button>
            {successMessage && <p className="success">{successMessage}</p>}
          </form>
        </div>
        <div className="register-image">
          <img src={Image} alt="Регистрация" />
        </div>
      </div>
    </div>
  );
}

export default RegisterPage;
