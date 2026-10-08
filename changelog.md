# Changelog

## [Maintenance] - 2026-10-07

- fix: SINGLE-mode edit of one occurrence in a recurring series now takes effect (it was stored in a map nothing read); the occurrence is split out and the series keeps its count
- fix: FROM-mode edit starting at a series' first occurrence no longer fails and truncates the original series; a FROM split that conflicts is rolled back
- fix: FROM and ALL edits now also apply to standalone events sharing the subject instead of aborting midway
- fix: start/end edits in FROM or ALL mode are rejected before anything changes (a FROM time edit used to cut the series short and then fail)
- fix: recurring series with a count but no weekdays, or a non-positive count, are rejected instead of looping forever
- fix: CSV export quotes fields containing commas or quotes (RFC 4180) and import reads them back
- fix: export honours absolute paths instead of nesting them under the working directory
- fix: `show status` reports busy at an event's exact start time
- build: failing tests and coverage below 85% line / 70% branch now fail the build; CI no longer depends on an unconfigured Codecov token
- chore: removed generated Javadoc, stale PIT reports and scratch files from the repo; tests no longer write CSVs into the project root; JAR renamed to `virtual-calendar.jar`

## [Sprint 4] - 2025-06-15

- feat: Java Swing GUI providing month view, day detail dialog, and visual event creation/editing
- feat: CSV import for bulk event upload compatible with Google Calendar format
- feat: Unified controller now supports CLI, headless scripts, and GUI modes from single JAR (`Assignment6.jar`)
- feat: Complete CI/CD pipeline with GitHub Actions for automated testing, coverage, and quality gates
- feat: JaCoCo integration for line coverage reporting with 85% threshold enforcement
- feat: Enhanced PIT mutation testing with XML/HTML reports and coverage thresholds
- feat: Automated JAR artifact generation and test report archiving (30-90 day retention)
- docs: Added CI/CD badges, coverage integration, and comprehensive testing documentation
- docs: Updated README with GUI screenshots, usage guide, and new architecture diagrams
- chore: Added release tag `Ass6` and automated GitHub Actions build for JAR artifact

## [Sprint 3] - 2025-01-15

- feat: Multi-calendar support with unique names and IANA timezones
- feat: Calendar creation, selection, and editing commands
- feat: Event copy operations between calendars with automatic timezone conversion
- feat: Automatic timezone migration when editing calendar properties
- feat: Universal auto-decline conflict detection enabled by default for all events
- feat: JAR-based distribution

## [Sprint 2] - 2025-06-02

- MVC redesign - cleaner service layer, SOLID-compliant controllers.
- See full Sprint2 design notes in [`docs/Sprint2.md`](docs/Sprint2.md). 