# AgentProject - JADE + Maven

Este proyecto es un ejemplo de cómo ejecutar agentes JADE usando Maven en un entorno Java.

## 📁 Estructura del proyecto

```
AgentProject/
├── pom.xml
└── src/
    ├── main/
    │   └── java/
    │       └── pe/edu/fisi/AgentProject/
    │           ├── App.java
    │           ├── HelloAgent.java
    │           └── MyAgent.java
    └── test/
        └── java/
            └── ... (tests opcionales)
```

## ✅ Prerrequisitos

- Java 11 o superior (se recomienda Java 17).
- [Apache Maven](https://maven.apache.org) instalado y disponible en el `PATH`.

Verifica la instalación con:

```bash
java -version
mvn -v
```

## ⚙️ Compilación

Para compilar el proyecto:

```bash
mvn clean install
```

Esto compilará las clases bajo `src/main/java`.

## ▶️ Ejecución del agente principal

Para ejecutar la clase `App.java`, que inicializa la plataforma JADE:

```bash
mvn exec:java -Dexec.mainClass=pe.edu.fisi.AgentProject.App
```

> Esto abrirá la interfaz gráfica de JADE y lanzará el agente `MyAgent`.

## 🛠 Personalización

Puedes modificar la clase `App.java` para iniciar varios agentes JADE:

```java
ac.acceptNewAgent("Agente1", new HelloAgent()).start();
ac.acceptNewAgent("Agente2", new MyAgent()).start();
```

## 📦 Dependencias

La plataforma JADE se obtiene desde el repositorio de [JitPack](https://jitpack.io), ya configurado en el `pom.xml`.

---

## 🧪 Pruebas (opcional)

Si implementas pruebas unitarias con JUnit:

```bash
mvn test
```

---

## 📝 Licencia

Proyecto académico para uso educativo con JADE.
