```markdown
# Expenss-App Development Patterns

> Auto-generated skill from repository analysis

## Overview
This skill teaches you the core development patterns, coding conventions, and workflows used in the Expenss-App Python codebase. The repository is structured for clarity and maintainability, using conventional commit messages, snake_case file naming, and relative imports. While no specific framework is detected, the codebase follows best practices for modular Python development and testing.

## Coding Conventions

### File Naming
- Use `snake_case` for all file and module names.
  - **Example:**  
    `expense_manager.py`, `user_profile.py`

### Import Style
- Use **relative imports** within the package.
  - **Example:**
    ```python
    from .utils import calculate_total
    from .models import Expense
    ```

### Export Style
- Use **named exports** for functions, classes, and constants.
  - **Example:**
    ```python
    # In expense_manager.py
    def add_expense(...):
        ...

    class ExpenseManager:
        ...
    ```

### Commit Messages
- Use **conventional commit** format.
- Common prefix: `refactor`
- Keep messages concise (average ~70 characters).
  - **Example:**  
    `refactor: update expense validation logic for clarity`

## Workflows

### Refactoring Code
**Trigger:** When improving code structure or readability without changing functionality  
**Command:** `/refactor`

1. Identify code that can be improved (e.g., simplify logic, rename variables).
2. Make changes using snake_case and relative imports as needed.
3. Write a commit message starting with `refactor:` describing the change.
4. Run tests to ensure nothing is broken.
5. Push your changes.

### Adding a New Module
**Trigger:** When introducing a new feature or logical component  
**Command:** `/add-module`

1. Create a new file using snake_case (e.g., `budget_tracker.py`).
2. Use relative imports to integrate with existing modules.
3. Export functions/classes using named exports.
4. Write or update tests (see Testing Patterns).
5. Commit with a descriptive message (e.g., `feat: add budget tracker module`).

### Writing Tests
**Trigger:** When adding or updating features  
**Command:** `/write-test`

1. Create a test file matching the pattern `*.test.*` (e.g., `expense_manager.test.py`).
2. Write test functions for each exported function/class.
3. Use assertions to verify expected behavior.
4. Run tests to validate changes.
5. Commit with a message like `test: add tests for expense manager`.

## Testing Patterns

- Test files follow the `*.test.*` naming convention.
  - **Example:** `expense_manager.test.py`
- Place test files alongside the modules they test or in a dedicated test directory.
- Each test file should import the module using relative imports.
- Use assertions to check expected outcomes.

  ```python
  # expense_manager.test.py
  from .expense_manager import add_expense

  def test_add_expense():
      result = add_expense(...)
      assert result == expected
  ```

- The specific test framework is not detected; use standard Python `assert` statements or integrate with a framework of your choice.

## Commands
| Command      | Purpose                                             |
|--------------|-----------------------------------------------------|
| /refactor    | Refactor code for clarity or structure              |
| /add-module  | Add a new module or feature                         |
| /write-test  | Write or update tests for a module                  |
```
