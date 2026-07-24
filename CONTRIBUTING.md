# Contributing to MubeenMart

Thank you for contributing to the MubeenMart project! This document outlines guidelines and instructions for developers.

---

## 🚀 Quickstart: Clone to Running Local Instance

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/Mrmubeen001/MubeenMart.git
   cd MubeenMart
   ```

2. **Verify Java & Maven Versions:**
   ```bash
   java -version    # OpenJDK 17 LTS required
   mvn -version     # Apache Maven 3.9+ required
   ```

3. **Configure Environment:**
   ```bash
   cp .env.example .env
   ```

4. **Compile and Run Tests:**
   ```bash
   mvn clean verify
   ```

5. **Deploy to Apache Tomcat 9:**
   ```bash
   cp target/mubeenmart.war $CATALINA_HOME/webapps/
   $CATALINA_HOME/bin/startup.sh
   ```
   Navigate to: `http://localhost:8080/mubeenmart/`

---

## 🌿 Branching Model & Pull Requests (Section 8 & 15)

- **`main` Branch:** Always production-ready and deployable.
- **Feature Branches:** Create branches following the convention `feature/<name>` (e.g. `feature/auth-and-dao`, `feature/cart-checkout`).
- **Commits:** Follow the **Conventional Commits** specification:
  - `feat:` for new capabilities
  - `fix:` for bug fixes
  - `test:` for test additions and updates
  - `docs:` for documentation and diagram changes

---

## ✅ Definition of Done (Section 19)

Every PR must satisfy:
- [x] Code compiles with zero major Checkstyle or SpotBugs warnings.
- [x] All unit and DAO tests passing against embedded H2.
- [x] PreparedStatement strictly used for every SQL query.
- [x] Output properly escaped via `<c:out>`.
- [x] DB migrations added if schema modified.
- [x] README or API docs updated.
