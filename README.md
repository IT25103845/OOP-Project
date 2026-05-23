# FreshMart - Component 4: Order Management

This ZIP contains only the **Order Management** component separated from the original FreshMart website.

## Features included

- Customer order history
- Order detail view
- Order cancellation for non-delivered orders
- Admin order dashboard
- Admin order status update
- Revenue and pending-order summary
- File-based order storage using `orders.txt`
- Minimal login/session support required to test the order module

## Test accounts

- Admin: `admin@freshmart.com` / `admin123`
- Customer: `john@email.com` / `pass123`

## Main URLs

- Login: `http://localhost:8080/login`
- Customer orders: `http://localhost:8080/orders`
- Admin orders: `http://localhost:8080/orders/admin`

## Run

```bash
mvn spring-boot:run
```

The app starts on port `8080`.

## Data files

Runtime data is stored in:

```text
data/users.txt
data/orders.txt
```

Bundled seed data is stored in:

```text
src/main/resources/data/users.txt
src/main/resources/data/orders.txt
```

## Component boundary

This package focuses on Component 4 only. Authentication is included only as minimal support so the order pages can be tested with customer/admin sessions. Product Management, Cart, Payment/Checkout, and Admin Dashboard modules are intentionally excluded.
