# MubeenMart — Project Presentation Slide Deck
**Anna University Regulations 2025, Semester 3**  
**Presenter:** Mohamed Mubeen &bull; **Project:** MubeenMart  

---

<!-- slide -->
## Slide 1: Problem Statement & System Architecture

### 🎯 Problem Statement
- Traditional monolithic web apps often interleave SQL queries directly into presentation scripts, causing security vulnerabilities (SQL Injection, XSS) and high maintenance friction.
- **Objective:** Engineer a scalable, secure, multi-seller e-commerce marketplace utilizing **Java Servlets**, **JDBC**, and **Tomcat 9.0.x** that strictly adheres to **Layered MVC**, **SOLID principles**, and **Enterprise Design Patterns** without Spring/ORM magic.

### 🏛️ System Architecture Topology
```
[Browser: HTML/CSS/Vanilla JS + AJAX]
         │ (HTTP Request)
         ▼
[Filter Interceptors: Encoding, MDC Logging, Session RBAC]
         │
         ▼
[Thin Servlets: Front Controller & Resource Routing]
         │
         ▼
[Service Tier: Business Rules, Top-level Validation, ACID Transactions]
         │
         ▼
[DAO Layer: 100% PreparedStatement, try-with-resources]
         │
         ▼
[HikariCP Connection Pool (Owned by AppContextListener)]
         │
         ▼
[H2 Database: Indexed Foreign Keys, DECIMAL(10,2) Precision]
```

---

<!-- slide -->
## Slide 2: Completed Deliverables & Engineering Accomplishments

### ✅ Delivered Capabilities (F1–F8 + O1–O4)
| Requirement | Status | Verification |
| :--- | :--- | :--- |
| **F1: Auth & RBAC** | Completed | jBCrypt hashing, session fixation defense, seed admin |
| **F2: Seller CRUD** | Completed | Full product catalog management with image URLs |
| **F3: Search & Discovery** | Completed | Real-time category and keyword search filtering |
| **F4: Shopping Cart** | Completed | Dynamic quantities, running subtotal & total calculations |
| **F5: Mock Checkout** | Completed | Strategy pattern (UPI, Card, Net Banking) & ACID transactions |
| **F6: Order Tracking** | Completed | Buyer history & seller incoming fulfillment portal |
| **F7: Admin Moderation** | Completed | Oversight of users, marketplace volume, and listing moderation |
| **F8: Verified Reviews** | Completed | Star ratings restricted strictly to verified delivered buyers |
| **O1–O3: Wishlist & Metrics** | Completed | Save-for-later, status workflow, seller analytics |
| **O4: AI Chatbot** | Completed | Floating assistant with rate limits & cache (Gemini/Mock) |

### 🏆 Engineering Highlights
- **100% Parameterized SQL:** Zero SQL concatenation anywhere in the codebase.
- **CI / CD Pipeline:** GitHub Actions workflow executing `mvn -B clean verify` on push.
- **Test Automation:** 30+ passing unit and DAO tests run against embedded in-memory H2.
- **Production Artifact:** Clean, self-contained `mubeenmart.war` deployed to Tomcat.
