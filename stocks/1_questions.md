# Question 1

Suppose you are developing a similar (if not identical) project for a company. One teammate poses the following:

> "We do not have to worry about logging. The application is very small and tests should take care of any potential bugs. If we really need it, we can print some important data and just comment it out later."

Do you agree or disagree with the proposition? Please elaborate on your reason to agree or disagree. (~50-100 words)

___

**Answer**:
We disagree. We would have agreed if the statement would not have contained: “we
can print some important data and just comment it out later.”. Using prints is
considered bad practice in this case for multiple reasons; however, one of them would be
that the developer does not know where the print is coming from(provenance). Given the
statement, we believe that logging should be used instead because it is the most
insightful debugging resource, enabling developers to trace the errors that occurred in
the program.

___

# Question 2

One of your requirements is to create a message class where `key` and `value` are strings. How could you modify your class so that the key and value could be any different data types and do not require casting by the developer? Preferably, provide the code of the modified class in the answer.
___

**Answer**:
We would make use of Generics so that any data type could be used for the fields key
and value.

```java
package nl.rug.aoop.messagequeue;

import lombok.Getter;
import java.time.LocalDateTime;

public final class MessageGenerics<T, U> {
    @Getter
    private final T key;
    @Getter
    private final U value;
    @Getter
    private final LocalDateTime timestamp;
    
    public MessageGenerics(T key, U value) {
        this.key = key;
        this.value = value;
        this.timestamp = LocalDateTime.now();
    }
}
```

___

# Question 3

How is Continuous Integration applied to (or enforced on) your assignment? (~30-100 words)

___

**Answer**:
A continuous integration tool called Jenkins is used on our github repository. This
continuous integration tool ensures good quality of code, style by enforcing quality gates.
It ensures good commits by doing quality checks on the repository and by only accepting
good commits.
___

# Question 4

One of your colleagues wrote the following class:

```java
import java.util.*;

public class MyMenu {

    private Map<Integer, PlayerAction> actions;

    public MyMenu() {
        actions = new HashMap<>();
        actions.put(0, DoNothingAction());
        actions.put(1, LookAroundAction());
        actions.put(2, FightAction());
    }

    public void printMenuOptions(boolean isInCombat) {
        List<String> menuOptions = new ArrayList<>();
        menuOptions.add("What do you want to?");
        menuOptions.add("\t0) Do nothing");
        menuOptions.add("\t1) Look around");
        if(isInCombat) {
            menuOptions.add("\t1) Fight!");
        }
    }

    public void doOption() {
        int option = getNumber();
        if(actions.containsKey(option)) {
            actions.get(option).execute();
        }
    }

    public int getNumber() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
}
```
List at least 2 things that you would improve, how it relates to test-driven development and why you would improve these things. Provide the improved code below.

___

**Answer**:
Throwing an exception is considered advantageous as it maintains the desired flow of the program whenever an unexpected action occurs.
- An IllegalArgumentException should arise when a user inputs a negative number or a number above two. The user will then be informed to write a number that is suitable for the doOption to perform. This is also important during testing, we will be able to assertThrows the IllegalArgumentException and check if the user will get the expected message when they write an unsuitable number. 
- Another improvement would be to throw an InputMismatchException whenever a user enters anything other than an int (for example, String).

Improved code:

```java
import java.util.*;

public class MyMenu {

    private Map<Integer, PlayerAction> actions;

    public MyMenu() {
        actions = new HashMap<>();
        actions.put(0, DoNothingAction());
        actions.put(1, LookAroundAction());
        actions.put(2, FightAction());
    }

    public void printMenuOptions(boolean isInCombat) {
        List<String> menuOptions = new ArrayList<>();
        menuOptions.add("What do you want to?");
        menuOptions.add("\t0) Do nothing");
        menuOptions.add("\t1) Look around");
        if(isInCombat) {
            menuOptions.add("\t1) Fight!");
        }
    }

    public void doOption() throws IllegalArgumentException {
        int option = getNumber();
        if(actions.containsKey(option)) {
            actions.get(option).execute();
        } else {
            throw new IllegalArgumentException("Input a number between 0 and 2");
        }
    }

    public int getNumber() {
        int number = 0;
        try {
            Scanner scanner = new Scanner(System.in);
            number =  scanner.nextInt();
        } catch(InputMismatchException e) {
            System.out.println("value must contain only number");
        }
        return number;
    }
}
```
___