// src/pages/ContactUs.jsx
import { FaPhoneAlt, FaEnvelope, FaMapMarkerAlt, FaClock, FaInstagram } from "react-icons/fa";
import { useState } from "react";

export default function ContactUs() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    message: "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    alert(`Thank you ${formData.name}, your message has been sent ✅`);
    setFormData({ name: "", email: "", message: "" }); // reset form
  };

  return (
    <div className="container py-5">
      {/* Heading */}
      <div className="text-center mb-5">
        <h2 className="fw-bold">Contact Us</h2>
        <p className="text-muted">
          Have questions or need help? We're here for you 24/7.
        </p>
      </div>

      <div className="row g-4">
        {/* Contact Info */}
        <div className="col-md-6">
          <div className="card shadow-sm border-0 p-4 h-100" style={{ borderRadius: "15px" }}>
            <h4 className="fw-bold mb-4">Get in Touch</h4>
            
            <p className="d-flex align-items-center mb-3">
              <FaPhoneAlt className="text-info me-3" /> 
              <span>+91 98765 43210</span>
            </p>

            <p className="d-flex align-items-center mb-3">
              <FaEnvelope className="text-info me-3" /> 
              <span>support@VN-SIM.com</span>
            </p>

            <p className="d-flex align-items-center mb-3">
              <FaMapMarkerAlt className="text-info me-3" />  
              <span>VN-SIM HQ, Hi-tech City, Hyderabad, India</span>
            </p>

            <p className="d-flex align-items-center mb-3">
              <FaClock className="text-info me-3" /> 
              <span>Available: Mon - Sun, 9:00 AM - 9:00 PM</span>
            </p>

            <p className="d-flex align-items-center">
              <FaInstagram className="text-danger me-3" /> 
              <a href="https://instagram.com/veritel" target="_blank" rel="noopener noreferrer" style={{ textDecoration: "none" }}>
                @veritel_official
              </a>
            </p>
          </div>
        </div>

        {/* Contact Form */}
        <div className="col-md-6">
          <div className="card shadow-sm border-0 p-4 h-100" style={{ borderRadius: "15px" }}>
            <h4 className="fw-bold mb-4">Send Us a Message</h4>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Your Name</label>
                <input 
                  type="text" 
                  name="name"
                  className="form-control" 
                  placeholder="Enter your name" 
                  value={formData.name}
                  onChange={handleChange}
                  required 
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Email</label>
                <input 
                  type="email" 
                  name="email"
                  className="form-control" 
                  placeholder="Enter your email"
                  value={formData.email}
                  onChange={handleChange}
                  required 
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Message</label>
                <textarea 
                  name="message"
                  className="form-control" 
                  rows="4" 
                  placeholder="Write your message"
                  value={formData.message}
                  onChange={handleChange}
                  required
                ></textarea>
              </div>
              <button 
                type="submit" 
                className="btn fw-semibold w-100" 
                style={{ backgroundColor: "#00bcd4", color: "#fff" }}
              >
                Send Message
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
