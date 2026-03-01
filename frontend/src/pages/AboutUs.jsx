// src/pages/AboutUs.jsx
import { FaUsers, FaSignal, FaHeadset, FaLightbulb } from "react-icons/fa";

export default function AboutUs() {
  return (
    <div className="bg-light text-dark">
      {/* Hero Section */}
      <section
        className="text-center text-white py-5"
        style={{ background: "linear-gradient(90deg, #00bcd4, #e91e63)" }}
      >
        <h1 className="fw-bold">About VN-SIM</h1>
        <p className="lead">Connecting people with seamless telecom solutions</p>
      </section>

      {/* Story */}
      <section className="container py-5">
        <h2 className="fw-bold mb-3">Our Story</h2>
        <p>
          Founded in 2020, <strong>VN-SIM</strong> was built with one mission:
          to provide reliable, affordable, and innovative telecom services.
          Whether it’s mobile, broadband, or digital solutions, we believe
          connectivity is the backbone of progress.
        </p>
        <p>
          Today, VN-SIM serves thousands of happy customers across the country
          with unmatched service quality and customer support.
        </p>
      </section>

      {/* Core Values */}
      <section className="container py-5 text-center">
        <h2 className="fw-bold mb-4">Why Choose Us</h2>
        <div className="row g-4">
          <div className="col-md-3">
            <FaSignal size={40} className="text-info mb-3" />
            <h5>Reliable Connectivity</h5>
            <p className="text-muted">99.9% network uptime</p>
          </div>
          <div className="col-md-3">
            <FaUsers size={40} className="text-info mb-3" />
            <h5>1M+ Customers</h5>
            <p className="text-muted">Trusted by millions</p>
          </div>
          <div className="col-md-3">
            <FaHeadset size={40} className="text-info mb-3" />
            <h5>24/7 Support</h5>
            <p className="text-muted">Always here for you</p>
          </div>
          <div className="col-md-3">
            <FaLightbulb size={40} className="text-info mb-3" />
            <h5>Innovation</h5>
            <p className="text-muted">Smart solutions for tomorrow</p>
          </div>
        </div>
      </section>

      {/* CTA */}
      <section
        className="text-center text-white py-5"
        style={{ background: "linear-gradient(90deg, #00bcd4, #e91e63)" }}
      >
        <h2 className="fw-bold">Join the VN-SIM Family Today</h2>
        <p className="mb-4">Experience connectivity like never before.</p>
        <a href="/" className="btn btn-light fw-semibold">
          Explore Plans
        </a>
      </section>
    </div>
  );
}
