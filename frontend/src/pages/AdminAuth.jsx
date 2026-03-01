// src/pages/AdminAuth.jsx
import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

export default function AdminAuth() {
  const [activeTab, setActiveTab] = useState("login");
  
  // Login state
  const [loginData, setLoginData] = useState({
    mobileNumber: "",
    password: "",
  });

  // Register state
  const [registerData, setRegisterData] = useState({
    firstname: "",
    lastname: "",
    city: "",
    mobileNumber: "",
    password: "",
    adminKey: "",
  });

  const navigate = useNavigate();

const handleLoginChange = (e) => {
    setLoginData({ ...loginData, [e.target.name]: e.target.value });
  };

  const handleRegisterChange = (e) => {
    setRegisterData({ ...registerData, [e.target.name]: e.target.value });
  };

const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post("http://localhost:8080/api/admin/login", loginData);
      // Save admin info in localStorage
      localStorage.setItem("admin", JSON.stringify(res.data));
      alert("Login successful!");
      navigate("/admin/dashboard"); // <-- redirect to dashboard
    } catch (err) {
      alert("Login failed: " + err.response?.data || err.message);
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const { adminKey, ...rest } = registerData;
      const res = await axios.post(
        "http://localhost:8080/api/admin/create",
        rest,
        {
          headers: {
            "X-ADMIN-KEY": adminKey,
          },
        }
      );
      alert("Admin created: ");
      setRegisterData({
        firstname: "",
        lastname: "",
        city: "",
        mobileNumber: "",
        password: "",
        adminKey: "",
      });
      setActiveTab("login"); // switch to login after registration
    } catch (err) {
      alert("Registration failed: " + err.response?.data || err.message);
    }
  };

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card shadow-sm">
            <div className="card-header d-flex justify-content-around">
              <button
                className={`btn ${activeTab === "login" ? "btn-primary" : "btn-outline-primary"}`}
                onClick={() => setActiveTab("login")}
              >
                Login
              </button>
              <button
                className={`btn ${activeTab === "register" ? "btn-primary" : "btn-outline-primary"}`}
                onClick={() => setActiveTab("register")}
              >
                Register
              </button>
            </div>
            <div className="card-body">
              {activeTab === "login" && (
                <form onSubmit={handleLogin}>
                  <div className="mb-3">
                    <label className="form-label">Mobile Number</label>
                    <input
                      type="text"
                      className="form-control"
                      name="mobileNumber"
                      value={loginData.mobileNumber}
                      onChange={handleLoginChange}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Password</label>
                    <input
                      type="password"
                      className="form-control"
                      name="password"
                      value={loginData.password}
                      onChange={handleLoginChange}
                      required
                    />
                  </div>
                  <button type="submit" className="btn btn-primary w-100">
                    Login
                  </button>
                </form>
              )}

              {activeTab === "register" && (
                <form onSubmit={handleRegister}>
                  <div className="mb-3">
                    <label className="form-label">First Name</label>
                    <input
                      type="text"
                      className="form-control"
                      name="firstname"
                      value={registerData.firstname}
                      onChange={handleRegisterChange}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Last Name</label>
                    <input
                      type="text"
                      className="form-control"
                      name="lastname"
                      value={registerData.lastname}
                      onChange={handleRegisterChange}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">City</label>
                    <input
                      type="text"
                      className="form-control"
                      name="city"
                      value={registerData.city}
                      onChange={handleRegisterChange}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Mobile Number</label>
                    <input
                      type="text"
                      className="form-control"
                      name="mobileNumber"
                      value={registerData.mobileNumber}
                      onChange={handleRegisterChange}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Password</label>
                    <input
                      type="password"
                      className="form-control"
                      name="password"
                      value={registerData.password}
                      onChange={handleRegisterChange}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Admin Key</label>
                    <input
                      type="password"
                      className="form-control"
                      name="adminKey"
                      value={registerData.adminKey}
                      onChange={handleRegisterChange}
                      required
                    />
                  </div>
                  <button type="submit" className="btn btn-success w-100">
                    Register
                  </button>
                </form>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
