# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SplitSmart (MasterMinds) is a group expense management and settlement platform built with Flask and SQLite. It features UPI-style payment simulation, greedy debt minimization, an immutable SHA256 hash-chain ledger, and cash settlement with receiver approval.

## Commands

```bash
# Install dependencies
pip install -r requirements.txt

# Run the application (starts on http://localhost:5000, debug mode)
python app.py

# Run the standalone expense tracker algorithm demo
python expense_tracker.py

# Run validation module self-test
python validation.py
```

There are no automated tests, linter, or formatter configured.

## Architecture

### Core Files

- **app.py** (~2550 lines) — The entire Flask backend: database initialization, all route handlers, settlement algorithm (DB-backed version), ledger creation, notification helpers. This is a monolithic single-file backend.
- **expense_tracker.py** — Standalone in-memory expense tracker with the greedy settlement algorithm. Uses dataclasses (`Expense`, `Settlement`, `AdvancedExpenseTracker`). Not imported by app.py; serves as algorithm reference/demo.
- **validation.py** — Server-side validation module (signup, login). Uses bcrypt for password hashing with werkzeug fallback for legacy passwords. Imported by app.py for `sanitize_input`, `validate_name`, `validate_username`, `validate_email_format`, `validate_upi_id`.

### Database

SQLite file: `expense_tracker.db` (auto-created on first run via `init_db()`).

Tables: `users`, `friend_requests`, `friends`, `groups`, `groups_members`, `groups_invitation`, `expenses`, `expense_splits`, `transactions`, `balances`, `settlements`, `payments`, `ledger_transactions`, `notifications`.

All SQL is raw (no ORM). The `get_db()` function returns connections with `row_factory = sqlite3.Row`. Schema migrations are handled inline in `init_db()` with `ALTER TABLE` try/except blocks.

### Auth

Session-based authentication using Flask sessions. `session['user_id']` stores the logged-in username. Password hashing uses Werkzeug's `generate_password_hash` in app.py, while validation.py offers bcrypt as an alternative (with backward-compatible hash detection in `verify_credentials`).

### Key Backend Patterns

- **Settlement algorithm**: `advanced_greedy_settlement(group_id)` in app.py reads from DB, computes optimal settlements using greedy matching (sort by balance, match largest creditor/debtor). Returns `(settlements_list, balances_dict)`.
- **Immutable ledger**: `create_ledger_transaction()` chains transactions via `SHA256(tx_id + from + to + amount + timestamp + previous_hash)`. Genesis block uses `'GENESIS'` as previous hash.
- **Balance refresh**: `refresh_group_balances(group_id)` syncs computed balances into the `balances` table using `INSERT ... ON CONFLICT DO UPDATE`.
- **Notifications**: `create_notification()` inserts into `notifications` table; accepts optional `conn` parameter to reuse an open transaction.

### Frontend

- **Templates**: Jinja2 templates in `templates/` with a shared `base.html` layout and `partials/ui_macros.html` for reusable UI components.
- **Static JS** (`static/js/`): `main.js` (dashboard/group logic), `auth.js` (login/signup forms), `friends.js` (friend management), `ledger.js` (ledger display), `components.js` (shared UI components), `validation.js` (client-side form validation).
- **Static CSS**: `static/css/main.css` — single stylesheet.

### API Routes

Page routes render templates: `/`, `/signup`, `/login`, `/dashboard`, `/profile`, `/friends`, `/groups`, `/groups/<id>`, `/groups/create`, `/ledger`.

JSON API endpoints under `/api/`: search-users, friend requests (send/accept/reject), get-friends, notifications (list/count/read), groups CRUD, group members, expenses CRUD, balances, settlements (request-cash, approve-cash, initiate-upi, confirm-upi), transactions, and group join via invite token.

## Important Notes

- The README states FastAPI but the app actually uses Flask.
- The `MasterMinds/` directory is a Python virtual environment (included in repo) — do not modify files in it.
- Profile picture uploads go to `uploads/` with 16MB max size, restricted to png/jpg/jpeg/gif.
