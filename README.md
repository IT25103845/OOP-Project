# FreshMart - Component 3: Shopping Cart

This ZIP contains only **Component 3: Shopping Cart** from the FreshMart website.

## Included features

- View shopping cart
- Add products to cart
- Update item quantity
- Remove a single item from cart
- Clear the full cart
- Cart total and subtotal calculation
- File-based cart persistence using `data/carts.txt`

## Supporting code included

Product browsing and product file reading are included only to support adding items to the cart. Authentication, order management, checkout, payments and admin dashboard are intentionally not included in this component.

## Main files

- `CartController.java`
- `CartService.java`
- `Cart.java`
- `CartItem.java`
- `ProductController.java` (support only)
- `ProductService.java` (support only)
- `templates/cart/view.html`
- `templates/products/list.html`
- `data/carts.txt`
- `data/products.txt`

## How to run

```bash
./mvnw spring-boot:run
```

Then open:

```text
http://localhost:8080
```

## Notes

This component uses a demo session user ID (`demo-user`) so the cart can run independently without the authentication component.
