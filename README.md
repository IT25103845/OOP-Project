# FreshMart - Component 5: Payment & Checkout

This ZIP contains only the **Payment & Checkout** component separated from the original FreshMart website.

## Features included

- Checkout page
- Delivery address form
- Card payment option
- Cash on delivery option
- Client-side card formatting preview
- Server-side card validation
- Order total summary from cart items
- Instant order confirmation after checkout
- Confirmation page with paid items and total
- File-based storage using `carts.txt`, `orders.txt`, `products.txt`, and `users.txt`
- Minimal login and cart support required to test checkout

## Test accounts

- Customer: `john@email.com` / `pass123`
- Admin data is present only as seed data: `admin@freshmart.com` / `admin123`

## Main URLs

- Login: `http://localhost:8080/login`
- Checkout: `http://localhost:8080/checkout`
- Cart support page: `http://localhost:8080/cart`

## Run

```bash
mvn spring-boot:run
```

or, if Maven is available through the wrapper script:

```bash
./mvnw spring-boot:run
```

The app starts on port `8080`.

## Testing checkout

1. Log in with `john@email.com` / `pass123`.
2. Open `/checkout`.
3. The project includes a demo cart for John by default.
4. Choose **Card Payment** and enter test card details such as:
   - Card number: `1234 5678 9012 3456`
   - Expiry: `12/30`
   - CVV: `123`
5. Submit checkout to create a confirmed order.

If the cart is empty, open `/cart` and click **Load Demo Cart**.

## Data files

Runtime data is stored in:

```text
data/users.txt
data/products.txt
data/carts.txt
data/orders.txt
```

Bundled seed data is stored in:

```text
src/main/resources/data/users.txt
src/main/resources/data/products.txt
src/main/resources/data/carts.txt
src/main/resources/data/orders.txt
```

## Component boundary

This package focuses on Component 5 only. Authentication and cart/product/order services are included only as minimal support so the checkout workflow can be tested end-to-end. Product Management, Shopping Cart management, Order Management screens, and Admin Dashboard screens are intentionally excluded.
