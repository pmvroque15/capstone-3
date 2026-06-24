![grocery-store-header.jpg](backend/src/main/resources/readme-images/grocery-store-header.jpg)

# Pat's Online Grocery Store  🛒

---
## Table Of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack-)
- []
---
### Overview

This full-stack e-commerce application is built using Java, Spring boot, JavaScript, and MySQL. Api endpoints are developed and tested using Insomnia, while mySQL serves as the application's relational database.

---

### Features

![grocery-store-filters.gif](backend/src/main/resources/readme-images/grocery-store-filters.gif)

|               User               |       Administrator        |
|:--------------------------------:|:--------------------------:|
|   View products by category *    |     Add new categories     |
| Filter products by price range * |  Edit existing categories  |
|      View profile details *      |     Delete categories      |
|     Update profile details *     |                            |

**`*`** = Both authorized users can use the feature.

---

### Technology Stack 

- Backend: ![Java-SpringBoot](https://www.readmecodegen.com/api/social-icon?name=Java%2CSpringBoot&size=38&link=)
- Frontend: ![Javascript](https://www.readmecodegen.com/api/social-icon?name=Javascript&size=38&link=)
- Database: ![mySQL](https://www.readmecodegen.com/api/social-icon?name=mySQL&size=38&link=)
- API Testing: ![insomnia](https://www.readmecodegen.com/api/social-icon?name=insomnia&size=38&link=)
--- 

### Favorite code

In the project, I noticed that I was typing the same line of codes when accessing the usersId by passing in Principle data type.

In the ShoppingCartController we have to access the logged-in user by passing in a Principle object and get its user's ID: 

```java
 public ResponseEntity<ShoppingCart> getCart(Principal principal) 
```

I thought that it was redundant, so instead doing it in the Controller, I passed the principal to the ShoppingCartService class to get the ID:

```java
    public int getUserId(Principal principal) {

        String userName = principal.getName();
        User user = userService.getByUserName(userName);

        return user.getId();
    }
```

and I think it was a great way to keep from dry and keeping the Controller doing some business logic when the Service layer does the business logic.

---

### Project Board

---

### What would I improve on 

---

### Author

---

### Closing