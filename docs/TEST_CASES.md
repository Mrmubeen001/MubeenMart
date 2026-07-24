# MubeenMart — Manual Test Cases & Security Verification Sheet
**Author:** Mohamed Mubeen &bull; Anna University Regulations 2025  

---

## 📋 End-to-End Test Cases (Section 9)

| Test ID | Feature Scope | Steps to Execute | Expected Outcome | Status |
| :--- | :--- | :--- | :--- | :--- |
| **TC-01** | User Registration (F1) | Navigate to `/register`. Fill name, email, password, select "Buyer". Submit. | User created with bcrypt hashed password; redirected to `/login` with success banner. | PASS |
| **TC-02** | Admin Public Block (F1) | Attempt to post registration payload with `role=ADMIN`. | Registration rejected with HTTP 403 / validation error; admin account remains exclusive to seed account. | PASS |
| **TC-03** | User Authentication (F1) | Log in with valid credentials at `/login`. | User authenticated; session established; session ID regenerated (`changeSessionId()`); redirected to dashboard. | PASS |
| **TC-04** | Invalid Login (F1) | Submit incorrect password at `/login`. | Form renders error message; password never logged or displayed. | PASS |
| **TC-05** | Catalog Search & Filter (F3) | On `/home`, choose "Electronics" and search "ANC". | Filtered listing displays only matching electronics products; stock & ratings visible. | PASS |
| **TC-06** | Cart Management (F4) | Click "Add to Cart" on a product. Change quantity in `/cart`. | Cart badge updates interactively; running subtotal and total calculated accurately. | PASS |
| **TC-07** | Stock Exceeded (F4) | Attempt to add quantity greater than available stock. | System returns 409 Conflict with friendly notification; excess quantity blocked. | PASS |
| **TC-08** | Checkout & Mock Pay (F5) | Fill address, select Mock UPI, click "Confirm Payment & Place Order". | Payment authorized; order created; stock deducted; cart cleared; redirected to `/orders`. | PASS |
| **TC-09** | Seller Dashboard (F2, O3) | Log in as seller. Open `/seller/dashboard`. | Metrics cards display correct order counts, items sold, and revenue; active listings rendered. | PASS |
| **TC-10** | Order Workflow (O2) | Seller selects order and advances status `Pending` &rarr; `Confirmed` &rarr; `Shipped` &rarr; `Delivered`. | Status updated in DB; visible in buyer's order tracking interface. | PASS |
| **TC-11** | Review Restriction (F8) | Buyer tries to review an item they have never purchased. | Review form blocked; policy banner informs user that only delivered purchasers can review. | PASS |
| **TC-12** | Review Submission (F8) | Buyer submits star rating and feedback on a delivered order item. | Verified review recorded; product average rating and count updated. | PASS |
| **TC-13** | Admin Moderation (F7) | Admin logs in; opens `/admin/dashboard`; clicks "Remove Listing" on test product. | Listing removed from catalog; confirmation feedback shown. | PASS |
| **TC-14** | AI Chatbot Interaction (O4)| Open chat bubble; click FAQ chip; send prompt. | Instant domain response returned; rate limiter permits up to 10 queries per minute. | PASS |
| **TC-15** | System Health (Section 18) | Send `GET /api/v1/health`. | Response returns `{ "success": true, "data": { "status": "UP", "db": "UP" }, "error": null }`. | PASS |

---

## 🔒 Security Checklist Verification (Section 9)

- [x] **PreparedStatement Verification:** Checked via `grep -rn "Statement)" src/` — All queries parameterized. Zero raw SQL string concatenation.
- [x] **Password Hashing:** Verified that `jBCrypt.hashpw(plain, gensalt(10))` is applied before any database write. Passwords never appear in log files.
- [x] **Protected Routes Enforcement:** Confirmed via `AuthFilter` that `/seller/*`, `/admin/*`, `/checkout`, and protected `/api/v1/*` routes strictly reject unauthenticated requests.
- [x] **XSS Output Escaping:** Every dynamic attribute rendered in JSP is escaped using `<c:out value="${...}" />` or `fn:escapeXml`.
- [x] **Stack Trace Concealment:** Forced 404 and 500 error scenarios; verified custom error pages render clean notices with zero stack trace leakage.
- [x] **Credential Exclusion:** `config.properties` and `.env` are verified inside `.gitignore`.
