import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

export default function Register() {
  const [formData, setFormData] = useState({
    firstname: "",
    lastname: "",
    mobileNumber: "",
    city: "",
    password: "",
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post("http://localhost:8080/api/user/register", formData);
      localStorage.setItem("user", JSON.stringify(res.data));
      navigate("/dashboard");
    } catch (err) {
      alert("Registration failed: " + (err.response?.data || err.message));
    }
  };

  return (
    <div className="register-page">
      <form onSubmit={handleRegister} className="register-form">
        <h2 className="form-title">Register</h2>

        <input
          name="firstname"
          placeholder="First Name"
          onChange={handleChange}
          className="form-input"
          required
        />

        <input
          name="lastname"
          placeholder="Last Name"
          onChange={handleChange}
          className="form-input"
          required
        />

        <input
          name="mobileNumber"
          placeholder="Mobile Number"
          onChange={handleChange}
          className="form-input"
          required
        />

        <input
          name="city"
          placeholder="City"
          onChange={handleChange}
          className="form-input"
          required
        />

        <input
          name="password"
          type="password"
          placeholder="Password"
          onChange={handleChange}
          className="form-input"
          required
        />

        <button type="submit" className="submit-btn">
          Register
        </button>
      </form>
    </div>
  );
}
