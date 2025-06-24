import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "../axiosConfig";
import "./LoginPage.css";
import Image from "/projectX.jpg";

function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const validate = () => {
    const newErrors = {};
    if (!email) newErrors.email = "Email обязателен";
    else if (!/\S+@\S+\.\S+/.test(email)) newErrors.email = "Некорректный email";
    if (!password) newErrors.password = "Пароль обязателен";
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setLoading(true);
    try {
      const response = await axios.post("/auth/login", { email, password });
      const token = response.data.token;
      if (!token) throw new Error("Токен не получен от сервера");

      localStorage.setItem("token", token);
      alert("Авторизация прошла успешно!");
      navigate("/profile");
    } catch (error) {
      console.error("Full error:", error);
  console.error("Response data:", error.response?.data);
  alert("Ошибка входа: " + (error.response?.data?.message || error.message));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-wrapper">
      <div className="login-content">
        <div className="login-left">
          <form className="login-form" onSubmit={handleSubmit}>
            <h2 className="icon-login">Вход в систему</h2>

            <label>Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              disabled={loading}
            />
            {errors.email && <p className="error">{errors.email}</p>}

            <label>Пароль</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              disabled={loading}
            />
            {errors.password && <p className="error">{errors.password}</p>}

            <button type="submit" disabled={loading}>
              {loading ? "Вход..." : "Войти"}
            </button>
          </form>
        </div>
        <div className="login-image">
          <img src={Image} alt="Вход" />
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
