import React, { useState, useEffect } from "react";
import axios from "../axiosConfig";
import "./AdminPanelPage.css";

function AdminPanelPage() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [page, setPage] = useState(0);
  const size = 5;
  const [hasMore, setHasMore] = useState(true);
  const [searchId, setSearchId] = useState("");
  const [searchEmail, setSearchEmail] = useState("");
  const [isSearchActive, setIsSearchActive] = useState(false);

  const fetchUsers = async (customPage = page, resetSearch = false) => {
    setLoading(true);
    setError("");

    try {
      let response;

      if (searchId.trim() && !resetSearch) {
        try {
          response = await axios.get(`/users/${searchId}`);
          setUsers(response.data ? [response.data] : []);
          setHasMore(false);
          setIsSearchActive(true);
        } catch {
          setUsers([]);
          setHasMore(false);
        }
      } else if (searchEmail.trim() && !resetSearch) {
        try {
          response = await axios.get(`/users?email=${encodeURIComponent(searchEmail)}`);
          const result = Array.isArray(response.data) ? response.data : [response.data];
          setUsers(result || []);
          setHasMore(false);
          setIsSearchActive(true);
        } catch {
          setUsers([]);
          setHasMore(false);
        }
      } else {
        await fetchAll(customPage);
        setIsSearchActive(false);
      }
    } catch (err) {
      console.error(err);
      setError("Ошибка при загрузке пользователей");
      setUsers([]);
    }

    setLoading(false);
  };

  const fetchAll = async (customPage) => {
    try {
      const res = await axios.get(`/users?page=${customPage}&size=${size}`);
      const fetchedUsers = res.data;
  
      if (fetchedUsers.length === 0 && customPage > 0) {
        setPage((prev) => Math.max(prev - 1, 0));
      } else {
        setUsers(fetchedUsers);
        setHasMore(fetchedUsers.length === size);
      }
    } catch (err) {
      console.error(err);
      setUsers([]);
      setError("Ошибка при загрузке пользователей");
    }
  };
  

  useEffect(() => {
    fetchUsers();
  }, [page]);

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(0);
    fetchUsers(0);
  };

  const handleReset = async () => {
    setSearchId("");
    setSearchEmail("");
    setPage(0);
    await fetchUsers(0, true);
  };

  const handleDelete = async (userId) => {
    if (!window.confirm("Вы уверены, что хотите удалить этого пользователя?")) return;

    try {
      await axios.delete(`/users/${userId}`);
      alert("Пользователь удалён");
      fetchUsers();
    } catch (err) {
      alert("Ошибка при удалении пользователя");
      console.error(err);
    }
  };

  const handleToggleActive = async (userId, currentActiveStatus) => {
    const newActiveStatus = !currentActiveStatus;
    const action = newActiveStatus ? 'активировать' : 'деактивировать';

    if (!window.confirm(`Вы уверены, что хотите ${action} этого пользователя?`)) return;

    try {
      await axios.patch(`/users/active/${userId}?isActive=${newActiveStatus}`);
      alert(`Пользователь успешно ${action === 'активировать' ? 'активирован' : 'деактивирован'}`);
      setUsers(users.map(user =>
        user.id === userId ? { ...user, active: newActiveStatus } : user
      ));
    } catch (err) {
      alert("Ошибка при изменении статуса пользователя");
      console.error(err);
    }
  };

  return (
    <div className="admin-wrapper">
      <div className="admin-content">
        <h2>Панель администратора</h2>

        <div className="admin-actions">
          <button
            className="create-user-btn"
            onClick={() => (window.location.href = "/admin/create-user")}
          >
            Создать пользователя
          </button>
        </div>

        <form className="admin-search" onSubmit={handleSearch}>
          <div className="search-group">
            <label>
              Поиск по ID:
              <input
                type="number"
                value={searchId}
                onChange={(e) => setSearchId(e.target.value)}
                disabled={!!searchEmail.trim()}
                placeholder="Введите ID"
              />
            </label>
          </div>

          <div className="search-group">
            <label>
              Поиск по Email:
              <input
                type="text"
                value={searchEmail}
                onChange={(e) => setSearchEmail(e.target.value)}
                disabled={!!searchId.trim()}
                placeholder="Введите email"
              />
            </label>
          </div>

          <div className="search-buttons">
            <button type="submit" className="search-btn">
              Искать
            </button>
            <button
              type="button"
              onClick={handleReset}
              className="reset-btn"
            >
              Сбросить
            </button>
          </div>
        </form>

        {loading && <div className="status-message loading-message">Загрузка...</div>}
        {error && <div className="status-message error-message">{error}</div>}
        {!loading && users.length === 0 && (
          <div className="status-message no-users-message">Пользователи не найдены</div>
        )}

        {users.length > 0 && (
          <>
            <div className="table-container">
              <table className="admin-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Email</th>
                    <th>Роль</th>
                    <th>Активен</th>
                    <th>Действия</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map((user) => (
                    <tr key={user.id ?? user.email}>
                      <td>{user.id}</td>
                      <td>{user.email}</td>
                      <td>{user.role}</td>
                      <td className={user.active ? "active" : "inactive"}>
                        {user.active ? "Да" : "Нет"}
                      </td>
                      <td className="action-buttons">
                        {user.role !== "ADMIN" ? (
                          <>
                            <button
                              className="action-btn edit-btn"
                              onClick={() =>
                                (window.location.href = `/admin/edit-user/${user.id}`)
                              }
                            >
                              Изменить
                            </button>
                            <button
                              className={`action-btn toggle-btn ${
                                user.active ? "deactivate" : "activate"
                              }`}
                              onClick={() => handleToggleActive(user.id, user.active)}
                            >
                              {user.active ? "Деактивировать" : "Активировать"}
                            </button>
                            <button
                              className="action-btn delete-btn"
                              onClick={() => handleDelete(user.id)}
                            >
                              Удалить
                            </button>
                          </>
                        ) : (
                          <span className="disabled-actions">—</span>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {!isSearchActive && (
              <div className="pagination">
                <button
                  className="pagination-btn"
                  onClick={() => setPage((p) => Math.max(p - 1, 0))}
                  disabled={page === 0}
                >
                  Назад
                </button>
                <span className="page-number">Страница: {page + 1}</span>
                <button
                  className="pagination-btn"
                  onClick={() => hasMore && setPage((p) => p + 1)}
                  disabled={!hasMore}
                >
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
