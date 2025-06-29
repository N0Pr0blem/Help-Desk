import axios from "axios";

// Базовый axios-инстанс
const instance = axios.create({
    baseURL: "http://localhost:8080/api/v1",
    withCredentials: true,
});

instance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  console.log("Токен в запросе:", token);
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default instance;
