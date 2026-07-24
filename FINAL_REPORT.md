# MubeenMart — Comprehensive Project Final Report
**Anna University Regulations 2025, Semester 3**  
**Course Project:** Java Servlets, JDBC, Apache Tomcat  
**Student Name:** Mohamed Mubeen  
**Project Title:** MubeenMart — Multi-Seller E-Commerce Marketplace  
**Evaluation Date:** October 10, 2026  

---

## 1. Executive Summary & Problem Definition
In contemporary web applications, multi-seller platforms require rigorous isolation between data access, business logic, and presentation tiers. **MubeenMart** is designed and implemented as an enterprise-grade multi-tenant e-commerce web application running natively on **Java Servlets (Servlet 4.0)**, **JDBC**, and **Apache Tomcat 9.0.x**. 

The solution enables independent merchants to list and manage merchandise, customers to discover goods, manage carts, and execute mock transactions, and administrators to govern the ecosystem, all while operating without heavyweight modern abstraction frameworks like Spring Boot or Hibernate to highlight fundamental architectural competencies.

---

## 2. System Architecture & Layered MVC Pattern
The application strictly applies the **Layered MVC with Front Controller** architecture pattern:

1. **Presentation Layer (JSP + JSTL + CSS + Vanilla JS):** 
   - Rendered using JSP 2.3 with JSTL taglibs.
   - All dynamic user data is escaped via `<c:out>` or `fn:escapeXml` to prevent Cross-Site Scripting (XSS).
   - Interactive components (Quick Add to Cart, Floating AI Support Widget) utilize ES6 `fetch()` communicating with JSON REST endpoints versioned under `/api/v1/`.

2. **Filter & Security Interceptors:**
   - `EncodingFilter`: Enforces UTF-8 character encoding on all requests and responses.
   - `LoggingFilter`: Intercepts incoming requests, generates a unique request correlation ID, binds it to SLF4J MDC, and logs processing latency.
   - `AuthFilter`: Enforces Session verification and Role-Based Access Control (RBAC) across `/buyer/*`, `/seller/*`, `/admin/*`, and protected `/api/v1/*` endpoints.

3. **Controller Layer (Thin Servlets):**
   - Servlets handle HTTP orchestration, query parameter parsing, session validation, and status code dispatching.
   - Zero SQL queries or business rules exist in servlets, maintaining Single Responsibility (SRP).

4. **Service Layer (Domain Business Rules):**
   - Implements business logic and transactional orchestration.
   - Input validation is conducted at the top of every method prior to any DAO invocation.
   - Governs ACID transaction boundaries across order placement, stock deduction, and cart cleanup.

5. **Data Access Object (DAO) Layer:**
   - All SQL operations are isolated within DAO classes.
   - Every query strictly utilizes `PreparedStatement` to prevent SQL Injection.
   - Every `Connection`, `PreparedStatement`, and `ResultSet` is managed using Java try-with-resources.

6. **Connection Pooling & Persistence:**
   - High-performance **HikariCP** connection pool lifecycle owned exclusively by `AppContextListener`.
   - Persistence backed by **H2 Database** operating in server mode for production and embedded in-memory mode for development and testing.

---

## 3. Design Patterns Applied & Documented (Section 12)

| Design Pattern | Purpose & Implementation in Code |
| :--- | :--- |
| **DAO Pattern** | Data access abstraction separating persistence logic from domain services (`UserDAO`, `ProductDAO`, `OrderDAO`, `CartDAO`, `ReviewDAO`, `WishlistDAO`). |
| **Front / Resource Controller** | Centralized dispatching and HTTP lifecycle management across thin servlets (`ProductServlet`, `OrderServlet`, `SellerServlet`, etc.). |
| **Singleton Pattern** | Thread-safe, single-instance DataSource pool lifecycle managed via `AppContextListener` and accessed through `DatabaseConnectionManager`. |
| **Factory Pattern** | Encapsulated instantiation of DAO singletons through `DAOFactory` and payment handlers via `PaymentStrategyFactory`. |
| **Strategy Pattern** | Swappable payment confirmation algorithms implementing `PaymentStrategy`: `MockUpiPayment`, `MockCreditCardPayment`, and `MockNetBankingPayment`. |
| **Builder Pattern** | Type-safe construction of unified REST response envelopes (`ApiResponse.builder()`) ensuring consistent `{ "success": true, "data": ..., "error": null }` shapes. |

