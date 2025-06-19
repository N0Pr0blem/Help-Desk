import React, { useState } from "react";
import "./CreateTaskPage.css";

const CreateTaskPage = () => {
  const [text, setText] = useState("");
  const [image, setImage] = useState(null);
  const [preview, setPreview] = useState(null);

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setImage(file);
    setPreview(URL.createObjectURL(file));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("text", text);
    if (image) formData.append("image", image);

    console.log("Отправка заявки:", text, image);
    // Здесь отправка formData на бэк
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
        />

        <label>Прикрепить изображение (необязательно)</label>
        <input type="file" accept="image/*" onChange={handleImageChange} />

        {preview && <img className="image-preview" src={preview} alt="Preview" />}

        <button type="submit">Отправить</button>
      </form>
    </div>
  );
};

export default CreateTaskPage;
