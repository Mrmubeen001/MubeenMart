# 🛍️ MubeenMart — Multi-Seller E-Commerce Marketplace

[![build-and-test](https://github.com/Mrmubeen001/MubeenMart/actions/workflows/build.yml/badge.svg)](https://github.com/Mrmubeen001/MubeenMart/actions/workflows/build.yml)
[![Java 17](https://img.shields.io/badge/JDK-17%20LTS-orange.svg)](https://adoptium.net)
[![Servlet 4.0](https://img.shields.io/badge/Servlet%20API-4.0%20(Tomcat%209)-blue.svg)](https://tomcat.apache.org/)
[![Database](https://img.shields.io/badge/Database-H2%20(Server%2FEmbedded)-green.svg)](https://www.h2database.com/)
[![License](https://img.shields.io/badge/Academic-Anna%20University%20R2025-blueviolet.svg)](#)

> **Builder:** Mohamed Mubeen &bull; Solo Project &bull; Anna University Regulations 2025, Semester 3  
> **Evaluation Window:** July 24 – October 10, 2026 &bull; **Release:** `v1.1.0`

---

## 📌 1. Problem Statement & Scope

Modern online retail requires distributed seller onboarding, real-time catalog moderation, and safe transaction handling. **MubeenMart** is a full-featured, multi-seller e-commerce marketplace web application engineered strictly over **Java Servlets**, **JDBC**, and **Apache Tomcat 9.0.x**, completely avoiding heavy frameworks (e.g. Spring/Hibernate) to demonstrate fundamental systems design, architectural layering, and security fundamentals.

### Core Feature Matrix
- **F1: User Authentication & RBAC:** Buyer and Seller registration; Admin seeded account; bcrypt password hashing; session fixation prevention with explicit 30-minute timeout.
- **F2: Seller Catalog Management:** Full CRUD operations for product listings (title, price, description, category, stock inventory, image URL).
- **F3: Buyer Search & Discovery:** Multi-criteria keyword and category filtering.
- **F4: Shopping Cart:** Persistent cart management, dynamic quantity updates, and running subtotal/total calculations.
- **F5: Checkout & Mock Payment:** Multi-channel simulated payment confirmation (UPI, Card, Net Banking) via Strategy pattern with atomic inventory deduction.
- **F6: Order History & Fulfillment:** Buyer past order tracker and seller incoming order dispatch dashboard.
- **F7: Administrator Moderation:** Full oversight of registered users, platform GMV volume, and catalog listing moderation.
- **F8: Verified Reviews & Star Ratings:** Strict verification ensuring only buyers with completed, delivered orders can post ratings and reviews.
- **O1: Save-for-Later Wishlist:** Independent customer wishlist with one-click move to cart.
- **O2: Order Status Workflow:** Complete lifecycle: `PENDING → CONFIRMED → SHIPPED → DELIVERED → CANCELLED`.
- **O3: Seller Analytics Dashboard:** Real-time metrics showing total orders, units sold, revenue earned, and active listings.
- **O4: AI Chatbot Assistant:** Floating customer support widget backed by server-side `ChatProvider` interface (`GeminiChatProvider` and `MockChatProvider`), rate-limited to 10 req/min with in-memory caching and offline degradation.

---

## 🏗️ 2. System Architecture & Design Patterns

The system adheres strictly to the **Layered MVC over Servlets (Front Controller pattern)**:

```
Browser (HTML5, Responsive CSS, Vanilla JS + AJAX fetch)
  │ HTTP Request
  ▼
Filter Layer ──────► EncodingFilter (UTF-8)
                     LoggingFilter (SLF4J MDC Request ID tracking)
                     AuthFilter (Session verification & RBAC enforcement)
  │
  ▼
Controller Layer ──► Thin Servlets (ProductServlet, OrderServlet, CartServlet, SellerServlet...)
  │
  ▼
Service Layer ─────► Business logic, validation at top of method, transactions (no JDBC here)
  │
  ▼
DAO Layer ─────────► UserDAO, ProductDAO, OrderDAO, CartDAO, ReviewDAO, WishlistDAO
                     (All SQL PreparedStatement only, try-with-resources)
  │
  ▼
Connection Pool ───► HikariCP (Managed exclusively by AppContextListener)
  │
  ▼
Database ──────────► H2 Database (Server / Embedded / In-Memory Test Mode)
```

### Required Design Patterns (Section 12)
1. **DAO (Data Access Object):** Interfaces decouple SQL operations from business rules (`UserDAO`, `ProductDAO`, `OrderDAO`, etc.).
2. **Front Controller / Resource Controller:** Thin servlets orchestrate HTTP requests and dispatch to appropriate service actions.
3. **Singleton:** Connection pool lifecycle owned strictly by single `AppContextListener` via `DatabaseConnectionManager`.
4. **Factory:** `DAOFactory` and `PaymentStrategyFactory` encapsulate object instantiation.
5. **Strategy:** `PaymentStrategy` provides swappable payment channels (`MockUpiPayment`, `MockCreditCardPayment`, `MockNetBankingPayment`).
6. **Builder:** `ApiResponse<T>` and DTO construction utilizing type-safe builder patterns.

---

## 📊 3. System Design Diagrams

| Diagram | Depiction | Reference |
| :--- | :--- | :--- |
| **D1: ER Diagram** | All entities, foreign keys, indexes, and cardinality | [docs/diagrams/D1_ER_Diagram.svg](docs/diagrams/D1_ER_Diagram.svg) |
| **D2: Use Case Diagram** | Buyer, Seller, Admin actor workflows (F1–F8) | [docs/diagrams/D2_UseCase_Diagram.svg](docs/diagrams/D2_UseCase_Diagram.svg) |
| **D3: Sequence Diagram** | Place-Order flow: Browser &rarr; Servlet &rarr; Service &rarr; DAO &rarr; DB | [docs/diagrams/D3_Sequence_Diagram.svg](docs/diagrams/D3_Sequence_Diagram.svg) |

---

## 💻 4. Technology Stack Specification

| Component | Specification |
| :--- | :--- |
| **JDK** | OpenJDK 17 LTS |
| **Servlet Container** | Apache Tomcat 9.0.x (`javax.servlet.*` Servlet 4.0 API) |
| **Build Tool** | Apache Maven 3.9+ |
| **Database** | H2 Database 2.2.x (Server mode for prod, Embedded/In-Memory for dev/test) |
| **Connection Pooling**| HikariCP 5.1.0 |
| **View Layer** | JSP 2.3 + JSTL 1.2 with HTML output escaping (`<c:out>`) |
| **Client Scripts** | Vanilla JS (ES6+) with `fetch()` API |
| **JSON Serialization**| Google Gson 2.10.1 |
| **Password Hashing** | jBCrypt 0.4 (Blowfish salt rounds: 10) |
| **Testing** | JUnit 5.10 + Mockito 5.11 |
| **Logging** | SLF4J 2.0 + Logback 1.4 with MDC request correlation |
| **Continuous Integration** | GitHub Actions (`mvn -B clean verify`) |

---

## 🚀 5. Local Setup & Execution Guide

### Prerequisites
- JDK 17 installed (`java -version`)
- Apache Maven 3.9+ installed (`mvn -version`)

### Step-by-Step Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Mrmubeen001/MubeenMart.git
   cd MubeenMart
   ```

2. **Configure Environment:**
   Copy the example environment file:
   ```bash
   cp .env.example .env
   ```
   *(Defaults are pre-configured to use H2 in-memory mode with auto-seeding for immediate execution)*

3. **Run Automated Test Suite:**
   ```bash
   mvn clean test
   ```
   *Executes 30+ comprehensive DAO, Service, and Controller unit tests against an embedded in-memory database.*

4. **Package Application WAR:**
   ```bash
   mvn clean package
   ```
   *Builds the deployable web archive: `target/mubeenmart.war`.*

5. **Deploy to Apache Tomcat 9:**
   - Copy `target/mubeenmart.war` to your Tomcat installation's `webapps/` directory:
     ```bash
     cp target/mubeenmart.war $CATALINA_HOME/webapps/
     ```
   - Start Tomcat:
     ```bash
     $CATALINA_HOME/bin/startup.sh   # (or startup.bat on Windows)
     ```
   - Open your browser at:
     ```
     http://localhost:8080/mubeenmart/
     ```

---

## 🔑 6. Evaluation Seed Accounts

For evaluation and testing, the database automatically initializes with pre-configured seed accounts:

| Role | Email Address | Password | Permissions |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@mubeenmart.com` | `Password@123` | Platform oversight, user lists, listing moderation |
| **Seller** | `seller@mubeenmart.com` | `Password@123` | Create/edit listings, sales dashboard, dispatch orders |
| **Buyer** | `buyer@mubeenmart.com` | `Password@123` | Browse catalog, shopping cart, checkout, reviews |

---

## 🔒 7. Security & Compliance Checklist

- [x] **PreparedStatement Only:** Zero concatenated queries (`grep -rn "Statement)" src/` confirms only `PreparedStatement` usage).
- [x] **jBCrypt Password Hashing:** 10-round salted Blowfish hashing; passwords never logged or stored in plaintext.
- [x] **Session Fixation Defense:** `request.changeSessionId()` called upon login; 30-minute explicit timeout.
- [x] **XSS Prevention:** All user-supplied output escaped with `<c:out>` before JSP rendering.
- [x] **Resource Leak Prevention:** Strict try-with-resources for every `Connection`, `PreparedStatement`, and `ResultSet`.
- [x] **Information Concealment:** Custom error pages (400, 403, 404, 500) prevent stack trace exposure.
- [x] **Credential Isolation:** `.env` and `config.properties` excluded via `.gitignore`.
- [x] **AI Guardrails:** Per-session rate limiter (10 req/min), input length caps, server-side prompt template, and offline degradation fallback.

---

## 📈 8. Milestones & Delivery Documentation

- [Final Project Report](docs/FINAL_REPORT.md) — Comprehensive architecture, technical trade-offs, and design patterns.
- [Presentation Slide Deck](docs/SLIDE_DECK.md) — Problem statement, architecture sketch, completed deliverables.
- [Rehearsed Demo Script](docs/DEMO_SCRIPT.md) — Step-by-step evaluator walkthrough script.
- [Manual Test Case Sheet](docs/TEST_CASES.md) — Documented manual test execution verification.
- [Sprint Retrospectives](RETRO.md) — Sprints 1 to 5 retrospectives.
- [Release Changelog](CHANGELOG.md) — Version tracking across SemVer tags (`v0.1.0` &rarr; `v1.1.0`).

---

**Developed with academic integrity by Mohamed Mubeen**  
*Anna University Regulations 2025*
