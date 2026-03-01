import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080/api/user", // Spring Boot backend
});

// Register user
export const registerUser = (userData) => API.post("/register", userData);

// Login user
export const loginUser = (loginData) => API.post("/login", loginData);
