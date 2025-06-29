import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "../axiosConfig";
import "./LoginPage.css";
import Image from "/loginphoto.webp";
import Toast from "../components/Toast.jsx"; 

function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [toastMessage, setToastMessage] = useState("");
  const [toastKey, setToastKey] = useState(0);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const showToast = (msg) => {
    setToastMessage(msg);
    setToastKey(k => k + 1);
  };

  const validate = () => {
    const newErrors = {};
    if (!email) newErrors.email = "Email обязателен";
    else if (!/\S+@\S+\.\S+/.test(email)) newErrors.email = "Некорректный email";

    if (!password) newErrors.password = "Пароль обязателен";
    else if (password.length < 8) newErrors.password = "Пароль минимум 8 символов";

    if (Object.keys(newErrors).length > 0) {
      const firstError = Object.values(newErrors)[0];
      showToast(firstError);
      return false;
    }

    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setLoading(true);
    try {
      const response = await axios.post("/auth/login", { email, password });
      const { token, role } = response.data;

      if (!token || !role) throw new Error("Данные авторизации не получены");

      localStorage.setItem("token", token);
      localStorage.setItem("role", role);

      navigate("/profile");
    } catch (error) {
      console.error("Full error:", error);

      const errorKey = Object.keys(error.response?.data || {})[0];
      const errorMessages = {
        INVALID_PASSWORD: "Неверный пароль",
        NO_SUCH_USER_EXCEPTION: "Пользователь с таким email не найден",
      };

      const message = errorMessages[errorKey] || "Неизвестная ошибка авторизации";
      showToast("Ошибка входа: " + message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-wrapper">
      <Toast key={toastKey} message={toastMessage} onClose={() => setToastMessage("")} />

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

            <label>Пароль</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              disabled={loading}
            />

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
