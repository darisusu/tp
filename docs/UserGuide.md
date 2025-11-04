---
layout: default.md
title: "User Guide"
pageNav: 3
---

# FitBook User Guide

### Welcome Trainers!
FitBook is a desktop app built for personal trainers and fitness coaches who manage multiple clients.
It helps you stay organized, so that you can spend more time focusing on your clients' fitness journeys.

This is all done through a simple Command Line Interface (CLI) with a clean Graphical User Interface (GUI).

**FitBook helps you:**
- **Manage clients information** – Store and update clients contact details, body statistics, and personal data.
- **Track payments** – Monitor payment status and upcoming deadlines.
- **Schedule sessions** – Organize one-off, weekly, biweekly, or monthly training sessions with automatic conflict detection.
- **Track progress** – Record and update height, weight, body fat percentage, age, and gender data over time.
- **Set goals** – Assign and monitor fitness goals with deadlines for each client.
- **Organize efficiently** – Use tags to categorize clients and sort lists by payment status or deadlines.
- **Stay secure and portable** – Your data is stored locally in JavaScript Object Notation (JSON) format, allowing easy backup and transfer across devices.
- **Boost productivity** – With automated saving and intuitive command structure, trainers can manage 50+ clients effortlessly.


![FitBook Interface](images/Ui.png)

---
## Table of Contents

