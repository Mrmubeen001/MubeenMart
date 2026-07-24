# MubeenMart — Rehearsed Demonstration Script
**Presenter:** Mohamed Mubeen  
**Duration:** ~3 Minutes &bull; **Audience:** Faculty Evaluation Committee  

---

## 🎬 Demonstration Walkthrough

### Part 1: Storefront Discovery & Buyer Journey (0:00 – 1:00)
1. **Homepage & Catalog Filtering:**
   - Open `http://localhost:8080/mubeenmart/`.
   - Point out brand header, category filter chips (Electronics, Fashion, Books, Home), and keyword search bar.
   - Type "Headphones" into the search bar and filter by "Electronics". Demonstrate instant matching.
2. **Product Details & Verified Review Restriction:**
   - Click on the product *Aura Wireless ANC Headphones*.
   - Show stock level indicator, unit price in ₹, seller attribution (*Apex Electronics Store*), and customer star ratings.
   - Point out the review notice: *"Verified Review Policy: Reviews can only be submitted after purchasing and receiving this product."*

### Part 2: Cart, Checkout & Mock Payment (1:00 – 1:45)
1. **Shopping Cart:**
   - Click **Add to Shopping Cart**. Notice the navbar cart badge increments interactively.
   - Open Cart view at `/cart`. Show line item quantity updates and running total recalculation.
2. **Checkout Flow (F5):**
   - Click **Proceed to Checkout**.
   - Enter delivery address: *"42 Anna Salai, Guindy, Chennai 600025"*.
   - Select **Mock Instant UPI** from payment strategies.
   - Click **Confirm Payment & Place Order**.
   - Show instantaneous redirection to `/orders` with generated Order ID and status badge `CONFIRMED`.

### Part 3: Seller Portal & Fulfillment Workflow (1:45 – 2:20)
1. **Switch to Seller Account:**
   - Log out and log in as `seller@mubeenmart.com` / `Password@123`.
   - Navigate to `/seller/dashboard`.
2. **Seller Metrics (O3):**
   - Show sales cards: Total Revenue Earned (₹), Total Orders Received, Units Sold, and Active Listings.
3. **Order Status Transition (O2):**
   - Locate the newly placed order in **Incoming Customer Orders**.
   - Change status from `Confirmed` &rarr; `Shipped` &rarr; `Delivered`.
4. **Product CRUD (F2):**
   - Open "+ Add New Product Listing" modal, show validation, and demonstrate adding/editing a listing.

### Part 4: Admin Portal & AI Assistant (2:20 – 3:00)
1. **Administrator Governance (F7):**
   - Log in as `admin@mubeenmart.com` / `Password@123`.
   - Open `/admin/dashboard`.
   - Showcase platform overview metrics, all registered accounts table, and listing moderation actions.
2. **AI Chatbot Assistant (O4 / Phase 3):**
   - Click the floating 💬 button at the bottom-right.
   - Click quick FAQ chips: *"How do I track my order?"* and *"What is the shipping policy?"*.
   - Type custom question: *"How do I start selling on MubeenMart?"*.
   - Demonstrate intelligent, instant response rendering with sliding-window rate limit guardrails.