---

## 4. Entity-Relationship & Database Design
The schema design satisfies 3rd Normal Form (3NF) and includes strict constraints:
- **Foreign Key Indexing:** Every foreign key (`seller_id`, `buyer_id`, `order_id`, `product_id`, `user_id`) is explicitly indexed for indexed join performance.
- **Monetary Precision:** Stored strictly as `DECIMAL(10,2)`. Float and double types are prohibited to prevent IEEE floating-point rounding errors.
- **Audit Timestamps:** `created_at` timestamp present on every table.
- **Referential Integrity:** Enforced via foreign key constraints with appropriate cascade triggers.

*(Refer to [D1_ER_Diagram.svg](diagrams/D1_ER_Diagram.svg) for visual schema topology)*

---

## 5. Sequence Workflow: Place Order Transaction (D3)
The order placement flow enforces transactional atomicity:
1. **Client Request:** Browser transmits `POST /checkout` with shipping address and mock payment selection.
2. **Session & Stock Validation:** `AuthFilter` validates the active session; `OrderService` verifies cart items and stock quantities.
3. **Strategy Execution:** `PaymentStrategyFactory` resolves the selected channel (`UPI`, `Card`, `NetBanking`) and executes mock authorization.
4. **Transaction Boundary:** `OrderService` acquires a connection and disables auto-commit (`conn.setAutoCommit(false)`).
5. **Atomic Execution:**
   - Order row inserted into `orders`.
   - Line items inserted into `order_items`.
   - Inventory decremented atomically: `UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?`.
   - Cart cleared: `DELETE FROM cart_items WHERE user_id = ?`.
6. **Commit & Redirect:** `conn.commit()` commits the changes; buyer is redirected to `/orders?placedOrderId={id}`.

*(Refer to [D3_Sequence_Diagram.svg](diagrams/D3_Sequence_Diagram.svg) for detailed message flow)*

---

## 6. AI Feature Integration (Phase 3 Deliverable)
The customer assistant widget fulfills Section 11 & 17 specifications:
- **Interface Decoupling:** Implemented via `ChatProvider` interface.
- **Implementations:** `GeminiChatProvider` (server-side Gemini 1.5 Flash API calls) and `MockChatProvider` (zero-latency domain FAQ replies).
- **Configuration Switch:** Controlled via `ai.chatbot.provider=gemini|mock` in `config.properties` / `.env`.
- **Safety Guardrails:**
  - 10 requests per minute sliding-window rate limit per session (`ChatRateLimiter`).
  - 500-character input length cap.
  - Server-side fixed prompt template restricting queries to e-commerce domain.
  - In-memory per-session query caching for repeated questions.
  - Outbound timeout and graceful offline fallback degradation.

---

## 7. Known Limitations & Future Roadmap
1. **Full-Text Search Engine:** Current keyword matching uses SQL `LIKE %query%`. Transition to Apache Lucene or Elasticsearch would enhance typo tolerance and relevance ranking.
2. **Asynchronous Notification Queue:** Email / SMS order notifications currently operate synchronously in mock mode; adding an Apache ActiveMQ or RabbitMQ message queue would enhance throughput under heavy concurrency.
3. **Multi-Region Caching:** Utilizing Redis for distributed session storage and catalog caching would enable horizontal scaling across multiple Tomcat nodes.

---

**Submitted by Mohamed Mubeen**  
*Anna University Department of Computer Science & Engineering*
