import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

export default function Login() {
  const [mobileNumber, setMobileNumber] = useState("");
  const [password, setPassword] = useState("");
  const [otp, setOtp] = useState("");
  const [otpSent, setOtpSent] = useState(false);
  const [loginMethod, setLoginMethod] = useState("password"); // "password" or "otp"

  const navigate = useNavigate();

  // Handle password login
  const handlePasswordLogin = (e) => {
    e.preventDefault();
    axios
      .post("http://localhost:8080/api/user/login", { mobileNumber, password })
      .then((res) => {
        localStorage.setItem("user", JSON.stringify(res.data));
        console.log(res.data);
        navigate("/dashboard");
      })
      .catch(() => alert("Invalid mobile number or password"));
  };

  return (
    <div
      className="d-flex justify-content-center align-items-center vh-100"
      style={{
        background: "linear-gradient(135deg, #00c6ff, #ff0080)",
      }}
    >
      <div
        className="card shadow-lg p-4"
        style={{
          width: "380px",
          borderRadius: "20px",
          background: "white",
        }}
      >
        <h2 className="text-center mb-4 fw-bold" style={{ color: "#ff0080" }}>
          Login
        </h2>

        {/* Password Login Form */}
        {loginMethod === "password" && (
          <form onSubmit={handlePasswordLogin}>
            <div className="mb-3">
              <label className="form-label fw-semibold">Mobile Number</label>
              <input
                type="text"
                value={mobileNumber}
                onChange={(e) => setMobileNumber(e.target.value)}
                className="form-control"
                placeholder="Enter your mobile number"
                required
              />
            </div>

            <div className="mb-4">
              <label className="form-label fw-semibold">Password</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="form-control"
                placeholder="Enter your password"
                required
              />
            </div>

            <button
              type="submit"
              className="btn w-100 text-white fw-bold"
              style={{
                background: "linear-gradient(135deg, #00c6ff, #ff0080)",
                border: "none",
              }}
            >
              Login
            </button>
          </form>
        )}

        <p className="text-center mt-3 text-muted" style={{ fontSize: "14px" }}>
          Don’t have an account?{" "}
          <a href="/register" style={{ color: "#00c6ff", fontWeight: "500" }}>
            Register here
          </a>
        </p>
      </div>
    </div>
  );
}
