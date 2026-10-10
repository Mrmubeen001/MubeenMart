/**
 * MubeenMart — Interactive Client Application for GitHub Pages
 * Anna University R2025 Semester 3
 */

// Product Seed Data
const INITIAL_PRODUCTS = [
    {
        id: 1,
        name: "Aura Wireless ANC Headphones",
        category: "Electronics",
        price: 149.99,
        stockQty: 25,
        rating: 5.0,
        reviewsCount: 1,
        seller: "Apex Electronics Store",
        imageUrl: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80",
        description: "Premium over-ear active noise-cancelling headphones with 40-hour battery life and high-fidelity sound."
    },
    {
        id: 2,
        name: "Pulse Pro Smart Fitness Watch",
        category: "Electronics",
        price: 89.50,
        stockQty: 40,
        rating: 4.8,
        reviewsCount: 12,
        seller: "Apex Electronics Store",
        imageUrl: "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80",
        description: "Waterproof AMOLED fitness tracker with heart-rate, SpO2, sleep tracking, and built-in GPS."
    },
    {
        id: 3,
        name: "Tactile Mechanical Gaming Keyboard",
        category: "Electronics",
        price: 74.99,
        stockQty: 18,
        rating: 4.9,
        reviewsCount: 8,
        seller: "Apex Electronics Store",
        imageUrl: "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop&q=80",
        description: "Compact 75% hot-swappable mechanical keyboard with custom pre-lubed brown switches and RGB backlight."
    },
    {
        id: 4,
        name: "Ultra-Slim 10000mAh Power Bank",
        category: "Electronics",
        price: 34.99,
        stockQty: 50,
        rating: 4.7,
        reviewsCount: 19,
        seller: "Apex Electronics Store",
        imageUrl: "https://images.unsplash.com/photo-1609592424368-24d18ecad031?w=600&auto=format&fit=crop&q=80",
        description: "Fast-charging USB-C portable charger with LED power indicator and aircraft-grade aluminum casing."
    },
    {
        id: 5,
        name: "Organic Combed Cotton Crew Tee",
        category: "Fashion",
        price: 24.50,
        stockQty: 60,
        rating: 4.6,
        reviewsCount: 14,
        seller: "Urban Artisan Collective",
        imageUrl: "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=600&auto=format&fit=crop&q=80",
        description: "100% sustainable organic ringspun cotton regular-fit tee with double-stitched hem."
    },
    {
        id: 6,
        name: "Waterproof Commuter Daypack",
        category: "Fashion",
        price: 49.99,
        stockQty: 30,
        rating: 4.9,
        reviewsCount: 22,
        seller: "Urban Artisan Collective",
        imageUrl: "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600&auto=format&fit=crop&q=80",
        description: "Weatherproof rolltop backpack with padded 16-inch laptop compartment and ergonomic straps."
    },
    {
        id: 7,
        name: "Polarized UV400 Aviator Sunglasses",
        category: "Fashion",
        price: 29.99,
        stockQty: 35,
        rating: 4.5,
        reviewsCount: 7,
        seller: "Urban Artisan Collective",
        imageUrl: "https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=600&auto=format&fit=crop&q=80",
        description: "Lightweight titanium frame sunglasses with scratch-resistant gradient polarized lenses."
    },
    {
        id: 8,
        name: "Artisan Ceramic Mug Set (Pair)",
        category: "Home",
        price: 22.00,
        stockQty: 45,
        rating: 4.8,
        reviewsCount: 11,
        seller: "Urban Artisan Collective",
        imageUrl: "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=600&auto=format&fit=crop&q=80",
        description: "Handcrafted speckled ceramic coffee mugs with heat-insulating thick walls, 350ml capacity."
    },
    {
        id: 9,
        name: "Architect LED Ergonomic Desk Lamp",
        category: "Home",
        price: 42.50,
        stockQty: 20,
        rating: 4.7,
        reviewsCount: 6,
        seller: "Apex Electronics Store",
        imageUrl: "https://images.unsplash.com/photo-1534349762230-e0cadf78f5da?w=600&auto=format&fit=crop&q=80",
        description: "Stepless dimmable eye-caring desk lamp with 5 color temperatures, memory mode and USB port."
    },
    {
        id: 10,
        name: "Ultrasonic Ceramic Aroma Diffuser",
        category: "Home",
        price: 28.00,
        stockQty: 25,
        rating: 4.6,
        reviewsCount: 9,
        seller: "Urban Artisan Collective",
        imageUrl: "https://images.unsplash.com/photo-1608571423902-eed4a5ad8108?w=600&auto=format&fit=crop&q=80",
        description: "Whisper-quiet essential oil diffuser with ambient warm light and automatic shutoff timer."
    },
    {
        id: 11,
        name: "Clean Architecture in Practice",
        category: "Books",
        price: 39.99,
        stockQty: 15,
        rating: 5.0,
        reviewsCount: 18,
        seller: "Apex Electronics Store",
        imageUrl: "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80",
        description: "Comprehensive engineering guide to clean software architecture, hexagonal boundaries, and SOLID principles."
    },
    {
        id: 12,
        name: "Modern Java Design Patterns",
        category: "Books",
        price: 45.00,
        stockQty: 12,
        rating: 4.9,
        reviewsCount: 15,
        seller: "Apex Electronics Store",
        imageUrl: "https://images.unsplash.com/photo-1532012164546-f432f2e3777a?w=600&auto=format&fit=crop&q=80",
        description: "In-depth reference on real-world Java concurrency, JDBC patterns, servlet architectures, and best practices."
    }
];

