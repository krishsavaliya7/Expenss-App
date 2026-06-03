import pytest
import sqlite3
from app import calculate_group_balances, get_db, app

@pytest.fixture
def test_db():
    conn = sqlite3.connect(':memory:')
    conn.row_factory = sqlite3.Row
    c = conn.cursor()
    # Create necessary tables based on the error
    c.execute('''CREATE TABLE groups_members (group_id INTEGER, user_id TEXT, is_active INTEGER)''')
    c.execute('''CREATE TABLE expenses (id INTEGER PRIMARY KEY, group_id INTEGER, amount REAL, paid_by TEXT)''')
    c.execute('''CREATE TABLE expense_splits (expense_id INTEGER, user_id TEXT, amount_owed REAL)''')
    c.execute('''CREATE TABLE settlements (id INTEGER PRIMARY KEY, group_id INTEGER, from_user TEXT, to_user TEXT, amount REAL, settlement_status TEXT)''')

    # Insert some test data
    c.execute("INSERT INTO groups_members (group_id, user_id, is_active) VALUES (1, 'A', 1), (1, 'B', 1), (1, 'C', 1)")

    # A pays 300, splits 100 to A, 100 to B, 100 to C
    c.execute("INSERT INTO expenses (id, group_id, amount, paid_by) VALUES (1, 1, 300, 'A')")
    c.execute("INSERT INTO expense_splits (expense_id, user_id, amount_owed) VALUES (1, 'A', 100), (1, 'B', 100), (1, 'C', 100)")

    # B pays 150, splits 50 to B, 100 to C
    c.execute("INSERT INTO expenses (id, group_id, amount, paid_by) VALUES (2, 1, 150, 'B')")
    c.execute("INSERT INTO expense_splits (expense_id, user_id, amount_owed) VALUES (2, 'B', 50), (2, 'C', 100)")

    conn.commit()
    yield conn
    conn.close()

def test_calculate_group_balances(test_db, monkeypatch):
    monkeypatch.setattr('app.get_db', lambda: test_db)

    with app.app_context():
        balances = calculate_group_balances(1)

        assert 'A' in balances
        # In the original function, if balance is 0 it is removed
        # assert 'B' in balances
        assert 'C' in balances

        assert balances['A'] == 200.0
        assert balances.get('B', 0.0) == 0.0
        assert balances['C'] == -200.0

def test_advanced_greedy_settlement(test_db, monkeypatch):
    monkeypatch.setattr('app.get_db', lambda: test_db)
    from app import advanced_greedy_settlement

    with app.app_context():
        settlements, balances = advanced_greedy_settlement(1)

        assert len(settlements) == 1
        assert settlements[0]['from'] == 'C'
        assert settlements[0]['to'] == 'A'
        assert settlements[0]['amount'] == 200.0
