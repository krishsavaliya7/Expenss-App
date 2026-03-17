# 💸 SplitSmart — Group Expense Management & Settlement Platform

> Split bills. Settle smart. Track every rupee.

SplitSmart is a modern **group expense management platform** built with Flask and SQLite. It features UPI-style payment simulation, intelligent debt minimization using a greedy algorithm, and a blockchain-inspired immutable ledger for tamper-proof financial records.

---

## ✨ Features

- 👥 **Group Expense Management** — Create groups, add friends, split bills equally, by percentage, or exact amount
- 🧠 **Greedy Debt Minimization** — Reduces the number of settlements needed using an optimized algorithm
- 💸 **UPI-Style Payment Simulation** — Simulate instant payments without real banking APIs
- 💵 **Cash Settlement with Approval** — Offline payments with receiver confirmation flow
- 🔗 **Immutable Ledger** — SHA256 hash-chain ensures every transaction is tamper-proof
- 🔔 **Notification System** — Real-time alerts for expenses, settlements, and friend requests
- 🔐 **Secure Auth** — Session-based login with CSRF protection and bcrypt password hashing
- 📱 **Responsive UI** — Mobile-friendly design for all screen sizes
- 🤝 **Friend System** — Send/accept friend requests, search users, manage connections

---

## 🏗️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Python 3.11 + Flask 2.3 |
| Database | SQLite (via raw SQL, no ORM) |
| Auth | Flask Sessions + Werkzeug/bcrypt |
| Security | SHA256 ledger hashing, CSRF tokens |
| Frontend | Jinja2 Templates + Vanilla JS + CSS |
| Server | Gunicorn + PythonAnywhere |

---

## 📂 Project Structure

```
SplitSmart/
├── app.py                  # Main Flask backend (~2900 lines)
├── expense_tracker.py      # Standalone greedy algorithm demo
├── validation.py           # Server-side input validation module
├── wsgi.py                 # WSGI entry point for production
├── requirements.txt        # Python dependencies
├── runtime.txt             # Python version spec
├── Procfile                # Process config
│
├── templates/              # Jinja2 HTML templates
│   ├── base.html
│   ├── dashboard.html
│   ├── group_detail.html
│   ├── ledger.html
│   ├── friends.html
│   └── ...
│
├── static/
│   ├── css/main.css
│   └── js/
│       ├── main.js
│       ├── auth.js
│       ├── friends.js
│       ├── ledger.js
│       └── validation.js
│
└── uploads/                # User profile pictures (gitignored)
    └── .gitkeep
```

---

## 🚀 Local Setup

### 1. Clone the repo
```bash
git clone https://github.com/krishsavaliya7/Expenss-App.git
cd Expenss-App
```

### 2. Create a virtual environment
```bash
python -m venv venv

# Windows
venv\Scripts\activate

# Linux / Mac
source venv/bin/activate
```

### 3. Install dependencies
```bash
pip install -r requirements.txt
```

### 4. Set environment variable
```bash
# Windows PowerShell
$env:SECRET_KEY = "your-random-secret-key-here"

# Linux / Mac
export SECRET_KEY="your-random-secret-key-here"
```

### 5. Run the app
```bash
python app.py
```

Open `http://localhost:5000` in your browser.

---

## ☁️ Production Deployment (PythonAnywhere)

### 1. Clone on PythonAnywhere Bash console
```bash
git clone https://github.com/krishsavaliya7/Expenss-App.git splitsmart
cd splitsmart
python3.11 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python3 -c "from app import init_db; init_db()"
```

### 2. WSGI Configuration
In the PythonAnywhere **Web tab → WSGI file**, add:
```python
import sys, os
sys.path.insert(0, '/home/YOUR_USERNAME/splitsmart')
os.environ['SECRET_KEY'] = 'your-secret-key-here'
from app import app as application
```

### 3. Static Files
| URL | Directory |
|---|---|
| `/static/` | `/home/YOUR_USERNAME/splitsmart/static` |
| `/uploads/` | `/home/YOUR_USERNAME/splitsmart/uploads` |

### 4. Reload and visit
```
https://YOUR_USERNAME.pythonanywhere.com
```

---

## 🧠 Settlement Algorithm

SplitSmart uses a **Greedy Debt Minimization Algorithm** to reduce the number of transactions needed to settle all balances within a group.

**Before optimization:**
```
User A → User B  ₹200
User B → User C  ₹150
User C → User A  ₹300
```

**After optimization:**
```
User C → User B  ₹50
User C → User A  ₹150
```

The algorithm runs in **O(n log n)** time and guarantees the minimum number of transactions.

---

## 🔐 Security Features

- CSRF token validation on all POST/PUT/DELETE requests
- SHA256 hash-chain ledger (blockchain-inspired tamper detection)
- Bcrypt password hashing with werkzeug fallback for legacy passwords
- Input sanitization to prevent XSS injection
- File upload restrictions (png/jpg/jpeg/gif only, 16MB max)
- Profile picture access control (only friends/group members can view)

---

## 📱 Demo Flow

1. Sign up with username, email, phone, and UPI ID
2. Add friends via search
3. Create a group and add members
4. Add expenses — split equally, by percentage, or exact amount
5. View optimized settlement suggestions
6. Settle via UPI simulation or cash with receiver approval
7. Every completed settlement is recorded in the immutable ledger

---

## 👨‍💻 Credits

### 🚀 App Creator & Deployment
**Krish Savaliya**
> Configured deployment, bug fixes, server setup, and Git hygiene

[![GitHub](https://img.shields.io/badge/GitHub-krishsavaliya7-181717?style=flat&logo=github)](https://github.com/krishsavaliya7)

---

### 💻 Original Code & Backend Development
**Vishvesh Sharma** and **Raga (error-raga-008)**
> Built the full Flask backend, settlement algorithm, ledger system, frontend templates, and Android app during a hackathon

[![GitHub](https://img.shields.io/badge/GitHub-VishveshSharma2005-181717?style=flat&logo=github)](https://github.com/VishveshSharma2005)
[![GitHub](https://img.shields.io/badge/GitHub-error--raga--008-181717?style=flat&logo=github)](https://github.com/error-raga-008)

---

## 📄 License

This project was built as a hackathon demonstration. All original backend logic and frontend design credit goes to the MasterMinds team.