// App State Management
class AppState {
    constructor() {
        this.products = INITIAL_PRODUCTS;
        this.cart = JSON.parse(localStorage.getItem('mm_cart')) || [];
        this.wishlist = JSON.parse(localStorage.getItem('mm_wishlist')) || [3]; // Pre-seeded keyboard
        this.orders = JSON.parse(localStorage.getItem('mm_orders')) || [
            {
                id: 1,
                date: "Aug 12, 2026",
                status: "DELIVERED",
                total: 149.99,
                items: [{ name: "Aura Wireless ANC Headphones", qty: 1, price: 149.99 }]
            },
            {
                id: 2,
                date: "Aug 15, 2026",
                status: "SHIPPED",
                total: 74.49,
                items: [
                    { name: "Organic Combed Cotton Crew Tee", qty: 1, price: 24.50 },
                    { name: "Waterproof Commuter Daypack", qty: 1, price: 49.99 }
                ]
            },
            {
                id: 3,
                date: "Aug 18, 2026",
                status: "CONFIRMED",
                total: 89.50,
                items: [{ name: "Pulse Pro Smart Fitness Watch", qty: 1, price: 89.50 }]
            }
        ];
        this.activeCategory = "ALL";
        this.searchQuery = "";
        this.currentUser = { name: "Mohamed Buyer", email: "buyer@mubeenmart.com", role: "BUYER" };
    }

    save() {
        localStorage.setItem('mm_cart', JSON.stringify(this.cart));
        localStorage.setItem('mm_wishlist', JSON.stringify(this.wishlist));
        localStorage.setItem('mm_orders', JSON.stringify(this.orders));
        updateNavBadges();
    }

    addToCart(productId, qty = 1) {
        const item = this.cart.find(i => i.productId === productId);
        if (item) {
            item.qty += qty;
        } else {
            this.cart.push({ productId, qty });
        }
        this.save();
    }

    updateCartQty(productId, qty) {
        if (qty <= 0) {
            this.removeFromCart(productId);
            return;
        }
        const item = this.cart.find(i => i.productId === productId);
        if (item) {
            item.qty = qty;
            this.save();
        }
    }

