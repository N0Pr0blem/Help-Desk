import React, { useEffect, useState } from "react";
import axios from "../axiosConfig";
import "./SysadminTasksPage.css";

function SysadminTasksPage() {
  const [availableTasks, setAvailableTasks] = useState([]);
  const [inProgressTasks, setInProgressTasks] = useState([]);
  const [finishedTasks, setFinishedTasks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const fetchTasks = async () => {
    setLoading(true);
    try {
      const [waitRes, progressRes, finishedRes] = await Promise.all([
        axios.get("/admin/tasks?status=WAIT"),
        axios.get("/admin/tasks?status=IN_PROGRESS"),
        axios.get("/admin/tasks?status=FINISHED"),
      ]);
      setAvailableTasks(waitRes.data);
      setInProgressTasks(progressRes.data);
      setFinishedTasks(finishedRes.data);
    } catch (err) {
      console.error(err);
      setError("Ошибка при загрузке задач");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const handleDeleteTask = async (id) => {
    if (!window.confirm("Удалить задачу?")) return;
    try {
      await axios.delete(`/admin/tasks/${id}`);
      fetchTasks();
    } catch (err) {
      alert("Не удалось удалить задачу");
      console.error(err);
    }
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return "Дата не указана";
    const correctedStr = dateStr.replace(" ", "T");
    const d = new Date(correctedStr);
    return isNaN(d) ? "Некорректная дата" : d.toLocaleString();
  };

  return (
    <div className="sysadmin-wrapper">
      <h2>Все заявки (администратор)</h2>
      {loading && <p>Загрузка задач...</p>}
      {error && <p className="error-message">{error}</p>}

      <div className="task-columns">
        <div className="task-column">
          <h3>Ожидают обработки</h3>
          {availableTasks.length === 0 ? (
            <p>Нет заявок</p>
          ) : (
            availableTasks.map((task) => (
              <div key={task.id} className="task-card">
                <p><strong>Заявка {task.id}</strong></p>
                <p>{task.description}</p>
                <p>Создана: {formatDate(task.created_at)}</p>
                <div className="task-actions">
                  <button onClick={() => handleDeleteTask(task.id)} className="delete-btn">Удалить</button>
                </div>
              </div>
            ))
          )}
        </div>

        <div className="task-column">
          <h3>В процессе</h3>
          {inProgressTasks.length === 0 ? (
            <p>Нет задач в работе</p>
          ) : (
            inProgressTasks.map((task) => (
              <div key={task.id} className="task-card">
                <p><strong>Заявка {task.id}</strong></p>
                <p>{task.description}</p>
                <p>Взята: {formatDate(task.taken_at)}</p>
                <div className="task-actions">
                  <button onClick={() => handleDeleteTask(task.id)} className="delete-btn">Удалить</button>
                </div>
              </div>
            ))
          )}
        </div>

        <div className="task-column">
          <h3>Завершённые</h3>
          {finishedTasks.length === 0 ? (
            <p>Нет завершённых заявок</p>
          ) : (
            finishedTasks.map((task) => (
              <div key={task.id} className="task-card">
                <p><strong>Заявка {task.id}</strong></p>
                <p>{task.description}</p>
                <p>Завершена: {formatDate(task.finished_at)}</p>
                <div className="task-actions">
                  <button onClick={() => handleDeleteTask(task.id)} className="delete-btn">Удалить</button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}

export default SysadminTasksPage;
