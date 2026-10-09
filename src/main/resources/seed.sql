-- MubeenMart Seed Data Script
-- Anna University R2025 Semester 3
-- Pre-populated users, products, orders, items, reviews, and wishlist

-- 1. Users
-- Default password for all seed accounts is: Password@123
INSERT INTO users (id, name, email, password_hash, role, created_at) VALUES
(1, 'Admin Mubeen', 'admin@mubeenmart.com', '$2a$10$vEuAcVc0DHKelwMydW2KFe/aGMkWMJzJ4sBc7VeAHiF74Jcqp6FkG', 'ADMIN', TIMESTAMP '2026-07-28 10:00:00'),
(2, 'Apex Electronics Store', 'seller@mubeenmart.com', '$2a$10$vEuAcVc0DHKelwMydW2KFe/aGMkWMJzJ4sBc7VeAHiF74Jcqp6FkG', 'SELLER', TIMESTAMP '2026-07-29 11:30:00'),
(3, 'Urban Artisan Collective', 'artisan@mubeenmart.com', '$2a$10$vEuAcVc0DHKelwMydW2KFe/aGMkWMJzJ4sBc7VeAHiF74Jcqp6FkG', 'SELLER', TIMESTAMP '2026-07-30 09:15:00'),
(4, 'Mohamed Buyer', 'buyer@mubeenmart.com', '$2a$10$vEuAcVc0DHKelwMydW2KFe/aGMkWMJzJ4sBc7VeAHiF74Jcqp6FkG', 'BUYER', TIMESTAMP '2026-08-01 14:20:00'),
(5, 'Priya Sharma', 'priya@example.com', '$2a$10$vEuAcVc0DHKelwMydW2KFe/aGMkWMJzJ4sBc7VeAHiF74Jcqp6FkG', 'BUYER', TIMESTAMP '2026-08-02 16:45:00'),
(6, 'Apex Electronics Store (Alt)', 'seller1@mubeenmart.com', '$2a$10$vEuAcVc0DHKelwMydW2KFe/aGMkWMJzJ4sBc7VeAHiF74Jcqp6FkG', 'SELLER', TIMESTAMP '2026-07-29 11:30:00'),
(7, 'Mohamed Buyer (Alt)', 'buyer1@mubeenmart.com', '$2a$10$vEuAcVc0DHKelwMydW2KFe/aGMkWMJzJ4sBc7VeAHiF74Jcqp6FkG', 'BUYER', TIMESTAMP '2026-08-01 14:20:00');