    removeFromCart(productId) {
        this.cart = this.cart.filter(i => i.productId !== productId);
        this.save();
    }

    toggleWishlist(productId) {
        if (this.wishlist.includes(productId)) {
            this.wishlist = this.wishlist.filter(id => id !== productId);
        } else {
            this.wishlist.push(productId);
        }
        this.save();
    }

    checkout(shippingAddress, paymentMethod) {
        if (this.cart.length === 0) return null;

        const cartItems = this.cart.map(ci => {
            const prod = this.products.find(p => p.id === ci.productId);
            return { name: prod.name, qty: ci.qty, price: prod.price };
        });

        const total = cartItems.reduce((sum, item) => sum + (item.price * item.qty), 0);
        const newOrderId = this.orders.length + 1;
        const newOrder = {
            id: newOrderId,
            date: new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }),
            status: "CONFIRMED",
            total: total,
            shippingAddress: shippingAddress,
            paymentMethod: paymentMethod,
            items: cartItems
        };

        this.orders.unshift(newOrder);
        this.cart = [];
        this.save();
        return newOrder;
    }
}

const state = new AppState();

// UI Render Helpers
function updateNavBadges() {
    const cartBadge = document.getElementById('navbarCartCount');
    if (cartBadge) {
        const count = state.cart.reduce((sum, i) => sum + i.qty, 0);
        cartBadge.textContent = count;
    }
    const wishlistBadge = document.getElementById('navbarWishlistCount');
    if (wishlistBadge) {
        wishlistBadge.textContent = state.wishlist.length;
    }
}

function renderProducts() {
    const grid = document.getElementById('productGrid');
    if (!grid) return;

    let filtered = state.products;
    if (state.activeCategory !== "ALL") {
        filtered = filtered.filter(p => p.category === state.activeCategory);
    }
    if (state.searchQuery) {
        const q = state.searchQuery.toLowerCase();
        filtered = filtered.filter(p => p.name.toLowerCase().includes(q) || p.description.toLowerCase().includes(q));
    }

    if (filtered.length === 0) {
        grid.innerHTML = `
            <div style="grid-column: 1 / -1; text-align: center; padding: 4rem 1rem; background: #fff; border-radius: 8px; border: 1px solid var(--border-color);">
                <h3>No matching products found</h3>
                <p style="color: var(--text-muted); margin-top: 0.5rem;">Try choosing another category or clearing your search query.</p>
            </div>
        `;
        return;
    }

    grid.innerHTML = filtered.map(p => {
        const inWishlist = state.wishlist.includes(p.id);
        return `
            <div class="product-card">
                <img src="${p.imageUrl}" alt="${p.name}" class="product-image" onclick="openProductDetail(${p.id})">
                <div class="product-body">
                    <span class="product-category">${p.category}</span>
                    <h3 class="product-title" onclick="openProductDetail(${p.id})">${p.name}</h3>
                    <div class="product-rating">
                        <span>★</span>
                        <strong>${p.rating.toFixed(1)}</strong>
                        <span style="color: var(--text-muted); font-size: 0.75rem;">(${p.reviewsCount})</span>
                    </div>
                    <div style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 0.5rem;">
                        Seller: <strong>${p.seller}</strong>
                    </div>
                    <div style="display: flex; justify-content: space-between; align-items: baseline; margin-top: auto;">
                        <span class="product-price">₹${p.price.toFixed(2)}</span>
                        <span class="badge badge-instock">In Stock (${p.stockQty})</span>
                    </div>
                </div>
                <div class="product-footer">
                    <button class="btn btn-primary btn-sm btn-block" onclick="handleAddToCart(${p.id}, this)">
                        🛒 Add to Cart
                    </button>
                    <button class="btn btn-secondary btn-sm" onclick="handleToggleWishlist(${p.id}, this)" title="Save to Wishlist">
                        ${inWishlist ? '❤️' : '🤍'}
                    </button>
                </div>
            </div>
        `;
    }).join('');
}

