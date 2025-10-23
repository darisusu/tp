---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Sort by Paid Status feature

The sort by paid status feature allows trainers to sort their client list by payment status, with unpaid clients appearing first for easy follow-up.

#### Implementation

The sortbypaid command is implemented through the following components:

* `SortByPaidCommand` — The command class that executes the sorting operation
* `SortByPaidCommandParser` — Parses the "sortbypaid" command input
* `Model#sortPersonListByPaid()` — Interface method for sorting by paid status
* `AddressBook#sortByPaid()` — Delegates sorting to the person list
* `UniquePersonList#sortByPaid()` — Performs the actual sorting logic

The sorting logic uses `Boolean.compare()` to sort unpaid clients (false) before paid clients (true).

#### Class Diagram

The following class diagram shows the relationship between the sortbypaid command and related classes:

<puml src="diagrams/SortByPaidCommandDiagram.puml" alt="SortByPaidCommand Class Diagram" />

#### Sequence Diagram

The following sequence diagram shows the execution flow of the sortbypaid command:

<puml src="diagrams/SortByPaidSequenceDiagram.puml" alt="SortByPaidCommand Sequence Diagram" />

#### Activity Diagram

The following activity diagram shows the workflow for sorting by paid status:

<puml src="diagrams/SortByPaidActivityDiagram.puml" alt="SortByPaidCommand Activity Diagram" />

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a **personal trainer** or **fitness coach**
* has a need to manage a **significant number of clients**
* needs to record and access **client session schedules, payments, body stats, and medical notes**
* prefers a **desktop app** for better data visibility and keyboard efficiency
* can type fast and prefers **CLI commands** to mouse-driven GUIs
* is reasonably comfortable with technology and command-based workflows

**Value proposition:**
Manage client information, sessions, and progress more efficiently than with typical spreadsheet or GUI apps, improving organization and reducing admin time.

---

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​ | I want to …​ | So that I can…​ |
|-----------|----------|---------------|------------------|
| `* * *` | new user | see usage instructions | learn how to navigate and use the app effectively |
| `* * *` | trainer | add a new client | record details of a new client for tracking |
| `* * *` | trainer | delete a client | remove clients I no longer manage |
| `* * *` | trainer | edit a client’s information | update details such as contact, weight, or payment status |
| `* * *` | trainer | view a client’s full profile | review all details before or during a session |
| `* * *` | trainer | schedule a new training session | plan my workday and avoid double-booking |
| `* *` | trainer | receive reminders for upcoming sessions | stay aware of upcoming commitments |
| `* *` | trainer | record a client’s payment | track who has paid and who still owes fees |
| `* *` | trainer | log a client’s progress (e.g., weight, strength stats) | monitor their improvement over time |
| `* *` | trainer | search clients by name or medical condition | locate specific clients easily |
| `* *` | organised trainer | categorise clients by fitness goals | segment clients for tailored programs |
| `* *` | analytical trainer | generate reports of client progress | evaluate training effectiveness |
| `* *` | busy trainer | quickly view today’s sessions | save time checking the schedule |
| `* *` | safety-conscious trainer | encrypt sensitive medical info | keep client data secure |
| `* *` | current user | duplicate a client’s plan template | save time creating similar plans for others |
| `* *` | organised trainer | archive inactive clients | keep my main list clutter-free |
| `* *` | analytical user | view income summary per month | analyse business growth |
| `* *` | beginner user | have access to helpful command suggestions | reduce typing errors while learning |
| `* *` | busy trainer | add sessions via a single-line command | input faster during a packed day |
| `* *` | money-minded trainer | see total pending payments | track finances effectively |
| `* *` | analytical user | export client data to CSV | perform deeper analysis in spreadsheets |
| `* *` | organised trainer | assign tags (e.g., “rehab”, “weight loss”) | filter clients by goals |
| `* *` | safe user | back up all data automatically | prevent data loss |
| `*` | trainer | customise the theme or font of the app | work in a visually comfortable environment |
| `*` | trainer | set recurring sessions | avoid manual re-entry of weekly appointments |
| `*` | organised trainer | attach notes or documents to sessions | keep relevant information handy |
| `*` | analytical user | compare before/after progress charts | demonstrate results to clients |
| `*` | busy trainer | integrate with Google Calendar | sync session schedules easily |
| `*` | current user | view quick stats summary on startup | get an overview without typing commands |
| `*` | beginner user | undo the last action | correct mistakes easily |

---

### Use cases

(For all use cases below, the **System** is the `FitBook` and the **Actor** is the `Trainer`.)

