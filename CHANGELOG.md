# Change Logs

## All changes to this project will be documented here.

---

# Version: 0.0.1, Nov 24, 2026

- Fixed an incorrect formatting tag in `DummyCommands`.
- Improved the GUI system.
- Added a player system to track first and last login timestamps.
- Fixed the "No Reports" item not showing when there are no reports.
- Fixed an issue where only one report was displayed even when multiple reports existed.

---

---

# Version 0.0.2, Nov 24, 2026.

- Added support for XSeries.
- Removed an unused project dependency.
- Added a `TODO.md` file.
- Fixed a zip closed error caused by the database not being created.
- Hid item attributes in GUIs.
- Added border items to GUIs.
- Set up the basic structure for the rank system.
- Deleted `DatabaseRepository.java` class.

---

---

# Version 0.0.3, Nov 25, 2026.

- Reports are no longer deleted from the database regardless of if the status is `REJECTED` or
  `ACCEPTED`
- Created objects called `NetworkPlayer` which will be used for commands and guis.
- Fixed memory address being displayed in `ReportsGUI.java`.
- Refractored some parts of `PlayerReportsGUI.java` into `ReportType.java`.
- Created the basics of `README.md`.
- Removed Test GUIs.
- Improved the rank system.

---

---

# Version 0.0.4, Nov 26, 2026.

- Deleted `TODO.md`, we will be using GitHub PRs & Projects instead.
- Added the rank permission system.
- Removed an unused table from `RankRepository.java`.
- Planning to split config sections into their own files in the future.
- Added some null safety (probably missed a few spots).
- Replaced the old command system with a new one inspired by the Spectaculation project.
- Fixed NPEs in `ReportsGUI.java` and `PlayerReportsGUI.java` when there are no items to display.
- Added default rank setup on startup and fixed new player rank assignment.
---

---

# Version 0.0.5, Nov 28, 2026.

- Implemented the scoreboard system.
- Implemented the tab system.
- Updated `NOTES.md` with notes about the tab and scoreboard systems.
- Added a null check in `playerListener` for cases where `tabNameColor` might be null.
- Added separate configuration files for most services using the Configurate API.
- Updated `README.md` with a list of all APIs used.
- Added a way to remove vanilla Minecraft commands through the configuration file. This is mainly because the plugin uses a permission system that doesn't follow Bukkit's.
- Modularized the project.
---