// User Actions
window.handleAddToCart = function(productId, btn) {
    state.addToCart(productId, 1);
    const orig = btn.innerHTML;
    btn.innerHTML = "✓ Added!";
    btn.classList.add('btn-secondary');
    btn.classList.remove('btn-primary');
    setTimeout(() => {
        btn.innerHTML = orig;
        btn.classList.add('btn-primary');
        btn.classList.remove('btn-secondary');
    }, 1200);
};

window.handleToggleWishlist = function(productId, btn) {
    state.toggleWishlist(productId);
    btn.innerHTML = state.wishlist.includes(productId) ? '❤️' : '🤍';
    if (document.getElementById('wishlistModal').classList.contains('open')) {
        renderWishlistModal();
    }
};

window.filterCategory = function(cat, element) {
    state.activeCategory = cat;
    document.querySelectorAll('.category-pill').forEach(el => el.classList.remove('active'));
    if (element) element.classList.add('active');
    renderProducts();
};

window.handleSearch = function(e) {
    if (e) e.preventDefault();
    const input = document.getElementById('searchInput');
    state.searchQuery = input ? input.value : '';
    const select = document.getElementById('searchCategory');
    if (select && select.value !== 'ALL') {
        state.activeCategory = select.value;
    }
    renderProducts();
};

// Modal Logic
function openModal(id) {
    const modal = document.getElementById(id);
    if (modal) modal.classList.add('open');
}

function closeModal(id) {
    const modal = document.getElementById(id);
    if (modal) modal.classList.remove('open');
}

window.closeModal = closeModal;

// Product Detail Modal
window.openProductDetail = function(productId) {
    const p = state.products.find(item => item.id === productId);
    if (!p) return;
    const body = document.getElementById('productDetailBody');
    body.innerHTML = `
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; align-items: start;">
            <img src="${p.imageUrl}" alt="${p.name}" style="width: 100%; border-radius: 8px; border: 1px solid var(--border-color); object-fit: cover;">
            <div>
                <span class="product-category">${p.category}</span>
                <h2 style="font-size: 1.5rem; font-weight: 700; margin-bottom: 0.5rem;">${p.name}</h2>
                <div class="product-rating" style="margin-bottom: 1rem;">
                    <span>★</span>
                    <strong>${p.rating.toFixed(1)}</strong> (${p.reviewsCount} verified reviews)
                </div>
                <div style="font-size: 1.75rem; font-weight: 700; color: var(--secondary); margin-bottom: 1rem;">
                    ₹${p.price.toFixed(2)}
                </div>
                <p style="color: #475569; font-size: 0.95rem; line-height: 1.6; margin-bottom: 1.5rem;">
                    ${p.description}
                </p>
                <div style="background: #f8fafc; padding: 1rem; border-radius: 6px; margin-bottom: 1.5rem; font-size: 0.85rem;">
                    <div>Seller: <strong>${p.seller}</strong></div>
                    <div>Stock: <strong style="color: var(--success);">${p.stockQty} units available</strong></div>
                    <div>Shipping: <strong>Free Express Delivery</strong></div>
                </div>
                <div style="display: flex; gap: 0.75rem;">
                    <button class="btn btn-primary" style="flex: 1; padding: 0.75rem;" onclick="handleAddToCart(${p.id}, this)">
                        🛒 Add to Shopping Cart
                    </button>
                    <button class="btn btn-secondary" style="padding: 0.75rem;" onclick="handleToggleWishlist(${p.id}, this)">
                        ${state.wishlist.includes(p.id) ? '❤️ In Wishlist' : '🤍 Wishlist'}
                    </button>
                </div>
            </div>
        </div>
    `;
    openModal('productDetailModal');
};

// Cart Modal
window.openCart = function() {
    renderCartModal();
    openModal('cartModal');
};

