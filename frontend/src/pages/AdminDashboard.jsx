// src/pages/AdminDashboard.jsx
import { useEffect, useState } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

export default function AdminDashboard() {
  const [activeTab, setActiveTab] = useState("users");
  const [admin, setAdmin] = useState(null);
  const [users, setUsers] = useState([]);
  const [plans, setPlans] = useState([]);
  const [recharges, setRecharges] = useState([]);
  const [complaints, setComplaints] = useState([]);

  const [newPlan, setNewPlan] = useState({
    planName: "",
    planType: "",
    price: 0,
    validity: "",
    dataLimit: "",
    description: "",
  });

  const [planToUpdate, setPlanToUpdate] = useState(null);
  const [complaintUpdate, setComplaintUpdate] = useState({ id: null, status: "" });

  const API_BASE = "http://localhost:8080/api";

  useEffect(() => {
    const adminData = localStorage.getItem("admin");
    if (adminData) {
      setAdmin(JSON.parse(adminData));
    } else {
      // if not logged in, redirect to login
      window.location.href = "/admin";
    }
  }, []);

  // Fetch all data based on tab
  useEffect(() => {
    if (activeTab === "users") fetchUsers();
    else if (activeTab === "plans") fetchPlans();
    else if (activeTab === "recharges") fetchAllRecharges();
    else if (activeTab === "complaints") fetchComplaints();
  }, [activeTab]);

  // ------------------- USERS -------------------
  const fetchUsers = async () => {
    try {
      const res = await axios.get(`${API_BASE}/user/all`);
      setUsers(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  // ------------------- PLANS -------------------
  const fetchPlans = async () => {
    try {
      const res = await axios.get(`${API_BASE}/plans`);
      setPlans(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const handlePlanChange = (e) => {
    setNewPlan({ ...newPlan, [e.target.name]: e.target.value });
  };

  const addPlan = async () => {
    try {
      await axios.post(`${API_BASE}/plans`, newPlan);
      setNewPlan({ planName: "", planType: "", price: 0, validity: "", dataLimit: "", description: "" });
      fetchPlans();
    } catch (err) {
      alert("Error adding plan: " + err.message);
    }
  };

  const updatePlan = async () => {
    try {
      await axios.put(`${API_BASE}/plans/${planToUpdate.id}`, planToUpdate);
      setPlanToUpdate(null);
      fetchPlans();
    } catch (err) {
      alert("Error updating plan: " + err.message);
    }
  };

  const deletePlan = async (id) => {
    try {
      await axios.delete(`${API_BASE}/plans/${id}`);
      fetchPlans();
    } catch (err) {
      alert("Error deleting plan: " + err.message);
    }
  };

  // ------------------- RECHARGES -------------------
  const fetchAllRecharges = async () => {
    try {
      const res = await axios.get(`${API_BASE}/recharges/all`);
      setRecharges(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchRechargesByUser = async (userId) => {
    try {
      const res = await axios.get(`${API_BASE}/recharges/user/${userId}`);
      setRecharges(res.data);
    } catch (err) {
      alert("Error fetching recharges by user: " + err.message);
    }
  };

  const fetchRechargesByPlan = async (planId) => {
    try {
      const res = await axios.get(`${API_BASE}/recharges/plan/${planId}`);
      setRecharges(res.data);
    } catch (err) {
      alert("Error fetching recharges by plan: " + err.message);
    }
  };

  // ------------------- COMPLAINTS -------------------
  const fetchComplaints = async () => {
    try {
      const res = await axios.get(`${API_BASE}/complaints/all`);
      setComplaints(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const updateComplaintStatus = async () => {
    try {
      await axios.put(`${API_BASE}/complaints/updateStatus/${complaintUpdate.id}`, {
        status: complaintUpdate.status,
      });
      setComplaintUpdate({ id: null, status: "" });
      fetchComplaints();
    } catch (err) {
      alert("Error updating complaint: " + err.message);
    }
  };

  const handleLogout = () => {
    localStorage.clear();
    window.location.href = "/";
  };

  return (
    <div className="container py-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="text-center">
          Admin Dashboard {admin ? `- Welcome, ${admin.firstname}` : ""}
        </h2>
        <button className="btn btn-danger" onClick={handleLogout}>
          Logout
        </button>
      </div>

      {/* Tabs */}
      <ul className="nav nav-tabs mb-4">
        <li className="nav-item">
          <button className={`nav-link ${activeTab === "users" ? "active" : ""}`} onClick={() => setActiveTab("users")}>
            Users
          </button>
        </li>
        <li className="nav-item">
          <button className={`nav-link ${activeTab === "plans" ? "active" : ""}`} onClick={() => setActiveTab("plans")}>
            Plans
          </button>
        </li>
        <li className="nav-item">
          <button className={`nav-link ${activeTab === "recharges" ? "active" : ""}`} onClick={() => setActiveTab("recharges")}>
            Recharges
          </button>
        </li>
        <li className="nav-item">
          <button className={`nav-link ${activeTab === "complaints" ? "active" : ""}`} onClick={() => setActiveTab("complaints")}>
            Complaints
          </button>
        </li>
      </ul>

      {/* ------------------- USERS TAB ------------------- */}
      {activeTab === "users" && (
        <div>
          <h4>All Users</h4>
          <table className="table table-bordered table-hover">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Mobile</th>
                <th>City</th>
                <th>Role</th>
                <th>Activation Date</th>
              </tr>
            </thead>
            <tbody>
              {users.map((u) => (
                <tr key={u.id}>
                  <td>{u.id}</td>
                  <td>{u.firstname} {u.lastname}</td>
                  <td>{u.mobileNumber}</td>
                  <td>{u.city}</td>
                  <td>{u.role}</td>
                  <td>{u.activationDate}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* ------------------- PLANS TAB ------------------- */}
      {activeTab === "plans" && (
        <div>
          <h4>Manage Plans</h4>
          {/* Add / Update Plan Form */}
          <div className="mb-4">
            <h5>{planToUpdate ? "Update Plan" : "Add New Plan"}</h5>
            <div className="row g-2">
              {["planName","planType","price","validity","dataLimit","description"].map((field) => (
                <div className="col-md-4 mb-2" key={field}>
                  <input
                    type={field === "price" ? "number" : "text"}
                    className="form-control"
                    placeholder={field}
                    name={field}
                    value={planToUpdate ? planToUpdate[field] : newPlan[field]}
                    onChange={(e) =>
                      planToUpdate
                        ? setPlanToUpdate({ ...planToUpdate, [field]: e.target.value })
                        : handlePlanChange(e)
                    }
                  />
                </div>
              ))}
            </div>
            <button className="btn btn-success mt-2 me-2" onClick={planToUpdate ? updatePlan : addPlan}>
              {planToUpdate ? "Update Plan" : "Add Plan"}
            </button>
            {planToUpdate && (
              <button className="btn btn-secondary mt-2" onClick={() => setPlanToUpdate(null)}>
                Cancel
              </button>
            )}
          </div>

          {/* Plans Table */}
          <table className="table table-bordered table-hover">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Type</th>
                <th>Price</th>
                <th>Validity</th>
                <th>Data</th>
                <th>Description</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {plans.map((p) => (
                <tr key={p.id}>
                  <td>{p.id}</td>
                  <td>{p.planName}</td>
                  <td>{p.planType}</td>
                  <td>{p.price}</td>
                  <td>{p.validity}</td>
                  <td>{p.dataLimit}</td>
                  <td>{p.description}</td>
                  <td>
                    <button className="btn btn-primary btn-sm me-2" onClick={() => setPlanToUpdate(p)}>
                      Edit
                    </button>
                    <button className="btn btn-danger btn-sm" onClick={() => deletePlan(p.id)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* ------------------- RECHARGES TAB ------------------- */}
      {activeTab === "recharges" && (
        <div>
          <h4>Recharges</h4>
          <table className="table table-bordered table-hover">
            <thead>
              <tr>
                <th>ID</th>
                <th>User ID</th>
                <th>Plan ID</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {recharges.map((r) => (
                <tr key={r.id}>
                  <td>{r.id}</td>
                  <td>{r.user.id}</td>
                  <td>{r.plan.id}</td>
                  <td>{new Date(r.rechargeDate).toLocaleDateString()}</td> {/* Only date */}
                </tr>
              ))}
            </tbody>
          </table>

          {/* Optional: Filter by user or plan */}
          <div className="mt-3">
            <input type="number" placeholder="User ID" className="me-2" id="userIdInput" />
            <button
              className="btn btn-primary me-2"
              onClick={() => fetchRechargesByUser(document.getElementById("userIdInput").value)}
            >
              By User
            </button>
            <input type="number" placeholder="Plan ID" className="me-2" id="planIdInput" />
            <button
              className="btn btn-primary"
              onClick={() => fetchRechargesByPlan(document.getElementById("planIdInput").value)}
            >
              By Plan
            </button>
          </div>
        </div>
      )}

      {/* ------------------- COMPLAINTS TAB ------------------- */}
      {activeTab === "complaints" && (
        <div>
          <h4>Complaints</h4>
          <table className="table table-bordered table-hover">
            <thead>
              <tr>
                <th>ID</th>
                <th>User ID</th>
                <th>Title</th>
                <th>Description</th>
                <th>Created At</th>
                <th>Updated At</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {complaints.map((c) => (
                <tr key={c.id}>
                  <td>{c.id}</td>
                  {/* Using optional chaining to safely access nested properties */}
                  <td>{c.user?.id}</td>
                  <td>{c.subject}</td>
                  <td>{c.description}</td>
                  <td>{new Date(c.createdAt).toLocaleDateString()}</td> {/* Only date */}
                  <td>{new Date(c.updatedAt).toLocaleDateString()}</td> {/* Only date */}
                  <td>{c.status}</td>
                </tr>
              ))}
            </tbody>
          </table>

          {/* Update Complaint Status */}
          <div className="mt-3">
            <input
              type="number"
              placeholder="Complaint ID"
              className="me-2"
              value={complaintUpdate.id || ""}
              onChange={(e) => setComplaintUpdate({ ...complaintUpdate, id: e.target.value })}
            />
            <select
              className="me-2"
              value={complaintUpdate.status}
              onChange={(e) => setComplaintUpdate({ ...complaintUpdate, status: e.target.value })}
            >
              <option value="">Select Status</option>
              <option value="Pending">Pending</option>
              <option value="In Progress">In Progress</option>
              <option value="Resolved">Resolved</option>
            </select>
            <button className="btn btn-primary" onClick={updateComplaintStatus}>
              Update Status
            </button>
          </div>
        </div>
      )}
    </div>
  );
}