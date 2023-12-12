
INSERT INTO Product (title, price, serial_number) VALUES
                                                      ('Laptop', 999.99, 'ABC123'),
                                                      ('T-shirt', 19.99, 'XYZ456'),
                                                      ('Sofa', 499.99, '123XYZ'),
                                                      ('Smartphone', 699.99, 'DEF789'),
                                                      ('Jeans', 29.99, 'LMN101'),
                                                      ('TV', 799.99, '456JKL'),
                                                      ('Running Shoes', 59.99, 'OPQ202'),
                                                      ('Coffee Table', 129.99, 'UVW303'),
                                                      ('Headphones', 89.99, 'XYZ404'),
                                                      ('Desk Chair', 149.99, 'ABC505'),
                                                      ('Digital Camera', 399.99, 'JKL606'),
                                                      ('Backpack', 39.99, 'DEF707'),
                                                      ('Refrigerator', 899.99, 'MNO808'),
                                                      ('Dress', 49.99, 'PQR909'),
                                                      ('Microwave', 129.99, 'STU010');

-- Dummy data for Category table
INSERT INTO Category (name) VALUES
                                ('Electronics'),
                                ('Clothing'),
                                ('Furniture'),
                                ('Appliances'),
                                ('Footwear'),
                                ('Home Decor'),
                                ('Audio'),
                                ('Office Furniture'),
                                ('Fashion Accessories'),
                                ('Home Appliances'),
                                ('Cameras'),
                                ('Bags'),
                                ('Kitchen Appliances'),
                                ('Formal Wear'),
                                ('Small Electronics');

-- Dummy data for Campaign table
INSERT INTO Campaign (name, start_date, bid, is_active) VALUES
                                                            ('Holiday Sale', '2023-12-20', 100.00, true),
                                                            ('Year-End Clearance', '2023-12-15', 50.00, true),
                                                            ('Black Friday Deals', '2023-12-10', 75.00, true),
                                                            ('Summer Clearance', '2023-12-10', 30.00, true),
                                                            ('Back-to-School Sale', '2023-12-10', 40.00, true),
                                                            ('Spring Fashion Sale', '2023-12-10', 60.00, true),
                                                            ('Home Makeover Event', '2023-12-10', 90.00, true),
                                                            ('Gaming Gear Promo', '2023-12-10', 55.00, true),
                                                            ('Fitness Essentials Sale', '2023-12-10', 45.00, true),
                                                            ('Valentines Day Special', '2023-12-14', 70.00, true),
                                                            ('Tech Expo Discounts', '2023-10-01', 80.00, true),
                                                            ('Outdoor Adventure Sale', '2023-05-01', 65.00, true),
                                                            ('Autumn Home Decor', '2023-12-16', 85.00, true),
                                                            ('Winter Fashion Showcase', '2023-12-25', 55.00, true),
                                                            ('New Years Clearance', '2023-12-31', 60.00, true);

-- Dummy data for Product_Category table (linking products to categories)
INSERT INTO Product_Category (product_id, category_id) VALUES
                                                           (1, 1), (2, 2), (3, 3), (4, 1), (5, 2),
                                                           (6, 1), (7, 5), (8, 3), (9, 6), (10, 8),
                                                           (11, 1), (12, 11), (13, 4), (14, 2), (15, 4);

-- Dummy data for Campaign_Product table (linking products to campaigns)
INSERT INTO Product_Campaign (product_id, campaign_id) VALUES
                                                           (1, 1), (2, 2), (3, 1), (4, 3), (5, 2),
                                                           (6, 3), (7, 5), (8, 4), (9, 6), (10, 8),
                                                           (11, 7), (12, 10), (13, 4), (14, 9), (15, 11);