-- 2. Products
INSERT INTO products (id, seller_id, name, description, price, stock_qty, category, image_url, created_at) VALUES
(1, 2, 'Aura Wireless ANC Headphones', 'Premium over-ear active noise-cancelling headphones with 40-hour battery life and high-fidelity sound.', 149.99, 25, 'Electronics', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80', TIMESTAMP '2026-08-03 10:00:00'),
(2, 2, 'Pulse Pro Smart Fitness Watch', 'Waterproof AMOLED fitness tracker with heart-rate, SpO2, sleep tracking, and built-in GPS.', 89.50, 40, 'Electronics', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80', TIMESTAMP '2026-08-03 11:30:00'),
(3, 2, 'Tactile Mechanical Gaming Keyboard', 'Compact 75% hot-swappable mechanical keyboard with custom pre-lubed brown switches and RGB backlight.', 74.99, 18, 'Electronics', 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop&q=80', TIMESTAMP '2026-08-04 12:00:00'),
(4, 2, 'Ultra-Slim 10000mAh Power Bank', 'Fast-charging USB-C portable charger with LED power indicator and aircraft-grade aluminum casing.', 34.99, 50, 'Electronics', 'https://images.unsplash.com/photo-1609592424368-24d18ecad031?w=600&auto=format&fit=crop&q=80', TIMESTAMP '2026-08-04 14:15:00'),
(5, 3, 'Organic Combed Cotton Crew Tee', '100% sustainable organic ringspun cotton regular-fit tee with double-stitched hem.', 24.50, 60, 'Fashion', 'https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=600&auto=format&fit=crop&q=80', TIMESTAMP '2026-08-05 09:30:00'),
(6, 3, 'Waterproof Commuter Daypack', 'Weatherproof rolltop backpack with padded 16-inch laptop compartment and ergonomic straps.', 49.99, 30, 'Fashion', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600&auto=format&fit=crop&q=80', TIMESTAMP '2026-08-05 10:45:00'),
(7, 3, 'Polarized UV400 Aviator Sunglasses', 'Lightweight titanium frame sunglasses with scratch-resistant gradient polarized lenses.', 29.99, 35, 'Fashion', 'https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=600&auto=format&fit=crop&q=80', TIMESTAMP '2026-08-06 15:00:00'),
(8, 3, 'Artisan Ceramic Mug Set (Pair)', 'Handcrafted speckled ceramic coffee mugs with heat-insulating thick walls, 350ml capacity.', 22.00, 45, 'Home', 'https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=600&auto=format&fit=crop&q=80', TIMESTAMP '2026-08-06 16:30:00'),
(9, 2, 'Architect LED Ergonomic Desk Lamp', 'Stepless dimmable eye-caring desk lamp with 5 color temperatures, memory mode and USB port.', 42.50, 20, 'Home', 'https://images.unsplash.com/photo-1534349762230-e0cadf78f5da?w=600&auto=format&fit=crop&q=80', TIMESTAMP '2026-08-07 11:20:00'),
(10, 3, 'Ultrasonic Ceramic Aroma Diffuser', 'Whisper-quiet essential oil diffuser with ambient warm light and automatic shutoff timer.', 28.00, 25, 'Home', 'https://images.unsplash.com/photo-1608571423902-eed4a5ad8108?w=600&auto=format&fit=crop&q=80', TIMESTAMP '2026-08-07 14:00:00'),
(11, 2, 'Clean Architecture in Practice', 'Comprehensive engineering guide to clean software architecture, hexagonal boundaries, and SOLID principles.', 39.99, 15, 'Books', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80', TIMESTAMP '2026-08-08 10:10:00'),
(12, 2, 'Modern Java Design Patterns', 'In-depth reference on real-world Java concurrency, JDBC patterns, servlet architectures, and best practices.', 45.00, 12, 'Books', 'https://images.unsplash.com/photo-1532012164546-f432f2e3777a?w=600&auto=format&fit=crop&q=80', TIMESTAMP '2026-08-08 11:40:00');

-- 3. Orders
INSERT INTO orders (id, buyer_id, status, total_amount, created_at) VALUES
(1, 4, 'DELIVERED', 149.99, TIMESTAMP '2026-08-12 14:30:00'),
(2, 5, 'SHIPPED', 74.49, TIMESTAMP '2026-08-15 16:00:00'),
(3, 4, 'CONFIRMED', 89.50, TIMESTAMP '2026-08-18 10:20:00');

-- 4. Order Items
INSERT INTO order_items (id, order_id, product_id, quantity, unit_price, created_at) VALUES
(1, 1, 1, 1, 149.99, TIMESTAMP '2026-08-12 14:30:00'),
(2, 2, 5, 1, 24.50, TIMESTAMP '2026-08-15 16:00:00'),
(3, 2, 6, 1, 49.99, TIMESTAMP '2026-08-15 16:00:00'),
(4, 3, 2, 1, 89.50, TIMESTAMP '2026-08-18 10:20:00');

-- 5. Reviews
INSERT INTO reviews (id, product_id, user_id, rating, comment, created_at) VALUES
(1, 1, 4, 5, 'Outstanding sound isolation and battery longevity. Exactly what I needed for studying and commute!', TIMESTAMP '2026-08-14 18:20:00'),
(2, 5, 5, 4, 'Comfortable breathable fabric and high quality stitch. Fits true to size.', TIMESTAMP '2026-08-17 12:15:00'),
(3, 6, 5, 5, 'Very spacious laptop bag, withstood heavy rain during transit without leaking. Highly recommend!', TIMESTAMP '2026-08-18 09:40:00');

-- 6. Cart Items (Buyer 4 active cart)
INSERT INTO cart_items (id, user_id, product_id, quantity, created_at) VALUES
(1, 4, 4, 1, TIMESTAMP '2026-08-20 17:00:00');

-- 7. Wishlist Items (Buyer 4 wishlist)
INSERT INTO wishlist_items (id, user_id, product_id, created_at) VALUES
(1, 4, 3, TIMESTAMP '2026-08-20 17:05:00');