function renderCartModal() {
    const body = document.getElementById('cartModalBody');
    if (state.cart.length === 0) {
        body.innerHTML = `
            <div style="text-align: center; padding: 3rem 1rem;">
                <p style="font-size: 2rem; margin-bottom: 0.5rem;">🛒</p>
                <h3>Your Cart is Empty</h3>
                <p style="color: var(--text-muted); margin-top: 0.5rem; margin-bottom: 1.5rem;">Add some verified marketplace items to get started.</p>
                <button class="btn btn-primary" onclick="closeModal('cartModal')">Start Shopping</button>
            </div>
        `;
        return;
    }

    const items = state.cart.map(ci => {
        const prod = state.products.find(p => p.id === ci.productId);
        return { prod, qty: ci.qty, subtotal: prod.price * ci.qty };
    });

    const total = items.reduce((sum, item) => sum + item.subtotal, 0);

    body.innerHTML = `
        <div style="display: flex; flex-direction: column; gap: 1rem; margin-bottom: 1.5rem;">
            ${items.map(i => `
                <div style="display: flex; align-items: center; justify-content: space-between; padding-bottom: 1rem; border-bottom: 1px solid var(--border-color); gap: 1rem;">
                    <img src="${i.prod.imageUrl}" style="width: 50px; height: 50px; border-radius: 6px; object-fit: cover;">
                    <div style="flex: 1;">
                        <h4 style="font-size: 0.95rem; font-weight: 600;">${i.prod.name}</h4>
                        <div style="font-size: 0.8rem; color: var(--text-muted);">₹${i.prod.price.toFixed(2)} each</div>
                    </div>
                    <div style="display: flex; align-items: center; gap: 0.4rem;">
                        <button class="btn btn-secondary btn-sm" onclick="state.updateCartQty(${i.prod.id}, ${i.qty - 1}); renderCartModal();">-</button>
                        <span style="font-weight: 600; min-width: 20px; text-align: center;">${i.qty}</span>
                        <button class="btn btn-secondary btn-sm" onclick="state.updateCartQty(${i.prod.id}, ${i.qty + 1}); renderCartModal();">+</button>
                    </div>
                    <div style="font-weight: 700; min-width: 70px; text-align: right;">₹${i.subtotal.toFixed(2)}</div>
                    <button class="btn btn-secondary btn-sm" style="color: var(--danger); border: none;" onclick="state.removeFromCart(${i.prod.id}); renderCartModal();">&times;</button>
                </div>
            `).join('')}
        </div>
        <div style="background: #f8fafc; padding: 1.25rem; border-radius: 8px; border: 1px solid var(--border-color); margin-bottom: 1.5rem;">
            <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem; font-size: 0.9rem;">
                <span style="color: var(--text-muted);">Items Subtotal:</span>
                <strong>₹${total.toFixed(2)}</strong>
            </div>
            <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem; font-size: 0.9rem;">
                <span style="color: var(--text-muted);">Standard Shipping:</span>
                <strong style="color: var(--success);">FREE</strong>
            </div>
            <div style="display: flex; justify-content: space-between; padding-top: 0.75rem; border-top: 2px dashed var(--border-color); font-size: 1.25rem;">
                <strong>Running Total:</strong>
                <strong style="color: var(--primary);">₹${total.toFixed(2)}</strong>
            </div>
        </div>
        <button class="btn btn-primary btn-block" style="padding: 0.85rem; font-size: 1rem;" onclick="closeModal('cartModal'); openCheckout();">
            Proceed to Checkout &rarr;
        </button>
    `;
}

