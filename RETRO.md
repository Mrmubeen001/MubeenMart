# Sprint Retrospectives (RETRO.md)

Project: MubeenMart &bull; Anna University R2025 Semester 3  
Format: *What worked*, *what didn't*, *one change for the next sprint*.

---

### Sprint 1 (Jul 27 – Aug 9, 2026) — Core Foundation & Authentication
- **What worked:** Clean layering with DAO and Service separation; jBCrypt password hashing and session fixation handling was straightforward.
- **What didn't:** Initial manual database connection creation caused leaks until HikariCP pool lifecycle was unified into `AppContextListener`.
- **One change for the next sprint:** Ensure all database queries strictly use try-with-resources and PreparedStatement templates before committing code.

---

### Sprint 2 (Aug 10 – Aug 23, 2026) — Catalog Management & Seller Dashboard
- **What worked:** Fast iteration on seller CRUD listings and modal popups using responsive CSS and clean JSP expressions.
- **What didn't:** Stock decrement concurrency was initially handled in Java memory rather than atomic SQL `UPDATE` queries.
- **One change for the next sprint:** Delegate all inventory decrements to atomic SQL statements with `stock_qty >= ?` validation.

---

### Sprint 3 (Aug 24 – Sep 6, 2026) — Multi-Filter Search & Verified Reviews
- **What worked:** Category and keyword search query composition using parameterized SQL was reliable and injection-proof.
- **What didn't:** Review eligibility logic needed additional join queries against order items and status to guarantee only completed deliveries can review.
- **One change for the next sprint:** Add automated DAO tests for review eligibility across completed vs pending orders.

---

### Sprint 4 (Sep 7 – Sep 20, 2026) — Security Hardening & Observability
- **What worked:** Logback SLF4J MDC request ID correlation in `LoggingFilter` provided crystal clear request tracing.
- **What didn't:** Mockito strict stubbing detected argument mismatches in servlet request parameter mocks during early unit tests.
- **One change for the next sprint:** Write explicit test fixtures for HTTP servlet requests and response envelopes.

---

### Sprint 5 (Sep 21 – Oct 9, 2026) — AI Chatbot Assistant & Final Polish
- **What worked:** The `ChatProvider` interface cleanly separated the Gemini API client from the zero-latency mock FAQ provider for offline evaluation.
- **What didn't:** High chat message volume required sliding-window rate limiting and session-level query caching to avoid quota errors.
- **One change for the next sprint:** Project completed for final submission; maintain clear documentation and rehearsed demonstration scripts.
