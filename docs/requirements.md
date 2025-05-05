## Functional Requirements
- As a customer, I want to be able to browse available products so that I can choose what to purchase.
- As a customer, I want to be able to view product details.
- As a customer, I want to be able to add products to my shopping cart so that I can collect items before placing an order.
- As a customer, I want to be able to view my shopping cart so that I can review the selected products and their quantities.
- As a customer, I want to be able to modify my shopping cart (add/remove items or change quantities) so that I can adjust my order before confirming it.
- As a customer, I want to be able to place an order from my shopping cart so that I can purchase the selected products.
- As a customer, I want to be able to see my orders.
- As a customer, I want to be able to see order details.
- As a customer, I want to be able to confirm my order via email so that I can verify my purchase before it is finalized.
- As a customer, I want to receive email notifications about my order status so that I am informed about its progress (e.g., confirmed, cancelled, or payment failed).
- As a customer, I want to be able to pay for my order via bank card so that I can complete the purchase process.
- As an admin, I want to be able to add new products to the inventory so that customers can purchase them. 
- As an admin, I want to be able to update product details so that inventory information remains accurate. 
- As an admin, I want to be able to view product details to be sure that inventory information remains accurate.
- As an admin, I want to be able to delete a product.
- As an admin, I want to be able to view current inventory levels so that I can manage stock effectively.


## Quality Requirements

### Reliability
- The overall order process must handle failures gracefully. If something fails or user has canceled the order, the system must initiate a compensating action (e.g. to release the reserved inventory), and the order status should be updated accordingly.
- The system must prevent overselling through reliable inventory checking and reservation mechanisms.

### Scalability
- All core services should be designed as independent units (microservices) capable of being scaled horizontally based on their specific load.

### Performance
- The system must handle inventory checks and reservations within 3 seconds for 95% of requests to support real-time stock updates.
- The system must send email notifications within 10 seconds of a triggering event to ensure timely communication with customers.

### Testability
- Minimum unit test coverage - 80%

### Maintainability
- The system must include comprehensive API documentation (OpenAPI/AsyncAPI) and data models (ERD) to facilitate future maintenance by developers.
- The backend codebase must follow consistent coding standards, clean code and SOLID principles.

### Interoperability
- The system must integrate with standard message brokers for asynchronous communication between microservices.

### Security
- The system must prevent CSRF and XSS attacks and SQL injection.
- The system must protect customer data using JWT Authentication.
- The system must restrict admin actions (e.g., updating inventory) to authorized users only, using role-based access control.