// Checkout Modal
window.openCheckout = function() {
    if (state.cart.length === 0) {
        openCart();
        return;
    }
    const items = state.cart.map(ci => {
        const prod = state.products.find(p => p.id === ci.productId);
        return { prod, qty: ci.qty, subtotal: prod.price * ci.qty };
    });
    const total = items.reduce((sum, item) => sum + item.subtotal, 0);

    const body = document.getElementById('checkoutModalBody');
    body.innerHTML = `
        <form onsubmit="handlePlaceOrder(event)">
            <h3 style="font-size: 1.1rem; font-weight: 600; margin-bottom: 0.75rem;">1. Shipping Address</h3>
            <textarea id="checkoutAddress" class="form-control" rows="2" style="width: 100%; padding: 0.65rem; border: 1px solid var(--border-color); border-radius: 6px; margin-bottom: 1.5rem;" required>42 Anna Salai, Guindy, Chennai, Tamil Nadu 600025</textarea>

            <h3 style="font-size: 1.1rem; font-weight: 600; margin-bottom: 0.75rem;">2. Payment Method</h3>
            <div style="display: flex; flex-direction: column; gap: 0.75rem; margin-bottom: 1.5rem;">
                <label style="display: flex; align-items: flex-start; gap: 0.75rem; padding: 0.75rem 1rem; border: 1px solid var(--border-color); border-radius: 6px; cursor: pointer; background: #f8fafc;">
                    <input type="radio" name="payMethod" value="Instant UPI" checked style="margin-top: 3px;">
                    <div>
                        <strong>📱 Instant UPI</strong>
                        <div style="font-size: 0.8rem; color: var(--text-muted);">Simulated Google Pay / PhonePe / BHIM</div>
                    </div>
                </label>
                <label style="display: flex; align-items: flex-start; gap: 0.75rem; padding: 0.75rem 1rem; border: 1px solid var(--border-color); border-radius: 6px; cursor: pointer;">
                    <input type="radio" name="payMethod" value="Credit/Debit Card" style="margin-top: 3px;">
                    <div>
                        <strong>💳 Credit / Debit Card</strong>
                        <div style="font-size: 0.8rem; color: var(--text-muted);">Simulated Visa, MasterCard, RuPay</div>
                    </div>
                </label>
                <label style="display: flex; align-items: flex-start; gap: 0.75rem; padding: 0.75rem 1rem; border: 1px solid var(--border-color); border-radius: 6px; cursor: pointer;">
                    <input type="radio" name="payMethod" value="Net Banking" style="margin-top: 3px;">
                    <div>
                        <strong>🏦 Net Banking</strong>
                        <div style="font-size: 0.8rem; color: var(--text-muted);">Direct bank transfer settlement</div>
                    </div>
                </label>
            </div>

            <div style="background: #f0fdf4; border: 1px solid #bbf7d0; padding: 0.75rem 1rem; border-radius: 6px; margin-bottom: 1.5rem; font-size: 0.85rem; color: #166534;">
                🛡️ <strong>Sandbox Demo Mode:</strong> Test payment authorization is simulated. No real money will be charged.
            </div>

            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; font-size: 1.2rem; font-weight: 700;">
                <span>Total Payable:</span>
                <span style="color: var(--primary);">₹${total.toFixed(2)}</span>
            </div>

            <button type="submit" class="btn btn-primary btn-block" style="padding: 0.85rem; font-size: 1.05rem;">
                ✓ Confirm Payment & Place Order
            </button>
        </form>
    `;
    openModal('checkoutModal');
};

window.handlePlaceOrder = function(e) {
    e.preventDefault();
    const address = document.getElementById('checkoutAddress').value;
    const method = document.querySelector('input[name="payMethod"]:checked').value;

    const order = state.checkout(address, method);
    closeModal('checkoutModal');
    openOrders(order.id);
};

// Orders Modal
window.openOrders = function(placedId = null) {
    renderOrdersModal(placedId);
    openModal('ordersModal');
};

