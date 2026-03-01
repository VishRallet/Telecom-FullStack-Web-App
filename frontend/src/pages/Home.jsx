// src/pages/Home.jsx
import { motion } from "framer-motion";
import { Link, useNavigate } from "react-router-dom";
import { FaWifi, FaMobileAlt, FaTv, FaLayerGroup } from "react-icons/fa";
import { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import logo from "../assets/logo.png";

export default function Home() {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [plans, setPlans] = useState([]);

  useEffect(() => {
    const user = localStorage.getItem("user");
    if (user) setIsLoggedIn(true);

    fetch("http://localhost:8080/api/plans")
      .then((res) => res.json())
      .then((data) => setPlans(data))
      .catch((err) => console.error(err));
  }, []);

  const handleRecharge = (planId) => {
    if (!isLoggedIn) {
      navigate("/login");
    } else {
      navigate(`/recharge/${planId}`);
    }
  };

  return (
    <div className="d-flex flex-column min-vh-100 bg-light text-dark">
     {/* Navbar */}
<nav
  className="navbar navbar-expand-lg navbar-dark fixed-top"
  style={{ background: "linear-gradient(90deg, #00bcd4, #e91e63)" }}
>
  <div className="container">
    <a className="navbar-brand fw-bold d-flex align-items-center" href="#">
<img src={logo} alt="Telecom Logo" height="51" className="me-2" />
      VN-SIM
    </a>
    <div className="collapse navbar-collapse">
      <ul className="navbar-nav ms-auto me-3">
        <li className="nav-item">
  <Link className="nav-link" to="/port">
    Port Your Number
  </Link>
</li>
        <li className="nav-item">
            <Link className="nav-link" to="/about">
            About Us
            </Link>
        </li>
        <li className="nav-item">
          <Link className="nav-link" to="/contact">
            Contact Us
          </Link>
        </li>
        <li className="nav-item">
          <Link className="nav-link" to="/admin">
            Business
          </Link>
        </li>
      </ul>
      <div>
        {isLoggedIn ? (
          <Link to="/dashboard" className="btn btn-light text-dark fw-semibold">
            My Account
          </Link>
        ) : (
          <>
            <Link to="/login" className="btn btn-outline-light me-2 fw-semibold">
              Login
            </Link>
            <Link to="/register" className="btn btn-dark fw-semibold">
              Register
            </Link>
          </>
        )}
      </div>
    </div>
  </div>
</nav>
x

      {/* Plans Section */}
      <section className="container py-5 text-center">
        <h2 className="fw-bold mb-4">Available Plans</h2>
        <div className="row g-4">
          {plans.map((plan) => (
            <motion.div
              key={plan.id}
              whileHover={{ scale: 1.05 }}
              className="col-md-4"
            >
              <div
                className="card shadow-sm h-100 border-0"
                style={{ borderRadius: "15px" }}
              >
                <div className="card-body">
                  <h5 className="card-title fw-bold">{plan.planName}</h5>
                  <p className="text-muted">{plan.planType}</p>
                  <p className="fs-5 fw-bold text-info">₹{plan.price}</p>
                  <p className="text-secondary">Validity: {plan.validity}</p>
                  <p className="text-secondary">Data: {plan.dataLimit}</p>
                  <p className="small">{plan.description}</p>
                  <button
                    onClick={() => handleRecharge(plan.id)}
                    className="btn w-100 mt-3 fw-semibold"
                    style={{ backgroundColor: "#00bcd4", color: "#fff" }}
                  >
                    Recharge
                  </button>
                </div>
              </div>
            </motion.div>
          ))}
        </div>
      </section>

      {/* Footer */}
      <footer
        className="text-center py-4 mt-auto"
        style={{ background: "linear-gradient(90deg, #00bcd4, #e91e63)", color: "#fff" }}
      >
        <p className="mb-0">
          © {new Date().getFullYear()} Veritel Telecom. All Rights Reserved.
        </p>
      </footer>
    </div>
  );
}

function ServiceCard({ icon, title }) {
  return (
    <motion.div whileHover={{ scale: 1.1 }} className="col-6 col-md-2">
      <div
        className="card border-0 shadow-sm h-100 text-center p-3"
        style={{ borderRadius: "15px" }}
      >
        <div className="text-info mb-2">{icon}</div>
        <p className="fw-semibold">{title}</p>
      </div>
    </motion.div>
  );
}
