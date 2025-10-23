# FitBook Command Reference

## Format conventions
- Words in `UPPER_CASE` are parameters you should replace with your own values.
- Items in square brackets `[ ]` are optional. Items followed by `…` can be repeated multiple times.
- Commands that update a trainee use the index shown in the currently displayed list. The first trainee has index `1`.
- Prefixes (such as `n/` for name) label each parameter so you can provide them in any order.

## Core trainee management
### `add` — create a trainee profile
- **Format:** `add n/NAME p/PHONE e/EMAIL a/ADDRESS h/HEIGHT w/WEIGHT age/AGE g/GENDER dl/DEADLINE paid/PAID bf/BODYFAT [goal/GOAL] [t/TAG]…`
- **Example:** `add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 h/170 w/70 age/25 g/male dl/2025-11-10 paid/false bf/18.5 goal/Build muscle t/friend t/owesMoney`
- **Guidance:**
    - `HEIGHT` is in centimetres, `WEIGHT` in kilograms, `AGE` in years, `BODYFAT` as a percentage (decimals allowed).
    - `GENDER` accepts: `male`, `female`, `other`, `non-binary`, `prefer not to say` (or `m` / `f` / `o` / `nb` / `pns`).
    - `DEADLINE` uses the `yyyy-MM-dd` format (e.g. `2025-11-10`).
    - `PAID` accepts `true` or `false`.
    - Add as many `TAG` values as needed to categorise the trainee.

### `edit` — update a trainee profile
- **Format:** `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [h/HEIGHT] [w/WEIGHT] [age/AGE] [g/GENDER] [dl/DEADLINE] [paid/PAID] [bf/BODYFAT] [goal/GOAL] [t/TAG]…`
- **Example:** `edit 2 p/91234567 e/alex@example.com goal/Run a half marathon`
- **Guidance:**
    - Provide at least one field to change.
    - When you specify tags, the existing set is replaced. Include every tag you want to keep.

### `list` — show every trainee currently stored
- **Format:** `list`

### `find` — filter trainees by keywords
- **Format:** `find KEYWORD [MORE_KEYWORDS]…`
- **Example:** `find alex bernice`
- **Guidance:**
    - Name matching is case-insensitive and searches within the trainee’s full name.
    - All provided keywords must match for a trainee to appear.

### `delete` — remove a trainee
- **Format:** `delete INDEX`
- **Example:** `delete 3`
- **Guidance:** Ensure the displayed list contains the trainee you intend to remove before running the command.

### `clear` — reset the trainee list
- **Format:** `clear`
- **Guidance:** Deletes **all** trainees. This cannot be undone.

## Progress tracking updates
### `age` — update a trainee’s age
- **Format:** `age INDEX age/AGE`
- **Example:** `age 1 age/26`
- **Guidance:** Provide a whole number between `1` and `120`.

### `bodyfat` — update body fat percentage
- **Format:** `bodyfat INDEX bf/BODYFAT`
- **Example:** `bodyfat 1 bf/17.2`
- **Guidance:** Provide a value between `5.0` and `60.0`, with at most 1 decimal place (e.g. `18.5`).

### `height` — update height measurements
- **Format:** `height INDEX h/HEIGHT`
- **Example:** `height 2 h/168`
- **Guidance:** Provide height in centimetres as a positive integer (e.g. `170`).

### `weight` — update weight measurements
- **Format:** `weight INDEX w/WEIGHT`
- **Example:** `weight 2 w/68.5`
- **Guidance:** Provide weight in kilograms as a positive number with up to 2 decimal places (e.g. `68.5`).

### `goal` — set or clear fitness goals
- **Format:** `goal INDEX goal/GOAL`
- **Example:** `goal 1 goal/Complete a triathlon`
- **Guidance:**
    - Supply a short description (up to 100 characters).
    - Use `goal/` without a value to clear the stored goal.

### `deadline` — adjust the goal deadline
- **Format:** `deadline INDEX dl/DATE`
- **Example:** `deadline 4 dl/2024-12-31`
- **Guidance:**
    - Use the `yyyy-MM-dd` format (e.g. `2024-12-31`).
    - Use `dl/` without a value to clear the deadline.

### `paid` — record payment status
- **Format:** `paid INDEX paid/STATUS`
- **Example:** `paid 3 paid/true`
- **Guidance:** Use `true` if the trainee has paid, or `false` otherwise.

### `gender` — update stored gender information
- **Format:** `gender INDEX g/GENDER`
- **Example:** `gender 5 g/non-binary`
- **Guidance:**
    - Accepts: `male`, `female`, `other`, `non-binary`, `prefer not to say` (or `m` / `f` / `o` / `nb` / `pns`).
    - Values are case-insensitive.

## Other essentials
### `help` — open this help window
- **Format:** `help`

### `exit` — close FitBook
- **Format:** `exit`

## Tips
- Commands that change data are automatically saved.
- If a command fails, the error message will describe the correction needed.