function renderOrdersModal(highlightId = null) {
    const body = document.getElementById('ordersModalBody');
    if (state.orders.length === 0) {
        body.innerHTML = `
            <div style="text-align: center; padding: 3rem 1rem;">
                <p style="font-size: 2rem;">📦</p>
                <h3>No Orders Placed Yet</h3>
                <p style="color: var(--text-muted);">Your completed purchase history will appear here.</p>
            </div>
        `;
        return;
    }

    body.innerHTML = `
        ${highlightId ? `
            <div class="alert alert-success">
                🎉 Order #${highlightId} placed successfully! Payment authorization was confirmed.
            </div>
        ` : ''}
        <div style="display: flex; flex-direction: column; gap: 1.5rem;">
            ${state.orders.map(o => `
                <div style="border: 1px solid var(--border-color); border-radius: 8px; padding: 1.25rem; background: #fff;">
                    <div style="display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 0.5rem; flex-wrap: wrap; gap: 0.5rem;">
                        <div>
                            <h4 style="font-size: 1.1rem; font-weight: 700;">Order #${o.id}</h4>
                            <span style="font-size: 0.8rem; color: var(--text-muted);">Placed on ${o.date}</span>
                        </div>
                        <span class="badge badge-${o.status.toLowerCase()}">${o.status}</span>
                    </div>

                    <!-- Workflow Tracker -->
                    <div style="display: flex; justify-content: space-around; margin: 1rem 0; padding: 0.75rem; background: #f8fafc; border-radius: 6px; text-align: center; font-size: 0.8rem;">
                        <span style="opacity: 1;">📝 Pending</span>
                        <span>&rarr;</span>
                        <span style="opacity: ${o.status === 'CONFIRMED' || o.status === 'SHIPPED' || o.status === 'DELIVERED' ? '1' : '0.4'}; font-weight: 600; color: var(--primary);">✅ Confirmed</span>
                        <span>&rarr;</span>
                        <span style="opacity: ${o.status === 'SHIPPED' || o.status === 'DELIVERED' ? '1' : '0.4'};">🚚 Shipped</span>
                        <span>&rarr;</span>
                        <span style="opacity: ${o.status === 'DELIVERED' ? '1' : '0.4'};">📦 Delivered</span>
                    </div>

                    <div style="display: flex; flex-direction: column; gap: 0.4rem; font-size: 0.875rem; border-top: 1px solid var(--border-color); padding-top: 0.75rem;">
                        ${o.items.map(it => `
                            <div style="display: flex; justify-content: space-between;">
                                <span>${it.qty} &times; ${it.name}</span>
                                <strong>₹${(it.price * it.qty).toFixed(2)}</strong>
                            </div>
                        `).join('')}
                    </div>

                    <div style="display: flex; justify-content: space-between; margin-top: 0.75rem; padding-top: 0.75rem; border-top: 1px dashed var(--border-color); font-weight: 700;">
                        <span>Total Amount:</span>
                        <span style="color: var(--primary);">₹${o.total.toFixed(2)}</span>
                    </div>
                </div>
            `).join('')}
        </div>
    `;
}

// Wishlist Modal
window.openWishlist = function() {
    renderWishlistModal();
    openModal('wishlistModal');
};

function renderWishlistModal() {
    const body = document.getElementById('wishlistModalBody');
    if (state.wishlist.length === 0) {
        body.innerHTML = `
            <div style="text-align: center; padding: 3rem 1rem;">
                <p style="font-size: 2rem;">🤍</p>
                <h3>Your Wishlist is Empty</h3>
                <p style="color: var(--text-muted); margin-top: 0.5rem; margin-bottom: 1.5rem;">Click the heart icon on any product to save it here for later.</p>
                <button class="btn btn-primary" onclick="closeModal('wishlistModal')">Browse Marketplace</button>
            </div>
        `;
        return;
    }

    const items = state.wishlist.map(id => state.products.find(p => p.id === id)).filter(Boolean);

    body.innerHTML = `
        <div style="display: flex; flex-direction: column; gap: 1rem;">
            ${items.map(p => `
                <div style="display: flex; align-items: center; justify-content: space-between; padding-bottom: 1rem; border-bottom: 1px solid var(--border-color); gap: 1rem;">
                    <img src="${p.imageUrl}" style="width: 50px; height: 50px; border-radius: 6px; object-fit: cover;">
                    <div style="flex: 1;">
                        <h4 style="font-size: 0.95rem; font-weight: 600;">${p.name}</h4>
                        <div style="font-size: 0.85rem; color: var(--secondary); font-weight: 700;">₹${p.price.toFixed(2)}</div>
                    </div>
                    <button class="btn btn-primary btn-sm" onclick="state.addToCart(${p.id}, 1); state.toggleWishlist(${p.id}); renderWishlistModal();">
                        🛒 Move to Cart
                    </button>
                    <button class="btn btn-secondary btn-sm" style="color: var(--danger);" onclick="state.toggleWishlist(${p.id}); renderWishlistModal();">
                        &times;
                    </button>
                </div>
            `).join('')}
        </div>
    `;
}

