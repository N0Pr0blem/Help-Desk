import React, { useEffect, useState } from "react";
import axios from "../axiosConfig";
import "./SysadminTasksPage.css";

function SysadminSoloPage() {
  const [availableTasks, setAvailableTasks] = useState([]);
  const [myTasks, setMyTasks] = useState([]);
  const [finishedTasks, setFinishedTasks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const fetchTasks = async () => {
    setLoading(true);
    try {
      const [availableRes, myRes, finishedRes] = await Promise.all([
        axios.get("/admin/tasks?status=WAIT"),
        axios.get("/admin/tasks/my&status=IN_PROGRESS"),
        axios.get("/admin/tasks/my?status=FINISHED"), 
      ]);
      setAvailableTasks(availableRes.data);
      setMyTasks(myRes.data);
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

  const handleTakeTask = async (id) => {
    try {
      await axios.post(`/admin/tasks/take/${id}`);
      fetchTasks();
    } catch (err) {
      alert("Не удалось взять задачу");
      console.error(err);
    }
  };

  const handleFinishTask = async (id) => {
    try {
      await axios.post(`/admin/tasks/finish/${id}`);
      fetchTasks();
    } catch (err) {
      alert("Не удалось завершить задачу");
      console.error(err);
    }
  };

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
      <h2>Панель системного администратора</h2>
      {loading && <p>Загрузка задач...</p>}
      {error && <p className="error-message">{error}</p>}

      <div className="task-columns">
        <div className="task-column">
          <h3>Доступные заявки</h3>
          {availableTasks.length === 0 ? (
            <p>Нет заявок</p>
          ) : (
            availableTasks.map((task) => (
              <div key={task.id} className="task-card">
                <p><strong>Заявка #{task.id}</strong></p>
                <p>{task.description}</p>
                <div className="task-actions">
                  <button onClick={() => handleTakeTask(task.id)}>Взять</button>
                  <button onClick={() => handleDeleteTask(task.id)} className="delete-btn">Удалить</button>
                </div>
              </div>
            ))
          )}
        </div>

        <div className="task-column">
          <h3>Мои заявки</h3>
          {myTasks.length === 0 ? (
            <p>Вы ещё не брали заявки</p>
          ) : (
            myTasks.map((task) => (
              <div key={task.id} className="task-card">
                <p><strong>Заявка #{task.id}</strong></p>
                <p>{task.description}</p>
                <p>Взята: {formatDate(task.created_at)}</p>
                <div className="task-actions">
                  <button onClick={() => handleFinishTask(task.id)}>Завершить</button>
                </div>
              </div>
            ))
          )}
        </div>

        <div className="task-column">
          <h3>Завершённые заявки</h3>
          {finishedTasks.length === 0 ? (
            <p>Завершённых заявок нет</p>
          ) : (
            finishedTasks.map((task) => (
              <div key={task.id} className="task-card">
                <p><strong>Заявка #{task.id}</strong></p>
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

export default SysadminSoloPage;
