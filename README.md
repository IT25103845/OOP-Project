# FreshMart - Component 2: Product Management

This ZIP contains only **Component 2: Product Management** from the FreshMart Online Grocery website.

## Features included

- Product listing page
- Product detail page
- Search products by name, category, or description
- Filter products by category
- Admin CRUD for products
  - Add product
  - Edit product
  - Delete product
- File-based persistence using `data/products.txt`
- Seed product data in `src/main/resources/data/products.txt`

## Files included

```text
src/main/java/com/grocery/
  GroceryApplication.java
  DataInitializer.java
  controller/
    HomeController.java
    ProductController.java
  model/
    BaseEntity.java
    Searchable.java
    Product.java
  service/
    ProductService.java
  util/
    FileUtil.java

src/main/resources/
  application.properties
  data/products.txt
  templates/products/
    list.html
    detail.html
    admin.html
  static/css/style.css
  static/js/main.js

data/products.txt
pom.xml
mvnw / mvnw.cmd
```

## How to run

```bash
./mvnw spring-boot:run
```

Then open:

```text
http://localhost:8080/products
```

Admin CRUD page:

```text
http://localhost:8080/products/admin
```

## Notes

This component is separated from authentication, cart, orders, payment/checkout, and dashboard features. It can run independently and focuses only on product management.
Product management module updated