// Architecture & Spec Modal
window.openArchitecture = function() {
    openModal('architectureModal');
};

// AI Chatbot Assistant Widget Logic
const FAQ_ANSWERS = {
    "track": "You can track your order status live under the 'Orders' tab in the navbar. We support a transparent 4-stage tracking workflow: Pending → Confirmed → Shipped → Delivered.",
    "shipping": "We offer Free Express Delivery on all orders. Standard transit time is 2-4 business days across India with real-time package updates.",
    "seller": "To become a seller on MubeenMart, click on your profile in the top right to switch to the Seller Portal, where you can list products, manage inventory, and view sales revenue.",
    "payment": "We support Simulated Instant UPI (Google Pay, PhonePe, BHIM), Credit/Debit Cards (Visa, MasterCard, RuPay), and Net Banking. All transactions run in secure academic sandbox mode.",
    "return": "We provide a 7-day hassle-free return and replacement policy for all verified purchases. Simply visit your Orders history to initiate a claim.",
    "mubeen": "MubeenMart is developed by Mohamed Mubeen according to Anna University R2025 regulations, utilizing Java 17, Servlets, JDBC, and Apache Tomcat."
};

function initChatbot() {
    const btn = document.getElementById('chatWidgetBtn');
    const panel = document.getElementById('chatPanel');
    const close = document.getElementById('chatCloseBtn');
    const form = document.getElementById('chatForm');
    const input = document.getElementById('chatInput');
    const messages = document.getElementById('chatMessages');

    if (!btn || !panel) return;

    btn.addEventListener('click', () => {
        panel.classList.toggle('open');
    });

    close.addEventListener('click', () => {
        panel.classList.remove('open');
    });

    // Chips
    document.querySelectorAll('.chip-btn').forEach(chip => {
        chip.addEventListener('click', () => {
            const query = chip.getAttribute('data-query');
            sendChatMessage(query);
        });
    });

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        const msg = input.value.trim();
        if (!msg) return;
        input.value = '';
        sendChatMessage(msg);
    });

    function sendChatMessage(text) {
        // Append user msg
        const userDiv = document.createElement('div');
        userDiv.className = 'chat-msg user';
        userDiv.textContent = text;
        messages.appendChild(userDiv);
        messages.scrollTop = messages.scrollHeight;

        // Determine bot response
        const lower = text.toLowerCase();
        let reply = "I can help you with order tracking, shipping policies, seller registration, or mock payment checkout. Feel free to ask!";
        for (const [key, val] of Object.entries(FAQ_ANSWERS)) {
            if (lower.includes(key)) {
                reply = val;
                break;
            }
        }

        setTimeout(() => {
            const botDiv = document.createElement('div');
            botDiv.className = 'chat-msg bot';
            botDiv.textContent = reply;
            messages.appendChild(botDiv);
            messages.scrollTop = messages.scrollHeight;
        }, 500);
    }
}

// App Initialization
document.addEventListener('DOMContentLoaded', () => {
    renderProducts();
    updateNavBadges();
    initChatbot();
});
