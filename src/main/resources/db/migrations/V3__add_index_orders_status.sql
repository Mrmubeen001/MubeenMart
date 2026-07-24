-- V3__add_index_orders_status.sql
-- Add performance indexes for order status queries and product category queries
-- Anna University R2025 Semester 3

CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at);
