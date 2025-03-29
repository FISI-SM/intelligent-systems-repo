# HelloAgent - JADE Tutorial

This example shows how to compile and run a simple JADE agent called `HelloAgent`.

## 📦 Requirements

- Java 8 (JDK)
- JADE library (`jade.jar` placed in the root folder)

## 🛠️ Compile the Agent (from inside the `2.1-HelloAgent` folder)

### On Linux/macOS:
```bash
cd 2.1-HelloAgent
javac -cp .:../jade.jar HelloAgent.java
```

### On Windows (use `;` instead of `:`):
```bash
cd 2.1-HelloAgent
javac -cp .;../jade.jar HelloAgent.java
```

## ▶️ Run the Agent without GUI

```bash
java -cp .:../jade.jar jade.Boot -agents "hello:HelloAgent"
```

## ▶️ Run the Agent with GUI

```bash
java -cp .:../jade.jar jade.Boot -gui -agents "hello:HelloAgent"
```

This starts a JADE container with an agent named `hello` of class `HelloAgent`.

> 💡 You can also run the agent from the root folder, as long as you adjust the classpath and fully qualified class name accordingly.
