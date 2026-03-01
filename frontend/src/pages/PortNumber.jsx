import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function PortNumber() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstname: "",
    lastname: "",
    mobileNumber: "",
    city: "",
    password: "",
    oldOperator: "",
    reason: "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await fetch("http://localhost:8080/api/user/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (res.ok) {
        alert("✅ Your number portability request has been submitted!");
        navigate("/login");
      } else {
        alert("❌ Failed to submit portability request.");
      }
    } catch (error) {
      console.error(error);
      alert("⚠️ Something went wrong!");
    }
  };

  return (
    <div className="form-page">
      <h2 className="form-title">Port Your Number</h2>

      <form onSubmit={handleSubmit} className="form-container">
        {/* First Name */}
        <div className="mb-3 w-100">
          <label className="form-label">First Name</label>
          <input
            type="text"
            name="firstname"
            className="form-control"
            value={formData.firstname}
            onChange={handleChange}
            required
          />
        </div>

        {/* Last Name */}
        <div className="mb-3 w-100">
          <label className="form-label">Last Name</label>
          <input
            type="text"
            name="lastname"
            className="form-control"
            value={formData.lastname}
            onChange={handleChange}
            required
          />
        </div>

        {/* Mobile Number */}
        <div className="mb-3 w-100">
          <label className="form-label">Mobile Number</label>
          <input
            type="text"
            name="mobileNumber"
            className="form-control"
            value={formData.mobileNumber}
            onChange={handleChange}
            required
          />
        </div>

        {/* City */}
        <div className="mb-3 w-100">
          <label className="form-label">City</label>
          <input
            type="text"
            name="city"
            className="form-control"
            value={formData.city}
            onChange={handleChange}
            required
          />
        </div>

        {/* Old Operator */}
        <div className="mb-3 w-100">
          <label className="form-label">Old Operator</label>
          <input
            type="text"
            name="oldOperator"
            className="form-control"
            value={formData.oldOperator}
            onChange={handleChange}
            required
          />
        </div>

        {/* Reason */}
        <div className="mb-3 w-100">
          <label className="form-label">Reason for Porting</label>
          <textarea
            name="reason"
            className="form-control"
            rows="3"
            value={formData.reason}
            onChange={handleChange}
            required
          />
        </div>

        {/* Password */}
        <div className="mb-3 w-100">
          <label className="form-label">Password</label>
          <input
            type="password"
            name="password"
            className="form-control"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>

        {/* Submit */}
        <button type="submit" className="submit-btn">
          Submit Port Request
        </button>
      </form>
    </div>
  );
}
