# NullSafe Library v1.0-alpha - Release Notes

## 🚀 Quick Start Guide

### Installation

#### Option 1: Direct JAR Usage
```bash
# Download the JAR from the release assets
java -cp nullsafe-1.0.jar your.main.Class
```

#### Option 2: Maven Dependency (Future)
```xml
<dependency>
    <groupId>com.github</groupId>
    <artifactId>nullsafe</artifactId>
    <version>1.0-alpha</version>
</dependency>
```

#### Option 3: Clone and Build
```bash
git clone https://github.com/yasmramos/nullsafe.git
cd nullsafe
mvn clean package
```

### Basic Usage

#### Null-Safe Operations
```java
import com.github.nullsafe.NullSafe;

// Basic null-safe operations
String result = NullSafe.of(null)
    .orElse("default");

// Primitive operations
Integer primitive = NullSafe.ofPrimitive(null)
    .orElse(0);
```

#### Functional Patterns
```java
import com.github.nullsafe.functional.Optional;

// Functional operations with null handling
Optional<String> safeString = Optional.ofNullable(someValue)
    .map(String::toUpperCase)
    .filter(s -> s.length() > 0);
```

## 🧪 Testing

The library includes comprehensive test coverage:

```bash
# Run all tests
mvn test

# Run specific test suites
mvn test -Dtest=NullSafeTest
mvn test -Dtest=NullSafePrimitiveTest
```

## 🛠️ Development

### Build Requirements
- Java 17 or higher
- Maven 3.8+
- JUnit 5 for testing

### Development Setup
```bash
# Clone repository
git clone https://github.com/yasmramos/nullsafe.git

# Build and test
mvn clean test

# Generate coverage report
mvn jacoco:report
```

### Code Quality
- Multi-platform CI/CD (Ubuntu, Windows, macOS)
- JaCoCo code coverage
- Maven Surefire testing
- Automated quality checks

## 📊 Current Status

- ✅ **73 unit tests** - All passing
- ✅ **Java 17 compatible**
- ✅ **Multi-platform support**
- ✅ **Code coverage integrated**
- ✅ **CI/CD workflows functional**

## 🔮 Future Roadmap

### v1.0-stable (Planned)
- [ ] Performance optimizations
- [ ] Extended documentation
- [ ] Maven Central publishing
- [ ] Enhanced error handling
- [ ] Additional utility methods

## 🐛 Known Issues

This is an **alpha release**. Please be aware:
- APIs may change based on feedback
- Performance optimizations pending
- Documentation improvements ongoing

## 📞 Feedback & Support

Please report issues and provide feedback via:
- GitHub Issues: https://github.com/yasmramos/nullsafe/issues
- Email: yasmramos95@gmail.com

## 📄 License

MIT License - see LICENSE file for details.

---

**Release Information**
- Version: 1.0-alpha
- Build Date: 2025-12-01
- Java Version: 17
- Maven Version: 3.8+
- Git Commit: v1.0-alpha

Thank you for testing the NullSafe library!