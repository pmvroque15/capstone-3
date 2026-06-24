![grocery-store-header.gif](backend/src/main/resources/readme-images/grocery-store-header.gif)
# Pat's Online Grocery Store  🛒

---
## Table Of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack-)
- [Project Board](#project-board)
- [What Would I Improve On](#what-would-i-improve-on-)
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

Another thing I am proud also was I made a project board to experience how this feature works.
![project-board.gif](backend/src/main/resources/readme-images/project-board.gif)

And here's a little snippet of what my ticket and the user story looked like:

![project-board-example.png](backend/src/main/resources/readme-images/project-board-example.png)

---

### What would I improve on 
Hmm... 
![thebatman-thinking.gif](backend/src/main/resources/readme-images/thebatman-thinking.gif)

This project I had fun really, understanding behind the scenes of a Web Application.
Giving me an opportunity to understand on reading and understanding code line by line and understanding the architecture of Spring Boot 
REST applications. There's nothing that I want to change in the back end, but I definitely want to improve the UI of this application to make it sleeker and user friendly.

---

### Author
Hi, I’m Pat Roque, an aspiring software developer passionate about learning and growing in the tech space. With a background in customer experience and leadership, I bring strong communication and problem-solving skills into my development journey. I’m currently focused on Java, building applications and expanding my knowledge of programming fundamentals.

Built with lots of brain cells and mooskels by a developer who takes both their gains and their grocery shopping skills seriously (ᕗ ͠° ਊ ͠° )ᕗ ᕙ(⇀‸↼‶)ᕗ

---

### Closing