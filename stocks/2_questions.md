# Question 1

In the assignment, you had to create a `MessageHandler` interface. Please answer the following two questions:

1. Describe the benefits of using this `MessageHandler` interface. (~50-100 words)
2. Instead of creating an implementation of `MessageHandler` that invokes a command handler, we could also pass the command handler to the client/server directly without the middle man of the `MessageHandler` implementation. What are the implications of this? (~50-100 words)

___

**Answer**:
1. The interface helps us to achieve decoupling in this case, 
   meaning that we separated the networking from the way messages are handled.
   Another strength of using the interface is that the code becomes more readable and maintainable;
   the MessageHandler interface only underlines the behaviour without providing the implementation.
   For instance, if we were to provide the implementation of the MessageHandler inside the ClientHandler,
   then the code would not be reusable, although the program would still work.

2. If we would not create an implementation of a MessageHandler that invokes a command handler, 
   we would need to add more responsibility to our command handler. This means, that the command handler should also
   deal with the handling of messages: converting a message from JSON to Message or checking if a message is null.
   Although, the program would still work, the design would become worse. The code would be cluttered, and also
   the decoupling of our program would be affected, because it is not the job of a command handler to deal with the
   handling of messages.
___

# Question 2

One of your colleagues wrote the following class:

```java
public class RookieImplementation {

    private final Car car;

    public RookieImplementation(Car car) {
        this.car = car;
    }

    public void carEventFired(String carEvent) {
        if("steer.left".equals(carEvent)) {
            car.steerLeft();
        } else if("steer.right".equals(carEvent)) {
            car.steerRight();
        } else if("engine.start".equals(carEvent)) {
            car.startEngine();
        } else if("engine.stop".equals(carEvent)) {
            car.stopEngine();
        } else if("pedal.gas".equals(carEvent)) {
            car.accelerate();
        } else if("pedal.brake".equals(carEvent)) {
            car.brake();
        }
    }
}
```

This code makes you angry. Briefly describe why it makes you angry and provide the improved code below.

___

**Answer**: The code written is neither readable nor maintainable. We need to think what would happen if we want to add
            more functionality to our carEventFired method. The answer is that we would need to add more if else 
            statements. This would result in the code being clustered. 
            It would also affect the decoupling of the program. Moreover, testing this function would get more complex
            as we add more to it. We could use the command pattern instead, so we avoid the problems described above.

Improved code:

```java
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

public interface Command {
    void execute();
}

public abstract class CarCommand implements Command {
    protected final Car car;

    protected CarCommand(Car car) {
        this.car = car;
    }
}

public class CarSteerLeftCommand extends CarCommand {
    public CarSteerLeftCommand(Car car) {
        super(car);
    }

    @Override
    public void execute() {
        car.steerLeft();
    }
}

public class CarSteerRightCommand extends CarCommand {
    public CarSteerRightCommand(Car car) {
        super(car);
    }

    @Override
    public void execute() {
        car.steerRight();
    }
}

public class CarStartEngineCommand extends CarCommand {
    public CarStartEngineCommand(Car car) {
        super(car);
    }

    @Override
    public void execute() {
        car.startEngine();
    }
}

public class CarStopEngineCommand extends CarCommand {
    public CarStopEngineCommand(Car car) {
        super(car);
    }

    @Override
    public void execute() {
        car.stopEngine();
    }
}

public class CarAccelerateCommand extends CarCommand {
    public CarAccelerateCommand(Car car) {
        super(car);
    }

    @Override
    public void execute() {
        car.accelerate();
    }
}

public class CarBrakeCommand extends CarCommand {
    public CarSteerLeftCommand(Car car) {
        super(car);
    }

    @Override
    public void execute() {
        car.brake();
    }
}

@Slf4j
public class CommandHandler {
    private final Map<String, Command> commandMap;

    public CommandHandler() {
        commandMap = new HashMap<>();
    }

    public void executeCommand(String commandDescription) {
        Command command = null;
        if (commandMap.containsKey(commandDescription)) {
            commandMap.get(commandDescription).execute();
            return;
        }
        log.error("Command not found");
    }
    
    public void addCommand(String name, Command command) {
        commandMap.put(name, command);
    }
}

public interface CommandHandlerFactory {
    CommandHandler create();
}

public class CarCommandHandlerFactory implements CommandHandlerFactory {
    private final Car car;
    
    public CarCommandHandlerFactory(Car car) {
        this.car = car;
    }
    
    @Override
    public CommandHandler create() {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.addCommand("steer.left", new CarSteerLeftCommand(car));
        commandHandler.addCommand("steer.right", new CarSteerRightCommand(car));
        commandHandler.addCommand("engine.start", new CarStartEngineCommand(car));
        commandHandler.addCommand("engine.stop", new CarStopEngineCommand(car));
        commandHandler.addCommand("pedal.gas", new CarAccelerateCommand(car));
        commandHandler.addCommand("pedal.brake", new CarBrakeCommand(car));
        return commandHandler;
    }
}

public class RookieImplementation {
    private final Car car;
    private final CommandHandler commandHandler;

    public RookieImplementation(CommandHandler commandHandler) {
        this.commandHandler = new CarCommandHandlerFactory(car).create();
    }

    public void carEventFired(String carEvent) {
        commandHandler.executeCommand(carEvent);
    }
}
```
___