---

#### **Use case: UC01 – Add a client**

**MSS**

1. Trainer enters the `add` command with client details.
2. FitBook validates the input fields.
3. FitBook adds the new client and displays a confirmation message.

    Use case ends.

**Extensions**

* 2a. Input format is invalid.
    * 2a1. FitBook shows an error message and the correct usage format.
    * 2a2. Trainer re-enters the details.
      Use case resumes at step 2.

---

#### **Use case: UC02 – Delete a client**

**MSS**

1. Trainer requests to list clients.
2. FitBook shows a list of clients.
3. Trainer requests to delete a specific client in the list.
4. FitBook deletes the client and confirms the deletion.

    Use case ends.

**Extensions**

* 2a. The list is empty.
  * Use case ends.

* 3a. The given index is invalid.
  * 3a1. FitBook shows an error message.
    Use case resumes at step 2.

---

#### **Use case: UC03 – Schedule a training session**

**MSS**

1. Trainer enters the `addsession` command with client name, date, and time.
2. FitBook checks for scheduling conflicts.
3. FitBook adds the session to the client’s record and confirms.

    Use case ends.

**Extensions**

* 2a. The given client does not exist.
  * 2a1. FitBook shows an error message.
  * Use case ends.

* 2b. The session conflicts with another scheduled session.
  * 2b1. FitBook shows a warning and asks for confirmation.
  * 2b2. Trainer confirms to override or cancels.
  * Use case resumes at step 3.

---

#### **Use case: UC04 – Record a payment**

**MSS**

1. Trainer enters the `pay` command with client name and payment details.
2. FitBook updates the client’s payment history.
3. FitBook displays updated balance or confirmation.

    Use case ends.

**Extensions**

* 2a. Invalid payment amount or format.
  * 2a1. FitBook shows an error message.
  * Use case resumes at step 1.

---

#### **Use case: UC05 – View client’s progress**

**MSS**

1. Trainer enters the `view` command with client name.
2. FitBook displays the client’s recorded stats and past sessions.

    Use case ends.

**Extensions**

* 1a. Client name not found.
  * 1a1. FitBook displays an error message.
  * Use case ends.

---

### Non-Functional Requirements

1. Should work on any mainstream OS with Java `17` or above installed.
2. Should be able to handle at least **1,000 client records** without performance degradation.
3. Commands should execute and update the interface in **under 1 second** under normal usage.
4. Should be usable entirely via the **keyboard (CLI)** without requiring the mouse.
5. The application should store data **locally** in a human-readable JSON format for easy backup.
6. The app should automatically save data after each command to prevent data loss.
7. Codebase should follow **OOP and modular design principles** to allow future scalability (e.g., adding session reminders).
8. Should maintain full functionality offline (no dependency on internet).
9. The system should be easy to set up — requiring no additional dependencies beyond Java.
10. The UI should remain readable and usable across different screen resolutions.
11. System logs should record errors and commands for debugging and maintenance.
12. FitBook should start within **3 seconds** on a standard machine.
13. Data integrity must be preserved — no corrupted entries even after forced shutdown.
14. The design should allow easy integration with potential future APIs (e.g., fitness tracking).
15. Backup files should not exceed 5MB per 1,000 clients.

---

### Glossary

| Term | Definition                                                                                                                                |
|------|-------------------------------------------------------------------------------------------------------------------------------------------|
| **CLI (Command-Line Interface)** | A text-based interface where users type commands instead of using graphical buttons.                                                      |
| **Client** | A person managed by the trainer, whose data includes personal info, sessions, payments, and stats. May be referred to as trainee as well. |
| **Session** | A scheduled training appointment between the trainer and a client. Stored as either a one-off timestamp (`YYYY-MM-DD HH:MM`), a monthly slot (`MONTHLY:DD HH:MM`), or a weekly/biweekly string containing one or more `DAY-START-END` ranges (e.g. `WEEKLY:MON-1700-1930-TUE-1800-1900`). Each session should take up a few hours in a day.                      |
| **Payment Record** | A log of transactions between a client and the trainer. Currently recorded on a monthly basis.                                            |
| **Body Stats** | Measurable fitness indicators like weight, BMI, and strength level.                                                                       |
| **JSON** | JavaScript Object Notation — the format used to store FitBook data.                                                                       |
| **OOP** | Object-Oriented Programming — the coding paradigm used to structure the app.                                                              |
| **FitBook** | The name of the system, a CLI-based desktop app for managing clients, sessions, and fitness data.                                         |

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
