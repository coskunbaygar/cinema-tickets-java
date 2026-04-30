# DWP Code Exercise

After cloning the template project, a `.gitignore` file was added to exclude IDE (VSCode) and macOS-specific files from the repository.

---

## Immutability

Although the `TicketTypeRequest` class did not include setter methods, it was not fully immutable. To address this, `final` keywords were added to both the class and its fields to ensure proper immutability.

---

## Service Implementation

The `TicketService` interface was implemented in `TicketServiceImpl` in accordance with the provided documentation.

Even though a more modular design would normally be preferred (such as separating validators, calculators, and other responsibilities), the exercise explicitly required that `TicketServiceImpl` only contain private methods apart from the main interface method. Therefore, the implementation was kept within a single class using private helper methods.

---

## Testing

The project included JUnit and Mockito dependencies, although test cases were not explicitly defined in the requirements.

Based on this, 5 different unit test scenarios were implemented using JUnit 5 and Mockito to validate the main business logic and edge cases.

---