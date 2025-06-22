import React, { useState, useEffect } from "react";
import axios from "axios";
import './AdminPanelPage.css';

function AdminPanelPage() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const [page, setPage] = useState(1);
  const size = 5;

  const [searchId, setSearchId] = useState("");
  const [searchEmail, setSearchEmail] = useState("");

  const fetchUsers = async () => {
    setLoading(true);
    setError("");

    try {
      if (searchId.trim()) {
        const response = await axios.get(`http://localhost:8080/api/v1/users/${searchId}`, {
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        });
        setUsers([response.data]);
      } else if (searchEmail.trim()) {
        const response = await axios.get(`http://localhost:8080/api/v1/users?email=${encodeURIComponent(searchEmail)}`, {
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        });
        setUsers(response.data);
      } else {
        const response = await axios.get(`http://localhost:8080/api/v1/users?page=${page}&size=${size}`, {
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        });
        setUsers(response.data);
      }
    } catch (err) {
      console.error(err);
      setError("Ошибка при загрузке пользователей");
      setUsers([]);
    }
    setLoading(false);
  };

  useEffect(() => {
    if (!searchId.trim() && !searchEmail.trim()) {
      fetchUsers();
    }
  }, [page]);

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(1);
    fetchUsers();
  };

  const handleReset = () => {
    setSearchId("");
    setSearchEmail("");
    setPage(1);
    fetchUsers();
  };

  const handleDelete = async (userId) => {
    if (!window.confirm("Вы уверены, что хотите удалить этого пользователя?")) return;

    try {
      await axios.delete(`http://localhost:8080/api/v1/users/${userId}`, {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });
      alert("Пользователь удалён");
      fetchUsers();
    } catch (err) {
      alert("Ошибка при удалении пользователя");
      console.error(err);
    }
  };

  return (
    <div className="admin-wrapper">
      <div className="admin-content">
        <h2>Панель администратора</h2>

        <div className="admin-actions">
          <button onClick={() => (window.location.href = "/admin/create-user")}>
            Создать пользователя
          </button>
        </div>

        <form className="admin-search" onSubmit={handleSearch}>
          <label>
            Поиск по ID:
            <input
              type="number"
              value={searchId}
              onChange={(e) => setSearchId(e.target.value)}
              disabled={!!searchEmail.trim()}
            />
          </label>

          <label>
            Поиск по Email:
            <input
              type="text"
              value={searchEmail}
              onChange={(e) => setSearchEmail(e.target.value)}
              disabled={!!searchId.trim()}
            />
          </label>

          <button type="submit">Искать</button>
          <button type="button" onClick={handleReset} style={{ marginLeft: 10 }}>
            Сбросить
          </button>
        </form>

        {loading && <p className="loading-message">Загрузка...</p>}
        {error && <p className="error-message">{error}</p>}
        {!loading && users.length === 0 && <p className="no-users-message">Пользователи не найдены</p>}

        {Array.isArray(users) && users.length > 0 && (
          <>
            <table className="admin-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Email</th>
                  <th>Роль</th>
                  <th>Действия</th>
                </tr>
              </thead>
              <tbody>
                {users.map((u) => (
                  <tr key={u.id ?? u.email}>
                    <td>{u.id}</td>
                    <td>{u.email}</td>
                    <td>{u.role}</td>
                    <td className="action-buttons">
                      <button
                        className="edit"
                        onClick={() => (window.location.href = `/admin/edit-user/${u.id}`)}
                      >
                        Изменить
                      </button>
                      <button
                        className="delete"
                        onClick={() => handleDelete(u.id)}
                      >
                        Удалить
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            {!searchId.trim() && !searchEmail.trim() && (
              <div className="pagination">
                <button onClick={() => setPage((p) => Math.max(p - 1, 1))} disabled={page === 1}>
                  Назад
                </button>
                <span>Страница: {page}</span>
                <button onClick={() => setPage((p) => p + 1)}>
                  Вперед
                </button>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default AdminPanelPage;
