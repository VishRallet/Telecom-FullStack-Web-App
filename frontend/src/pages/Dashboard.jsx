import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
  const [user, setUser] = useState(null);
  const [recharges, setRecharges] = useState([]);
  const [plans, setPlans] = useState([]);
  const [complaints, setComplaints] = useState([]);
  const [newComplaint, setNewComplaint] = useState({ subject: "", description: "" });
  const [editProfile, setEditProfile] = useState(false);
  const [profileData, setProfileData] = useState({});
  const [message, setMessage] = useState(null);
  const [activeTab, setActiveTab] = useState('profile');
  const navigate = useNavigate();

  const showMessage = (text, type = 'info') => {
    setMessage({ text, type });
    setTimeout(() => setMessage(null), 3000);
  };

  useEffect(() => {
    const storedUser = JSON.parse(localStorage.getItem("user"));
    if (!storedUser) {
      navigate("/login");
      return;
    }
    setUser(storedUser);
    setProfileData(storedUser);

    // Fetch user's recharges
    axios.get(`http://localhost:8080/api/recharges/user/${storedUser.id}`)
      .then((res) => setRecharges(res.data));

    // Fetch all plans
    axios.get("http://localhost:8080/api/plans").then((res) => setPlans(res.data));

    // Fetch complaints
    axios.get(`http://localhost:8080/api/complaints/user/${storedUser.id}`)
      .then((res) => setComplaints(res.data));
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("user");
    navigate("/");
    showMessage("Logged out successfully.", 'success');
  };

  const handleComplaintSubmit = (e) => {
    e.preventDefault();
    if (!newComplaint.subject.trim() || !newComplaint.description.trim()) {
      showMessage("Please fill in both subject and description.", 'error');
      return;
    }

    axios
      .post(`http://localhost:8080/api/complaints/create/${user.id}`, newComplaint)
      .then((res) => {
        setComplaints([...complaints, res.data]);
        setNewComplaint({ subject: "", description: "" });
        showMessage("Complaint submitted successfully!", 'success');
      })
      .catch((err) => {
        console.error("Complaint submit failed:", err);
        showMessage("Failed to submit complaint. Please try again.", 'error');
      });
  };

  const parseAnyDate = (value) => {
    if (!value) return null;
    if (value instanceof Date) return value;
    if (typeof value === "string") {
      if (/^\d{4}-\d{2}-\d{2}(T.*)?$/.test(value)) return new Date(value);
      if (/^\d{2}-\d{2}-\d{4}$/.test(value)) {
        const [dd, mm, yyyy] = value.split("-").map(Number);
        return new Date(yyyy, mm - 1, dd);
      }
    }
    const d = new Date(value);
    return Number.isNaN(d.getTime()) ? null : d;
  };

  const parseValidityDays = (validity) => {
    if (!validity) return 0;
    const v = String(validity).toLowerCase().trim();
    if (v.includes("unlimited")) return 0;
    const n = parseInt(v.match(/\d+/)?.[0] ?? "0", 10);
    if (v.includes("year")) return n * 365;
    if (v.includes("month")) return n * 30;
    if (v.includes("day")) return n;
    return n;
  };

  const addDays = (date, days) => {
    if (!date || Number.isNaN(date.getTime()) || !days) return null;
    const d = new Date(date);
    d.setDate(d.getDate() + days);
    return d;
  };

  const formatDate = (date) => {
    if (!date || Number.isNaN(date.getTime())) return "—";
    return date.toLocaleDateString("en-IN", { day: "2-digit", month: "2-digit", year: "numeric" });
  };


  const handleProfileUpdate = (e) => {
    e.preventDefault();
    const payload = { ...profileData };
    if (!payload.password || payload.password.trim() === "") {
      delete payload.password;
    }

    axios.put(`http://localhost:8080/api/user/${user.id}`, payload)
      .then((res) => {
        setUser(res.data);
        localStorage.setItem("user", JSON.stringify(res.data));
        setEditProfile(false);
        showMessage("Profile updated successfully!", 'success');
      })
      .catch((err) => {
        console.error("Profile update failed:", err);
        showMessage("Failed to update profile", 'error');
      });
  };

  const renderContent = () => {
    switch (activeTab) {
      case 'profile':
        return (
          <>
            <div className="text-center mb-4">
              <h2 className="text-2xl font-bold">
                Welcome, {user.firstname} {user.lastname}
              </h2>
              <p><b>Mobile:</b> {user.mobileNumber}</p>
              <p><b>City:</b> {user.city}</p>
              <p><b>Activation Date:</b> {user.activationDate}</p>
            </div>
            <div className="flex justify-center gap-4">
              {!editProfile ? (
                <button
                  onClick={() => setEditProfile(true)}
                  className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                  Update Profile
                </button>
              ) : (
                <form onSubmit={handleProfileUpdate} className="space-y-3 mt-3 w-full max-w-lg">
                  <input type="text" value={profileData.firstname || ""} onChange={(e) => setProfileData({ ...profileData, firstname: e.target.value })} placeholder="First Name" className="w-full p-2 border rounded" required />
                  <input type="text" value={profileData.lastname || ""} onChange={(e) => setProfileData({ ...profileData, lastname: e.target.value })} placeholder="Last Name" className="w-full p-2 border rounded" required />
                  <input type="text" value={profileData.mobileNumber || ""} onChange={(e) => setProfileData({ ...profileData, mobileNumber: e.target.value })} placeholder="Mobile Number" className="w-full p-2 border rounded" required />
                  <input type="text" value={profileData.city || ""} onChange={(e) => setProfileData({ ...profileData, city: e.target.value })} placeholder="City" className="w-full p-2 border rounded" required />
                  <input type="password" value={profileData.password || ""} onChange={(e) => setProfileData({ ...profileData, password: e.target.value })} placeholder="New Password (optional)" className="w-full p-2 border rounded" />
                  <div className="flex gap-3">
                    <button type="submit" className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600">Save</button>
                    <button type="button" onClick={() => setEditProfile(false)} className="px-4 py-2 bg-gray-400 text-white rounded hover:bg-gray-500">Cancel</button>
                  </div>
                </form>
              )}
            </div>
          </>
        );
      case 'plans':
        return (
          <div className="p-4">
            <h3 className="text-lg font-semibold mb-2">Available Plans</h3>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {plans.map((plan) => (
                <div key={plan.id} className="p-4 bg-white shadow rounded">
                  <h4 className="font-bold">{plan.planName}</h4>
                  <p>₹{plan.price} | {plan.validity}</p>
                  <p>{plan.dataLimit}</p>
                  <p className="text-sm text-gray-600">{plan.description}</p>
                  <p className="text-sm text-gray-500 mt-2">{plan.planType}</p>
                  <button className="bg-green-500 text-white px-4 py-2 rounded-lg shadow hover:bg-green-600 transition" onClick={() => navigate(`/payment?planId=${plan.id}`)}>
                    Recharge
                  </button>
                </div>
              ))}
            </div>
          </div>
        );
      case 'recharges':
        return (
          <div className="p-4">
            <h3 className="text-lg font-semibold mb-2">Your Recharges</h3>
            {recharges.length > 0 ? (
              <table className="w-full border">
                <thead>
                  <tr className="bg-gray-200">
                    <th className="p-2 border text-left">Plan</th>
                    <th className="p-2 border text-left">Amount</th>
                    <th className="p-2 border text-left">Recharge Date</th>
                    <th className="p-2 border text-left">Expiry Date</th>
                    <th className="p-2 border text-left">Validity</th>
                  </tr>
                </thead>
                <tbody>
                  {recharges.map((r) => {
                    const rechargeDateObj = parseAnyDate(r.rechargeDate || r.recharge_date);
                    const validityStr = r?.plan?.validity ?? plans.find((p) => p.id === (r?.plan?.id || r?.planId))?.validity ?? "";
                    const validityDays = parseValidityDays(validityStr);
                    const expiryObj = validityDays > 0 ? addDays(rechargeDateObj, validityDays) : null;
                    return (
                      <tr key={r.id} className="border">
                        <td className="p-2 border">{r?.plan?.planName ?? "—"}</td>
                        <td className="p-2 border">₹{r?.plan?.price ?? "—"}</td>
                        <td className="p-2 border">{formatDate(rechargeDateObj)}</td>
                        <td className="p-2 border">{expiryObj ? formatDate(expiryObj) : "No expiry"}</td>
                        <td className="p-2 border">{validityStr || "—"}</td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            ) : (
              <p>No recharges yet.</p>
            )}
          </div>
        );
      case 'complaints':
        return (
          <div className="p-4">
            <h3 className="text-lg font-semibold mb-2">Raise a Complaint</h3>
            <form onSubmit={handleComplaintSubmit} className="space-y-3 mb-6">
              <input type="text" value={newComplaint.subject} onChange={(e) => setNewComplaint({ ...newComplaint, subject: e.target.value })} placeholder="Enter complaint subject" className="w-full p-2 border rounded" required />
              <textarea value={newComplaint.description} onChange={(e) => setNewComplaint({ ...newComplaint, description: e.target.value })} placeholder="Enter complaint details" className="w-full p-2 border rounded" rows="3" required></textarea>
              <button type="submit" className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Submit</button>
            </form>
            <h3 className="text-lg font-semibold mb-2">Your Complaints</h3>
            {complaints.length > 0 ? (
              <ul className="space-y-3">
                {complaints.map((c) => (
                  <li key={c.id} className="p-3 border rounded shadow-sm">
                    <p className="font-semibold text-blue-600">{c.subject}</p>
                    <p className="text-gray-700">{c.description}</p>
                    <p className="text-sm text-gray-500">Status: <span className="font-medium">{c.status}</span> | Created: {new Date(c.createdAt).toLocaleString()}</p>
                  </li>
                ))}
              </ul>
            ) : (
              <p className="text-gray-500">No complaints raised yet.</p>
            )}
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <div className="p-8">
      {message && (
        <div className={`fixed top-4 right-4 z-50 p-4 rounded-lg text-white font-semibold transition-all duration-300 transform ${message.type === 'success' ? 'bg-green-500' : 'bg-red-500'}`}>
          {message.text}
        </div>
      )}
      <div className="flex justify-end items-center mb-4">
        {/* Logout button on the right */}
        <button
          onClick={handleLogout}
          className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
        >
          Logout
        </button>
      </div>
      
      <div className="bg-gray-100 p-4 rounded-lg shadow min-h-[500px]">
        {user && (
          <>
            <div className="flex border-b border-gray-300 mb-4">
              <button
                onClick={() => setActiveTab('profile')}
                className={`px-4 py-2 -mb-px border-b-2 font-semibold ${activeTab === 'profile' ? 'border-blue-500 text-blue-600' : 'border-transparent text-black hover:text-gray-700'}`}
              >
                Profile
              </button>
              <button
                onClick={() => setActiveTab('plans')}
                className={`px-4 py-2 -mb-px border-b-2 font-semibold ${activeTab === 'plans' ? 'border-blue-500 text-blue-600' : 'border-transparent text-black hover:text-gray-700'}`}
              >
                Plans
              </button>
              <button
                onClick={() => setActiveTab('recharges')}
                className={`px-4 py-2 -mb-px border-b-2 font-semibold ${activeTab === 'recharges' ? 'border-blue-500 text-blue-600' : 'border-transparent text-black hover:text-gray-700'}`}
              >
                Recharges
              </button>
              <button
                onClick={() => setActiveTab('complaints')}
                className={`px-4 py-2 -mb-px border-b-2 font-semibold ${activeTab === 'complaints' ? 'border-blue-500 text-blue-600' : 'border-transparent text-black hover:text-gray-700'}`}
              >
                Complaints
              </button>
            </div>
            {renderContent()}
          </>
        )}
      </div>
    </div>
  );
}
