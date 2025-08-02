-- -- Create products table
-- -- Using JSON for flexible product data (H2 compatible)
-- CREATE TABLE products (
--                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
--                           title VARCHAR(255) NOT NULL,
--                           price DECIMAL(10,2) NOT NULL,
--                           handle VARCHAR(255) NOT NULL UNIQUE,
--                           product_type VARCHAR(100),
--                           vendor VARCHAR(100),
--                           tags JSON, -- ← Changed to JSON for List<String>
--                           variants TEXT,
--                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );
--
-- -- Index for better search performance
-- CREATE INDEX idx_products_title ON products(title);
-- CREATE INDEX idx_products_handle ON products(handle);
-- CREATE INDEX idx_products_vendor ON products(vendor);

-- Create products table for PostgreSQL
CREATE TABLE products (
                          id SERIAL PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          price DECIMAL(10,2) NOT NULL,
                          handle VARCHAR(255) NOT NULL UNIQUE,
                          product_type VARCHAR(100),
                          vendor VARCHAR(100),
                          tags TEXT,
                          variants TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index for better search performance
CREATE INDEX idx_products_title ON products(title);
CREATE INDEX idx_products_handle ON products(handle);
CREATE INDEX idx_products_vendor ON products(vendor);