# Question 3

You have the following exchange with a colleague:

> **Colleague**: "Hey, look at this! It's super handy. Pretty simple to write custom experiments."

```java
class Experiments {
    public static Model runExperimentA(DataTable dt) {
        CommandHandler commandSequence = new CleanDataTableCommand()
            .setNext(new RemoveCorrelatedColumnsCommand())
            .setNext(new TrainSVMCommand())

        Config config = new Options();
        config.set("broadcast", true);
        config.set("svmdatatable", dt);

        commandSequence.handle(config);

        return (Model) config.get("svmmodel");
    }

    public static Model runExperimentB() {
        CommandHandler commandSequence = new CleanDataTableCommand()
            .setNext(new TrainSGDCommand())

        Config config = new Options();
        config.set("broadcast", true);
        config.set("sgddatatable", dt);

        commandSequence.handle(config);

        return (Model) config.get("sgdmodel");
    }
}
```

> **Colleague**: "I could even create this method to train any of the models we have. Do you know how Jane did it?"

```java
class Processor {
    public static Model getModel(String algorithm, DataTable dt) {
        CommandHandler commandSequence = new TrainSVMCommand()
            .setNext(new TrainSDGCommand())
            .setNext(new TrainRFCommand())
            .setNext(new TrainNNCommand())

        Config config = new Options();
        config.set("broadcast", false);
        config.set(algorithm + "datatable", dt);

        commandSequence.handle(config);

        return (Model) config.get(algorithm + "model");
    }
}
```

> **You**: "Sure! She is using the command pattern. Easy indeed."
>
> **Colleague**: "Yeah. But look again. There is more; she uses another pattern on top of it. I wonder how it works."

1. What is this other pattern? What advantage does it provide to the solution? (~50-100 words)

2. You know the code for `CommandHandler` has to be a simple abstract class in this case, probably containing four methods:
- `CommandHandler setNext(CommandHandler next)` (implemented in `CommandHandler`),
- `void handle(Config config)` (implemented in `CommandHandler`),
- `abstract boolean canHandle(Config config)`,
- `abstract void execute(Config config)`.

Please provide a minimum working example of the `CommandHandler` abstract class.

___

**Answer**:

1. The other pattern that was used is the Builder pattern. This pattern is useful in this case because it allows
   the developer to specify properties one by one. Let's say the developer wants to add more methods similar to 
   runExperimentA(), where the only difference is the algorithm the method is using. If the Builder pattern would not 
   be used, the code would become less readable and hard to maintain.

2.

```java
public abstract class CommandHandler {
    
    CommandHandler setNext(CommandHandler next) {
        this = next;
        return this;
    }
    
    void handle(Config config) {
        if(canHandle(config)) {
            execute(config);
        }
    }
    
    abstract boolean canHandle(Config config);
    
    abstract void execute(Config config);
}	
 ```
___
