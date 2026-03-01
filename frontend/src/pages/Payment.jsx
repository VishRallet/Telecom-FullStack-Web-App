import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";

const Payment = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [plan, setPlan] = useState(null);
  const [cardNumber, setCardNumber] = useState("");
  const [cvv, setCvv] = useState("");
  const [expiry, setExpiry] = useState("");
  const [loading, setLoading] = useState(false);

  const queryParams = new URLSearchParams(location.search);
  const planId = queryParams.get("planId");
  const user = JSON.parse(localStorage.getItem("user"));

  // Fetch plan details
  useEffect(() => {
    const fetchPlan = async () => {
      try {
        const res = await axios.get(`http://localhost:8080/api/plans/${planId}`);
        setPlan(res.data);
      } catch (err) {
        console.error("Error fetching plan", err);
      }
    };
    fetchPlan();
  }, [planId]);

  const handlePayment = async () => {
    if (!cardNumber || !cvv || !expiry) {
      alert("Please enter all payment details");
      return;
    }
    setLoading(true);
    try {
      await axios.post(`http://localhost:8080/api/recharges/create/${user.id}/${planId}`);
      alert("Payment successful! Recharge activated.");
      navigate("/dashboard");
    } catch (err) {
      console.error("Payment failed", err);
      alert("Payment failed, try again!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex justify-center items-center bg-gray-100 p-6">
      <div className="bg-white shadow-lg rounded-2xl p-8 w-full max-w-md">
        <h1 className="text-2xl font-bold text-center mb-6">Payment</h1>

        {plan ? (
          <>
            <div className="mb-4">
              <p><strong>Plan:</strong> {plan.planName}</p>
              <p><strong>Price:</strong> ₹{plan.price}</p>
              <p><strong>Validity:</strong> {plan.validity}</p>
              <p><strong>Data:</strong> {plan.dataLimit}</p>
              <p><strong>Benefits:</strong> {plan.description}</p>
              <p><strong>Plan Type:</strong> {plan.planType}</p>
            </div>

            <input
              type="text"
              placeholder="Card Number"
              value={cardNumber}
              onChange={(e) => setCardNumber(e.target.value)}
              className="w-full p-2 mb-3 border rounded-lg"
            />
            <input
              type="text"
              placeholder="Expiry (MM/YY)"
              value={expiry}
              onChange={(e) => setExpiry(e.target.value)}
              className="w-full p-2 mb-3 border rounded-lg"
            />
            <input
              type="password"
              placeholder="CVV"
              value={cvv}
              onChange={(e) => setCvv(e.target.value)}
              className="w-full p-2 mb-4 border rounded-lg"
            />

            <button
              onClick={handlePayment}
              disabled={loading}
              className="w-full bg-green-500 text-white py-2 rounded-lg hover:bg-green-600 transition disabled:bg-gray-400"
            >
              {loading ? "Processing..." : "Pay Now"}
            </button>
          </>
        ) : (
          <p>Loading plan details...</p>
        )}
      </div>
    </div>
  );
};

export default Payment;
