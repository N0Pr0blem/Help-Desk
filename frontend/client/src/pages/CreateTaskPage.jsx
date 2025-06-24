import React, { useState } from "react";
import axios from "axios";
import "./CreateTaskPage.css";

const CreateTaskPage = () => {
  const [text, setText] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post("http://localhost:8080/api/v1/tasks",
        { description: text },
        {
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
          }
        }
      );

      console.log("Заявка отправлена:", response.data);
      alert("Заявка успешно отправлена!");

      // Сброс формы
      setText("");
    } catch (error) {
      console.error("Ошибка при отправке заявки:", error);
      alert("Не удалось отправить заявку. Попробуйте позже.");
    }
  };

  return (
    <div className="create-task-container">
      <h2>Оставить заявку</h2>
      <form onSubmit={handleSubmit}>
        <label>Описание проблемы</label>
        <textarea
          value={text}
          onChange={(e) => setText(e.target.value)}
          rows="5"
          placeholder="Опишите вашу проблему..."
          required
        />

        <button type="submit">Отправить</button>
      </form>
    </div>
  );
};

export default CreateTaskPage;