- [Quick Start](#quick-start)
- [Command Summary](#command-summary)
- [UI Guide](#ui-guide)
- [Command Reference](#command-reference)
- [Data Handling](#data-handling)
- [FAQ](#faq)
- [Known Issues](#known-issues)



---

## Quick Start

1. Ensure you have **Java 17 or above** installed.
    - **Mac users:** Follow the [installation guide](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Download the latest `.jar` file from your team’s [GitHub Releases](https://github.com/AY2526S1-CS2103T-F09-4/tp/releases).

3. Copy the `.jar` file to any folder - this will serve as your **FitBook home folder**.

4. Open a terminal and run:
   ```bash
   java -jar FitBook.jar
   ```
   The GUI should appear within a few seconds, showing you a dashboard. Sample data will be pre-loaded.

5. Try these example commands for starters:
    - `client` or `list` - Switches from the dashboard to the full client list view
    - `add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 goal/Build muscle h/170 w/70 age/25 g/male dl/2025-11-10 paid/false s/WEEKLY:MON-1800-1930-TUE-1800-1900 bf/18.5 t/friends t/owesMoney` - Adds a new client with basic information
    - `delete 1` – Deletes the first client in the list
    - `exit` – Exits the application

6. Refer to the [Command Reference](#command-reference) below for full details.

[↑ Back to top](#fitbook-user-guide)

---
## Command Summary

> ⚠️ **Note:** Please review the [Important Input Rule](#important-input-rule) before entering commands to avoid errors.


| **Command**                                                                   | **Description**                     | **Example**                                        |
|-------------------------------------------------------------------------------|-------------------------------------|----------------------------------------------------|
| [`add`](#add---add-a-client)                                                  | Add a new client                    | `add n/John Doe p/98765432 e/john@example.com ...` |
| [`edit`](#edit---edit-a-client)                                               | Edit an existing client             | `edit 2 n/James Tan e/jamestan@example.com`        |
| [`list` / `client`](#list-or-client---view-all-clients)                       | View all clients                    | `list` or `client`                                 |
| [`find`](#find---search-clients-by-name)                                      | Search for clients by name          | `find John Jane`                                   |
| [`delete`](#delete---remove-a-client)                                         | Delete a client                     | `delete 3`                                         |
| [`clear`](#clear---delete-all-clients)                                        | Delete all clients                  | `clear`                                            |
| [`sortbypaid`](#sortbypaid---sort-clients-by-payment-status)                  | Sort clients by payment status      | `sortbypaid`                                       |
| [`sortbydeadline`](#sortbydeadline---sort-clients-by-payment-deadline)        | Sort clients by payment deadline    | `sortbydeadline desc`                              |
| [`sortbysession`](#sortbysession---sort-clients-by-upcoming-session)          | Sort clients by upcoming session    | `sortbysession`                                    |
| [`session`](#session---update-a-clients-scheduled-session)                    | Update a client’s scheduled session | `session 1 s/WEEKLY:MON-1800-1930`                 |
| [`goal`](#goal---set-or-clear-a-fitness-goal)                                 | Set or clear a fitness goal         | `goal 1 goal/Run a marathon`                       |
| [`deadline`](#deadline---update-a-goal-deadline)                              | Update a goal deadline              | `deadline 2 dl/2025-12-31`                         |
| [`paid`](#paid---record-payment-status)                                       | Record payment status               | `paid 3 paid/true`                                 |
| [`height`](#height-weight-age-bodyfat-gender---update-individual-attributes)  | Update height                       | `height 1 h/170`                                   |
| [`weight`](#height-weight-age-bodyfat-gender---update-individual-attributes)  | Update weight                       | `weight 1 w/70`                                    |
| [`age`](#height-weight-age-bodyfat-gender---update-individual-attributes)     | Update age                          | `age 1 age/25`                                     |
| [`bodyfat`](#height-weight-age-bodyfat-gender---update-individual-attributes) | Update body fat                     | `bodyfat 1 bf/18.5`                                |
| [`gender`](#height-weight-age-bodyfat-gender---update-individual-attributes)  | Update gender                       | `gender 1 g/female`                                |
| [`dashboard`](#dashboard---switch-to-dashboard-view)                          | Switch to dashboard view            | `dashboard`                                        |
| [`help`](#help---open-help-window)                                            | Open help window                    | `help`                                             |
| [`exit`](#exit---close-the-program)                                           | Exit the program                    | `exit`                                             |

[↑ Back to top](#fitbook-user-guide)

---
## UI Guide

The UI comprises a few main components, which can be manipulated via the commands below.
![FitBook Interface](images/Ui.png)

When entering the app, the user will see a dashboard component taking up the bulk of the screen.
The dashboard contains two lists:
1. Upcoming Sessions:
    - Lists the current clients in order of nearest upcoming session from current datetime
    - Each card contains only the client name and their session type
2. Unpaid Clients:
    - Lists the current clients who have not paid (`paid` is false)
    - Each card contains only the client name, deadline date, and phone number

Pressing the client button on the left, the dashboard will be replaced with the full client list,
containing the full information of every client.
![Client List](images/ClientList.png)
Here, the user can further use commands like `find`, `sortbydeadline` to refine or reorder the results shown in the list.

To the left, there is a sidebar with four buttons.

![Sidebar](images/Sidebar.png)

The top two buttons allow the user to toggle between the dashboard and the main client list.
The Help button opens a new window containing a link to the user guide.
The Exit button will close the program.
All the buttons' functionalities can also be accessed via text commands, as seen below.

At the bottom lies the CommandBox and ResultDisplay. 

![CommandBox](images/CommandBox.png)

The user can input their commands into the CommandBox.
Results of the command input will be immediately shown in the ResultDisplay, including error messages.
This is the main medium users are expected to interact with the program.

[↑ Back to top](#fitbook-user-guide)


---

## Command Reference

This section lists all available commands and how to use them.

> ### Format conventions
> - Words in `UPPER_CASE` are parameters you should replace with your own values.
> - Items in `[square brackets]` are optional.
> - Items followed by `…` can be repeated multiple times.
> - Commands that update clients use the `INDEX` from the **full client list** (starting at 1). It does NOT refer to
the index of the dashboard lists.
> - Prefixes such as `n/` for name let you provide parameters in **any order**.

> ### ⚠️ Important Input Rule
>
> You **must not include the `/` character in any input values**, except where it is used as a command prefix (e.g., `n/`, `p/`, `e/`).
>
> Using `/` inside actual data fields - such as names (`s/o`), addresses, goals, or tags - will cause the command to fail or be misinterpreted.
>
>
>> ❌ **Incorrect:** `add n/John s/o Doe ...`
>>
>> ✅ **Correct:** `add n/John s|o Doe ...` (use a different character instead of `/`)

[↑ Back to Command Summary](#command-summary)

---

### `add` - add a client

**Format:**
```
add n/NAME p/PHONE e/EMAIL a/ADDRESS dl/DEADLINE paid/PAID s/SCHEDULE [goal/GOAL] [h/HEIGHT] [w/WEIGHT] [age/AGE] [g/GENDER] [bf/BODYFAT] [t/TAG]…
```

**Example:**
```
add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 dl/2025-11-10 paid/false s/2025-11-12 18:00 goal/Build muscle h/170 w/70 age/25 g/male bf/18.5 t/friend t/owesMoney
```

**Guidance:**
- Required prefixes: `n/`, `p/`, `e/`, `a/`, `dl/`, `paid/`, `s/`
- Optional: `goal/`, `h/`, `w/`, `age/`, `g/`, `bf/`, `t/`
- `PHONE` must contain exactly 8 digits (numbers only, no spaces or symbols)
- Units: height (cm), weight (kg), age (years), body fat (%)
- `GENDER` accepts: `male`, `female`, `other`, `non-binary`, `prefer not to say` (case-insensitive, e.g., `Male`, `FEMALE`, `Non-Binary` are all accepted)
- `PAID` accepts `true` or `false`
- `DEADLINE` format: `yyyy-MM-dd`
- Automatically saves data after successful addition

---

### `edit` - Edit a client

**Format:**
```
edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [h/HEIGHT] [w/WEIGHT] [age/AGE] [g/GENDER] [dl/DEADLINE] [paid/PAID] [s/SESSION] [bf/BODYFAT] [goal/GOAL] [t/TAG]…
```

**Example:**
```
edit 2 p/91234567 e/alex@example.com goal/Run a half marathon
```

**Guidance:**
- Provide at least one field to update.
- When specifying tags, existing tags are replaced with the new ones.
- Automatically saves changes after editing.

---

### `list` or `client` - View all clients

**Format:**

Use either of the following commands:

`list` or
`client`

**Guidance:**
- Both commands display the full client list view.
- If the user is currently on the dashboard, these commands switch the view to the client list tab.
- They do **not** modify any data — only change the displayed view.
- You can use either command interchangeably.

> 💡 **Design Note:**
> `list` is provided for users familiar with traditional CLI syntax,
>  While `client` offers a more intuitive, context-based option consistent with `dashboard`.
> Both are retained to improve accessibility and user experience.
---

### `find` - Search clients by name
**Format:**
```
find KEYWORD [MORE_KEYWORDS]…
```
**Example:**
```
find alex bernice
```

**Guidance:**
- Case-insensitive search within names
- Multiple keywords perform an OR-search (matches if any keyword is present)
- Only full words will be matched e.g. `Han` will not match `Hans`
- Matches are displayed in the respective client and dashboard panels


  ![result for 'find alex bernice'](images/findAlexBernice.png)


---

### `delete` - Remove a client
**Format:**
```
delete INDEX
```
**Example:**
```
delete 3
```

**Guidance:**
- Deletes the client at the given index in the displayed list.
- Note: Index corresponds to the client list currently shown, which may be filtered or sorted.

---

### `clear` - Delete all clients
**Format:**
```
clear
```
> ⚠️ **Warning:** This action **cannot be undone**. Think twice!



---

### `sortbypaid` - Sort clients by payment status
**Format:**
```
sortbypaid
```

**Guidance:**
- Unpaid clients (paid:`false`) appear first.
- Paid clients (paid:`true`) appear last.
- Will switch back to client list to display sorted list.

---

### `sortbydeadline` - Sort clients by payment deadline
**Format:**
```
sortbydeadline [asc/desc]
```
**Examples:**
```
- `sortbydeadline` or `sortbydeadline asc` → ascending
- `sortbydeadline desc` → descending
```
**Guidance:**
- Ascending: earliest → latest → no deadline
- Descending: no deadline → latest → earliest
- Will switch back to client list to display sorted list.

---

### `sortbysession` - Sort clients by upcoming session
**Format:**
```
sortbysession
```
**Guidance:**
- Clients with nearest upcoming sessions will appear first.
- Will switch back to client list to display sorted list.
---

### `session` - Update a client’s scheduled session
**Format:**
```
session INDEX s/SESSION
```
**Example:**
```
session 1 s/WEEKLY:MON-1800-1930
```
**Guidance:**

- FitBook automatically detects and rejects conflicting session timings.

**Supported Session Formats:**

| **Type** | **Format** | **Example** |
|-----------|-------------|-------------|
| **One-off** | `YYYY-MM-DD HH:MM` | `2025-06-10 14:30` |
| **Weekly (single slot)** | `WEEKLY:DAY-START-END` | `weekly:mon-1800-1930` |
| **Weekly (multi-slot)** | `WEEKLY:DAY-START-END-DAY-START-END` | `weekly:mon-1800-1930-tue-1800-1900` |
| **Biweekly** | `BIWEEKLY:DAY-START-END` | `biweekly:fri-0900-1030` |
| **Monthly** | `MONTHLY:DD HH:MM` | `monthly:15 10:00` |

**Additional Notes:**
- FitBook is optimised for same-day training sessions and therefore rejects unrealistic overnight ranges where the end time is earlier than the start time (e.g., `23:00-01:00`). Schedule these as two separate slots so each range stays within a single calendar day
- `DAY` accepts: mon, tue, wed, thu, fri, sat, sun
- Time uses 24-hour format (`HHmm` or `HH:MM`)
- Multi-slot sessions list multiple day–time ranges sequentially
- All times are interpreted in local time
- Biweekly sessions assume the same weekday and time range is occupied for every occurrence. FitBook will block another client from taking that slot even on the assumption they would attend on alternate weeks, so coordinate such arrangements outside the app for now

---

### `goal` - Set or clear a fitness goal
**Format:**
```
goal INDEX goal/GOAL
```
**Example:**
```
goal 1 goal/Complete a triathlon
```
**Guidance:**
- Use `goal/` with no text to clear the goal.
- There is a limit of 100 characters for the goal field.

---

### `deadline` - Update a payment deadline
**Format:**
```
deadline INDEX dl/DATE
```
**Example:**
```
deadline 4 dl/2025-12-31
```
**Guidance:**
- Use `dl/` with no text to clear the deadline.
- You can key in a date that is in the **past** or **more than one year from now**, but a warning message will pop up

---

### `paid` - Record payment status
**Format:**
```
paid INDEX paid/STATUS
```
**Example:**
```
paid 3 paid/true
```
**Guidance:**
- Use `true` if the client has paid, or `false` otherwise.
- Clients with paid status of `false` will be displayed in the dashboard under Unpaid Clients.

---

### `height`, `weight`, `age`, `bodyfat`, `gender` - Update individual attributes
Each of these commands updates one attribute using the same format:

**Format:**
```
ATTRIBUTE INDEX PREFIX/VALUE
```
**Examples:**
```
- height 2 h/168
- weight 2 w/68.5
- age 1 age/26
- bodyfat 1 bf/17.2
- gender 5 g/non-binary
- gender 1 g/Male        (case-insensitive: accepts Male, male, MALE, etc.)
- gender 2 g/FEMALE      (case-insensitive: accepts FEMALE, Female, female, etc.)
```

**Guidance:**
- Height: Integer value between 50 and 300 (inclusive)
- Weight: Numeric value greater than 20 and less than 500 (decimals allowed)
- Age: Integer between 1 and 120 (inclusive)
- Bodyfat: Numeric value between 5.0 and 60.0 (inclusive, up to one decimal place)
- Gender: Accepts any one of the following (case-insensitive):
  - `male`, `female`, `other`, `non-binary`, `prefer not to say` (or `Male`, `FEMALE`, `Non-Binary`, etc.)
  - Short forms: `m`, `f`, `o`, `nb`, `pns` (also case-insensitive, e.g., `M`, `F`, `NB`) 

---

### `dashboard` - Switch to dashboard view
**Format:**
```
dashboard
```

---

### `help` - Open help window
**Format:**
```
help
```
**Guidance:**

Displays a new window with a link to the user guide.

![help_window_screenshot](images/helpMessageUpdated.png)


---

### `exit` - Close the program
**Format:**
```
exit
```

[↑ Back to top](#fitbook-user-guide)

---

## Data Handling

### Saving data
FitBook automatically saves all data to the hard disk after any command that changes data. No manual save is required.

### Data file location
Data is stored at:
```
[JAR file location]/data/FitBook.json
```

### Editing data manually
Advanced users may edit the JSON file directly.
⚠️ Invalid edits (e.g. malformed JSON) will cause FitBook to start with an empty dataset. Always back up before editing.

[↑ Back to top](#fitbook-user-guide)

---

# FAQ

**Q: Where is my data stored?**
**A:** In `data/FitBook.json`. You can change the path via `preferences.json` or by launching with `--data <path>`.

**Q: How do I back up or migrate my data?**
**A:** Copy the entire app folder (including `data/` and `preferences.json`) to the new machine, or copy just `FitBook.json` and keep your current `preferences.json`.

**Q: Can I rename `FitBook.json`?**
**A:** Yes—also update `preferences.json → addressBookFilePath`, or launch once with:

```bash
java -jar tp.jar --data data/FitBook.json
```

**Q: Why does `deadline` accept past dates?**
**A:** For backfilling/record-keeping. A warning appears if the date is in the past or unusually far in the future.

**Q: What happens if I set the same deadline again?**
**A:** The command is idempotent—FitBook shows “Payment deadline unchanged” and doesn’t write data.

**Q: How do I clear an optional field?**
**A:** Provide the prefix with no value, e.g., `goal/` clears the goal.

**Q: Can I sort or filter unpaid clients?**
**A:** Yes—use `sortbypaid`, `sortbydeadline`, and check the Dashboard panels (“Unpaid Clients”, “Upcoming Sessions”).

**Q: Why did my command fail with “Unknown prefix: x/”?**
**A:** A mistyped prefix (e.g., `w/70 x/hello`) was detected before parsing. Fix the typo or remove the extra prefix.

**Q: How do I resolve session conflicts?**
**A:** FitBook prevents overlapping sessions. Edit or delete the conflicting client’s session, or reschedule the new one.

**Q: Can I import from JSON/CSV?**
**A:** Not directly. Convert to FitBook’s schema or add via commands. *(Roadmap: CSV import helper.)*

**Q: How do I change the theme or window size?**
**A:** Adjust via Settings (GUI) or edit `preferences.json → guiSettings` (width/height/coordinates).

**Q: Is there undo/redo?**
**A:** Not yet. Back up `FitBook.json` before bulk edits.

[↑ Back to top](#fitbook-user-guide)

---

# Known Issues

1. **Old file recreated (`addressbook.json` reappears).**
   *Cause:* `preferences.json` still points to the old name.
   *Fix:* Update `addressBookFilePath` to `data/FitBook.json` or delete `preferences.json` and relaunch.

2. **Unknown prefix typos.**
   *Symptom:* `w/70 x/abc` used to look like a weight error.
   *Fix:* Now reported as `Unknown prefix: x/`. Correct the prefix.

3. **Locale-specific date parsing.**
   *Symptom:* Dates misread on non-`en-SG` locales.
   *Fix:* Use `yyyy-MM-dd` or set system locale to English (Singapore).

4. **Help window minimized won’t reopen with `help`.**
   *Fix:* Restore it from the taskbar (JavaFX quirk).

5. **Window opens off-screen after monitor changes.**
   *Fix:* Delete `preferences.json` (resets window coordinates) and relaunch.

6. **Pasting from rich text adds hidden characters.**
   *Symptom:* Parser errors despite correct-looking text.
   *Fix:* Paste via a plain-text editor; ensure ASCII spaces.

7. **Session parsing rejects malformed schedules.**
   *Symptom:* `s/MONTHLY:05 05` or missing separators.
   *Fix:* Use `FREQ:DAY-HHMM-HHMM-...` (e.g., `WEEKLY:MON-1800-1930-TUE-1800-1900`).

8. **Slow on network drives.**
   *Cause:* Frequent JSON writes on network FS.
   *Fix:* Run locally, then copy `FitBook.json` back.

9. **Emoji/non-ASCII tags render oddly.**
   *Fix:* Prefer alphanumeric tags or ensure system fonts support them.

10. **Large data file slows startup (>10–20 MB).**
    *Fix:* Archive old clients (export/remove), or split files and switch via `--data`.

11. **Past-dated deadlines warn on “no-change” edits.**
    *Expected:* Informational warning even if unchanged.

12. **Case-sensitive tag duplicates (`friends` vs `Friends`).**
    *Fix:* Standardize casing; merge via `find tag/` + `edit`.

13. **Accidentally cleared a field (e.g., `goal/`).**
    *Fix:* Re-set the value; there’s no undo stack.

14. **Packaged JAR missing help assets.**
    *Symptom:* Help/Docs don’t open after shading.
    *Fix:* Ensure resources are on classpath and included by `shadowJar`.

15. **Git credential warning on Windows (`credential-manager-core`).**
    *Fix:*

    ```bash
    git config --global credential.helper manager
    # or: manager-core
    ```

[↑ Back to top](#fitbook-user-guide)

---
*End of User Guide.*
