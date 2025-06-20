import { useState, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "./VerificationPage.css";

function VerificationPage() {
  const [code, setCode] = useState(["", "", "", "", "", ""]);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  const location = useLocation();
  const successMessage = location.state?.successMessage || "";

  const inputsRef = useRef([]);

  const handleChange = (value, index) => {
    if (!/^\d?$/.test(value)) return;
    const newCode = [...code];
    newCode[index] = value;
    setCode(newCode);
    if (value && index < 5) {
      inputsRef.current[index + 1].focus();
    }
  };

  const handleKeyDown = (e, index) => {
    if (e.key === "Backspace" && !code[index] && index > 0) {
      inputsRef.current[index - 1].focus();
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    const fullCode = code.join("");
    if (fullCode.length !== 6) {
      setError("Введите полный 6-значный код.");
      return;
    }

    try {
      const response = await fetch(`/api/v1/auth/activate?code=${fullCode}`, {
        method: "GET",
      });

      if (!response.ok) {
        throw new Error("Неверный код или ошибка сервера.");
      }

      setSuccess("Аккаунт успешно активирован!");
      setTimeout(() => navigate("/login"), 2000);
    } catch (err) {
      setError(err.message || "Ошибка при активации.");
    }
  };

  return (
    <div className="verification-container">
      <form className="verification-form" onSubmit={handleSubmit}>
        <h2>Подтверждение аккаунта</h2>

        {successMessage && <p className="successMessage">{successMessage}</p>}

        <label>Введите код из письма</label>
        <div className="code-inputs">
          {code.map((digit, idx) => (
            <input
              key={idx}
              type="text"
              inputMode="numeric"
              maxLength="1"
              value={digit}
              ref={(el) => (inputsRef.current[idx] = el)}
              onChange={(e) => handleChange(e.target.value, idx)}
              onKeyDown={(e) => handleKeyDown(e, idx)}
              className="code-input"
              required
            />
          ))}
        </div>

        <button type="submit">Активировать</button>

        {error && <p className="error">{error}</p>}
        {success && <p className="success">{success}</p>}
      </form>
    </div>
  );
}

export default VerificationPage;
